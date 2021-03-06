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
package com.mcthepond.champs.bukkit.core.listeners;

import com.mcthepond.champs.library.event.CEventHandler;
import com.mcthepond.champs.library.event.EventListener;
import com.mcthepond.champs.library.event.EventPriority;
import com.mcthepond.champs.library.event.cplayer.CPlayerExpGainEvent;
import com.mcthepond.champs.library.event.cplayer.CPlayerLevelUpEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author B2OJustin
 */
public class BasicListener implements EventListener {

    @CEventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCPlayerExpGain(CPlayerExpGainEvent event) {
        Player player = Bukkit.getPlayerExact(event.getCPlayer().getName());
        player.sendMessage(String.format("Gained %f experience.", event.getExp().getExp()));
        player.sendMessage(String.format("You have %f experience.", event.getCPlayer().getPrimaryClassAttributes().getLevel().getExp()));
    }

    @CEventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCPlayerLevelUp(CPlayerLevelUpEvent event) {
        Player player = Bukkit.getPlayerExact(event.getCPlayer().getName());
        player.sendMessage(String.format("You have advanced to level %d", event.getCPlayer().getPrimaryClassAttributes().getLevel().getLevel()));
    }

}
