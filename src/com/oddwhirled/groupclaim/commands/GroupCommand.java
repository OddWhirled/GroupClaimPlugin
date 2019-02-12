/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oddwhirled.groupclaim.commands;

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

    public abstract boolean run(Player p, String[] args);

    public abstract List<String> onTabComplete(String[] args);
}
