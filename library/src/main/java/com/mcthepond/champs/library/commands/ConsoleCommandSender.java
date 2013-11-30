package com.mcthepond.champs.library.commands;

import com.mcthepond.champs.library.messaging.MessageHandler;

/**
 * @author YoshiGenius
 */
public class ConsoleCommandSender implements CommandSender {

    public static ConsoleCommandSender inst() {
        return new ConsoleCommandSender();
    }

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }

    @Override
    public String getName() {
        return "*CONSOLE*";
    }

    @Override
    public boolean sendMessage(String message) {
        MessageHandler.sendMessage(this, message);
        return false;
    }

}
