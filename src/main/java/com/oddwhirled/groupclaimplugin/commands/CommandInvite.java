/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oddwhirled.groupclaimplugin.commands;

import com.oddwhirled.groupclaimplugin.storage.DataStore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static com.oddwhirled.groupclaimplugin.Messages.msg;

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
        String group = d.getGroup(p);
        if (group == null) {
            p.sendMessage(msg("notInGroup"));
        } else if (!d.isLeader(p)) {
            p.sendMessage(msg("notLeader"));
        } else {
            Player invite = Bukkit.getPlayer(args[0]);
            if(invite == null) {
                p.sendMessage(msg("cantFindPlayer"));
            } else {
                d.addInvite(invite, group);
                p.sendMessage(msg("playerInvited", invite.getName()));
            }
        }
        return true;
    }
}
