/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oddwhirled.groupclaim;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

/**
 *
 * @author Drew
 */
public class GroupCommands implements TabExecutor {

    private DataStore data = DataStore.instance();

    @Override
    public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return new ArrayList<String>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if (args.length < 1) {
            return false;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("Please use this command in-game");
            return true;
        }
        Player p = (Player) sender;
        switch (args[0]) {
            case "claim":
                data.addClaim(data.getGroup(p), p.getLocation().getChunk());
                break;
            case "unclaim":
                data.removeClaim(p.getLocation().getChunk());
                break;
            case "create":
                if (args.length < 2) {
                    return false;
                } else {
                    data.joinGroup(p, args[1]);
                    data.setLeader(args[1], p);
                }
                break;
            case "leave":
                data.leaveGroup(p);
                break;
            case "join":
                if (args.length < 2) {
                    return false;
                } else {
                    data.joinGroup(p, args[1]);
                }
                break;
            case "info":
                p.sendMessage("You are in group " + String.valueOf(data.getGroup(p)));
                break;
            default:
                return false;
        }
        return true;
    }

}
