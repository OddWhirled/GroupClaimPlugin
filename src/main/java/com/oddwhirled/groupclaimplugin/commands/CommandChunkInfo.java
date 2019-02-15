/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oddwhirled.groupclaimplugin.commands;

import com.oddwhirled.groupclaim.DataStore;
import org.bukkit.entity.Player;

/**
 *
 * @author Drew
 */
public class CommandChunkInfo extends GroupCommand {

    public CommandChunkInfo() {
        super("chunkinfo");
    }

    @Override
    public boolean run(Player p, String... args) {
        p.sendMessage(DataStore.instance().getGroup(p.getLocation().getChunk()));
        return true;
    }
}
