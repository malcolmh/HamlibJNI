package test;

import hamlib.Hamlib;
import hamlib.powerstat_t;
import hamlib.rig_debug_level_e;

public class Test {
    
    static { System.loadLibrary("hamlib_wrap"); }

    public static void main(String args[]) throws Exception {

    	hamlib.Rig rig;
        Hamlib.rig_set_debug(rig_debug_level_e.RIG_DEBUG_NONE);
        System.out.println("Starting rig ...");
    	if (args.length >= 2) {
            rig = new hamlib.Rig(Integer.parseUnsignedInt(args[0]));
            rig.getState().getRigport().setPathname(args[1]);
    	} else {
    		rig = new hamlib.Rig(1);
    	}
        rig.open();
        System.out.println("Rig started");
        Thread.sleep(3000);
        double freq = rig.get_freq(hamlib.HamlibConstants.RIG_VFO_CURR);
        System.out.println(freq);
        Thread.sleep(3000);
        rig.set_freq(hamlib.HamlibConstants.RIG_VFO_MAIN, freq + 1.0e6);
        System.out.println(rig.get_freq(hamlib.HamlibConstants.RIG_VFO_MAIN));
        Thread.sleep(3000);
        rig.set_powerstat(powerstat_t.RIG_POWER_OFF);
        rig.close();
        System.out.println("Rig stopped");
    }
}
