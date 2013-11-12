package com.mcthepond.champs.library.commands;

import com.mcthepond.champs.library.messaging.MessageHandler;
import com.mcthepond.champs.library.messaging.Messenger;

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
    public void sendMessage(String message) {
        MessageHandler.sendMessage(this, message);
    }

}
