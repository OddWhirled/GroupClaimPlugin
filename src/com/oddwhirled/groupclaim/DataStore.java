/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oddwhirled.groupclaim;

import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

/**
 *
 * @author Drew
 */
public class DataStore {

    private class GroupInfo {

        private final String displayName;
        private UUID leader;

        private GroupInfo(String name, UUID l) {
            displayName = name;
            leader = l;
        }
    }

    private class SimpleChunk {

        private final int x;
        private final int z;

        private SimpleChunk(int x, int z) {
            this.x = x;
            this.z = z;
        }
    }

    private static DataStore instance;

    //chunks recently tried to edit cached for fast lookup
    private HashMap<Chunk, String> chunkCache = new HashMap<>();
    //players online cached for fast lookup
    private HashMap<Player, String> playerCache = new HashMap<>();
    //groups online cached for fast lookup
    private HashMap<String, GroupInfo> groupInfoCache = new HashMap<>();
    //*if any of these are edited we still want to save in storage

    public static DataStore instance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    private DataStore() {
    }

    //returns if group was created (false if already existed or if player in group)
    public boolean addGroup(String name, Player leader) {
        if (name == null) {
            return false;
        }
        String displayName = name;
        name = name.toLowerCase();
        //if the group isn't online we have to check the file and cache it
        //in case someone tries that again
        if (!joinGroup(leader, name)) {
            return false;
        }
        if (!groupInfoCache.containsKey(name)) {
            groupInfoCache.put(name, getGroupInfoFromFile(name));
        }
        if (groupInfoCache.get(name) == null) {
            GroupInfo g = new GroupInfo(displayName, leader.getUniqueId());
            groupInfoCache.put(name, g);
            saveGroupInfoToFile(g, name);
            return true;
        }
        return false;
    }

    //TODO: bigass method to remove all the players in this group :(
    public void removeGroup(String group) {
        if (group == null) {
            return;
        }
        group = group.toLowerCase();
        GroupInfo g = groupInfoCache.get(group);
        groupInfoCache.remove(group);
    }

    //returns if claim was created (false if already claimed)
    public boolean addClaim(String group, Chunk chunk) {
        if (group == null) {
            return false;
        }
        group = group.toLowerCase();
        //if the chunk isn't cached we cache it for fast access since it will
        //most likely be edited soon
        if (!chunkCache.containsKey(chunk)) {
            chunkCache.put(chunk, getChunkGroupFromFile(chunk.getX(), chunk.getZ()));
        }
        if (chunkCache.get(chunk) == null) {
            chunkCache.put(chunk, group);
            saveChunkGroupToFile(chunk, group);
            return true;
        }
        return false;
    }

    public void removeClaim(Chunk chunk) {
        chunkCache.put(chunk, null);
        saveChunkGroupToFile(chunk, null);
    }

    public String getGroup(Chunk chunk) {
        if (!chunkCache.containsKey(chunk)) {
            chunkCache.put(chunk, getChunkGroupFromFile(chunk.getX(), chunk.getZ()));
        }
        return chunkCache.get(chunk);
    }

    public String getGroup(Player p) {
        if (!playerCache.containsKey(p)) {
            playerCache.put(p, getPlayerGroupFromFile(p.getUniqueId()));
        }
        return playerCache.get(p);
    }

    private GroupInfo getGroupInfo(String name) {
        if (!groupInfoCache.containsKey(name)) {
            groupInfoCache.put(name, getGroupInfoFromFile(name));
        }
        return groupInfoCache.get(name);
    }

    //leader may not be online therefore can't return a player
    public UUID getGroupLeaderUUID(String group) {
        if (group == null) {
            return null;
        }
        group = group.toLowerCase();
        if (!groupInfoCache.containsKey(group)) {
            groupInfoCache.put(group, getGroupInfoFromFile(group));
        }
        return groupInfoCache.get(group).leader;
    }

    public boolean isLeader(Player p) {
        GroupInfo g = getGroupInfo(getGroup(p));
        return (g.leader.equals(p.getUniqueId()));
    }

    public String getGroupDisplayName(String group) {
        if (group == null) {
            return null;
        }
        group = group.toLowerCase();
        if (!groupInfoCache.containsKey(group)) {
            groupInfoCache.put(group, getGroupInfoFromFile(group));
        }
        return groupInfoCache.get(group).displayName;
    }

    //returns true if player is in a group and gets promoted
    public boolean setGroupLeader(Player p) {
        String group = getGroup(p);
        if (group != null) {
            groupInfoCache.get(group).leader = p.getUniqueId();
            return true;
        }
        return false;
    }

    //returns true if player not already in group and joins
    public boolean joinGroup(Player p, String group) {
        if (getGroup(p) == null) {
            playerCache.put(p, group);
            savePlayerGroupToFile(p, group);
            return true;
        }
        return false;
    }

    //returns true if player not the leader and leaves
    public boolean leaveGroup(Player p) {
        if (!isLeader(p)) {
            playerCache.put(p, null);
            savePlayerGroupToFile(p, null);
            return true;
        }
        return false;
    }

    //Simple un-caching methods that may be called on unload events or on schedule
    public void unCache(Chunk c) {
        chunkCache.remove(c);
    }

    public void unCache(Player p) {
        playerCache.remove(p);
    }

    public void unCache(String g) {
        groupInfoCache.remove(g);
    }

    /*
    *
    * Save and Load methods
    *   
     */
//<editor-fold defaultstate="collapsed" desc="save and load methods done">
    //TEMPORARY MAPS TO SIMULATE FILE STORAGE
    private HashMap<SimpleChunk, String> chunkSave = new HashMap<>();
    private HashMap<UUID, String> playerSave = new HashMap<>();
    private HashMap<String, GroupInfo> groupSave = new HashMap<>();

    //load
    private String getChunkGroupFromFile(int x, int z) {
        for (SimpleChunk sc : chunkSave.keySet()) {
            if (sc.x == x && sc.z == z) {
                return chunkSave.get(sc);
            }
        }
        return null;
    }

    private GroupInfo getGroupInfoFromFile(String name) {
//        for (String s : groupSave.keySet()) {
//            if (s.equals(name)) {
        return groupSave.get(name);
//            }
//        }
    }

    private String getPlayerGroupFromFile(UUID uuid) {
//        for (UUID u : playerSave.keySet()) {
//            if (uuid.equals(u)) {
        return playerSave.get(uuid);
//            }
//        }
//        return null;
    }

    //save
    private void saveChunkGroupToFile(Chunk c, String group) {
        if (group != null) {
            group = group.toLowerCase();
        }
        SimpleChunk sc = new SimpleChunk(c.getX(), c.getZ());
        chunkSave.put(sc, group);
    }

    private void saveGroupInfoToFile(GroupInfo g, String name) {
        if (name != null) {
            name = name.toLowerCase();
        }
        GroupInfo sg = new GroupInfo(name, g.leader);
        groupSave.put(name, sg);
    }

    private void savePlayerGroupToFile(Player p, String group) {
        if (group != null) {
            group = group.toLowerCase();
        }
        UUID uuid = p.getUniqueId();
        playerSave.put(uuid, group);
    }
//</editor-fold>
}
