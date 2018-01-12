#SET opposite IP, must be set
KB_OPPOSITE_IP=""

#set pool ip ,should be set
KB_POOL_IP1=""
KB_POOL_IP2=""

#set DB VIP
KB_VIP=""
#WARING, MUST CHANGE THE SAME NAME THAT ALL THE NETWORK CARDS, IF NOT ,DON'T ALTER
KB_DEV=""


#set kingbase PATH
KB_PATH="/home/kdb/KingbaseES/V8/Server/bin"
#set kingbasepool PATH. IF not same, pelease set it
KB_POOL_PATH="$KB_PATH"
#set kingbase LD_LIBRARY_PATH 
KB_LD_PATH="/home/kdb/KingbaseES/V8/Server/lib"
#set kingbase pool LD_LIBRARY_PATH 
KB_POOL_LD_PATH="/home/kdb/KingbaseES/V8/Server/lib"
#set kingbase data path
KB_DATA_PATH="/home/kdb/KingbaseES/V8/data"

#set kingbase system user execute the program
KB_EXECUTE_SYS_USER="lx"
#set kingbase POOL system user execute the program
KB_POOL_EXECUTE_SYS_USER="root"

#POOL_EXENAME, don't alter
POOL_EXENAME="kingbasecluster"



export PATH=$KB_PATH:$PATH
export LD_LIBRARY_PATH=$KB_LD_PATH:$LD_LIBRARY_PATH

