



#CLUSTER_BIN_PATH="$1"
#CLUSTER_LIB_PATH="$2"

ACT="$3"

WHICHPRO="$4"

#if I'm not same card,be SET
DEV=""

if [ "$WHICHPRO"x = "pool"x ]
then
CLUSTER_BIN_PATH="$1"
CLUSTER_LIB_PATH="$2"
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$CLUSTER_LIB_PATH
export PATH=$PATH:$CLUSTER_BIN_PATH
elif [ "$WHICHPRO"x = "db"x -o "$WHICHPRO"x = "dbvip"x ]
then
KB_PATH="$1"
KB_LD_PATH="$2"
KB_DATA_PATH="$5"
KB_VIP="$6"
KB_VIRTUAL_DEV_NAME="$7"
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$KB_LD_PATH
export PATH=$PATH:$KB_PATH
fi



#POOL_EXENAME, don't alter
POOL_EXENAME="kingbasecluster"

#EXE.SOCKET, don't alter if not change
KINGBASECLUSTERSOCKET1="/tmp/.s.KINGBASE.9999"
KINGBASECLUSTERSOCKET2="/tmp/.s.KINGBASE.9898"
KINGBASECLUSTERSOCKET3="/tmp/.s.KINGBASE.9000"
CLUSTER_STAT_FILE="/tmp/kingbasecluster_status"

CLUSTER_LOG_PATH="$CLUSTER_BIN_PATH/log"
CLUSTER_LOG_NAME="cluster.log"
#create file log path
if [ ! -d "$CLUSTER_LOG_PATH" ]
then
mkdir -p $CLUSTER_LOG_PATH
echo "File cluster log dir create now" 
fi




function stopcluster()
{
        echo `date +'%Y-%m-%d %H:%M:%S'` let kingbasecluster down ! 
        #1. stop the crontab
        service crond stop
        
        #2 . stop the pro
        havepro=`ps -ef | grep $POOL_EXENAME |grep -v grep |wc -l`
        if [ $havepro -ne 0 ]
        then
            cd $CLUSTER_BIN_PATH
            `./$POOL_EXENAME -m fast stop > /tmp/clusterstop 2>&1 &`
        else
            echo "cluster is already down.." 2>&1 &
        fi
        

        isstillalive=`ps -ef | grep $POOL_EXENAME |grep -v grep |wc -l`
        if [ $isstillalive -ne 0 ]
        then
            `ps -ef| grep $POOL_EXENAME | grep -v grep |awk '{print $2}' |xargs kill -9 2>&1` 
        fi
        FILEEXIST=`ls $KINGBASECLUSTERSOCKET1 | wc -l`
        if [ $FILEEXIST -eq 1 ]
        then
            rm -rf $KINGBASECLUSTERSOCKET1
        fi

        FILEEXIST=`ls $KINGBASECLUSTERSOCKET2 | wc -l`
        if [ $FILEEXIST -eq 1 ]
        then
            rm -rf $KINGBASECLUSTERSOCKET2
        fi
        
        FILEEXIST=`ls $KINGBASECLUSTERSOCKET3 | wc -l`
        if [ $FILEEXIST -eq 1 ]
        then
            rm -rf $KINGBASECLUSTERSOCKET3
        fi
        
        FILEEXIST=`ls $CLUSTER_STAT_FILE | wc -l`
        if [ $FILEEXIST -eq 1 ]
        then
            rm -rf $CLUSTER_STAT_FILE
        fi
}




function startcluster()
{
    service crond stop 2>&1
    restartcluster.sh 
    
    cronexist=`cat /etc/crontab | grep restartcluster |wc -l`
    if [ $cronexist -eq 1 ]
    then
        service crond start 2>&1
    elif [ $cronexist -eq 0 ]
    then
        echo "*/1 * * * * root  ${CLUSTER_BIN_PATH}/restartcluster.sh" >> /etc/crontab
        service crond start 2>&1
    else
            echo "crond is bad ,please check!"
    fi
   
}

function startdbcrontab()
{
  
    
    cronexist=`cat /etc/crontab | grep network_rewind.sh |wc -l`
    if [ $cronexist -eq 1 ]
    then
        service crond start 2>&1
    elif [ $cronexist -eq 0 ]
    then
        echo "*/1 * * * * root  ${CLUSTER_BIN_PATH}/network_rewind.sh" >> /etc/crontab
        service crond start 2>&1
    else
            echo "crond is bad ,please check!"
    fi
    

}


function checkdb()
{
kingbase_pid=`cat ${KB_DATA_PATH}/kingbase.pid |head -n 1`
if [ "$kingbase_pid"x != ""x ]
then
        kingbase_exist=`ps -ef | grep $kingbase_pid | grep -v grep | wc -l` 
        if [ "$kingbase_exist" -ge 1 ]
        then
            echo $kingbase_exist 2>&1
            return $kingbase_exist
        fi
fi
echo 0 2>&1
#return 0
}



