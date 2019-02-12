/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oddwhirled.groupclaim.commands;

import com.oddwhirled.groupclaim.DataStore;
import com.oddwhirled.groupclaim.GroupClaim;
import com.oddwhirled.groupclaim.Messages;
import java.util.List;
import org.bukkit.entity.Player;

/**
 *
 * @author Drew
 */
public class CommandInfo extends GroupCommand {

    public CommandInfo() {
        super("info");
    }
    
    @Override
    public boolean run(Player p, String[] args) {
        DataStore d = DataStore.instance();
        Messages m = GroupClaim.messages;
        String group = d.getGroup(p);
        if(group == null) {
            p.sendMessage(m.NOT_IN_GROUP);
        } else {
            p.sendMessage(String.format(m.GROUP_INFO, d.getGroupDisplayName(group), d.getGroupLeaderUUID(group)));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(String[] args) {
        return null;
    }
    
}
