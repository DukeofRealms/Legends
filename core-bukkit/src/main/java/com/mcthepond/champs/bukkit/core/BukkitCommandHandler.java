package com.mcthepond.champs.bukkit.core;

import com.mcthepond.champs.library.commands.CommandHandler;
import com.mcthepond.champs.library.commands.CommandResult;
import com.mcthepond.champs.library.commands.SubCommand;
import com.mcthepond.champs.library.cplayer.CPlayer;
import com.mcthepond.champs.library.cplayer.CPlayerHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author B2OJustin
 */
public class BukkitCommandHandler implements CommandExecutor {
    private CPlayerHandler playerHandler = CPlayerHandler.getInstance();

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String lbl, String[] arg) {
        if(!(cs instanceof Player)) return false;
        CPlayer player = playerHandler.get(cs.getName());
        CommandResult result = CommandHandler.exec(player, lbl, arg);
        switch(result) {
            case FAILURE:
                return false;
            case SUCCESS:
                return true;
            case NO_PERMISSION:
                player.sendMessage(ChatColor.RED + "Sorry, you don't have permission to use that command.");
                return false;
            case BAD_ARG:
                SubCommand sub = CommandHandler.getSubCommand(arg[0]);
                if(sub == null) {
                    player.sendMessage(ChatColor.RED + "Invalid command.");
                } else {
                    player.sendMessage(sub.getHelpText());
                }
                return false;
            default:
                return false;
        }
    }
}
