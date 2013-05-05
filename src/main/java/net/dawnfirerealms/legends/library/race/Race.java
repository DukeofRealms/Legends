/*
This file is part of Legends

    Legends is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Legends is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Legends  If not, see <http://www.gnu.org/licenses/>.
*/

package net.dawnfirerealms.legends.library.race;


import net.dawnfirerealms.legends.library.armor.ArmorRestrictions;
import net.dawnfirerealms.legends.library.armor.ArmorUser;
import net.dawnfirerealms.legends.library.restriction.IDRestrictable;
import net.dawnfirerealms.legends.library.skill.Skill;
import net.dawnfirerealms.legends.library.skill.SkillRestrictions;
import net.dawnfirerealms.legends.library.skill.SkillUser;
import net.dawnfirerealms.legends.library.weapon.WeaponRestrictions;
import net.dawnfirerealms.legends.library.weapon.WeaponUser;

import java.util.ArrayList;

/**
 * @author B2OJustin
 */
public class Race implements WeaponUser, ArmorUser, SkillUser, IDRestrictable {
	private String name;
    private String[] description;

    public Race setName(String name) {
        this.name = name;
        return this;
    }

    public Race setDescription(String[] description) {
        this.description = description;
        return this;
    }

    public String getName() {
        return name;
    }

	public String[] getDescription() {
        return description;
    }

    @Override
    public ArrayList<Skill> getSkills() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addSkill(Skill skill) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void removeSkill(Skill skill) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public SkillRestrictions getSkillRestrictions() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ArmorRestrictions getArmorRestrictions() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public WeaponRestrictions getWeaponRestrictions() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getId() {
        return name;
    }
}
