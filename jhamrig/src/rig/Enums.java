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

public final class Enums {
    
    public enum Errcode {
        OK, EINVAL, ECONF, ENOMEM, ENIMPL, ETIMEOUT, EIO, EINTERNAL, EPROTO, ERJCTED, ETRUNC,
        ENAVAIL, ENTARGET, BUSERROR, BUSBUSY, EARG, EVFO, EDOM, EERR };
        public static Errcode getERR(int n) {
            int i = 0; for (Errcode e : Errcode.values()) if (n == i++) return e;
            return Errcode.EERR;
        }
        public static int getERRval(Errcode err) {
            int i = 0; for (Errcode e : Errcode.values()) if (err == e) return i; else i++;
            return -1;
        }
    
    public enum VFO {
        NONE, A, B, C, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12, x13, x14, x15, x16, x17, x18, x19,
        x20, SUB_A, SUB_B, MAIN_A, MAIN_B, SUB, MAIN, VFO, MEM, CURR, TX_FLAG };
        public static VFO getVFO(int n) {
            for (VFO v : VFO.values()) if (n == 0) return v; else n >>= 1;
            return VFO.NONE;
        }
        public static int getVFOval(VFO vfo) {
            int i = 0; for (VFO v : VFO.values()) if (vfo == v) return i; else i = (i == 0) ? 1 : i << 1;
            return 0;
        }
}