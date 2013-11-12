/*******************************************************************************
 * This file is part of Champions.
 *
 *     Champions is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Champions is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Champions.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package com.mcthepond.champs.bukkit.core;

import com.mcthepond.champs.library.commands.CommandSender;
import com.mcthepond.champs.library.commands.ConsoleCommandSender;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.mcthepond.champs.library.cplayer.CPlayer;
import com.mcthepond.champs.library.messaging.Messenger;

/**
 * @author B2OJustin
 */
public class BukkitMessenger implements Messenger {
    private JavaPlugin plugin;

    public BukkitMessenger(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean send(CommandSender sender, String message) {
        if (sender instanceof ConsoleCommandSender) {
            Bukkit.getConsoleSender().sendMessage(message);
            return true;
        }
        plugin.getServer().getPlayerExact(sender.getName()).sendMessage(message);
        return true;
    }

}
