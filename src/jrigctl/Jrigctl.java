package jrigctl;

import java.util.Scanner;

import gnu.getopt.Getopt;

import hamlib.Hamlib;
import hamlib.powerstat_t;
import hamlib.rig_debug_level_e;

public class Jrigctl {

    static { System.loadLibrary("hamlib_wrap"); }

    public static void main(String args[]) throws Exception {

        int debug = 0;
        int model = 1;
        String port = "";
        String ptt = "";
        String dcd = "";
        String pttT = "";
        String dcdT = "";
        int baud = 0;
        String civ = "";
      
        Getopt g = new Getopt("jrigctl", args, "m:r:p:d:P:D:s:c:v");
        int c;
        while ((c = g.getopt()) != -1) {
            switch(c) {
            case 'm':
                model = Integer.parseUnsignedInt(g.getOptarg());
                break;
            case 'r':
                port = g.getOptarg();
                break;
            case 'v':
                debug++;
                break;
            case 'p':
                ptt = g.getOptarg();
                break;
            case 'd':
                dcd = g.getOptarg();
                break;
            case 'P':
                pttT = g.getOptarg();
                break;
            case 'D':
                dcd = g.getOptarg();
                break;
            case 's':
                baud = Integer.parseUnsignedInt(g.getOptarg());
                break;
            case 'c':
                civ = g.getOptarg();
                break;
            default:
                System.out.print("Invalid option\n");
                System.exit(0);
            }
        }
        Hamlib.rig_set_debug(rig_debug_level_e.swigToEnum(debug));
        hamlib.Rig rig = new hamlib.Rig(model);
        if (!port.isEmpty()) rig.getState().getRigport().setPathname(port);
        if (!ptt.isEmpty()) rig.getState().getPttport().setPathname(ptt);
        if (!dcd.isEmpty()) rig.getState().getDcdport().setPathname(dcd);
        if (!pttT.isEmpty()) rig.getState().getPttport().getType().setPtt(null);
        if (!dcdT.isEmpty()) rig.getState().getDcdport().getType().setDcd(null);
        if (baud > 0) rig.getState().getRigport().getParm().getSerial().setRate(baud);
//        if (!civ.isEmpty()) rig.set_conf(rig.token_lookup("civaddr"), civ);

        rig.open();
        System.out.println("Rig started");
        
        Scanner scanner = new Scanner(System.in);
        for (;;) {
            System.out.print("Rig command: ");
            String command = scanner.nextLine();
            if (command.equals("q")) break;
            if (!command.isEmpty()) Commands.doCommand(rig, command, scanner);
        }
        scanner.close();
        rig.close();
        System.out.println("Rig stopped");
    }
}
