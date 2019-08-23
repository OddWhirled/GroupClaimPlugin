/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oddwhirled.groupclaimplugin;

import com.oddwhirled.groupclaimplugin.storage.DataStore;
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
        if (BuildPermission.determineGroup(loc) == null) {
            return true;
        }
        //otherwise check if the group of the source and destination are the same
        return BuildPermission.determineGroup(loc).equals(BuildPermission.determineGroup(e));
    }

    public static boolean checkBuildPermissions(Block b, Location loc) {
        //if the location in question isn't owned always allow
        if (BuildPermission.determineGroup(loc) == null) {
            return true;
        }
        //otherwise check if the group of the source and destination are the same
        return BuildPermission.determineGroup(loc).equals(BuildPermission.determineGroup(b));
    }

    public static String determineGroup(Location loc) {
        return data.getGroup(loc.getChunk());
    }

    public static String determineGroup(Block b) {
        return data.getGroup(b.getChunk());
    }

    public static String determineGroup(Player p) {
        return data.getGroup(p);
    }

    public static String determineGroup(Entity e) {

        if (e == null) {
            return null;
        }

        if (e instanceof Player) {
            return BuildPermission.determineGroup((Player) e);
            
        } else if (e instanceof Monster) {
            return BuildPermission.determineGroup(((Mob) e).getTarget());
            
        } else if (e instanceof Projectile) {
            ProjectileSource ps = ((Projectile) e).getShooter();
            
            if (ps instanceof BlockProjectileSource) {
                return BuildPermission.determineGroup(((BlockProjectileSource) ps).getBlock());
            } else {
                return BuildPermission.determineGroup((Entity) ps);
            }
        }

        EntityType et = e.getType();
        if (et.equals(EntityType.PRIMED_TNT)) {
            return BuildPermission.determineGroup(((TNTPrimed) e).getSource());
            //TODO: check if source is null and see where this came from using metadata
        }

        return BuildPermission.determineGroup(e.getLocation());
    }
}