function startdb()
{

        sys_ctl -D $KB_DATA_PATH start > /tmp/kbstart 2>&1 &
        
        sleep 1
        kingbase_pid=`cat ${KB_DATA_PATH}/kingbase.pid |head -n 1`

        kingbase_exist=`ps -ef | grep $kingbase_pid | grep -v grep | wc -l` 
        if [ "$kingbase_exist" -ge 1 ]
        then
            echo "set $KB_DATA_PATH up now..." 2>&1
            
        else
            echo 0
            tail -n 20 /tmp/kbstart
        fi
}

function stopdb()
{
#1, set crontab stop  ### can't  , don't the root role

#service crontab stop 2>&1

#2.kill kingbase

kingbase_pid=`cat ${KB_DATA_PATH}/kingbase.pid |head -n 1`

if [ "$kingbase_pid"x != ""x ]
then
        kingbase_exist=`ps -ef | grep $kingbase_pid | grep -v grep | wc -l` 
        if [ "$kingbase_exist" -ge 1 ]
        then
            sys_ctl -D $KB_DATA_PATH stop -m fast >/tmp/kbstop 2>&1 &
            echo "set $KB_DATA_PATH down now..." 2>&1
            sleep 3
        fi
        kingbase_still_exist=`ps -ef | grep $kingbase_pid | grep -v grep | wc -l` 
        if [ "$kingbase_still_exist" -ge 1 ]
        then
            ps -ef| grep $kingbase_pid | grep -v grep |awk '{print $2}' |xargs kill -9 2>&1
            echo "set $KB_DATA_PATH down now..." 2>&1 
            sleep 1
        fi
fi
}

function changevip()
{
    KB_VIRTUAL_IP=$1
    KB_REAL_DEV=$2
    OPRATE=$3


    #KB_REAL_DEV=$KB_VIRTUAL_DEV_NAME

    if [ "$KB_VIRTUAL_IP"x = ""x ]
    then
        echo "no dbvip"
        exit 0;
    fi
    


    if [ "$DEV"x != ""x ]
    then
        KB_REAL_DEV=$DEV
    fi

    if [ "$OPRATE"x = "del"x -a "$KB_REAL_DEV"x != ""x ]
    then
        echo DEL VIP NOW AT `date` ON $KB_REAL_DEV

        /sbin/ip addr $OPRATE $KB_VIRTUAL_IP dev $KB_REAL_DEV 2>&1
        
        echo Oprate del ip cmd end.
        
    elif [ "$OPRATE"x = "add"x -a "$KB_REAL_DEV"x != ""x ]
    then
        master_node=`ls ${KB_DATA_PATH}/recovery.conf |wc -l`
        if [ "$master_node" -eq 1 ]
        then
            return 0
        fi
        echo ADD VIP NOW AT `date` ON $KB_REAL_DEV
        /sbin/ip addr add $KB_VIRTUAL_IP dev $KB_REAL_DEV label ${KB_REAL_DEV}:2 2>&1
       # /usr/sbin/arping -s 192.168.211.15 -c 3 -I $KB_REAL_DEV
    else
        echo oprate vip failed, details vip[$KB_VIRTUAL_IP], dev[$KB_REAL_DEV], oprate[$OPRATE]
    fi

}


main()
{
    if [ "$ACT"x = "stop"x ]
    then
         if [ "$WHICHPRO"x = "pool"x ]
         then 
            stopcluster
         elif [ "$WHICHPRO"x = "db"x ]
         then
            stopdb
         elif [ "$WHICHPRO"x = "dbvip"x ]
         then
            changevip $KB_VIP $KB_VIRTUAL_DEV_NAME del 
         else
            echo "wchi pro should be stop? pro is [$WHICHPRO]"
         fi
    elif [ "$ACT"x = "start"x ]
    then
    
         if [ "$WHICHPRO"x = "pool"x ]
         then 
            startcluster
         elif [ "$WHICHPRO"x = "db"x ]
         then
            startdb
            #startdbcrontab   #not root
         elif [ "$WHICHPRO"x = "dbcrond"x ]
         then
            startdbcrontab
         elif [ "$WHICHPRO"x = "dbvip"x ]
         then
            changevip $KB_VIP $KB_VIRTUAL_DEV_NAME add 
         else
            echo "wchi pro should be stop? pro is [$WHICHPRO]" 
         fi
    elif [ "$ACT"x = "check"x ]
    then
         checkdb
    else
        echo "please use start|stop"
    fi

}


main
