/*
This file is part of Legends.

    Legends is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Legends is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Legends.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.github.legendsdev.legends.canary.core;

import net.canarymod.Canary;
import net.canarymod.plugin.Plugin;


/**
 * @author B2OJustin
 */
public class LegendsCore extends Plugin {

    @Override
    public void enable() {
        long start = System.currentTimeMillis();
        getLogman().info("[Legends] starting up");
        // TODO::: Canary startup code
        Canary.hooks().registerListener(new LegendsMainListener(this), this);
        long end = System.currentTimeMillis();
        long startup = end - start;
        getLogman().info("[Legends] finished loading after " + startup + "ms");
    }

    @Override
    public void disable() {
        long start = System.currentTimeMillis();
        getLogman().info("[Legends] shutting down");
        // TODO::: Canary shutdown code
        
        long end = System.currentTimeMillis();
        long startup = end - start;
        getLogman().info("[Legends] finished disabling after " + startup + "ms");
    }
}
