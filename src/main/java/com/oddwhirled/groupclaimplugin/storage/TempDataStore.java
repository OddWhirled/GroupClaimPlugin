/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oddwhirled.groupclaimplugin.storage;

import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

/**
 *
 * @author Drew
 */
public class TempDataStore extends DataStore {

    /*
    This is a class to simulate storage while keeping everything in memory.
    It is meant for testing purposes only.
    */
    protected class SimpleChunk {

        protected final int x;
        protected final int z;

        protected SimpleChunk(int x, int z) {
            this.x = x;
            this.z = z;
        }
    }

    /*
    *
    * Save and Load methods
    *   
     */

    // MAPS TO SIMULATE FILE STORAGE
    private HashMap<SimpleChunk, String> chunkSave = new HashMap<>();
    private HashMap<UUID, String> playerSave = new HashMap<>();
    private HashMap<String, GroupInfo> groupSave = new HashMap<>();

    //load
    @Override
    protected String fileGetChunkGroup(Chunk c) {
        for (SimpleChunk sc : chunkSave.keySet()) {
            if (sc.x == c.getX() && sc.z == c.getZ()) {
                return chunkSave.get(sc);
            }
        }
        return null;
    }

    @Override
    protected GroupInfo fileGetGroupInfo(String name) {
        return groupSave.get(name);
    }

    @Override
    protected String fileGetPlayerGroup(UUID uuid) {
        return playerSave.get(uuid);
    }

    //save
    @Override
    protected void fileUpdateChunkGroup(Chunk c, String group) {
        if (group != null) {
            group = group.toLowerCase();
        } else {
            fileRemoveChunk(c);
            return;
        }
        SimpleChunk sc = new SimpleChunk(c.getX(), c.getZ());
        chunkSave.put(sc, group);
    }

    @Override
    protected void fileRemoveChunk(Chunk c) {
        int x = c.getX();
        int z = c.getZ();
        for (SimpleChunk sc : chunkSave.keySet()) {
            if (sc.x == x && sc.z == z) {
                chunkSave.remove(sc);
            }
        }
    }

    @Override
    protected void fileUpdateGroupInfo(GroupInfo g, String name) {
        if (name != null) {
            name = name.toLowerCase();
        } else {
            fileRemoveGroup(name);
            return;
        }
        GroupInfo sg = new GroupInfo(name, g.leader);
        groupSave.put(name, sg);
    }

    @Override
    protected void fileRemoveGroup(String group) {
        groupSave.remove(group);
        while (playerSave.containsValue(group)) {
            playerSave.values().remove(group);
        }
        while (chunkSave.containsValue(group)) {
            chunkSave.values().remove(group);
        }
    }

    @Override
    protected void fileUpdatePlayerGroup(Player p, String group) {
        if (group != null) {
            group = group.toLowerCase();
        } else {
            fileRemovePlayer(p);
            return;
        }
        UUID uuid = p.getUniqueId();
        playerSave.put(uuid, group);
    }

    @Override
    protected void fileRemovePlayer(Player p) {
        UUID uuid = p.getUniqueId();
        for (UUID u : playerSave.keySet()) {
            if (uuid.equals(u)) {
                playerSave.remove(u);
            }
        }
    }
}
