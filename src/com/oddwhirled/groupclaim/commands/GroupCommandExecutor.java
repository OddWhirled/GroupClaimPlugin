/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oddwhirled.groupclaim.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

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
        sender.sendMessage(Arrays.toString(args));
        //if the arg length is 1 then return the full set of commands available after "group"
        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();
            StringUtil.copyPartialMatches(args[0], register.keySet(), suggestions);
            return suggestions;
            
        } else if (args.length > 1) {
            GroupCommand gcmd = register.get(args[0]);
            if (gcmd == null) {
                return null;
            } else {
                String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
                return gcmd.onTabComplete(newArgs);
            }
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

        //plain /group is an alias for /group info
        if (args.length < 1) {
            return register.get("info").run(p, args);

        } else {
            GroupCommand gcmd = register.get(args[0]);
            if (gcmd == null) {
                return false;
            } else {
                String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
                return gcmd.run(p, newArgs);
            }
        }
    }
}
