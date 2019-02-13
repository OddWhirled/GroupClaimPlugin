/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oddwhirled.groupclaim;

import java.util.HashMap;
import java.util.IllegalFormatException;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author Drew
 */
public class Messages {

    private FileConfiguration c = GroupClaimPlugin.instance().getConfig();

    private static HashMap<String, String> messages;

    public Messages() {
        messages = new HashMap<>();
        for (String message : c.getKeys(true)) {
            messages.put(message, c.getString(message));
        }
    }

    public static String message(String key, Object... args) {
        key = "messages." + key;
        String message = messages.get(key);
        try {
            message = String.format(message, args);
        } catch (Exception e) {
            message = ChatColor.RED + "Plugin could not find a message for this action";
        }
        return message;
    }

    public static String msg(String key, Object... args) {
        return message(key, args);
    }

}
