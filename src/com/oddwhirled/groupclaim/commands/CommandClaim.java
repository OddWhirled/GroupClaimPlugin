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
public class CommandClaim extends GroupCommand {

    public CommandClaim() {
        super("claim");
    }

    @Override
    public boolean run(Player p, String[] args) {
        DataStore d = DataStore.instance();
        Messages m = GroupClaimPlugin.messages;
        String group = d.getGroup(p);
        if(group == null) {
            p.sendMessage(m.NOT_IN_GROUP);
        } else {
            boolean claimed = d.addClaim(group, p.getLocation().getChunk());
            if(claimed) {
                p.sendMessage(m.CHUNK_ALREADY_CLAIMED);
            } else {
                p.sendMessage(String.format(m.CLAIMED_CHUNK, d.getGroupDisplayName(group)));
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(String[] args) {
        return null;
    }
    
}
