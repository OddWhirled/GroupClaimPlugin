/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oddwhirled.groupclaim;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.projectiles.ProjectileSource;

/**
 *
 * @author Drew
 */
public class BuildPermission {

    private static DataStore data = DataStore.instance();

    public static boolean checkBuildPermissions(Entity e, Location loc) {
        //if the location in question isn't owned always allow
        if (BuildPermission.getGroup(loc) == null) {
            return true;
        }
        //otherwise check if the group of the source and destination are the same
        return BuildPermission.getGroup(loc).equals(getGroup(e));
    }

    public static boolean checkBuildPermissions(Block b, Location loc) {
        //if the location in question isn't owned always allow
        if (BuildPermission.getGroup(loc) == null) {
            return true;
        }
        //otherwise check if the group of the source and destination are the same
        return BuildPermission.getGroup(loc).equals(BuildPermission.getGroup(b));
    }

    public static String getGroup(Location loc) {
        return data.getGroup(loc.getChunk());
    }

    public static String getGroup(Block b) {
        return data.getGroup(b.getChunk());
    }

    public static String getGroup(Entity e) {

        if (e == null) {
            return null;
        }

        if (e instanceof Player) {
            return BuildPermission.getGroup((Player) e);
        }

        if (e instanceof Monster) {
            return getGroup(((Mob) e).getTarget());
        }

        if (e instanceof Projectile) {
            ProjectileSource ps = ((Projectile) e).getShooter();
            if (ps instanceof BlockProjectileSource) {
                return BuildPermission.getGroup(((BlockProjectileSource) ps).getBlock());
            } else {
                return getGroup((Entity) ps);
            }
        }

        EntityType et = e.getType();
        if (et.equals(EntityType.PRIMED_TNT)) {
            return getGroup(((TNTPrimed) e).getSource());
            //TODO: check if source is null and see where this came from using metadata
        }

//        if (et.equals(EntityType.MINECART_TNT) || et.equals(EntityType.ENDER_CRYSTAL)) {
//            return BuildPermission.getGroup(e.getLocation());
//        }
        return BuildPermission.getGroup(e.getLocation());
    }

    public static String getGroup(Player p) {
        return data.getGroup(p);
    }
}
