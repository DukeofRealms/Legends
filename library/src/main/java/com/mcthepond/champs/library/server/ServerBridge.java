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

package com.mcthepond.champs.library.server;

import com.mcthepond.champs.library.CWorld;
import com.mcthepond.champs.library.configuration.ChampsConfiguration;
import com.mcthepond.champs.library.armor.ArmorCategoryHandler;
import com.mcthepond.champs.library.armor.ArmorHandler;
import com.mcthepond.champs.library.cclass.CClassHandler;
import com.mcthepond.champs.library.cplayer.CPlayer;
import com.mcthepond.champs.library.cplayer.CPlayerHandler;
import com.mcthepond.champs.library.database.DataManager;
import com.mcthepond.champs.library.event.EventManager;
import com.mcthepond.champs.library.race.RaceHandler;
import com.mcthepond.champs.library.restriction.RestrictionHandler;
import com.mcthepond.champs.library.skill.SkillCategoryHandler;
import com.mcthepond.champs.library.skill.SkillHandler;
import com.mcthepond.champs.library.util.PlatformUtil;
import com.mcthepond.champs.library.weapon.WeaponCategoryHandler;
import com.mcthepond.champs.library.weapon.WeaponHandler;

import java.util.List;

/**
 * @author YoshiGenius
 */
public abstract class ServerBridge {

    private static ServerBridge instance;

    public static ServerBridge getInstance() {
        return instance;
    }

    public static void setInstance(ServerBridge instance) {
        if (instance == null || ServerBridge.instance != null) return;
        ServerBridge.instance = instance;
    }

    private final String name;

    public ServerBridge(String name) {
        init();
        this.name = name;
    }

    public String getServerName() {
        return name;
    }

    public abstract String getServerVersion();

    public abstract String getChampsCoreVersion();

    public abstract String getChampsLibVersion();

    public abstract PlatformUtil.PlatformType getServerPlatform();

    public abstract String getIp();

    public abstract int getPort();

    public abstract void loadConfiguration();

    public abstract void registerMessengers();

    public abstract void registerEvents();

    public abstract void registerPermissions();

    public abstract void registerCommands();

    public abstract CPlayer[] getOnlineCPlayers();

    public abstract int getMaxCPlayers();

    public abstract CPlayer getCPlayer(String name);

    public abstract CPlayer getCPlayerExact(String name);

    public abstract List<CPlayer> matchCPlayer(String name);

    public abstract CWorld getCWorld(String name);

    public abstract List<CWorld> getCWorlds();

    public abstract void broadcastMessage(String message);

    public abstract void broadcast(String message, String permission);

    protected void init() {
        initDataManagement();
        loadConfiguration();
        registerEvents();
        registerMessengers();
        registerPermissions();
        registerCommands();
    }

    protected void initDataManagement() {
        DataManager.init(ChampsConfiguration.getInstance());
    }

    public EventManager getEventManager() {
        return EventManager.getInstance();
    }

    public CPlayerHandler getPlayerHandler() {
        return CPlayerHandler.getInstance();
    }

    public SkillHandler getSkillHandler() {
        return SkillHandler.getInstance();
    }

    public SkillCategoryHandler getSkillCategoryHandler() {
        return SkillCategoryHandler.getInstance();
    }

    public WeaponHandler getWeaponHandler() {
        return WeaponHandler.getInstance();
    }

    public WeaponCategoryHandler getWeaponCategoryHandler() {
        return WeaponCategoryHandler.getInstance();
    }

    public ArmorHandler getArmorHandler() {
        return ArmorHandler.getInstance();
    }

    public ArmorCategoryHandler ArmorCategoryHandler() {
        return ArmorCategoryHandler.getInstance();
    }

    public CClassHandler getClassHandler() {
        return CClassHandler.getInstance();
    }

    public RaceHandler getRaceHandler() {
        return RaceHandler.getInstance();
    }

    public RestrictionHandler getRestrictionHandler() {
        return RestrictionHandler.getInstance();
    }

}
