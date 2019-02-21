/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oddwhirled.groupclaimplugin.storage;

import com.oddwhirled.groupclaimplugin.GroupClaimPlugin;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

/**
 *
 * @author Drew
 */
public abstract class DataStore {

    protected class GroupInfo {

        protected final String displayName;
        protected UUID leader;

        protected GroupInfo(String name, UUID l) {
            displayName = name;
            leader = l;
        }
    }

    private static final DataStore instance;

    static {
        boolean enabled = GroupClaimPlugin.instance().getConfig().getBoolean("mysql.enabled");
        boolean testing = GroupClaimPlugin.instance().getConfig().getBoolean("mysql.testmode");
        if (testing) {
            instance = new TempDataStore();
        } else if (enabled) {
            instance = new MySQLDataStore();
        } else {
            instance = new SQLiteDataStore();
        }
    }

    public static DataStore instance() {
        return instance;
    }

    //chunks recently tried to edit cached for fast lookup
    protected final HashMap<Chunk, String> chunkCache = new HashMap<>();
    //players online cached for fast lookup
    protected final HashMap<Player, String> playerCache = new HashMap<>();
    //groups online cached for fast lookup
    protected final HashMap<String, GroupInfo> groupInfoCache = new HashMap<>();
    //*if any of these are edited we still want to save in storage

    protected HashMap<Player, String> pendingInvites = new HashMap<>();

    /**
     *
     * @param p the player to invite
     * @param group the group to invite them to
     */
    public void addInvite(Player p, String group) {
        if (group != null) {
            group = group.toLowerCase();
            pendingInvites.put(p, group);
        }
    }

    /**
     *
     * @param p the player to remove invites for
     * @return group player was invited to if the player had a pending invite
     * otherwise null
     */
    public String clearInvite(Player p) {
        return pendingInvites.remove(p);
    }

    /**
     *
     * @param name the group display name
     * @param leader the player to make group leader
     * @return true if the group was created otherwise false (i.e. group already
     * existed or player in a group)
     */
    public boolean addGroup(String name, Player leader) {
        if (name == null) {
            return false;
        }
        //only proceed if this group doesn't exist
        //then check if the player is already in a group
        if (getGroupInfo(name) == null && joinGroup(leader, name)) {
            GroupInfo g = new GroupInfo(name, leader.getUniqueId());
            String group = name.toLowerCase();
            groupInfoCache.put(group, g);
            fileUpdateGroupInfo(g, group);
            return true;
        }
        return false;
    }

    /**
     *
     * @param group the group to remove
     */
    public void removeGroup(String group) {
        if (group == null) {
            return;
        }
        group = group.toLowerCase();
        groupInfoCache.remove(group);
        //@temporary, might be more efficient later
        playerCache.clear();
        chunkCache.clear();
        fileRemoveGroup(group);
    }

    /**
     *
     * @param p the player to promote
     * @return true if player is in a group that exists and gets promoted
     */
    public boolean setGroupLeader(Player p) {
        String group = getGroup(p);
        if (group != null) {
            GroupInfo g = getGroupInfo(group);
            if (g != null) {
                g.leader = p.getUniqueId();
                fileUpdateGroupInfo(g, group);
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param group the group to claim this chunk
     * @param chunk the chunk to claim
     * @return true if the claim was created otherwise false (i.e. already
     * claimed or player not in a group)
     */
    public boolean addClaim(String group, Chunk chunk) {
        if (group == null) {
            return false;
        }
        group = group.toLowerCase();

        if (getGroup(chunk) == null) {
            chunkCache.put(chunk, group);
            fileUpdateChunkGroup(chunk, group);
            return true;
        }
        return false;
    }

    /**
     *
     * @param chunk the chunk to remove claim from
     */
    public void removeClaim(Chunk chunk) {
        chunkCache.put(chunk, null);
        fileUpdateChunkGroup(chunk, null);
    }

    /**
     *
     * @param p the player to add to a group
     * @param group the group to join
     * @return true if player not already in group and joins
     */
    public boolean joinGroup(Player p, String group) {
        if (group == null) {
            return false;
        }
        group = group.toLowerCase();
        if (getGroup(p) == null) {
            playerCache.put(p, group);
            fileUpdatePlayerGroup(p, group);
            return true;
        }
        return false;
    }

    /**
     *
     * @param p
     * @return true if player not the leader and leaves
     */
    public boolean leaveGroup(Player p) {
        if (!isLeader(p)) {
            playerCache.put(p, null);
            fileUpdatePlayerGroup(p, null);
            return true;
        }
        return false;
    }

    public String getGroup(Chunk chunk) {
        if (!chunkCache.containsKey(chunk)) {
            chunkCache.put(chunk, fileGetChunkGroup(chunk));
        }
        return chunkCache.get(chunk);
    }

    public String getGroup(Player p) {
        if (!playerCache.containsKey(p)) {
            playerCache.put(p, fileGetPlayerGroup(p.getUniqueId()));
        }
        return playerCache.get(p);
    }

    private GroupInfo getGroupInfo(String name) {
        if (name == null) {
            return null;
        } else {
            name = name.toLowerCase();
        }
        if (!groupInfoCache.containsKey(name)) {
            groupInfoCache.put(name, fileGetGroupInfo(name));
        }
        return groupInfoCache.get(name);
    }

    /**
     *
     * @param group the group to get the display name
     * @return this groups display name or null if group doesn't exist
     */
    public String getGroupDisplayName(String group) {
        GroupInfo g = getGroupInfo(group);
        if (g == null) {
            return null;
        }
        return g.displayName;
    }

    /**
     * The leader may not be online therefore we can't return a player
     *
     * @param group the group to get the leader
     * @return this groups leader UUID or null if group doesn't exist
     */
    public UUID getGroupLeaderUUID(String group) {
        GroupInfo g = getGroupInfo(group);
        if (g == null) {
            return null;
        }
        return g.leader;
    }

    /**
     *
     * @param p player to check
     * @return true if the player is in a group and leader of that group
     */
    public boolean isLeader(Player p) {
        GroupInfo g = getGroupInfo(getGroup(p));
        if (g == null) {
            return false;
        }
        return (g.leader.equals(p.getUniqueId()));
    }

    //Simple un-caching methods that may be called on unload events or on schedule
    public void unCache(Chunk c) {
        chunkCache.remove(c);
    }

    public void unCache(Player p) {
        playerCache.remove(p);
        pendingInvites.remove(p);
    }

    public void unCache(String g) {
        groupInfoCache.remove(g);
    }

    /*
    *
    * Save and Load methods
    *   
     */
    //load
    protected abstract String fileGetChunkGroup(Chunk c);

    protected abstract GroupInfo fileGetGroupInfo(String name);

    protected abstract String fileGetPlayerGroup(UUID uuid);

    //save
    protected abstract void fileUpdateChunkGroup(Chunk c, String group);

    protected abstract void fileRemoveChunk(Chunk c);

    protected abstract void fileUpdateGroupInfo(GroupInfo g, String name);

    protected abstract void fileRemoveGroup(String group);

    protected abstract void fileUpdatePlayerGroup(Player p, String group);

    protected abstract void fileRemovePlayer(Player p);
}
