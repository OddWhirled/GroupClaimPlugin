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
public class CommandClaim extends GroupCommand {

    public CommandClaim() {
        super("claim");
    }

    @Override
    public boolean run(Player p, String... args) {
        DataStore d = DataStore.instance();
        String group = d.getGroup(p);
        if (group == null) {
            p.sendMessage(msg("notInGroup"));
        } else {
            boolean free = d.addClaim(group, p.getLocation().getChunk());
            if (free) {
                p.sendMessage(msg("chunkClaimed", d.getGroupDisplayName(group)));
            } else {
                p.sendMessage(msg("chunkAlreadyClaimed"));
            }
        }
        return true;
    }
}
