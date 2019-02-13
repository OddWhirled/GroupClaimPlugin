/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oddwhirled.groupclaim.commands;

import com.oddwhirled.groupclaim.DataStore;
import org.bukkit.entity.Player;

import static com.oddwhirled.groupclaim.Messages.msg;
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
        String group = d.getGroup(p);
        if (group == null) {
            p.sendMessage(msg("notInGroup"));
        } else if (d.isLeader(p)) {
            p.sendMessage(msg("leaderCantLeave"));
        } else {
            d.leaveGroup(p);
            p.sendMessage(msg("leftGroup", d.getGroupDisplayName(group)));
        }
        return true;
    }

}
