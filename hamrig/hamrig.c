/*
 * hamrig.c - (C) Stephane Fillod 2000-2010
 *            (C) Nate Bargmann 2003,2006,2008,2010,2011,2012,2013
 *            (C) The Hamlib Group 2002,2006,2007,2012
 *            (C) Malcolm Herring 2020
 *
 * This program provides start-up & shut-down of a radio using Hamlib.
 * It is based on rigctl.c and utilises a subset of the command-line options.
 *
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
 *
 */

#include "rig_Rig.h"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <ctype.h>
#include <errno.h>
#include <getopt.h>

#define HST_SHRT_OPTS ""

#include <hamlib/rig.h>

#define MAXNAMSIZ 32
#define MAXNBOPT 100    /* max number of different options */

#define SHORT_OPTIONS "+m:r:p:d:P:D:s:c:onvZ"
static struct option long_options[] =
{
    {"model",           1, 0, 'm'},
    {"rig-file",        1, 0, 'r'},
    {"ptt-file",        1, 0, 'p'},
    {"dcd-file",        1, 0, 'd'},
    {"ptt-type",        1, 0, 'P'},
    {"dcd-type",        1, 0, 'D'},
    {"serial-speed",    1, 0, 's'},
    {"civaddr",         1, 0, 'c'},
    {"vfo",             0, 0, 'o'},
    {"no-restore-ai",   0, 0, 'n'},
    {"debug-time-stamps", 0, 0, 'Z'},
    {"verbose",         0, 0, 'v'},
    {0, 0, 0, 0}

};

JNIEXPORT jlong JNICALL Java_rig_Rig_hamrig_1start(JNIEnv *env, jobject obj, jobjectArray args) {

	char str[20][40];
    char* argv[20];
    int argc = (*env)->GetArrayLength(env, args) + 1;
    RIG *rig;
    rig_model_t model = RIG_MODEL_DUMMY;

    int retcode;        /* generic return code from functions */

    int verbose = 0;

    const char *rig_file = NULL, *ptt_file = NULL, *dcd_file = NULL;
    ptt_type_t ptt_type = RIG_PTT_NONE;
    dcd_type_t dcd_type = RIG_DCD_NONE;
    int serial_rate = 0;
    char *civaddr = NULL;   /* NULL means no need to set conf */
    int vfo_mode = 0;       /* vfo_mode = 0 means target VFO is 'currVFO' */

    int i;
    for (i = 1; i < argc; i++) {
    	argv[i] = str[i];
        jstring string = (jstring) ((*env)->GetObjectArrayElement(env, args, i - 1));
        const char *ptr = (*env)->GetStringUTFChars(env, string, 0);
        strncpy(str[i], ptr, 40);
        (*env)->ReleaseStringUTFChars(env, (*env)->GetObjectArrayElement(env, args, i - 1), ptr);
    }

    while (1) {
        int c;
        int option_index = 0;

        c = getopt_long(argc,
                        argv,
                        SHORT_OPTIONS HST_SHRT_OPTS,
                        long_options,
                        &option_index);

        if (c == -1) {
            break;
        }

        switch (c) {
        case 'm':
            if (!optarg) {
                return(0);
            }
            model = atoi(optarg);
            break;

        case 'r':
            if (!optarg) {
                return(0);
            }
            rig_file = optarg;
            break;

        case 'p':
            if (!optarg) {
                return(0);
            }
            ptt_file = optarg;
            break;

        case 'd':
            if (!optarg) {
                return(0);
            }
            dcd_file = optarg;
            break;

        case 'P':
            if (!optarg) {
                return(0);
            }
            if (!strcmp(optarg, "RIG")) {
                ptt_type = RIG_PTT_RIG;
            } else if (!strcmp(optarg, "DTR")) {
                ptt_type = RIG_PTT_SERIAL_DTR;
            } else if (!strcmp(optarg, "RTS")) {
                ptt_type = RIG_PTT_SERIAL_RTS;
            } else if (!strcmp(optarg, "PARALLEL")) {
                ptt_type = RIG_PTT_PARALLEL;
            } else if (!strcmp(optarg, "CM108")) {
                ptt_type = RIG_PTT_CM108;
            } else if (!strcmp(optarg, "GPIO")) {
                ptt_type = RIG_PTT_GPIO;
            } else if (!strcmp(optarg, "GPION")) {
                ptt_type = RIG_PTT_GPION;
            } else if (!strcmp(optarg, "NONE")) {
                ptt_type = RIG_PTT_NONE;
            } else {
                ptt_type = atoi(optarg);
            }
            break;

        case 'D':
            if (!optarg) {
                return(0);
            }
            if (!strcmp(optarg, "RIG")) {
                dcd_type = RIG_DCD_RIG;
            } else if (!strcmp(optarg, "DSR")) {
                dcd_type = RIG_DCD_SERIAL_DSR;
            } else if (!strcmp(optarg, "CTS")) {
                dcd_type = RIG_DCD_SERIAL_CTS;
            } else if (!strcmp(optarg, "CD")) {
                dcd_type = RIG_DCD_SERIAL_CAR;
            } else if (!strcmp(optarg, "PARALLEL")) {
                dcd_type = RIG_DCD_PARALLEL;
            } else if (!strcmp(optarg, "CM108")) {
                dcd_type = RIG_DCD_CM108;
            } else if (!strcmp(optarg, "GPIO")) {
                dcd_type = RIG_DCD_GPIO;
            } else if (!strcmp(optarg, "GPION")) {
                dcd_type = RIG_DCD_GPION;
            } else if (!strcmp(optarg, "NONE")) {
                dcd_type = RIG_DCD_NONE;
            } else {
                dcd_type = atoi(optarg);
            }
            break;

        case 'c':
            if (!optarg) {
                return(0);
            }
            civaddr = optarg;
            break;

        case 's':
            if (!optarg) {
                return(0);
            }
            serial_rate = atoi(optarg);
            break;

        case 'o':
            vfo_mode++;
            break;

        case 'n':
            rig_no_restore_ai();
            break;

        case 'v':
            verbose++;
            break;

        case 'Z':
            rig_set_debug_time_stamp(1);
            break;

        default:
            return(0);
        }
    }

    rig_set_debug(verbose);

    rig_debug(RIG_DEBUG_VERBOSE, "hamrig, %s\n", hamlib_version);
    rig_debug(RIG_DEBUG_VERBOSE, "%s",
              "Report bugs to <hamlib-developer@lists.sourceforge.net>\n\n");

    rig = rig_init(model);

    if (!rig) {
        fprintf(stderr,
                "Unknown rig num %d, or initialization error.\n",
                model);
        fprintf(stderr, "Please check with --list option.\n");
        return(0);
    }

    if (rig_file) {
        strncpy(rig->state.rigport.pathname, rig_file, FILPATHLEN - 1);
    }

    if (ptt_type != RIG_PTT_NONE) {
        rig->state.pttport.type.ptt = ptt_type;
    }

    if (dcd_type != RIG_DCD_NONE) {
        rig->state.dcdport.type.dcd = dcd_type;
    }

    if (ptt_file) {
        strncpy(rig->state.pttport.pathname, ptt_file, FILPATHLEN - 1);
    }

    if (dcd_file) {
        strncpy(rig->state.dcdport.pathname, dcd_file, FILPATHLEN - 1);
    }

    if (serial_rate != 0) {
        rig->state.rigport.parm.serial.rate = serial_rate;
    }

    if (civaddr) {
        rig_set_conf(rig, rig_token_lookup(rig, "civaddr"), civaddr);
    }

    i=0;
    do { // we'll try 5 times and sleep 200ms between tries
        retcode = rig_open(rig);
        if (retcode != RIG_OK) {
            hl_usleep(200000);
            rig_debug(RIG_DEBUG_TRACE, "%s: error opening rig, try#%d\n", __func__, i+1);
        }
    } while (retcode != RIG_OK && ++i < 5);

    if (retcode != RIG_OK) {
        fprintf(stderr, "rig_open: error = %s \n", rigerror(retcode));
        return(0);
    }

    if (verbose > 0) {
        printf("Opened rig model %d, '%s'\n",
               rig->caps->rig_model,
               rig->caps->model_name);
    }

    if (rig->caps->rig_model == RIG_MODEL_NETRIGCTL) {
        /* We automatically detect if we need to be in vfo mode or not */
        int rigctld_vfo_mode = netrigctl_get_vfo_mode(rig);

        if (rigctld_vfo_mode && !vfo_mode) {
            fprintf(stderr,
                    "Looks like rigctld is using vfo mode so we're switching to vfo mode\n");
            vfo_mode = rigctld_vfo_mode;
        } else if (!rigctld_vfo_mode && vfo_mode) {
            fprintf(stderr,
                    "Looks like rigctld is not using vfo mode so we're switching vfo mode off\n");
            vfo_mode = rigctld_vfo_mode;
        } else if (vfo_mode && rig->caps->rig_model != RIG_MODEL_NETRIGCTL) {
            fprintf(stderr, "vfo mode doesn't make sense for any rig other than rig#2\n");
            fprintf(stderr, "But we'll let you run this way if you want\n");
        }
    }

    rig_debug(RIG_DEBUG_VERBOSE,
              "Backend version: %s, Status: %s\n",
              rig->caps->version,
              rig_strstatus(rig->caps->status));

    return((jlong)rig);
}

