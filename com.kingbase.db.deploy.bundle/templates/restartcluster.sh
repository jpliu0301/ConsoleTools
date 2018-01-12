


CLUSTER_LIB_PATH='/home/jpliu/cluster/we/kingbasecluster/lib'
CLUSTER_BIN_PATH='/home/jpliu/cluster/we/kingbasecluster/bin'
CLUSTER_LOG_PATH='/home/jpliu/cluster/we/log'
CLUSTER_LOG_NAME='cluster.log'
CLUSTER_GATEWAY_ROUTE='TEST'

#network down and set ping times
PING_TIMES=3

#EXENAME, don't alter
EXENAME="kingbasecluster"

#EXE.SOCKET, don't alter if not change
KINGBASECLUSTERSOCKET1='/tmp/.s.KINGBASE.9999'
KINGBASECLUSTERSOCKET2='/tmp/.s.KINGBASE.9898'
KINGBASECLUSTERSOCKET3="/tmp/.s.KINGBASE.9000"
CLUSTER_STAT_FILE="/tmp/kingbasecluster_status"

POOL_RESTART="/tmp/pool_restart.log"
ENDLS=$POOL_RESTART 2>&1

#create file log path

mkdir -p $CLUSTER_LOG_PATH


export PATH=$PATH:$CLUSTER_BIN_PATH



#1. kill if network not active
        isstillalive=`ps -ef | grep $EXENAME |grep -v grep |wc -l`
            `ps -ef| grep $EXENAME | grep -v grep |awk '{print $2}' |xargs kill -9 >>  $ENDLS` 



exestillalive=`ps -ef | grep $EXENAME |grep -v grep |wc -l`
	#echo `date` kingbase cluster is not alive! >> $ENDLS
	result=`ping $CLUSTER_GATEWAY_ROUTE -c $PING_TIMES | grep received | awk '{print $4}'`
	#result=1

        #del .socket file

        FILEEXIST=`ls $CLUSTER_STAT_FILE | wc -l`
        if [ $FILEEXIST -eq 1 ]
        then
            rm -rf $CLUSTER_STAT_FILE
		cd $CLUSTER_BIN_PATH
		`./$EXENAME -dn >>  $CLUSTER_LOG_PATH/${CLUSTER_LOG_NAME}  2>&1 &`

else
	echo `date +'%Y-%m-%d %H:%M:%S'` $EXENAME is still alive. >> $ENDLS 
fi




