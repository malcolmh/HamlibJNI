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

package rig;

import rig.Enums.*;

public class Rig {
    public native long hamrig_start(String av[]);
    public native void hamrig_stop(long rig);
    public native int rig_set_powerstat(long rig, int p);
    public native int rig_get_powerstat(long rig);
    public native int rig_set_freq(long rig, int v, double f);
    public native int rig_get_freq(long rig, int v);

    static long rig;
    static { System.loadLibrary("hamrig"); }
   
    public int powerstat;
    public double freq;
    public int vfo;
    
    public int rig_start(String args[]) {
        rig = hamrig_start(args);
        return (rig == 0) ? -1 : 0;
    }
    public void rig_stop() {
        hamrig_stop(rig);
    }
    
    public int set_powerstat(int p) {
        powerstat = p;
        return rig_set_powerstat(rig, p);
    }
    public int get_powerstat() {
        return rig_get_powerstat(rig);
    }

    public int set_freq(VFO v, double f) {
        freq = f;
        return rig_set_freq(rig, Enums.getVFOval(v), f);
    }
    public int get_freq(VFO v) {
        return rig_get_freq(rig, Enums.getVFOval(v));
    }

}
