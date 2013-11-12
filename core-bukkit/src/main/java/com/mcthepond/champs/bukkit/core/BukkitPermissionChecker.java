package com.mcthepond.champs.bukkit.core;

import com.mcthepond.champs.library.commands.CommandSender;
import com.mcthepond.champs.library.commands.ConsoleCommandSender;
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
    public boolean hasPermission(CommandSender sender, String permission) {
        if (sender instanceof ConsoleCommandSender) {
            return true;
        }
        return plugin.getServer().getPlayerExact(sender.getName()).hasPermission(permission);
    }

}
