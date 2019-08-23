/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oddwhirled.groupclaimplugin.storage;

import com.oddwhirled.groupclaimplugin.GroupClaimPlugin;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 *
 * @author Drew
 */
public class MySQLDataStore extends DataStore {

    FileConfiguration c = GroupClaimPlugin.instance().getConfig();

    String host = c.getString("mysql.host");
    String port = c.getString("mysql.port");
    String user = c.getString("mysql.username");
    String pass = c.getString("mysql.password");
    String database = c.getString("mysql.database");

    Connection conn;
    Statement stmt;

    public MySQLDataStore() {
        //Bukkit.getScheduler().runTaskAsynchronously(plugin, task);
        String url = String.format("jdbc:mysql://%s:%s/%s", host, port, database);
        try {
            this.conn = DriverManager.getConnection(url, user, pass);
            stmt = conn.prepareStatement(url);
            stmt.execute("create table if not exists groups(id varchar(30), display_name varchar(30), leader_uuid varchar(36));");
            stmt.execute("create table if not exists players(uuid varchar(36), group_id varchar(30), primary key (uuid), foreign key (group_id) references groups(id));");
            stmt.execute("create table if not exists chunks(x int, z int, group_id varchar(30), constraint pk_chunk primary key(x,z), foreign key (group_id) references groups(id));");
        } catch (SQLException ex) {
            System.err.println("Couldn't connect to database!\n" + ex.getMessage());
        }
    }

    @Override
    protected String fileGetChunkGroup(Chunk c) {
        String query = String.format("select group_id from chunks where x=%d and z=%d;", c.getX(), c.getZ());
        try {
            ResultSet rs = stmt.executeQuery(query);
            if (!rs.first()) {
                return null;
            }
            return rs.getString("group_id");
        } catch (SQLException ex) {
            System.err.println("Couldn't get chunk from database!\n" + ex.getMessage());
            return null;
        }
    }

    @Override
    protected GroupInfo fileGetGroupInfo(String name) {
        String query = String.format("select * from groups where id=\"%s\";", name);
        try {
            ResultSet rs = stmt.executeQuery(query);
            if (!rs.first()) {
                return null;
            }
            String displayName = rs.getString("display_name");
            UUID uuid = UUID.fromString(rs.getString("leader_uuid"));
            return new GroupInfo(displayName, uuid);
        } catch (SQLException ex) {
            System.err.println("Couldn't get group info from database!\n" + ex.getMessage());
            return null;
        }
    }

    @Override
    protected String fileGetPlayerGroup(UUID uuid) {
        String query = String.format("select group_id from players where uuid=\"%s\";", uuid.toString());
        try {
            ResultSet rs = stmt.executeQuery(query);
            if (!rs.first()) {
                return null;
            }
            return rs.getString("group_id");
        } catch (SQLException ex) {
            System.err.println("Couldn't get player from database!\n" + ex.getMessage());
            return null;
        }
    }

    @Override
    protected void fileUpdateChunkGroup(Chunk c, String group) {
        String query = String.format("update chunks set group_id=\"%s\" where x=%d and z=%d;", group, c.getX(), c.getZ());
        try {
            stmt.execute(query);
        } catch (SQLException ex) {
            System.err.println("Couldn't update chunk in database!\n" + ex.getMessage());
        }
    }

    @Override
    protected void fileRemoveChunk(Chunk c) {
        String query = String.format("delete from chunks where x=%d and z=%d;", c.getX(), c.getZ());
        try {
            stmt.execute(query);
        } catch (SQLException ex) {
            System.err.println("Couldn't remove chunk from database!\n" + ex.getMessage());
        }
    }

    @Override
    protected void fileUpdateGroupInfo(GroupInfo g, String name) {
        String query = String.format("update groups set display_name=\"%s\", leader_uuid=\"%s\" where id=\"%s\";", g.displayName, g.leader.toString(), name);
        try {
            stmt.execute(query);
        } catch (SQLException ex) {
            System.err.println("Couldn't update group in database!\n" + ex.getMessage());
        }
    }

    //bigass method
    @Override
    protected void fileRemoveGroup(String group) {
        String queryP = String.format("delete from players where group_id=\"%s\";", group);
        String queryC = String.format("delete from chunks where group_id=\"%s\";", group);
        String queryG = String.format("delete from groups where id=\"%s\";", group);
        try {
            stmt.execute(queryP);
            stmt.execute(queryC);
            stmt.execute(queryG);
        } catch (SQLException ex) {
            System.err.println("Couldn't remove group from database!\n" + ex.getMessage());
        }
    }

    @Override
    protected void fileUpdatePlayerGroup(Player p, String group) {
        String query = String.format("update players set group_id=\"%s\" where uuid=\"%s\";", group, p.getUniqueId().toString());
        try {
            stmt.execute(query);
        } catch (SQLException ex) {
            System.err.println("Couldn't update player in database!\n" + ex.getMessage());
        }
    }

    @Override
    protected void fileRemovePlayer(Player p) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void fileAddChunk(Chunk c, String group) {
        String query = String.format("insert into chunks (x, z, group_id) values (%d, %d, \"%s\");", c.getX(), c.getZ(), group);
        try {
            stmt.execute(query);
        } catch (SQLException ex) {
            System.err.println("Couldn't add chunk to database!\n" + ex.getMessage());
        }
    }

    @Override
    protected void fileAddGroup(GroupInfo g, String name) {
        String query = String.format("insert into groups (id, display_name, leader_uuid) values (\"%s\", \"%s\", \"%s\");", name, g.displayName, g.leader.toString());
        try {
            stmt.execute(query);
        } catch (SQLException ex) {
            System.err.println("Couldn't add group to database!\n" + ex.getMessage());
        }
    }

    @Override
    protected void fileAddPlayer(Player p, String group) {
        String query = String.format("insert into players (uuid, group_id) values (\"%s\", \"%s\");", p.getUniqueId().toString(), group);
        try {
            stmt.execute(query);
        } catch (SQLException ex) {
            System.err.println("Couldn't add player to database!\n" + ex.getMessage());
        }
    }
}
