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

    //@temporary class to simulate a database
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

    private HashMap<Player, String> pendingInvites = new HashMap<>();

    public static DataStore instance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    private DataStore() {
    }

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
        //temporary, might be more efficient later
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
            chunkCache.put(chunk, fileGetChunkGroup(chunk.getX(), chunk.getZ()));
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
        if (g != null) {
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
//<editor-fold defaultstate="collapsed" desc="save and load methods done">
    //TEMPORARY MAPS TO SIMULATE FILE STORAGE
    private HashMap<SimpleChunk, String> chunkSave = new HashMap<>();
    private HashMap<UUID, String> playerSave = new HashMap<>();
    private HashMap<String, GroupInfo> groupSave = new HashMap<>();

    //load
    private String fileGetChunkGroup(int x, int z) {
        for (SimpleChunk sc : chunkSave.keySet()) {
            if (sc.x == x && sc.z == z) {
                return chunkSave.get(sc);
            }
        }
        return null;
    }

    private GroupInfo fileGetGroupInfo(String name) {
        return groupSave.get(name);
    }

    private String fileGetPlayerGroup(UUID uuid) {
        return playerSave.get(uuid);
    }

    //save
    private void fileUpdateChunkGroup(Chunk c, String group) {
        if (group != null) {
            group = group.toLowerCase();
        } else {
            fileRemoveChunk(c);
            return;
        }
        SimpleChunk sc = new SimpleChunk(c.getX(), c.getZ());
        chunkSave.put(sc, group);
    }

    private void fileRemoveChunk(Chunk c) {
        int x = c.getX();
        int z = c.getZ();
        for (SimpleChunk sc : chunkSave.keySet()) {
            if (sc.x == x && sc.z == z) {
                chunkSave.remove(sc);
            }
        }
    }

    private void fileUpdateGroupInfo(GroupInfo g, String name) {
        if (name != null) {
            name = name.toLowerCase();
        } else {
            fileRemoveGroup(name);
            return;
        }
        GroupInfo sg = new GroupInfo(name, g.leader);
        groupSave.put(name, sg);
    }

    private void fileRemoveGroup(String group) {
        groupSave.remove(group);
        while (playerSave.containsValue(group)) {
            playerSave.values().remove(group);
        }
        while (chunkSave.containsValue(group)) {
            chunkSave.values().remove(group);
        }
    }

    private void fileUpdatePlayerGroup(Player p, String group) {
        if (group != null) {
            group = group.toLowerCase();
        } else {
            fileRemovePlayer(p);
            return;
        }
        UUID uuid = p.getUniqueId();
        playerSave.put(uuid, group);
    }

    private void fileRemovePlayer(Player p) {
        UUID uuid = p.getUniqueId();
        for (UUID u : playerSave.keySet()) {
            if (uuid.equals(u)) {
                playerSave.remove(u);
            }
        }
    }
//</editor-fold>
}
