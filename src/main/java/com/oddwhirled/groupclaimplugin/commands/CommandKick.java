/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oddwhirled.groupclaimplugin.commands;

import org.bukkit.entity.Player;

/**
 *
 * @author Drew
 */
public class CommandKick extends GroupCommand {

    public CommandKick() {
        super("kick");
    }

    @Override
    public boolean run(Player p, String... args) {
        return false;
    }
}
