/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oddwhirled.groupclaim.commands;

import com.oddwhirled.groupclaim.DataStore;
import com.oddwhirled.groupclaim.GroupClaim;
import com.oddwhirled.groupclaim.Messages;
import java.util.List;
import org.bukkit.entity.Player;

/**
 *
 * @author Drew
 */
public class CommandCreate extends GroupCommand {

    public CommandCreate() {
        super("create");
    }

    @Override
    public boolean run(Player p, String[] args) {
        if(args.length < 2) {
            return false;
        }
        DataStore d = DataStore.instance();
        Messages m = GroupClaim.messages;
        String group = d.getGroup(p);
        if (group != null) {
            p.sendMessage(m.ALREADY_IN_GROUP);
        } else {
            d.addGroup(args[1], p);
            p.sendMessage(String.format(m.CREATED_GROUP, args[1]));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(String[] args) {
        return null;
    }

}
