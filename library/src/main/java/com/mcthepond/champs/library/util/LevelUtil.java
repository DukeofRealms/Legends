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
package com.mcthepond.champs.library.util;

import java.util.HashMap;
import java.util.logging.Logger;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.mcthepond.champs.library.configuration.ChampsConfiguration;
import com.mcthepond.champs.library.cclass.CClassType;
import com.mcthepond.champs.library.cplayer.CPlayer;
import com.mcthepond.champs.library.level.Level;

/**
 * @author B2OJustin
 */
public class LevelUtil {
    private static Logger logger = Logger.getLogger(LevelUtil.class.getName());
    private static HashMap<Integer, Double> reqExpMap = new HashMap<>();
    private static String expCurve = ChampsConfiguration.getInstance().getExpCurve();
    private static ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");

    private LevelUtil() {
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void setExpCurve(String expCurve) {
        LevelUtil.expCurve = expCurve;
    }

    public static double getRequiredExp(int level) throws ScriptException {
        Double reqExp = reqExpMap.get(level);
        if(reqExp == null) {
            engine.put("L", level);
            reqExp = (Double)engine.eval(expCurve);
            reqExpMap.put(level, reqExp);
            System.out.println("Required exp:" + reqExp);
        }
        return reqExp;
    }

    public static boolean shouldLevelUp(Level level) {
        try {
            double reqExp = getRequiredExp(level.getLevel());
            if(level.getExp() >= reqExp) return true;
        } catch (ScriptException ex) {
            logger.warning("There seems to be an issue with your experience curve.");
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Prepares and fires a CPlayerLevelUpEvent.
     * This does not take into account whether or not the player
     * is actually qualified to gain a new level.
     *
     * @param player
     * @param classType
     * @return
     */
    public static void levelUp(CPlayer player, CClassType classType) {
        // TODO
    }
}
