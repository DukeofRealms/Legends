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

package com.github.championsdev.champions.library.skill;

import com.github.championsdev.champions.library.behavior.Behavioral;
import com.github.championsdev.champions.library.behavior.SkillBehavior;
import com.github.championsdev.champions.library.misc.Informative;

import java.util.ArrayList;

/**
 * @author B2OJustin
 */
public class Skill implements Informative<Skill, SkillInfo>, Behavioral<Skill, SkillBehavior> {
    private String name = "";
    private ArrayList<String> description = new ArrayList<>();

    private SkillBehavior skillBehavior = new SkillBehavior();

    private SkillInfo skillInfo = new SkillInfo();

    public Skill() {
    }

    public Skill(String name, ArrayList<String> description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public Skill setName(String name) {
        this.name = name;
        return this;
    }

    public ArrayList<String> getDescription() {
        return this.description;
    }

    public Skill setDescription(ArrayList<String> description) {
        this.description = description;
        return this;
    }

    @Override
    public SkillInfo getDefaultInfo() {
        return skillInfo;
    }

    @Override
    public Skill setDefaultInfo(SkillInfo info) {
        this.skillInfo = info;
        return this;
    }

    @Override
    public SkillBehavior getBehavior() {
        return skillBehavior;
    }

    @Override
    public Skill setBehavior(SkillBehavior behavior) {
        skillBehavior = behavior;
        return this;
    }
}
