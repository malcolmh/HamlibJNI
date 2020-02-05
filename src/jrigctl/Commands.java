package jrigctl;

import java.util.EnumMap;
import java.util.Map.Entry;
import java.util.Scanner;

import hamlib.Hamlib;
import hamlib.rptr_shift_t;

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
    
    enum Cmd {
        FRQ_S, FRQ_G, MOD_S, MOD_G, VFO_S, VFO_G, PWR_S, PWR_G, PTT_S, PTT_G, DCD_G, RSH_S, RSH_G,
        ROF_S, ROF_G, SPF_G, SPF_S, SPV_S, SPV_G, SPM_S, SPM_G
    }
    
    static final EnumMap<Cmd, CmdPar> CmdMap = new EnumMap<>(Cmd.class);
    static {
        CmdMap.put(Cmd.FRQ_S, new CmdPar("F", "\\set_freq", 1, new String[]{"Frequency (Hz)"}));
        CmdMap.put(Cmd.FRQ_G, new CmdPar("f", "\\get_freq", 0, null));
        CmdMap.put(Cmd.MOD_S, new CmdPar("M", "\\set_mode", 2, new String[]{"Mode", "Passband (Hz)"}));
        CmdMap.put(Cmd.MOD_G, new CmdPar("m", "\\get_mode", 0, null));
        CmdMap.put(Cmd.VFO_S, new CmdPar("V", "\\set_vfo", 1, new String[]{"VFO"}));
        CmdMap.put(Cmd.VFO_G, new CmdPar("v", "\\get_vfo", 0, null));
        CmdMap.put(Cmd.PWR_S, new CmdPar("‡", "\\set_powerstat", 1, new String[]{"Power status"}));
        CmdMap.put(Cmd.PWR_G, new CmdPar("^", "\\get_powerstat", 0, null));
        CmdMap.put(Cmd.PTT_S, new CmdPar("T", "\\set_ptt", 1, new String[]{"PTT"}));
        CmdMap.put(Cmd.PTT_G, new CmdPar("t", "\\get_ptt", 0, null));
        CmdMap.put(Cmd.DCD_G, new CmdPar("‹", "\\get_dcd", 0, null));
        CmdMap.put(Cmd.RSH_S, new CmdPar("R", "\\set_rptr_shift", 1, new String[]{"Rptr Shift"}));
        CmdMap.put(Cmd.RSH_G, new CmdPar("r", "\\get_rptr_shift", 0, null));
        CmdMap.put(Cmd.ROF_S, new CmdPar("O", "\\set_rptr_offs", 1, new String[]{"Rptr Offset"}));
        CmdMap.put(Cmd.ROF_G, new CmdPar("o", "\\get_rptr_offs", 0, null));
    }

    static Cmd getCmd(String cmd) {
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
            case FRQ_G:
                double freq = rig.get_freq(hamlib.HamlibConstants.RIG_VFO_CURR);
                if ((err = rig.getError_status()) != 0) System.out.println(Hamlib.rigerror(err));
                else System.out.println("Frequency: " + (int)freq + " Hz");
                break;
            case FRQ_S:
                rig.set_freq(hamlib.HamlibConstants.RIG_VFO_CURR, Double.parseDouble(args[0]));
                if ((err = rig.getError_status()) != 0) System.out.println(Hamlib.rigerror(err));
                break;
            case MOD_G:
                java.math.BigInteger[] mode = new java.math.BigInteger[1];
                int[] bw = new int[1];
                rig.get_mode(mode, bw, hamlib.HamlibConstants.RIG_VFO_CURR);
                if ((err = rig.getError_status()) != 0) System.out.println(Hamlib.rigerror(err));
                else System.out.println("Mode: " + Hamlib.rig_strrmode(mode[0]) + "\nPassband: " + bw[0] + (bw[0] > 0 ? " Hz" : ""));
                break;
            case MOD_S:
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
            case PWR_G:
                hamlib.powerstat_t pwr = rig.get_powerstat();
                if ((err = rig.getError_status()) != 0) System.out.println(Hamlib.rigerror(err));
                else System.out.println(pwr.toString());
                break;
            case PWR_S:
                rig.set_powerstat(hamlib.powerstat_t.swigToEnum(Integer.parseInt(args[0])));
                if ((err = rig.getError_status()) != 0) System.out.println(Hamlib.rigerror(err));
                break;
            case PTT_G:
                hamlib.ptt_t ptt = rig.get_ptt(hamlib.HamlibConstants.RIG_VFO_CURR);
                if ((err = rig.getError_status()) != 0) System.out.println(Hamlib.rigerror(err));
                else System.out.println(ptt.toString());
                break;
            case PTT_S:
                rig.set_ptt(hamlib.HamlibConstants.RIG_VFO_CURR, hamlib.ptt_t.swigToEnum(Integer.parseInt(args[0])));
                if ((err = rig.getError_status()) != 0) System.out.println(Hamlib.rigerror(err));
                break;
            case DCD_G:
//                hamlib.dcd_t dcd = rig.get_dcd();
//                if ((err = rig.getError_status()) != 0) System.out.println(Hamlib.rigerror(err));
//                else System.out.println(ptt.toString());
                break;
            case RSH_G:
                rptr_shift_t fsh = rig.get_rptr_shift(hamlib.HamlibConstants.RIG_VFO_CURR);
                if ((err = rig.getError_status()) != 0) System.out.println(Hamlib.rigerror(err));
                else System.out.println(Hamlib.rig_strptrshift(fsh));
                break;
            case RSH_S:
                rig.set_rptr_shift(hamlib.HamlibConstants.RIG_VFO_CURR, Hamlib.rig_parse_rptr_shift(args[0]));
                if ((err = rig.getError_status()) != 0) System.out.println(Hamlib.rigerror(err));
                break;
            case ROF_G:
                int off = rig.get_rptr_offs(hamlib.HamlibConstants.RIG_VFO_CURR);
                if ((err = rig.getError_status()) != 0) System.out.println(Hamlib.rigerror(err));
                else System.out.println("Rptr Offset: " + off + " Hz");
                break;
            case ROF_S:
                rig.set_rptr_offs(hamlib.HamlibConstants.RIG_VFO_CURR, Integer.parseInt(args[0]));
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
