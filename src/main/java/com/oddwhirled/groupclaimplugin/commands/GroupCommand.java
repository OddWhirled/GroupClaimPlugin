/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oddwhirled.groupclaimplugin.commands;

import com.oddwhirled.groupclaimplugin.GroupCommandExecutor;
import java.util.List;
import org.bukkit.entity.Player;

/**
 *
 * @author Drew
 */
public abstract class GroupCommand {

    protected String name;

    protected GroupCommand(String name) {
        this.name = name;
    }

    public void register() {
        GroupCommandExecutor.register(name, this);
    }

    public List<String> onTabComplete(String... args) {
        return null;
    }
    
    public abstract boolean run(Player p, String... args);
}
