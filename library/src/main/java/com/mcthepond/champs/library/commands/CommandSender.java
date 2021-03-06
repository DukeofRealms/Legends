package com.mcthepond.champs.library.commands;

/**
 * @author YoshiGenius
 */
public interface CommandSender {

    public boolean hasPermission(String permission);

    public String getName();

    public boolean sendMessage(String message);
}
