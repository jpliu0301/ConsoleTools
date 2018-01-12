#!/bin/sh
#source /home/kdb/KingbaseES/V8/kbrc
# %H hostname of the new master node
new_master=$1
# %h = host name
old_primary=$4
# %P = old primary node id
old_primary_node=$2
# %d = node id    (if node id is primary goes down, then we will ssh -T $new_master that standby takes over primary node.)
failed_node=$3

#set kingbase PATH
KB_PATH="/home/jpliu/cluster/we/db/bin"
#set kingbase LD_LIBRARY_PATH 
KB_LD_PATH="/home/jpliu/cluster/we/db/lib"
#set kingbase data path
KB_DATA_PATH="/home/jpliu/cluster/we/db/data"
#set kingbase data user
KB_USER="SYSTEM"
#set kingbase data name 
KB_DATANAME="TEST"
#set kingbase data port
KB_PORT="54321"
#set kingbase system user execute the program
KB_EXECUTE_SYS_USER="jpliu"

#Is need real kb nodes require virtual IP, like x.x.x.x/24
KB_VIRTUAL_IP=""
#WARING, MUST CHANGE THE SAME NAME THAT ALL THE NETWORK CARDS, IF NOT ,DON'T ALTER
KB_VIRTUAL_DEV_NAME=""

#SET FAILOVER_LOG_DIR="/tmp/failover.log"
FAILOVER_LOG_DIR="/tmp/failover.log"
ENDLS=$FAILOVER_LOG_DIR 2>&1

#SET THE PRIMARY FLAG NAME FILE
KB_PRIMARY_FLAG="KB_PRIMARY_FLAG"

function LOGFILE()
{
        SHELLNAME=$1
        fileindex=0
        $SHELLNAME   >> $RECOVERY_LOG_DIR 2>&1
#        while [ 0 -ne ${#LOGDETAIL[*]} ]
#        do
#                #fileindex=0
#
#                echo ${LOGDETAIL[fileindex]} >> $FAILOVER_LOG_DIR 2>&1
#                unset LOGDETAIL[fileindex]
#                let fileindex+=1;
#        done
}

#echo `date +'%Y-%m-%d %H:%M:%S'` failover beging.. >> $ENDLS


# master down
if [ "$KB_VIRTUAL_IP"x != ""x ]
then
    #LOGDETAIL=(`/usr/bin/ssh -T $old_primary "$KB_PATH/change_vip.sh $KB_VIRTUAL_IP $KB_VIRTUAL_DEV_NAME del 2>&1"`)
    #LOGFILE DELVIP






else

echo "standby down" >> $ENDLS

/usr/bin/ssh -l $KB_EXECUTE_SYS_USER -T $new_master "echo 1 >  ${KB_DATA_PATH}/${KB_PRIMARY_FLAG} " >> $ENDLS

# recover action old master to new standby by hand until network back; to_standby.sh
fi


