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

package com.mcthepond.champs.library.behavior;

import com.mcthepond.champs.library.event.cplayer.*;
import com.mcthepond.champs.library.event.skill.SkillUseEvent;
import com.mcthepond.champs.library.event.weapon.WeaponClickEvent;
import com.mcthepond.champs.library.event.weapon.WeaponHitEvent;

/**
 * @author B2OJustin
 */
public abstract class Behavior {

    // Player
    public void onQuit(CPlayerQuitEvent event) {}
    public void onDeath(CPlayerDeathEvent event) {}
    public void onPlayerKill(CPlayerKillEvent event) {}
    public void onMobKill(CPlayerMobKillEvent event) {}
    public void onJoin(CPlayerJoinEvent event) {}
    public void onLevelUp(CPlayerLevelUpEvent event) {}
    public void onExpGain(CPlayerExpGainEvent event) {}

    // Skill
    public void onSkillUse(SkillUseEvent event) {}

    // Weapon
    public void onClick(WeaponClickEvent event) {}
    public void onHit(WeaponHitEvent event) {}
    public void onWeaponChange(CPlayerWeaponChangeEvent event) {}
}
