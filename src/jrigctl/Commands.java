package jrigctl;

import java.util.EnumMap;
import java.util.Map.Entry;
import java.util.Scanner;

import hamlib.Hamlib;

public class Commands {

    static class CmdPar {
        String chr;
        String str;
        int narg;
        String promt[];
        CmdPar(String c, String s, int n, String[] p) {
            chr = c; str = s; narg = n; promt = p;
        }
    }
    
    public enum Cmd {
        FREQ_S, FREQ_G, MODE_S, MODE_G, VFO_S, VFO_G
    }
    
    private static final EnumMap<Cmd, CmdPar> CmdMap = new EnumMap<>(Cmd.class);
    static {
        CmdMap.put(Cmd.FREQ_S, new CmdPar("F", "\\set_freq", 1, new String[]{"Frequency (Hz)"})); CmdMap.put(Cmd.FREQ_G, new CmdPar("f", "\\get_freq", 0, null));
        CmdMap.put(Cmd.MODE_S, new CmdPar("M", "\\set_mode", 2, new String[]{"Mode", "Passband (Hz)"})); CmdMap.put(Cmd.MODE_G, new CmdPar("m", "\\get_mode", 0, null));
        CmdMap.put(Cmd.VFO_S, new CmdPar("V", "\\set_vfo", 1, new String[]{"VFO"})); CmdMap.put(Cmd.VFO_G, new CmdPar("v", "\\get_vfo", 0, null));
    }

    public static Cmd getCmd(String cmd) {
        for (Entry<Cmd, CmdPar> ent : CmdMap.entrySet()) {
            if (ent.getValue().chr.equals(cmd) || ent.getValue().str.equals(cmd)) {
                return ent.getKey();
            }
        }
        return null;
    }
    
    public static void doCommand(hamlib.Rig rig, String command, Scanner scanner) {
        String[] args = {null, null, null, null};
        String[] fields = command.split(" ");
        Cmd cmd = getCmd(fields[0]);
        if (cmd != null) {
            CmdPar par = CmdMap.get(getCmd(fields[0]));
            for (int i = 0; i < par.narg; i++) {
                if (fields.length < i + 2)  {
                    System.out.print(par.promt[i] + ": ");
                    args[i] = scanner.nextLine();
                } else {
                    args[i] = fields[i+1];
                }
            }
            int err = 0;
            switch (cmd) {
            case FREQ_G:
                if ((err = rig.getError_status()) != 0) System.out.println(Hamlib.rigerror(err));
                else System.out.println("Frequency: " + (int)rig.get_freq(hamlib.HamlibConstants.RIG_VFO_CURR) + " Hz");
                break;
            case FREQ_S:
                rig.set_freq(hamlib.HamlibConstants.RIG_VFO_CURR, Double.parseDouble(args[0]));
                if ((err = rig.getError_status()) != 0) System.out.println(Hamlib.rigerror(err));
                break;
            case MODE_G:
                java.math.BigInteger[] mode = new java.math.BigInteger[1];
                int[] bw = new int[1];
                rig.get_mode(mode, bw, hamlib.HamlibConstants.RIG_VFO_CURR);
                if ((err = rig.getError_status()) != 0) System.out.println(Hamlib.rigerror(err));
                else System.out.println("Mode: " + Hamlib.rig_strrmode(mode[0]) + "\nPassband: " + bw[0] + (bw[0] > 0 ? " Hz" : ""));
                break;
            case MODE_S:
                rig.set_mode(Hamlib.rig_parse_mode(args[0]), new Integer(Integer.parseInt(args[1])), hamlib.HamlibConstants.RIG_VFO_CURR);
                if ((err = rig.getError_status()) != 0) System.out.println(Hamlib.rigerror(err));
                break;
            case VFO_G:
                long vfo = rig.get_vfo();
                if ((err = rig.getError_status()) != 0) System.out.println(Hamlib.rigerror(err));
                else System.out.println("VFO: " + Hamlib.rig_strvfo(vfo));
                break;
            case VFO_S:
                rig.set_vfo(Hamlib.rig_parse_vfo(args[0]));
                if ((err = rig.getError_status()) != 0) System.out.println(Hamlib.rigerror(err));
                break;
            default:
                break;
            }
        } else {
            System.out.println("Invalid command");
        }
    }
    
}
