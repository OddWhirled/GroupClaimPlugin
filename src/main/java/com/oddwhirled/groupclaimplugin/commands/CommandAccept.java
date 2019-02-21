/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oddwhirled.groupclaimplugin.commands;

import com.oddwhirled.groupclaimplugin.storage.DataStore;
import org.bukkit.entity.Player;

import static com.oddwhirled.groupclaimplugin.Messages.msg;
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
        String group = d.getGroup(p);
        if (group != null) {
            p.sendMessage(msg("notInGroup"));
        } else {
            String groupInvite = d.clearInvite(p);
            if (groupInvite == null) {
                p.sendMessage(msg("noInvite"));
            } else {
                d.joinGroup(p, groupInvite);
                p.sendMessage(msg("joinedGroup", d.getGroupDisplayName(groupInvite)));
            }
        }
        return true;
    }

}
