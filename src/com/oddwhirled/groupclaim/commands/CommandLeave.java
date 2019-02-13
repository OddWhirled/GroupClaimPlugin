/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oddwhirled.groupclaim.commands;

import com.oddwhirled.groupclaim.DataStore;
import com.oddwhirled.groupclaim.GroupClaimPlugin;
import com.oddwhirled.groupclaim.Messages;
import org.bukkit.entity.Player;

/**
 *
 * @author Drew
 */
public class CommandLeave extends GroupCommand {

    public CommandLeave() {
        super("leave");
    }

    @Override
    public boolean run(Player p, String... args) {
        DataStore d = DataStore.instance();
        Messages m = GroupClaimPlugin.messages;
        String group = d.getGroup(p);
        if (group == null) {
            p.sendMessage(m.NOT_IN_GROUP);
        } else if (d.isLeader(p)) {
            p.sendMessage(m.LEADER_CANT_LEAVE);
        } else {
            d.leaveGroup(p);
            p.sendMessage(String.format(m.LEFT_GROUP, d.getGroupDisplayName(group)));
        }
        return true;
    }

}
