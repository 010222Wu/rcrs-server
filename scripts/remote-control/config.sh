LOCAL_USER=rescue
REMOTE_USER=rescue

MAPDIR=maps-testing
KERNELDIR=roborescue-git
CODEDIR=code
SCRIPTDIR=$KERNELDIR/scripts/remote-control
LOGDIR=logs
DISTDIR=logdist
EVALDIR=evaluation

RSYNC_OPTS=-C

CLUSTERS="1 2 3"

HOSTS="c1-1 c1-2 c1-3 c1-4 c2-1 c2-2 c2-3 c2-4 c3-1 c3-2 c3-3 c3-4 control"
SERVER_HOSTS="c1-1 c2-1 c3-1"
CLIENT_HOSTS="c1-2 c1-3 c1-4 c2-2 c2-3 c2-4 c3-2 c3-3 c3-4"

# HOSTS=localhost
# SERVER_HOSTS=localhost
# CLIENT_HOSTS=localhost

KERNEL_WAITING_TIME=5

DAY=testing

TEAM_SHORTHANDS="ANC APO CSU GUC LTI MIN MRL NAI POS RI1 RAK SOS ZJU"

declare -A TEAM_NAMES
TEAM_NAMES[ANC]=anct_rescue2013
TEAM_NAMES[APO]=Apollo-Rescue
TEAM_NAMES[CSU]=CSU-YUNLU
TEAM_NAMES[GUC]=GUC_ArtSapience
TEAM_NAMES[LTI]="LTI Agent Rescue"
TEAM_NAMES[MIN]=MinERS
TEAM_NAMES[MRL]=MRL
TEAM_NAMES[NAI]=NAITO-Rescue2013
TEAM_NAMES[POS]=Poseidon
TEAM_NAMES[RI1]=Ri-one
TEAM_NAMES[RAK]=RoboAKUT
TEAM_NAMES[SOS]=S.O.S.
TEAM_NAMES[ZJU]=ZJUBase

DIR=$(pwd)

#return hostname of the kernel server of the given cluster
function getServerHost() {
    echo c$1-1
    # echo 10.10.10.$11
    # echo localhost
}

#return hostnames of the client servers of the given cluster
function getClientHost() {
    local i=$(($2+1))
    echo "c$1-$i"
    # echo localhost
}

LOCAL_HOMEDIR=/home/$LOCAL_USER
LOCKFILE_NAME=rsl_run.lock
STATFILE_NAME=rsl_last_run.stat