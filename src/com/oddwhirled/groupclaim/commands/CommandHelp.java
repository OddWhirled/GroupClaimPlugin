/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oddwhirled.groupclaim.commands;

import org.bukkit.entity.Player;

/**
 *
 * @author Drew
 */
public class CommandHelp extends GroupCommand {

    public CommandHelp() {
        super("help");
    }

    @Override
    public boolean run(Player p, String... args) {
        return false; //TODO: Implement method
    }
}
