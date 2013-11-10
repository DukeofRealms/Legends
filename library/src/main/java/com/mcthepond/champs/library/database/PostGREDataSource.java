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
package com.mcthepond.champs.library.database;

import com.mcthepond.champs.library.BasicCategory;
import com.mcthepond.champs.library.cclass.CClass;
import com.mcthepond.champs.library.cplayer.CPlayer;
import com.mcthepond.champs.library.level.exp.ExpGroup;
import com.mcthepond.champs.library.party.Party;
import com.mcthepond.champs.library.race.Race;
import com.mcthepond.champs.library.skill.Skill;
import com.mcthepond.champs.library.weapon.Weapon;
import com.mcthepond.champs.library.weapon.WeaponAttributes;

import java.util.logging.Logger;

/**
 * @author B2OJustin
 */
public class PostGREDataSource implements DataSource {
    @Override
    public String getName() {
        return "PostGRE";
    }

    @Override
    public Logger getLogger() {
        return null; //TODO getLogger method stub
    }

    @Override
    public CPlayer loadLPlayer(String name) {
        return null; //TODO loadLPlayer method stub
    }

    @Override
    public void saveLPlayer(CPlayer lPlayer) {
        //TODO saveLPlayer method stub
    }

    @Override
    public Race loadRace(String name) {
        return null; //TODO loadRace method stub
    }

    @Override
    public CClass loadLClass(String name) {
        return null; //TODO loadLClass method stub
    }

    @Override
    public Skill loadSkill(String name) {
        return null; //TODO loadSkill method stub
    }

    @Override
    public Weapon loadWeapon(String name) {
        return null; //TODO loadWeapon method stub
    }

    @Override
    public BasicCategory<WeaponAttributes> loadWeaponCategory(String name) {
        return null; //TODO loadWeaponCategory method stub
    }

    @Override
    public Party loadParty(String name) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ExpGroup loadExpGroup(String name) {
        return null; //TODO loadExpGroup method stub
    }
}