JNIEXPORT void JNICALL Java_rig_Rig_hamrig_1stop(JNIEnv *env, jobject obj, jlong rig) {
    rig_close((RIG *)rig);   /* close port */
    rig_cleanup((RIG *)rig); /* if you care about memory */
}

JNIEXPORT jint JNICALL Java_rig_Rig_rig_1set_1powerstat(JNIEnv *env, jobject obj, jlong rig, jint stat) {
	return rig_set_powerstat((RIG*)rig, stat);
}
JNIEXPORT jint JNICALL Java_rig_Rig_rig_1get_1powerstat(JNIEnv *env, jobject obj, jlong rig) {
	jclass cls = (*env)->GetObjectClass(env, obj);
	jfieldID fid = (*env)->GetFieldID(env, cls, "powerstat", "I");
	powerstat_t stat = (*env)->GetIntField(env, obj, fid);
	int ret = rig_get_powerstat((RIG*)rig, &stat);
	(*env)->SetIntField(env, obj, fid, stat);
	return ret;
}

JNIEXPORT jint JNICALL Java_rig_Rig_rig_1set_1freq(JNIEnv *env, jobject obj, jlong rig, jint vfo, jdouble freq) {
	return rig_set_freq((RIG*)rig, vfo, freq);
}

JNIEXPORT jint JNICALL Java_rig_Rig_rig_1get_1freq(JNIEnv *env, jobject obj, jlong rig, jint vfo) {
	jclass cls = (*env)->GetObjectClass(env, obj);
	jfieldID fid = (*env)->GetFieldID(env, cls, "freq", "D");
	freq_t freq = (*env)->GetDoubleField(env, obj, fid);
	int ret = rig_get_freq((RIG*)rig, vfo, &freq);
	(*env)->SetDoubleField(env, obj, fid, freq);
	return ret;
}


int main(){return 0;}
