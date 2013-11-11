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

import java.util.logging.Logger;

import com.mcthepond.champs.library.server.ServerBridge;
import org.bukkit.plugin.java.JavaPlugin;

import com.mcthepond.champs.bukkit.core.utils.DependencyHandler;

/**
 * @author B2OJustin
 */
public class ChampsCore extends JavaPlugin {
    private static Logger logger = Logger.getLogger(ChampsCore.class.getName());
    private static ChampsCore instance;

    public static ChampsCore getInstance() {
        return instance;
    }

	@Override
	public void onEnable() {
        ChampsCore.instance = this;

        DependencyHandler.resolve();

        ServerBridge.setInstance(new BukkitChampsServer(this));

        getCommand("champs").setExecutor(new BukkitCommandHandler());

        logger.info("Champions successfully enabled!");
	}

}
