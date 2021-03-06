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
package com.mcthepond.champs.library.restriction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author B2OJustin
 */
public class BasicRestrictions<T extends Restrictable> implements BasicRestrictor<T> {
    protected HashMap<T, Boolean> rMap;
    protected boolean defaultAllow = false;

    public BasicRestrictions() {
        rMap = new HashMap<>();
    }

    @Override
    public void setAllowed(T restrictable, boolean allowed) {
        if(restrictable != null) {
            rMap.put(restrictable, allowed);
        }
    }

    @Override
    public boolean isAllowed(T restrictable) {
        Boolean allowed = rMap.get(restrictable);
        if(allowed == null) return defaultAllow;
        else return allowed;
    }

    @Override
    public ArrayList<T> getAllPermitted() {
        ArrayList<T> permittedList = new ArrayList<>();
        for(Map.Entry<T, Boolean> entry : rMap.entrySet()) {
            if(entry.getValue()) permittedList.add(entry.getKey());
        }
        return permittedList;
    }

    @Override
    public void setDefault(boolean defaultAllow) {
        this.defaultAllow = defaultAllow;
    }

}
