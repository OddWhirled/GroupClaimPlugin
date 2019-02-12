/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oddwhirled.groupclaim;

import com.oddwhirled.groupclaim.commands.*;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Drew
 */
public class GroupClaimPlugin extends JavaPlugin {
    
    private static GroupClaimPlugin instance;
    public static Messages messages;
    
    public static final boolean DEBUG = true;

    @Override
    public void onEnable() {
        getCommand("group").setExecutor(new GroupCommandExecutor());
        PluginManager plm = getServer().getPluginManager();
        plm.registerEvents(new ProtectionListener(), this);
        plm.registerEvents(new CacheListener(), this);
        
        this.saveDefaultConfig();
        instance = this;
        messages = new Messages();
        
        new CommandCreate().register();
        new CommandInfo().register();
        new CommandLeave().register();
        new CommandDisband().register();
        new CommandClaim().register();
        new CommandChunkInfo().register();
    }

    @Override
    public void onDisable() {

    }
    
    public static GroupClaimPlugin instance() {
        return instance;
    }
}
