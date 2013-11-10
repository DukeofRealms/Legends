package com.mcthepond.champs.bukkit.core;

import com.mcthepond.champs.bukkit.core.commands.ClassCommandExecutor;
import com.mcthepond.champs.bukkit.core.listeners.BasicListener;
import com.mcthepond.champs.bukkit.core.listeners.EventBridgeListener;
import com.mcthepond.champs.library.Configuration;
import com.mcthepond.champs.library.database.YAMLDataSource;
import com.mcthepond.champs.library.event.BaseListener;
import com.mcthepond.champs.library.event.EventManager;
import com.mcthepond.champs.library.messaging.MessageHandler;
import com.mcthepond.champs.library.permissions.PermissionHandler;
import com.mcthepond.champs.library.server.ServerBridge;
import com.mcthepond.champs.library.util.PlatformUtil;
import com.mcthepond.champs.library.util.ResourceUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author YoshiGenius
 */
public class BukkitChampionsServer extends ServerBridge {
    private static Logger logger = Logger.getLogger(BukkitChampionsServer.class.getName());
    private static String JAR_RESOURCE_DIRECTORY = "resources/";
    private static String CONFIG_PATH = "Champions/";
    private static String MAIN_CONFIG_FILE = "config.yml";

    JavaPlugin plugin;

    public BukkitChampionsServer(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getServerVersion() {
        return Bukkit.getServer().getVersion();
    }

    @Override
    public String getCoreVersion() {
        return ChampionsCore.getInstance().getDescription().getVersion();
    }

    @Override
    public String getLibVersion() {
        return "0.0.1";
    }

    @Override
    public PlatformUtil.PlatformType getServerPlatform() {
        return PlatformUtil.PlatformType.BUKKIT;
    }

    @Override
    public String getIp() {
        return Bukkit.getIp();
    }

    @Override
    public int getPort() {
        return Bukkit.getPort();
    }

    @Override
    public void loadConfiguration() {
        try {
            // Copy default configuration files
            logger.info("Copying default configuration files...");
            int filesCopied = ResourceUtil.copyDirectoryFromJar(ChampionsCore.class, JAR_RESOURCE_DIRECTORY, CONFIG_PATH, false);
            logger.info(String.format("Copied %d files", filesCopied));

            // Load configuration
            YAMLDataSource yamlDataSource = new YAMLDataSource(CONFIG_PATH);
            yamlDataSource.loadConfiguration(Configuration.getInstance(), MAIN_CONFIG_FILE);
        } catch (IOException e) {
            logger.severe("Could not write default configuration files to disk.");
        }
    }

    @Override
    public void registerMessengers() {
        MessageHandler.register(new BukkitMessenger(ChampionsCore.getInstance()));
    }

    @Override
    public void registerEvents() {
        plugin.getServer().getPluginManager().registerEvents(new EventBridgeListener(), plugin);
        EventManager.registerEvents(new BaseListener());
        EventManager.registerEvents(new BasicListener());
    }

    @Override
    public void registerPermissions() {
        PermissionHandler.register(new BukkitPermissionChecker(plugin));
    }

    @Override
    public void registerCommands() {
        plugin.getCommand("class").setExecutor(new ClassCommandExecutor());
    }
}
