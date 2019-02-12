/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oddwhirled.groupclaim;

import java.util.HashMap;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

/**
 *
 * @author Drew
 */
public class DataStore {

    private static DataStore instance;

    private HashMap<Chunk, String> chunkCache = new HashMap<>();
    private HashMap<Player, String> playerCache = new HashMap<>();
    private HashMap<String, Player> groups = new HashMap<>();

    public static DataStore instance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    private DataStore() {
    }
    
    public boolean addGroup(String group, Player leader) {
        group = group.toLowerCase();
        if(!groups.containsKey(group)) {
            groups.put(group, leader);
            return true;
        }
        return false;
    }
    
    public void removeGroup(String group) {
        group = group.toLowerCase();
        groups.remove(group);
    }

    public boolean addClaim(String group, Chunk chunk) {
        if (chunkCache.containsKey(chunk)) {
            return false;
        } else {
            chunkCache.put(chunk, group);
            return true;
        }
    }

    public boolean removeClaim(Chunk chunk) {
        return !(chunkCache.remove(chunk) == null);
    }

    public String getGroup(Chunk chunk) {
        return chunkCache.get(chunk);
    }

    public String getGroup(Player p) {
        return playerCache.get(p);
    }

    public Player getLeader(String group) {
        return groups.get(group);
    }

    public boolean setLeader(String group, Player p) {
        if (group.equals(DataStore.this.getGroup(p))) {
            groups.put(group, p);
            return true;
        }
        return false;
    }

    public boolean joinGroup(Player p, String group) {
        if (DataStore.this.getGroup(p) == null) {
            setGroup(p, group);
            return true;
        }
        return false;
    }
    
    public void leaveGroup(Player p) {
        playerCache.remove(p);
    }

    public void setGroup(Player p, String group) {
        playerCache.put(p, group);
    }
}
