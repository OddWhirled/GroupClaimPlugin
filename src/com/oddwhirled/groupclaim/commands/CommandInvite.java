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
public class CommandInvite extends GroupCommand {

    public CommandInvite() {
        super("invite");
    }

    @Override
    public boolean run(Player p, String... args) {
        if(args.length < 1) {
            return false;
        }
        DataStore d = DataStore.instance();
        Messages m = GroupClaimPlugin.messages;
        String group = d.getGroup(p);
        if (group == null) {
            p.sendMessage(m.NOT_IN_GROUP);
        } else if (!d.isLeader(p)) {
            p.sendMessage(m.NOT_LEADER);
        } else {
            Player invite = Bukkit.getPlayer(args[0]);
            if(invite == null) {
                p.sendMessage(m.COULDNT_FIND_PLAYER);
            } else {
                d.addInvite(invite, group);
                p.sendMessage(String.format(m.INVITED_PLAYER, invite.getName()));
            }
        }
        return true;
    }
}
