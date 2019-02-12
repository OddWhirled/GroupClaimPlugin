/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oddwhirled.groupclaim.commands;

import com.oddwhirled.groupclaim.DataStore;
import com.oddwhirled.groupclaim.GroupClaimPlugin;
import com.oddwhirled.groupclaim.Messages;
import java.util.List;
import org.bukkit.entity.Player;

/**
 *
 * @author Drew
 */
public class CommandDisband extends GroupCommand {

    public CommandDisband() {
        super("disband");
    }

    @Override
    public boolean run(Player p, String[] args) {
        DataStore d = DataStore.instance();
        Messages m = GroupClaimPlugin.messages;
        String group = d.getGroup(p);
        if (group == null) {
            p.sendMessage(m.NOT_IN_GROUP);
        } else if (!d.isLeader(p)) {
            p.sendMessage(m.NOT_LEADER);
        } else if (args.length < 1 || !args[0].equals("confirm")) {
            p.sendMessage(m.CONFIRM_DISBAND);
        } else {
            p.sendMessage(String.format(m.DISBANDED_GROUP, d.getGroupDisplayName(group)));
            d.removeGroup(group);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(String[] args) {
        //Don't tab complete "confirm" for safety purposes
        return null;
    }
}
