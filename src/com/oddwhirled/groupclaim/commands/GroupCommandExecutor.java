/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oddwhirled.groupclaim.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

/**
 *
 * @author Drew
 */
public class GroupCommandExecutor implements TabExecutor {

    private static HashMap<String, GroupCommand> register = new HashMap<>();

    public static void register(String name, GroupCommand command) {
        register.put(name, command);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (args.length == 1) {
            return new ArrayList<>(register.keySet());
        } else if(args.length >= 1) {
            return register.get(args[0]).onTabComplete(args);
        } else {
            return null;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player) && sender != null) {
            sender.sendMessage("Please use this command in-game");
            return true;
        }

        Player p = (Player) sender;

        if (args.length < 1) {
            register.get("info").run(p, args);
            return true;
        } else {
            GroupCommand gcmd = register.get(args[0]);
            if(gcmd == null) {
                return false;
            } else {
                gcmd.run(p, args);
                return true;
            }
        }
    }
}
