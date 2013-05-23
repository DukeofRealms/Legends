/*
This file is part of Champions.

    Champions is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Champions is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Champions.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.github.championsdev.champions.library.behavior;

import com.github.championsdev.champions.library.event.cplayer.*;
import com.github.championsdev.champions.library.event.skill.SkillUseEvent;
import com.github.championsdev.champions.library.event.weapon.WeaponClickEvent;
import com.github.championsdev.champions.library.event.weapon.WeaponEvent;
import com.github.championsdev.champions.library.event.weapon.WeaponHitEvent;

/**
 * @author B2OJustin
 */
public interface Behavior {
    public void onQuit(CPlayerQuitEvent event);
    public void onDeath(CPlayerDeathEvent event);
    public void onPlayerKill(CPlayerKillEvent event);
    public void onMobKill(CPlayerMobKillEvent event);
    public void onJoin(CPlayerJoinEvent event);
    public void onWeaponChange(CPlayerWeaponChangeEvent event);
    public void onLevelUp(CPlayerLevelUpEvent event);
    public void onExpGain(CPlayerExpGainEvent event);
    public void onUse(SkillUseEvent event);
    public void onClick(WeaponClickEvent event);
    public void onHit(WeaponHitEvent event);
    public void onSelect(WeaponEvent event);
}