function LOGFILE()
{
        SHELLNAME=$1
        fileindex=0
        echo `date +'%Y-%m-%d %H:%M:%S'` $SHELLNAME beging.. >> $RECOVERY_LOG_DIR 2>&1
        while [ 0 -ne ${#LOGDETAIL[*]} ]
        do
                #fileindex=0

                echo ${LOGDETAIL[fileindex]} >> $RECOVERY_LOG_DIR 2>&1
                unset LOGDETAIL[fileindex]
                let fileindex+=1;
        done
}


#################################################################################
#
# main script
#
################################################################################
function usage()
{
    echo "usage: $0 start | stop | restart ";exit 1
}




#AUTO RECOVERY
#STOPED AND PING act

AUTO_FLAG=0
PING_TIMES=3

echo "-----------------------------------------------------------------------"
echo `date +'%Y-%m-%d %H:%M:%S'` KingbaseES automation beging... 


function checkpool()
{
    local pool_num
    if [ "$KB_POOL_IP1"x != ""x ]
    then
        pool_num=`ssh -l $KB_POOL_EXECUTE_SYS_USER -T $KB_POOL_IP1 "ps -ef | grep $POOL_EXENAME | grep -v grep |wc -l"`
        if [ $pool_num -ne 0 ]
        then 
            echo "$KB_POOL_IP1 cluster is still alive, please use stop first"
            exit 0
        fi
    fi
    
    if [ "$KB_POOL_IP2"x != ""x ]
    then
        pool_num=`ssh -l $KB_POOL_EXECUTE_SYS_USER -T $KB_POOL_IP2 "ps -ef | grep $POOL_EXENAME | grep -v grep |wc -l"`
        if [ $pool_num -ne 0 ]
        then 
            echo "$KB_POOL_IP2 cluster is still alive, please use stop first"
            exit 0
        fi
    fi

    #1 mean all pool is start up
    return 1
}

function stoppool()
{
    ssh -l $KB_POOL_EXECUTE_SYS_USER -T $KB_POOL_IP1 "$KB_POOL_PATH/all_monitor.sh $KB_POOL_PATH $KB_POOL_LD_PATH stop pool"
    sleep 2 
    local pool_num=`ssh -l $KB_POOL_EXECUTE_SYS_USER -T $KB_POOL_IP1 "ps -ef | grep $POOL_EXENAME | grep -v grep  |wc -l"`
    if [ $pool_num -ne 0 ]
    then 
        echo "$KB_POOL_IP1 cluster is still alive, can't stop it! find error detail in log "
        exit 0
    fi
    
    ssh -l $KB_POOL_EXECUTE_SYS_USER -T $KB_POOL_IP2 "$KB_POOL_PATH/all_monitor.sh $KB_POOL_PATH $KB_POOL_LD_PATH stop pool"
    sleep 2 
    pool_num=`ssh -l $KB_POOL_EXECUTE_SYS_USER -T $KB_POOL_IP2 "ps -ef | grep $POOL_EXENAME |grep -v grep |wc -l"`
    if [ $pool_num -ne 0 ]
    then 
        echo "$KB_POOL_IP2 cluster is still alive, can't stop it! find error detail in log "
        exit 0
    fi
}



function stopdb()
{
    #1. stop crond localhost
    service crond stop 2>&1
    #2. stop crond opposite_IP
    ssh -l $KB_POOL_EXECUTE_SYS_USER -T $KB_OPPOSITE_IP "service crond stop 2>&1"
    
   
    
    echo "localhost stop kingbase" 2>&1
    ssh -l $KB_EXECUTE_SYS_USER -T localhost "$KB_PATH/all_monitor.sh $KB_PATH $KB_LD_PATH stop db $KB_DATA_PATH"
    sleep 1
    echo "opposite stop kingbase" 2>&1
    ssh -l $KB_EXECUTE_SYS_USER -T $KB_OPPOSITE_IP "$KB_PATH/all_monitor.sh $KB_PATH $KB_LD_PATH stop db $KB_DATA_PATH"
    sleep 1 

    #ssh -l $KB_POOL_EXECUTE_SYS_USER -T localhost "$KB_PATH/all_monitor.sh $KB_PATH $KB_LD_PATH stop dbvip $KB_DATA_PATH $KB_VIP $KB_DEV 2>&1" 2>&1
    
    $KB_PATH/all_monitor.sh $KB_PATH $KB_LD_PATH stop dbvip $KB_DATA_PATH $KB_VIP $KB_DEV 2>&1
    
    ssh -l $KB_POOL_EXECUTE_SYS_USER -T $KB_OPPOSITE_IP "$KB_PATH/all_monitor.sh $KB_PATH $KB_LD_PATH stop dbvip $KB_DATA_PATH $KB_VIP $KB_DEV 2>&1" 2>&1
}

function checkdb()
{
    local db_num
    #db_num=`ssh -l $KB_POOL_EXECUTE_SYS_USER -T localhost "$KB_PATH/all_monitor.sh $KB_PATH $KB_LD_PATH check db $KB_DATA_PATH "`
    db_num=`$KB_PATH/all_monitor.sh $KB_PATH $KB_LD_PATH check db $KB_DATA_PATH`
    if [ $db_num -ne 0 ]
    then 
        echo "localhost kingbase is still alive, please stop first"
        exit 0
    fi

    db_num=`ssh -l $KB_POOL_EXECUTE_SYS_USER -T $KB_OPPOSITE_IP "$KB_PATH/all_monitor.sh $KB_PATH $KB_LD_PATH check db $KB_DATA_PATH"`
    if [ $db_num -ne 0 ]
    then 
        echo "$KB_POOL_IP2 cluster is still alive, please stop first"
        exit 0
    fi

    #1 mean all pool is start up
    return 1
}


function startdb()
{
    #1. stop crond localhost
    service crond stop 2>&1
    #2. stop crond opposite_IP
    ssh -l $KB_POOL_EXECUTE_SYS_USER -T $KB_OPPOSITE_IP "service crond stop 2>&1"
    
    local db_num
    db_num=`ssh -l $KB_EXECUTE_SYS_USER -T localhost "$KB_PATH/all_monitor.sh $KB_PATH $KB_LD_PATH start db $KB_DATA_PATH "`
    if [ "$db_num"x = "0"x ]
    then
        echo "localhost kingbase is start fail, please read log detail"
        exit 0
    fi
    

    db_num=`ssh -l $KB_EXECUTE_SYS_USER -T $KB_OPPOSITE_IP "$KB_PATH/all_monitor.sh $KB_PATH $KB_LD_PATH start db $KB_DATA_PATH "`
    if [ "$db_num"x = "0"x ]
    then
        echo "$KB_OPPOSITE_IP kingbase is start fail, please read log detail"
        exit 0
    fi
    
    #ssh -l $KB_POOL_EXECUTE_SYS_USER -T localhost "$KB_PATH/all_monitor.sh $KB_PATH $KB_LD_PATH start dbcrond $KB_DATA_PATH 2>&1" 2>&1
    
    $KB_PATH/all_monitor.sh $KB_PATH $KB_LD_PATH start dbcrond $KB_DATA_PATH 2>&1
    
    ssh -l $KB_POOL_EXECUTE_SYS_USER -T $KB_OPPOSITE_IP "$KB_PATH/all_monitor.sh $KB_PATH $KB_LD_PATH start dbcrond $KB_DATA_PATH 2>&1" 2>&1
    
    #ssh -l $KB_POOL_EXECUTE_SYS_USER -T localhost "$KB_PATH/all_monitor.sh $KB_PATH $KB_LD_PATH start dbvip $KB_DATA_PATH $KB_VIP $KB_DEV 2>&1" 2>&1
    
    $KB_PATH/all_monitor.sh $KB_PATH $KB_LD_PATH start dbvip $KB_DATA_PATH $KB_VIP $KB_DEV 2>&1

    ssh -l $KB_POOL_EXECUTE_SYS_USER -T $KB_OPPOSITE_IP "$KB_PATH/all_monitor.sh $KB_PATH $KB_LD_PATH start dbvip $KB_DATA_PATH $KB_VIP $KB_DEV 2>&1" 2>&1
    
}


function startpool()
{
    echo "wait kingbase recovery 5 sec..."
    sleep 5
    ssh -l $KB_POOL_EXECUTE_SYS_USER -T $KB_POOL_IP1 "$KB_POOL_PATH/all_monitor.sh $KB_POOL_PATH $KB_POOL_LD_PATH start pool"
    sleep 5 
    local pool_num=`ssh -l $KB_POOL_EXECUTE_SYS_USER -T $KB_POOL_IP1 "ps -ef | grep $POOL_EXENAME | grep -v grep |wc -l"`
    if [ $pool_num -eq 0 ]
    then 
        echo "$KB_POOL_IP1 cluster is stoped, can not up it! find error detail in log /tmp/restartclust.log" 2>&1
        echo "Plz wati for crontab up the [$KB_POOL_IP1] cluster"
        #return 0
    fi
    
    ssh -l $KB_POOL_EXECUTE_SYS_USER -T $KB_POOL_IP2 "$KB_POOL_PATH/all_monitor.sh $KB_POOL_PATH $KB_POOL_LD_PATH start pool"
    sleep 5 
    pool_num=`ssh -l $KB_POOL_EXECUTE_SYS_USER -T $KB_POOL_IP2 "ps -ef | grep $POOL_EXENAME | grep -v grep |wc -l"`
    if [ $pool_num -eq 0 ]
    then 
        echo "$KB_POOL_IP2 cluster is stoped, can not up it! find error detail in log /tmp/restartclust.log " 2>&1
        echo "Plz wati for crontab up the [$KB_POOL_IP1] cluster"
        #return 0
    fi
}

function startall()
{
    checkpool
    checkdb
    startdb
    startpool
    
    echo ......................
    echo all start..
}

function stopall()
{
    stoppool
    stopdb
    echo ......................
    echo all stop..
}


if [ "$1"x = "start"x ]
then
    startall
    
elif [ "$1"x = "stop"x ]
then
    stopall

elif [ "$1"x = "restart"x ]
then
    stopall
    startall
else
    usage
fi



#usage





















