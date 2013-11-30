package com.mcthepond.champs.bukkit.core;

import com.mcthepond.champs.bukkit.core.commands.ClassCommandExecutor;
import com.mcthepond.champs.bukkit.core.listeners.BasicListener;
import com.mcthepond.champs.bukkit.core.listeners.EventBridgeListener;
import com.mcthepond.champs.bukkit.core.utils.LocationUtil;
import com.mcthepond.champs.library.CBlock;
import com.mcthepond.champs.library.CLocation;
import com.mcthepond.champs.library.CWorld;
import com.mcthepond.champs.library.configuration.ChampsConfiguration;
import com.mcthepond.champs.library.cplayer.CPlayer;
import com.mcthepond.champs.library.cplayer.CPlayerHandler;
import com.mcthepond.champs.library.database.YAMLDataSource;
import com.mcthepond.champs.library.event.BaseListener;
import com.mcthepond.champs.library.event.EventManager;
import com.mcthepond.champs.library.messaging.MessageHandler;
import com.mcthepond.champs.library.permissions.PermissionHandler;
import com.mcthepond.champs.library.server.ServerBridge;
import com.mcthepond.champs.library.util.PlatformUtil;
import com.mcthepond.champs.library.util.ResourceUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author YoshiGenius
 */
public class BukkitChampsServer extends ServerBridge {
    private static Logger logger = Logger.getLogger(BukkitChampsServer.class.getName());
    private static String JAR_RESOURCE_DIRECTORY = "resources/";
    private static String CONFIG_PATH = "Champions/";
    private static String MAIN_CONFIG_FILE = "config.yml";

    JavaPlugin plugin;

    public BukkitChampsServer(JavaPlugin plugin) {
        super("Champs|Bukkit");
        this.plugin = plugin;
    }

    @Override
    public String getServerVersion() {
        return Bukkit.getServer().getVersion();
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
    public String getChampsCoreVersion() {
        return ChampsCore.getInstance().getDescription().getVersion();
    }

    @Override
    public String getChampsLibVersion() {
        return "0.0.1";
    }

    @Override
    public CPlayer[] getOnlineCPlayers() {
        CPlayer[] players = new CPlayer[]{};
        for (Player p : Bukkit.getOnlinePlayers()) {
            CPlayer cPlayer = CPlayerHandler.getInstance().get(p.getName());
            if (cPlayer == null) {
                cPlayer = CPlayerHandler.getInstance().load(p.getName());
            }
            if (cPlayer != null) {
                players[players.length] = cPlayer;
            }
        }
        return players;
    }
            
    @Override
    public int getMaxCPlayers() {
        return Bukkit.getServer().getMaxPlayers();
    }
            
    @Override
    public CPlayer getCPlayer(String partialName) {
        if (partialName == null) return null;
        CPlayer exact = getCPlayerExact(partialName);
        if (exact != null) return exact;
        for (CPlayer p : getOnlineCPlayers()) {
            if (p.getName().contains(partialName)) {
                return p;
            }
        }
        return null;
    }
            
    @Override
        public CPlayer getCPlayerExact(String exactName) {
            if (exactName == null) return null;
                for (CPlayer p : getOnlineCPlayers()) {
                    if (p.getName().equalsIgnoreCase(exactName)) {
                       return p;
                    }
                }
            return null;
        }
            
    @Override
    public List<CPlayer> matchCPlayer(String name) {
        if (name == null) return null;
        List<CPlayer> players = new ArrayList<>();
        CPlayer exact = getCPlayerExact(name);
        if (exact != null) {
            players.clear();
            players.add(exact);
            return players;
        }
        for (CPlayer p : getOnlineCPlayers()) {
            if (p.getName().contains(name)) {
                players.add(p);
            }
        }
        return players;
    }

    @Override
    public CWorld getCWorld(String name) {
         if (name == null) return null;
         for (CWorld world : getCWorlds()) {
              if (world.getName().equalsIgnoreCase(name)) return world;
         }
         return null;
    }

    @Override
    public List<CWorld> getCWorlds() {
        List<CWorld> worlds = new ArrayList<>();
        for (final World bWorld : Bukkit.getWorlds()) {
            CWorld world = new CWorld() {
                @Override
                public CBlock getBlockAt(CLocation location) {
                    Block b = bWorld.getBlockAt(LocationUtil.toBukkitLoc(location));
                    CBlock block = new CBlock(location, b.getTypeId(), b.getData());
                    return block;
                }
                                
                @Override
                public String getName() {
                    return bWorld.getName();
                }
                                
                @Override
                public CPlayer[] getPlayers() {
                    CPlayer[] players = new CPlayer[]{};
                        for (Player player : bWorld.getPlayers()) {
                        CPlayer cPlayer = getCPlayerExact(player.getName());
                        if (cPlayer != null) {
                            players[players.length] = cPlayer;
                        }
                    }
                    return players;
                }
            };
            worlds.add(world);
            }
        return worlds;
    }
                            
    @Override
    public void broadcastMessage(String message) {
         if (message == null) return;
         Bukkit.broadcastMessage(message);
    }

    @Override
    public void broadcast(String message, String permission) {
        if (message == null || permission == null) return;
        Bukkit.broadcast(message, permission);
    }

    @Override
    public void loadConfiguration() {
        try {
            // Copy default configuration files
            logger.info("Copying default configuration files...");
            int filesCopied = ResourceUtil.copyDirectoryFromJar(ChampsCore.class, JAR_RESOURCE_DIRECTORY, CONFIG_PATH, false);
            logger.info(String.format("Copied %d files", filesCopied));

            // Load configuration
            YAMLDataSource yamlDataSource = new YAMLDataSource(CONFIG_PATH);
            yamlDataSource.loadConfiguration(ChampsConfiguration.getInstance(), MAIN_CONFIG_FILE);
        } catch (IOException e) {
            logger.severe("Could not write default configuration files to disk.");
        }
    }

    @Override
    public void registerMessengers() {
        MessageHandler.register(new BukkitMessenger(ChampsCore.getInstance()));
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
