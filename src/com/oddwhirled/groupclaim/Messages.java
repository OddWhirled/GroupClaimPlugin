/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oddwhirled.groupclaim;

import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author Drew
 */
public class Messages {

    private final GroupClaimPlugin plugin = GroupClaimPlugin.instance();
    FileConfiguration c = plugin.getConfig();
    
    public final String ALREADY_IN_GROUP = c.getString("messageAlreadyInGroup");
    public final String NOT_IN_GROUP = c.getString("messageNotInGroup");
    public final String JOINED_GROUP = c.getString("messageJoinedGroup");
    public final String CLAIMED_CHUNK = c.getString("messageClaimedChunk");
    public final String UNCLAIMED_CHUNK = c.getString("messageUnclaimedChunk");
    public final String LEFT_GROUP = c.getString("messageLeftGroup");
    public final String INVITED_PLAYER = c.getString("messageInvitedPlayer");
    public final String NOT_INVITED = c.getString("messageNotInvited");
    public final String CONFIRM_DISBAND = c.getString("messageConfirmDisband");
    public final String DISBANDED_GROUP = c.getString("messageDisbandedGroup");
    public final String KICKED_PLAYER = c.getString("messageKickedPlayer");
    public final String SET_OWNER = c.getString("messageSetOwner");
    public final String CREATED_GROUP = c.getString("messageCreatedGroup");
    public final String LEADER_CANT_LEAVE = c.getString("messageLeaderCantLeave");
    public final String NOT_LEADER = c.getString("messageNotLeader");
    public final String CHUNK_ALREADY_CLAIMED = c.getString("messageChunkAlreadyClaimed");
    public final String GROUP_INFO = c.getString("messageGroupInfo");
    public final String COULDNT_FIND_PLAYER = c.getString("messageCouldntFindPlayer");

    public Messages() {

    }

}
