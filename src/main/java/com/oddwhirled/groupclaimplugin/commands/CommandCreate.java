/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oddwhirled.groupclaimplugin.commands;

import com.oddwhirled.groupclaim.DataStore;
import org.bukkit.entity.Player;

import static com.oddwhirled.groupclaim.Messages.msg;

/**
 *
 * @author Drew
 */
public class CommandCreate extends GroupCommand {

    public CommandCreate() {
        super("create");
    }

    @Override
    public boolean run(Player p, String... args) {
        if (args.length < 1) {
            return false;
        }
        DataStore d = DataStore.instance();
        String group = d.getGroup(p);
        if (group != null) {
            p.sendMessage(msg("alreadyInGroup"));
        } else {
            boolean free = d.addGroup(args[0], p);
            if (free) {
                p.sendMessage(msg("groupCreated", args[0]));
            } else {
                p.sendMessage(msg("groupAlreadyExists"));
            }
        }
        return true;
    }
}
