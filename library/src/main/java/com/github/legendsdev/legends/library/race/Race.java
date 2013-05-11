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

package com.github.legendsdev.legends.library.race;


import com.github.legendsdev.legends.library.armor.*;
import com.github.legendsdev.legends.library.lclass.*;
import com.github.legendsdev.legends.library.misc.Informative;
import com.github.legendsdev.legends.library.skill.*;
import com.github.legendsdev.legends.library.weapon.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author B2OJustin
 */
public class Race implements Informative<Race, RaceInfo>, ArmorUser<Race>, SkillUser<Race>, WeaponUser<Race>, LClassUser<Race>,
        SkillRestricted, WeaponRestricted, ArmorRestricted, LClassRestricted {
    private String name = "";
    private ArrayList<String> description = new ArrayList<>();
    private ArrayList<Skill> currentSkills = new ArrayList<>();

    private HashMap<Weapon, WeaponInfo> weaponInfoMap = new HashMap<>();
    private HashMap<Armor, ArmorInfo> armorInfoMap = new HashMap<>();
    private HashMap<Skill, SkillInfo> skillInfoMap = new HashMap<>();
    private HashMap<LClass, LClassInfo> lClassInfoMap = new HashMap<>();

    private RaceInfo raceInfo = new RaceInfo();

    private WeaponRestrictions weaponRestrictions = new WeaponRestrictions();
    private ArmorRestrictions armorRestrictions = new ArmorRestrictions();
    private SkillRestrictions skillRestrictions = new SkillRestrictions();
    private LClassRestrictions lClassRestrictions = new LClassRestrictions();

    public Race() {
    }

    public Race setName(String name) {
        this.name = name;
        return this;
    }

    public Race setDescription(ArrayList<String> description) {
        this.description = description;
        return this;
    }

    @Override
    public RaceInfo getDefaultInfo() {
        return raceInfo;
    }

    @Override
    public Race setDefaultInfo(RaceInfo info) {
        this.raceInfo = info;
        return this;
    }

    public String getName() {
        return name;
    }
    
    public ArrayList<String> getDescription() {
        return description;
    }

    @Override
    public ArrayList<Skill> getSkills() {
        return currentSkills;
    }

    @Override
    public Race addSkill(Skill skill) {
        currentSkills.add(skill);
        return this;
    }

    @Override
    public Race removeSkill(Skill skill) {
        currentSkills.remove(skill);
        return this;
    }

    @Override
    public WeaponRestrictions getWeaponRestrictions() {
        return weaponRestrictions;
    }

    @Override
    public HashMap<Skill, SkillInfo> getSkillInfoMap() {
        return skillInfoMap;
    }

    @Override
    public SkillInfo getSkillInfo(Skill skill) {
        return skillInfoMap.get(skill);
    }

    @Override
    public Race setSkillInfo(Skill skill, SkillInfo info) {
        if(skill != null) {
            skillInfoMap.put(skill, info);
        }
        return this;
    }

    @Override
    public HashMap<Armor, ArmorInfo> getArmorInfoMap() {
        return armorInfoMap;
    }

    @Override
    public ArmorInfo getArmorInfo(Armor armor) {
        if(armor != null) {
            ArmorInfo armorInfo = armorInfoMap.get(armor);
            if(armorInfo == null) {
                armorInfo = new ArmorInfo();
                armorInfoMap.put(armor, armorInfo);
            }
            return armorInfo;
        }
        return null;
    }

    @Override
    public Race setArmorInfo(Armor armor, ArmorInfo info) {
        if(armor != null) {
            armorInfoMap.put(armor, info);
        }
        return this;
    }

    @Override
    public HashMap<Weapon, WeaponInfo> getWeaponInfoMap() {
        return weaponInfoMap;
    }

    @Override
    public WeaponInfo getWeaponInfo(Weapon weapon) {
        if(weapon != null) {
            WeaponInfo weaponInfo = weaponInfoMap.get(weapon);
            if(weaponInfo == null) {
                weaponInfo = new WeaponInfo();
                weaponInfoMap.put(weapon, weaponInfo);
            }
            return weaponInfo;
        }
        return null;
    }

    @Override
    public Race setWeaponInfo(Weapon weapon, WeaponInfo info) {
        if(weapon != null) {
            weaponInfoMap.put(weapon, info);
        }
        return this;
    }

    @Override
    public ArmorRestrictions getArmorRestrictions() {
        return armorRestrictions;
    }

    @Override
    public SkillRestrictions getSkillRestrictions() {
        return skillRestrictions;
    }

    @Override
    public LClassRestrictions getLClassRestrictions() {
        return lClassRestrictions;
    }

    @Override
    public HashMap<LClass, LClassInfo> getLClassInfoMap() {
        return lClassInfoMap;
    }

    @Override
    public LClassInfo getLClassInfo(LClass lClass) {
        if(lClass != null) {
            LClassInfo lClassInfo = lClassInfoMap.get(lClass);
            if(lClassInfo == null) {
                lClassInfo = new LClassInfo();
                lClassInfoMap.put(lClass, lClassInfo);
            }
            return lClassInfo;
        }
        return null;
    }

    @Override
    public Race setLClassInfo(LClass lClass, LClassInfo info) {
        if(lClass != null) {
            lClassInfoMap.put(lClass, info);
        }
        return this;
    }
}
