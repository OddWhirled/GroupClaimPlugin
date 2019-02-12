/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oddwhirled.groupclaim;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

/**
 *
 * @author Drew
 */
public class CacheListener implements Listener {
    
    private DataStore data = DataStore.instance();
    
    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent e) {
        data.unCache(e.getChunk());
    }
    
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        data.unCache(e.getPlayer());
    }
    
}
