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
package com.mcthepond.champs.library.skill;

import com.mcthepond.champs.library.BasicCategory;
import com.mcthepond.champs.library.BasicHandler;

/**
 * @author B2OJustin
 */
public class SkillCategoryHandler extends BasicHandler<BasicCategory<SkillAttributes>> {
    private static SkillCategoryHandler instance = new SkillCategoryHandler();

    public static SkillCategoryHandler getInstance() {
        return instance;
    }

    public BasicCategory<SkillAttributes> load(String id) {
        return null; // TODO
    }
}
