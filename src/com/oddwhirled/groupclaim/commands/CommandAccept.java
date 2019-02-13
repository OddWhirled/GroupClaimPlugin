/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oddwhirled.groupclaim.commands;

import com.oddwhirled.groupclaim.DataStore;
import com.oddwhirled.groupclaim.GroupClaimPlugin;
import com.oddwhirled.groupclaim.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 *
 * @author Drew
 */
public class CommandAccept extends GroupCommand {

    public CommandAccept() {
        super("accept");
    }

    @Override
    public boolean run(Player p, String... args) {
        DataStore d = DataStore.instance();
        Messages m = GroupClaimPlugin.messages;
        String group = d.getGroup(p);
        if (group != null) {
            p.sendMessage(m.ALREADY_IN_GROUP);
        } else {
            String groupInvite = d.clearInvite(p);
            if (groupInvite == null) {
                p.sendMessage(m.NOT_INVITED);
            } else {
                d.joinGroup(p, groupInvite);
                p.sendMessage(String.format(m.JOINED_GROUP, d.getGroupDisplayName(groupInvite)));
            }
        }
        return true;
    }

}
