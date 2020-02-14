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
        FRQ_S, FRQ_G, MOD_S, MOD_G, VFO_S, VFO_G, PTT_S, PTT_G, RSH_S, RSH_G, ROF_S, ROF_G, SPF_G, SPF_S,
        SPM_S, SPM_G, SFM_S, SFM_G, SPV_S, SPV_G, RIT_S, RIT_G, XIT_S, XIT_G, TNS_S, TNS_G, DCC_S, DCC_G,
        CTT_S, CTT_G, DCS_S, DCS_G, CTS_S, CTS_G, PWR_S, PWR_G, ANT_S, ANT_G, LVL_S, LVL_G, FNC_S, FNC_G,
        PRM_S, PRM_G, DTM_S, DTM_G, MEM_S, MEM_G, TRN_S, TRN_G, DCD_G, RST_S, MRS_S, BNK_S, VOP_S, SCN_S
    }
    
    static final EnumMap<Cmd, CmdPar> CmdMap = new EnumMap<>(Cmd.class);
    static {
        CmdMap.put(Cmd.FRQ_S, new CmdPar("F", "\\set_freq", 1, new String[]{"Frequency (Hz)"}));
        CmdMap.put(Cmd.FRQ_G, new CmdPar("f", "\\get_freq", 0, null));
        CmdMap.put(Cmd.MOD_S, new CmdPar("M", "\\set_mode", 2, new String[]{"Mode", "Passband (Hz)"}));
        CmdMap.put(Cmd.MOD_G, new CmdPar("m", "\\get_mode", 0, null));
        CmdMap.put(Cmd.VFO_S, new CmdPar("V", "\\set_vfo", 1, new String[]{"VFO"}));
        CmdMap.put(Cmd.VFO_G, new CmdPar("v", "\\get_vfo", 0, null));
        CmdMap.put(Cmd.PTT_S, new CmdPar("T", "\\set_ptt", 1, new String[]{"PTT"}));
        CmdMap.put(Cmd.PTT_G, new CmdPar("t", "\\get_ptt", 0, null));
        CmdMap.put(Cmd.DCD_G, new CmdPar("\u008b", "\\get_dcd", 0, null));
        CmdMap.put(Cmd.RSH_S, new CmdPar("R", "\\set_rptr_shift", 1, new String[]{"Rptr Shift"}));
        CmdMap.put(Cmd.RSH_G, new CmdPar("r", "\\get_rptr_shift", 0, null));
        CmdMap.put(Cmd.ROF_S, new CmdPar("O", "\\set_rptr_offs", 1, new String[]{"Rptr Offset"}));
        CmdMap.put(Cmd.ROF_G, new CmdPar("o", "\\get_rptr_offs", 0, null));
        CmdMap.put(Cmd.SPF_S, new CmdPar("I", "\\set_split_freq", 1, new String[]{"Tx Frequency (Hz)"}));
        CmdMap.put(Cmd.SPF_G, new CmdPar("i", "\\get_split_freq", 0, null));
        CmdMap.put(Cmd.SPM_S, new CmdPar("X", "\\set_split_mode", 2, new String[]{"Tx Mode", "Tx Passband"}));
        CmdMap.put(Cmd.SPM_G, new CmdPar("x", "\\get_split_mode", 0, null));
        CmdMap.put(Cmd.SFM_S, new CmdPar("K", "\\set_split_frequency_mode", 3, new String[]{"Tx Frequency", "Tx Mode", "Tx Passband"}));
        CmdMap.put(Cmd.SFM_G, new CmdPar("k", "\\get_split_frequency_mode", 0, null));
        CmdMap.put(Cmd.SPV_S, new CmdPar("S", "\\set_split_vfo", 2, new String[]{"Split", "Tx VFO"}));
        CmdMap.put(Cmd.SPV_G, new CmdPar("s", "\\get_split_vfo", 0, null));
        CmdMap.put(Cmd.RIT_S, new CmdPar("J", "\\set_rit", 1, new String[]{"RIT"}));
        CmdMap.put(Cmd.RIT_G, new CmdPar("j", "\\get_rit", 0, null));
        CmdMap.put(Cmd.XIT_S, new CmdPar("Z", "\\set_xit", 1, new String[]{"XIT"}));
        CmdMap.put(Cmd.XIT_G, new CmdPar("z", "\\get_xit", 0, null));
        CmdMap.put(Cmd.TNS_S, new CmdPar("N", "\\set_ts", 1, new String[]{"Tuning Step"}));
        CmdMap.put(Cmd.TNS_G, new CmdPar("n", "\\get_ts", 0, null));
        CmdMap.put(Cmd.DCC_S, new CmdPar("D", "\\set_dcs_code", 1, new String[]{"DCS Code"}));
        CmdMap.put(Cmd.DCC_G, new CmdPar("d", "\\get_dcs_code", 0, null));
        CmdMap.put(Cmd.CTT_S, new CmdPar("C", "\\set_ctcss_tone", 1, new String[]{"CTCSS Tone"}));
        CmdMap.put(Cmd.CTT_G, new CmdPar("c", "\\get_ctcss_tone", 0, null));
        CmdMap.put(Cmd.DCS_S, new CmdPar("\u0092", "\\set_dcs_sql", 1, new String[]{"DCS Code"}));
        CmdMap.put(Cmd.DCS_G, new CmdPar("\u0093", "\\get_dcs_sql", 0, null));
        CmdMap.put(Cmd.CTS_S, new CmdPar("\u0090", "\\set_ctcss_sql", 1, new String[]{"CTCSS Tone"}));
        CmdMap.put(Cmd.CTS_G, new CmdPar("\u0091", "\\get_ctcss_sql", 0, null));
        CmdMap.put(Cmd.PWR_S, new CmdPar("\u0087", "\\set_powerstat", 1, new String[]{"Power status"}));
        CmdMap.put(Cmd.PWR_G, new CmdPar("\u0088", "\\get_powerstat", 0, null));
        CmdMap.put(Cmd.ANT_S, new CmdPar("Y", "\\set_ant", 1, new String[]{"Antenna"}));
        CmdMap.put(Cmd.ANT_G, new CmdPar("y", "\\get_ant", 0, null));
        CmdMap.put(Cmd.LVL_S, new CmdPar("L", "\\set_level", 2, new String[]{"Level", "Value"}));
        CmdMap.put(Cmd.LVL_G, new CmdPar("l", "\\get_level", 1, new String[]{"Level"}));
        CmdMap.put(Cmd.FNC_S, new CmdPar("U", "\\set_func", 2, new String[]{"Func", "Status"}));
        CmdMap.put(Cmd.FNC_G, new CmdPar("u", "\\get_func", 1, new String[]{"Func"}));
        CmdMap.put(Cmd.PRM_S, new CmdPar("P", "\\set_parm", 2, new String[]{"Parm", "Value"}));
        CmdMap.put(Cmd.PRM_G, new CmdPar("p", "\\get_parm", 1, new String[]{"Parm"}));
        CmdMap.put(Cmd.DTM_S, new CmdPar("\u0089", "\\send_dtmf", 1, new String[]{"Digits"}));
        CmdMap.put(Cmd.DTM_G, new CmdPar("\u008a", "\\recv_dtmf", 0, null));
        CmdMap.put(Cmd.MRS_S, new CmdPar("b", "\\send_morse", 1, new String[]{"Morse"}));
        CmdMap.put(Cmd.BNK_S, new CmdPar("B", "\\set_bank", 1, new String[]{"Bank"}));
        CmdMap.put(Cmd.MEM_S, new CmdPar("E", "\\set_mem", 1, new String[]{"Channel"}));
        CmdMap.put(Cmd.MEM_G, new CmdPar("e", "\\get_mem", 0, null));
        CmdMap.put(Cmd.VOP_S, new CmdPar("G", "\\vfo_op", 1, new String[]{"VFO Op"}));
        CmdMap.put(Cmd.SCN_S, new CmdPar("g", "\\scan", 2, new String[]{"Scan", "Channel"}));
        CmdMap.put(Cmd.TRN_S, new CmdPar("A", "\\set_trn", 1, new String[]{"Tranceive"}));
        CmdMap.put(Cmd.TRN_G, new CmdPar("a", "\\get_trn", 0, null));
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
            case FRQ_S:
                rig.set_freq(hamlib.HamlibConstants.RIG_VFO_CURR, Double.parseDouble(args[0]));
                break;
            case FRQ_G:
                double freq = rig.get_freq(hamlib.HamlibConstants.RIG_VFO_CURR);
                if (rig.getError_status() == 0) System.out.println("Frequency: " + (int)freq + " Hz");
                break;
            case MOD_S:
                rig.set_mode(Hamlib.rig_parse_mode(args[0]), Integer.parseInt(args[1]), hamlib.HamlibConstants.RIG_VFO_CURR);
                break;
            case MOD_G:
                java.math.BigInteger[] mode = new java.math.BigInteger[1];
                int[] bw = new int[1];
                rig.get_mode(mode, bw, hamlib.HamlibConstants.RIG_VFO_CURR);
                if (rig.getError_status() == 0) System.out.println("Mode: " + Hamlib.rig_strrmode(mode[0]) + "\nPassband: " + bw[0] + (bw[0] > 0 ? " Hz" : ""));
                break;
            case VFO_S:
                rig.set_vfo(Hamlib.rig_parse_vfo(args[0]));
                break;
            case VFO_G:
                long vfo = rig.get_vfo();
                if (rig.getError_status() == 0) System.out.println("VFO: " + Hamlib.rig_strvfo(vfo));
                break;
            case PTT_S:
                rig.set_ptt(hamlib.HamlibConstants.RIG_VFO_CURR, hamlib.ptt_t.swigToEnum(Integer.parseInt(args[0])));
                break;
            case PTT_G:
                hamlib.ptt_t ptt = rig.get_ptt(hamlib.HamlibConstants.RIG_VFO_CURR);
                if (rig.getError_status() == 0) System.out.println("PTT: " + ptt.swigValue());
                break;
            case DCD_G:
                hamlib.dcd_t dcd = rig.get_dcd(hamlib.HamlibConstants.RIG_VFO_CURR);
                if (rig.getError_status() == 0) System.out.println("DCD: " + dcd.swigValue());
                break;
            case RSH_S:
                rig.set_rptr_shift(hamlib.HamlibConstants.RIG_VFO_CURR, Hamlib.rig_parse_rptr_shift(args[0]));
                break;
            case RSH_G:
                rptr_shift_t fsh = rig.get_rptr_shift(hamlib.HamlibConstants.RIG_VFO_CURR);
                if (rig.getError_status() == 0) System.out.println("Rptr Shift: " + Hamlib.rig_strptrshift(fsh));
                break;
            case ROF_S:
                rig.set_rptr_offs(hamlib.HamlibConstants.RIG_VFO_CURR, Integer.parseInt(args[0]));
                break;
            case ROF_G:
                int off = rig.get_rptr_offs(hamlib.HamlibConstants.RIG_VFO_CURR);
                if (rig.getError_status() == 0) System.out.println("Rptr Offset: " + off + " Hz");
                break;
            case SPF_S:
                rig.set_split_freq(hamlib.HamlibConstants.RIG_VFO_CURR, Double.parseDouble(args[0]));
                break;
            case SPF_G:
                freq = rig.get_split_freq(hamlib.HamlibConstants.RIG_VFO_CURR);
                if (rig.getError_status() == 0) System.out.println("Tx Frequency: " + (int)freq + " Hz");
                break;
            case SPM_S:
                rig.set_split_mode(Hamlib.rig_parse_mode(args[0]), Integer.parseInt(args[1]), hamlib.HamlibConstants.RIG_VFO_CURR);
                break;
            case SPM_G:
                mode = new java.math.BigInteger[1];
                bw = new int[1];
                rig.get_split_mode(mode, bw, hamlib.HamlibConstants.RIG_VFO_CURR);
                if (rig.getError_status() == 0) System.out.println("Mode: " + Hamlib.rig_strrmode(mode[0]) + "\nPassband: " + bw[0] + (bw[0] > 0 ? " Hz" : ""));
                break;
            case SFM_S:
//                rig.set_split_freq_mode();
                break;
            case SFM_G:
                break;
            case SPV_S:
                rig.set_split_vfo(hamlib.split_t.swigToEnum(Integer.parseInt(args[0])), Hamlib.rig_parse_vfo(args[1]), hamlib.HamlibConstants.RIG_VFO_CURR);
                break;
            case SPV_G:
                hamlib.SWIGTYPE_p_split_t[] split = new hamlib.SWIGTYPE_p_split_t[1];
                hamlib.SWIGTYPE_p_unsigned_int[] tx_vfo = new hamlib.SWIGTYPE_p_unsigned_int[1];
                rig.get_split_vfo(split[0], tx_vfo[0], hamlib.HamlibConstants.RIG_VFO_CURR);
                if (rig.getError_status() == 0) System.out.println("Split: " + split[0].toString() + "\nTx VFO: " + tx_vfo[0].toString());
                break;
            case RIT_S:
                rig.set_rit(hamlib.HamlibConstants.RIG_VFO_CURR, Integer.parseInt(args[0]));
                break;
            case RIT_G:
                int rit = rig.get_rit(hamlib.HamlibConstants.RIG_VFO_CURR);
                if (rig.getError_status() == 0) System.out.println("RIT: " + rit + " Hz");
                break;
            case XIT_S:
                rig.set_xit(hamlib.HamlibConstants.RIG_VFO_CURR, Integer.parseInt(args[0]));
                break;
            case XIT_G:
                int xit = rig.get_xit(hamlib.HamlibConstants.RIG_VFO_CURR);
                if (rig.getError_status() == 0) System.out.println("XIT: " + xit + " Hz");
                break;
            case TNS_S:
                rig.set_ts(hamlib.HamlibConstants.RIG_VFO_CURR, Integer.parseInt(args[0]));
                break;
            case TNS_G:
                int ts = rig.get_ts(hamlib.HamlibConstants.RIG_VFO_CURR);
                if (rig.getError_status() == 0) System.out.println("Tuning Step: " + ts + " Hz");
                break;
            case DCC_S:
                rig.set_dcs_code(hamlib.HamlibConstants.RIG_VFO_CURR, Long.parseLong(args[0]));
                break;
            case DCC_G:
               long dcs = rig.get_dcs_code(hamlib.HamlibConstants.RIG_VFO_CURR);
               if (rig.getError_status() == 0) System.out.println("DCS Code: " + dcs);
               break;
            case CTT_S:
                rig.set_ctcss_tone(hamlib.HamlibConstants.RIG_VFO_CURR, Long.parseLong(args[0]));
                break;
            case CTT_G:
                long ctcss = rig.get_ctcss_tone(hamlib.HamlibConstants.RIG_VFO_CURR);
                if (rig.getError_status() == 0) System.out.println("CTCSS Tone: " + ctcss + " Hz");
                break;
            case DCS_S:
                rig.set_dcs_sql(hamlib.HamlibConstants.RIG_VFO_CURR, Long.parseLong(args[0]));
                break;
            case DCS_G:
                dcs = rig.get_dcs_sql(hamlib.HamlibConstants.RIG_VFO_CURR);
                if (rig.getError_status() == 0) System.out.println("DCS Code: " + dcs);
                break;
            case CTS_S:
                rig.set_ctcss_sql(hamlib.HamlibConstants.RIG_VFO_CURR, Long.parseLong(args[0]));
                break;
            case CTS_G:
                ctcss = rig.get_ctcss_sql(hamlib.HamlibConstants.RIG_VFO_CURR);
                if (rig.getError_status() == 0) System.out.println("CTCSS Tone: " + ctcss + " Hz");
                break;
            case PWR_S:
                rig.set_powerstat(hamlib.powerstat_t.swigToEnum(Integer.parseInt(args[0])));
                break;
            case PWR_G:
                hamlib.powerstat_t pwr = rig.get_powerstat();
                if (rig.getError_status() == 0) System.out.println("Power Status: " + pwr.swigValue());
                break;
            case RST_S:
                rig.reset(hamlib.reset_t.swigToEnum(Integer.parseInt(args[0])));
                break;
            case ANT_S:
                break;
            case ANT_G:
                break;
            case LVL_S:
                rig.set_level(Hamlib.rig_parse_level(args[0]), Float.parseFloat(args[1]), hamlib.HamlibConstants.RIG_VFO_CURR);
                break;
            case LVL_G:
                float level = rig.get_level_f(Hamlib.rig_parse_level(args[0]), hamlib.HamlibConstants.RIG_VFO_CURR);
                if (rig.getError_status() == 0) System.out.println("Level value: " + level);
                break;
            case FNC_S:
                rig.set_func(Hamlib.rig_parse_func(args[0]), Integer.parseInt(args[1]), hamlib.HamlibConstants.RIG_VFO_CURR);
                break;
            case FNC_G:
                int status = rig.get_func(Hamlib.rig_parse_func(args[0]), hamlib.HamlibConstants.RIG_VFO_CURR);
                if (rig.getError_status() == 0) System.out.println("Func status: " + status);
                break;
            case PRM_S:
                rig.set_parm(Hamlib.rig_parse_parm(args[0]), Integer.parseInt(args[1]));
                break;
            case PRM_G:
                int parm = rig.get_parm_i(Hamlib.rig_parse_parm(args[0]));
                if (rig.getError_status() == 0) System.out.println("Parm value: " + parm);
                break;
            case DTM_S:
                rig.send_dtmf(hamlib.HamlibConstants.RIG_VFO_CURR, args[0]);
                break;
            case DTM_G:
                String dtmf = new String();
                rig.recv_dtmf(dtmf, hamlib.HamlibConstants.RIG_VFO_CURR);
                if (rig.getError_status() == 0) System.out.println("Digits: " + dtmf);
                break;
            case MRS_S:
                rig.send_morse(hamlib.HamlibConstants.RIG_VFO_CURR, args[0]);
                break;
            case BNK_S:
                rig.set_bank(hamlib.HamlibConstants.RIG_VFO_CURR, Integer.parseInt(args[0]));
                break;
            case MEM_S:
                rig.set_mem(hamlib.HamlibConstants.RIG_VFO_CURR, Integer.parseInt(args[0]));
                break;
            case MEM_G:
                int mem = rig.get_mem(hamlib.HamlibConstants.RIG_VFO_CURR);
                if (rig.getError_status() == 0) System.out.println("Memory#: " + mem);
                break;
            case VOP_S:
                rig.vfo_op(hamlib.HamlibConstants.RIG_VFO_CURR, Hamlib.rig_parse_vfo_op(args[0]));
                break;
            case SCN_S:
                rig.scan(Hamlib.rig_parse_scan(args[0]), Integer.parseInt(args[1]), hamlib.HamlibConstants.RIG_VFO_CURR);
                break;
            case TRN_S:
                rig.set_trn(Integer.parseInt(args[0]));
                break;
            case TRN_G:
                int trn = rig.get_trn();
                if (rig.getError_status() == 0) System.out.println("Tranceive: " + trn);
                break;
            default:
                break;
            }
            if ((err = rig.getError_status()) != 0) System.out.println(Hamlib.rigerror(err));
        } else {
            System.out.println("Invalid command");
        }
    }
    
}
