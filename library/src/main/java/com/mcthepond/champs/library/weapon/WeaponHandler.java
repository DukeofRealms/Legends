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
package com.mcthepond.champs.library.weapon;

import com.mcthepond.champs.library.BasicHandler;
import com.mcthepond.champs.library.database.DataManager;

/**
 * @author B2OJustin
 */
public class WeaponHandler extends BasicHandler<Weapon> {
    private static WeaponHandler instance = new WeaponHandler();
    
    public static WeaponHandler getInstance() {
        return instance;
    }

    public Weapon load(String id) {
        Weapon weapon = super.get(id);
        if(weapon == null) {
            weapon = DataManager.getDataSource().loadWeapon(id);
            if(weapon != null) register(id, weapon);
        }
        if(weapon == null) {
            weapon = new Weapon().setName(id);
            if(weapon != null) register(id, weapon);
        }
        return weapon;
    }
}
