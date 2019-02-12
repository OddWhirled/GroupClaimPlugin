/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oddwhirled.groupclaim;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.weather.LightningStrikeEvent;

/**
 *
 * @author Drew
 */
public class ProtectionListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        boolean perm = BuildPermission.checkBuildPermissions(e.getPlayer(), e.getBlock().getLocation());
        if (!perm) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        boolean perm = BuildPermission.checkBuildPermissions(e.getPlayer(), e.getBlock().getLocation());
        if (!perm) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onHangingPlace(HangingPlaceEvent e) {
        boolean perm = BuildPermission.checkBuildPermissions(e.getPlayer(), e.getEntity().getLocation());
        if (!perm) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onHangingBreak(HangingBreakByEntityEvent e) {
        boolean perm = BuildPermission.checkBuildPermissions(e.getRemover(), e.getEntity().getLocation());
        if (!perm) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemFrameClick(PlayerInteractEntityEvent e) {
        if (e.getRightClicked().getType().equals(EntityType.ITEM_FRAME)) {
            boolean perm = BuildPermission.checkBuildPermissions(e.getPlayer(), e.getRightClicked().getLocation());
            if (!perm) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityBreak(EntityDamageByEntityEvent e) {
        //e.getDamager().sendMessage("You damaged a " + e.getEntityType().toString());
        EntityType et = e.getEntityType();
        if (et.equals(EntityType.ARMOR_STAND) || et.equals(EntityType.ENDER_CRYSTAL) || et.equals(EntityType.ITEM_FRAME)) {
            boolean perm = BuildPermission.checkBuildPermissions(e.getDamager(), e.getEntity().getLocation());
            if (!perm) {
                e.setCancelled(true);
            }
        }
    }

//    @EventHandler
//    public void onEntityPlace(EntityPlaceEvent e) {
//        //e.getPlayer().sendMessage("You placed a " + e.getEntityType().toString());
//        EntityType et = e.getEntityType();
//        if (et.equals(EntityType.ARMOR_STAND) || et.equals(EntityType.ENDER_CRYSTAL) || et.equals(EntityType.MINECART_TNT)) {
//            boolean perm = BuildPermission.checkBuildPermissions(e.getPlayer(), e.getEntity().getLocation());
//            if (!perm) {
//                e.setCancelled(true);
//            }
//        }
//    }
    @EventHandler
    public void onEntityPlace(PlayerInteractEvent e) {
        //e.getLocation();
        //e.getPlayer().sendMessage("You placed a " + e.getEntityType().toString());

        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Material m = e.getItem().getType();
            if (m.equals(Material.ARMOR_STAND) || m.equals(Material.END_CRYSTAL) || m.equals(Material.TNT_MINECART)) {
                Location loc = null;
                switch (m) {
                    case ARMOR_STAND:
                        loc = e.getClickedBlock().getRelative(e.getBlockFace()).getLocation();
                        break;
                    case END_CRYSTAL:
                        loc = e.getClickedBlock().getRelative(BlockFace.UP).getLocation();
                        break;
                    case TNT_MINECART:
                        loc = e.getClickedBlock().getLocation();
                        break;
                    default:
                        break;

                }
                boolean perm = BuildPermission.checkBuildPermissions(e.getPlayer(), loc);
                if (!perm) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onSpread(BlockSpreadEvent e) {
        boolean perm = BuildPermission.checkBuildPermissions(e.getSource(), e.getBlock().getLocation());
        if (!perm) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onFlow(BlockFromToEvent e) {
        boolean perm = BuildPermission.checkBuildPermissions(e.getBlock(), e.getToBlock().getLocation());
        if (!perm) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDispense(BlockDispenseEvent e) {
        Material m = e.getItem().getType();
        if (checkDispenserItem(m)) {

            Block b = e.getBlock();
            org.bukkit.block.data.type.Dispenser d = (org.bukkit.block.data.type.Dispenser) b.getBlockData();
            BlockFace bf = d.getFacing();
            Location loc = b.getRelative(bf).getLocation();

            boolean perm = BuildPermission.checkBuildPermissions(b, loc);
            if (!perm) {
                e.setCancelled(true);
            }
        }

    }

    private boolean checkDispenserItem(Material m) {
        return (m.equals(Material.WATER_BUCKET) || m.equals(Material.LAVA_BUCKET)
                || m.equals(Material.COD_BUCKET) || m.equals(Material.SALMON_BUCKET)
                || m.equals(Material.TROPICAL_FISH_BUCKET) || m.equals(Material.PUFFERFISH_BUCKET)
                || m.equals(Material.BUCKET) || m.equals(Material.TNT)
                || m.equals(Material.BONE_MEAL) || m.equals(Material.TNT_MINECART)
                || m.equals(Material.FIRE_CHARGE) || m.equals(Material.FLINT_AND_STEEL)
                || m.equals(Material.CARVED_PUMPKIN) || m.equals(Material.SHULKER_BOX)
                || m.equals(Material.WITHER_SKELETON_SKULL));
    }

//    @EventHandler
//    public void onPistonPush() {
//
//    }
//
//    @EventHandler
//    public void onExplosion() {
//
//    }
//    @EventHandler
//    public void onTridentLightning(ProjectileHitEvent e) {
//        if (e.getEntity().getType().equals(EntityType.TRIDENT)) {
//            e.get
//        }
//    }
    @EventHandler
    public void onIgnite(BlockIgniteEvent e) {
        if (GroupClaim.DEBUG) {
            Bukkit.getServer().broadcastMessage("BlockIgniteEvent Called");
        }
        if (e.getCause().equals(IgniteCause.LIGHTNING)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onLightningStrike(LightningStrikeEvent e) {
        if (e.getCause().equals(LightningStrikeEvent.Cause.TRIDENT) && !e.isCancelled()) {
            Block b = e.getLightning().getLocation().getBlock();
            if (b.getType().equals(Material.AIR)) {
                b.setType(Material.FIRE);
            }
        }
    }

    @EventHandler
    public void onEntityExplosion(EntityExplodeEvent e) {
        if (GroupClaim.DEBUG) {
            Bukkit.getServer().broadcastMessage("EntityExplodeEvent Called");
        }
        boolean perm;
        for (Block b : e.blockList()) {
            if (BuildPermission.getGroup(b) != null) {
                perm = BuildPermission.checkBuildPermissions(e.getEntity(), b.getLocation());
                if (!perm) {
                    e.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent e) {
        //Don't get the block clicked but rather the block relative to it
        //This will be the liquid block
        BlockFace bf = e.getBlockFace();
        Location loc = e.getBlockClicked().getRelative(bf).getLocation();

        boolean perm = BuildPermission.checkBuildPermissions(e.getPlayer(), loc);
        if (!perm) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent e) {
        BlockFace bf = e.getBlockFace();
        Location loc = e.getBlockClicked().getRelative(bf).getLocation();

        boolean perm = BuildPermission.checkBuildPermissions(e.getPlayer(), loc);
        if (!perm) {
            e.setCancelled(true);
        }
    }

    //Testing events
    @EventHandler
    public void onEvent(EntityTransformEvent e) {
        if (GroupClaim.DEBUG) {
            Bukkit.getServer().broadcastMessage("Event triggered: " + e.toString());
        }
    }

    @EventHandler
    public void onEvent(ExplosionPrimeEvent e) {
        if (GroupClaim.DEBUG) {
            Bukkit.getServer().broadcastMessage("Event triggered: " + e.toString());
        }
    }

    @EventHandler
    public void onEvent(EntitySpawnEvent e) {
        if (GroupClaim.DEBUG) {
            Bukkit.getServer().broadcastMessage("Event triggered: " + e.toString());
        }
    }

    @EventHandler
    public void onEvent(EntityChangeBlockEvent e) {
        if (GroupClaim.DEBUG) {
            Bukkit.getServer().broadcastMessage("EntityChangeBlockEvent Called");
        }
    }
//

    @EventHandler
    public void onEvent(BlockRedstoneEvent e) {
        if (e.getBlock().getType().equals(Material.TNT)) {
            Bukkit.getServer().broadcastMessage("Event triggered: " + e.toString());
        }
    }
//
//    @EventHandler
//    public void onEvent(EntityTransformEvent e) {
//        Bukkit.getServer().broadcastMessage("Event triggered: " + e.toString());
//    }
//
//    @EventHandler
//    public void onEvent(EntityTransformEvent e) {
//        Bukkit.getServer().broadcastMessage("Event triggered: " + e.toString());
//    }
}
