/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oddwhirled.groupclaim;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Drew
 */
public class GroupClaim extends JavaPlugin {
    
    private static GroupClaim instance;

    @Override
    public void onEnable() {
        getCommand("group").setExecutor(new GroupCommands());
        PluginManager plm = getServer().getPluginManager();
        plm.registerEvents(new ProtectionListener(), this);
        instance = this;
    }

    @Override
    public void onDisable() {

    }
    
    public static GroupClaim instance() {
        return instance;
    }
}
