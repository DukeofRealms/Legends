package com.mcthepond.champs.bukkit.core;

import org.bukkit.plugin.java.JavaPlugin;

import com.mcthepond.champs.library.cplayer.CPlayer;
import com.mcthepond.champs.library.permissions.PermissionChecker;

/**
 * @author B2OJustin
 */
public class BukkitPermissionChecker implements PermissionChecker {
    private JavaPlugin plugin;

    public BukkitPermissionChecker(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean hasPermission(CPlayer cPlayer, String permission) {
        return plugin.getServer().getPlayerExact(cPlayer.getName()).hasPermission(permission);
    }

}
