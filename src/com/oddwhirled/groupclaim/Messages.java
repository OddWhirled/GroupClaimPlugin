/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oddwhirled.groupclaim;

/**
 *
 * @author Drew
 */
public class Messages {
    
    private final GroupClaimPlugin plugin = GroupClaimPlugin.instance();
    
    public final String ALREADY_IN_GROUP = plugin.getConfig().getString("messageAlreadyInGroup");
    public final String NOT_IN_GROUP = plugin.getConfig().getString("messageNotInGroup");
    public final String JOINED_GROUP = plugin.getConfig().getString("messageJoinedGroup");
    public final String CLAIMED_CHUNK = plugin.getConfig().getString("messageClaimedChunk");
    public final String UNCLAIMED_CHUNK =  plugin.getConfig().getString("messageUnclaimedChunk");
    public final String LEFT_GROUP =  plugin.getConfig().getString("messageLeftGroup");
    public final String INVITED_PLAYER =  plugin.getConfig().getString("messageInvitedPlayer");
    public final String CONFIRM_DISBAND = plugin.getConfig().getString("messageConfirmDisband");
    public final String DISBANDED_GROUP =  plugin.getConfig().getString("messageDisbandedGroup");
    public final String KICKED_PLAYER =  plugin.getConfig().getString("messageKickedPlayer");
    public final String SET_OWNER =  plugin.getConfig().getString("messageSetOwner");
    public final String CREATED_GROUP =  plugin.getConfig().getString("messageCreatedGroup");
    public final String LEADER_CANT_LEAVE =  plugin.getConfig().getString("messageLeaderCantLeave");
    public final String NOT_LEADER =  plugin.getConfig().getString("messageNotLeader");
    public final String CHUNK_ALREADY_CLAIMED =  plugin.getConfig().getString("messageChunkAlreadyClaimed");
    public final String GROUP_INFO =  plugin.getConfig().getString("messageGroupInfo");
    
    public Messages() {
    
    }
    
}
