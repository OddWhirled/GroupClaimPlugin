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
public class CommandDisband extends GroupCommand {

    public CommandDisband() {
        super("disband");
    }

    @Override
    public boolean run(Player p, String... args) {
        DataStore d = DataStore.instance();
        String group = d.getGroup(p);
        if (group == null) {
            p.sendMessage(msg("notInGroup"));
        } else if (!d.isLeader(p)) {
            p.sendMessage(msg("notLeader"));
        } else if (args.length < 1 || !args[0].equals("confirm")) {
            p.sendMessage(msg("confirmDisband"));
        } else {
            p.sendMessage(msg("groupDisbanded", d.getGroupDisplayName(group)));
            d.removeGroup(group);
        }
        return true;
    }
}
