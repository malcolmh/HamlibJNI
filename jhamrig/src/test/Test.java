/*
 * test.java - (C) Malcolm Herring 2020
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License along
 *   with this program; if not, write to the Free Software Foundation, Inc.,
 *   51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package test;

import rig.Rig;
import rig.Enums.*;

public class Test {
    public static void main(String args[]) throws Exception {
        Rig rig = new Rig();
        if (rig.rig_start(args) == 0) {
            System.out.println("Rig started");
            Thread.sleep(3000);
            rig.get_freq(VFO.MAIN);
            System.out.println(rig.freq);
            Thread.sleep(3000);
            rig.set_freq(VFO.MAIN, rig.freq + 1.0e6);
            rig.get_freq(VFO.MAIN);
            System.out.println(rig.freq);
            Thread.sleep(3000);
            rig.set_powerstat(0);
            rig.rig_stop();
            System.out.println("Rig stopped");
        }
    }
}
