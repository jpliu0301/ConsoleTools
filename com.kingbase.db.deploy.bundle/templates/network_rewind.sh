#SET opposite IP, must be set
KB_OPPOSITE_IP="192.168.2.98"
#SET SYNC_FLAG, default ASYNC(0), SYNC(1)  
SYNC_FLAG=1
#SET DEL  VIP , if use
KB_VIP="192.168.8.177"
KB_REAL_DEV="eth0"

#SET THE POOL VIP, option, can be attach the pool
KB_POOL_VIP="10.0.0.1"

#POOL default
KB_POOL_PORT="9999"
#PCP CONF , can attach node
PCP_USER="kingbase"
PCP_PASS="123456"

#set kingbase etc path
KB_ETC_PATH="/etc/"
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


#SET FAILOVER_LOG_DIR="/tmp/failover.log"
RECOVERY_LOG_DIR="/tmp/recovery.log"
ENDLS=$RECOVERY_LOG_DIR 2>&1

#SET THE PRIMARY FLAG NAME FILE
KB_PRIMARY_FLAG="KB_PRIMARY_FLAG"

function LOGFILE()
{
        SHELLNAME=$1
        fileindex=0
                #fileindex=0

                unset LOGDETAIL[fileindex]
                let fileindex+=1;
}



export LD_LIBRARY_PATH=$KB_LD_PATH:$LD_LIBRARY_PATH


#AUTO RECOVERY
#STOPED AND PING act

PING_TIMES=3



#if network down ,exit
result=`ping $KB_OPPOSITE_IP -c $PING_TIMES | grep received | awk '{print $4}'`
    AUTO_FLAG=`/usr/bin/ssh -T $KB_OPPOSITE_IP "cat ${KB_DATA_PATH}/${KB_PRIMARY_FLAG} |head -n 1"`

    #echo "PING $KB_GATEWAY_ROUTE FAILD" >> $ENDLS
#    if [ "$KB_VIP"x != ""x ]
#    then
#        echo "now will del vip" >> $ENDLS
#        vipnum=`/sbin/ip addr | grep "$KB_VIP" | wc -l`
#        if [ $vipnum -eq 1 ]
#        then
#           echo "excute [/sbin/ip addr del $KB_VIP dev $KB_REAL_DEV ]"
#        /sbin/ip addr del $KB_VIP dev $KB_REAL_DEV >> $ENDLS
#        fi
#    fi  
    exit 0;


    #del vip
    #echo "PING $KB_GATEWAY_ROUTE FAILD" >> $ENDLS
        vipnum=`ssh -l root -T localhost "/sbin/ip addr | grep "$KB_VIP" | wc -l"`
            ssh -l root -T localhost "/sbin/ip addr del $KB_VIP dev $KB_REAL_DEV" >> $ENDLS




#kingbase_pid=`cat ${KB_DATA_PATH}/kingbase.pid |head -n 1`
#
#if [ "$kingbase_pid"x != ""x ]
#then
#    kingbase_exist=`ps -ef | grep $kingbase_pid | grep -v grep | wc -l`
#    if [ "$kingbase_exist" -eq 0 ]
#    then
#        #echo `date` kingbase is not alive! >> $ENDLS
#        echo "status down , now I'm must be a standby  " >> $ENDLS
#        result=`ping $KB_GATEWAY_ROUTE -c $PING_TIMES | grep received | awk '{print $4}'`
#        if [ $result -eq $PING_TIMES ]
#        then
#            result=`ping $KB_OPPOSITE_IP -c $PING_TIMES | grep received | awk '{print $4}'`
#            if [ $result -eq $PING_TIMES ]
#            then
#            AUTO_FLAG=1
#            echo "set auto flag 1 , down and connect the primary" >> $ENDLS
#            else
#            echo "dont connect $KB_OPPOSITE_IP" >> $ENDLS
#            exit 0 
#            fi
#        else 
#        echo "dont connect $KB_GATEWAY_ROUTE" >> $ENDLS
#        exit 0
#        fi
#     else   
#        echo "don't down ,but net work may be not active, let check"  >> $ENDLS
#        result=`ping $KB_GATEWAY_ROUTE -c $PING_TIMES | grep received | awk '{print $4}'`
#        if [ $result -eq $PING_TIMES ]
#        then
#            OPP_kingbase_pid=`ssh -T $KB_OPPOSITE_IP "cat ${KB_DATA_PATH}/kingbase.pid |head -n 1"`
#            sleep 1
#            linkstat=`ssh -T $KB_OPPOSITE_IP "ps -ef | grep sender | grep $OPP_kingbase_pid | grep -v grep |wc -l"`
#            sleep 1
#            if [ $linkstat -eq 0 ]
#            then
#            AUTO_FLAG=1
#            echo "set auto flag 1 , not connect the primary" >> $ENDLS
#            fi
#        fi
#          
#
#    fi
#else
#    echo "status down , now I'm must be a standby  " >> $ENDLS
#    result=`ping $KB_GATEWAY_ROUTE -c $PING_TIMES | grep received | awk '{print $4}'`
#    if [ $result -eq $PING_TIMES ]
#    then
#        result=`ping $KB_OPPOSITE_IP -c $PING_TIMES | grep received | awk '{print $4}'`
#        if [ $result -eq $PING_TIMES ]
#        then
#        AUTO_FLAG=1
#        echo "set auto flag 1 , down and not connect the primary" >> $ENDLS
#        else
#        echo "dont connect $KB_OPPOSITE_IP" >> $ENDLS
#        exit 0 
#        fi
#    else 
#    echo "dont connect $KB_GATEWAY_ROUTE" >> $ENDLS
#    exit 0
#    fi
#fi
#
#
#if [ $AUTO_FLAG -eq 0 ]
#then
#exit 0
#fi



#1, if recover node up, let it down , for rewind
kingbase_pid=`cat ${KB_DATA_PATH}/kingbase.pid |head -n 1`

        kingbase_exist=`ps -ef | grep $kingbase_pid | grep -v grep | wc -l` 
            #echo set $KB_DATA_PATH down now...


#2, sys_rewind
#LOGDETAIL=(`sys_rewind  --target-data=$KB_DATA_PATH --source-server="host=$KB_OPPOSITE_IP port=$KB_PORT user=$KB_USER dbname=$KB_DATANAME" 2>&1`)
sys_rewind  --target-data=$KB_DATA_PATH --source-server="host=$KB_OPPOSITE_IP port=$KB_PORT user=$KB_USER dbname=$KB_DATANAME" >> $ENDLS

#3, sed conf change #synchronous_standby_names

sed -i 's/synchronous_standby_names/#synchronous_standby_names/' $KB_DATA_PATH/kingbase.conf

#4, MV recovery.conf if old primary, but also used in old standby
cp ${KB_ETC_PATH}/recovery.done ${KB_DATA_PATH}/recovery.conf 

#WARING : IMPORTANT ,  SYNC OR ASYNC
#5, 
# SYNC MODE
#LOGDETAIL=(`/usr/bin/ssh -l $KB_EXECUTE_SYS_USER -T $KB_OPPOSITE_IP "$KB_PATH/sync_async.sh $KB_PATH $KB_LD_PATH $KB_DATA_PATH $SYNC_FLAG" 2>&1`)
#LOGFILE "/usr/bin/ssh -l $KB_EXECUTE_SYS_USER -T $KB_OPPOSITE_IP "$KB_PATH/sync_async.sh $KB_PATH $KB_LD_PATH $KB_DATA_PATH $SYNC_FLAG" 2>&1"
/usr/bin/ssh -l $KB_EXECUTE_SYS_USER -T $KB_OPPOSITE_IP "$KB_PATH/sync_async.sh $KB_PATH $KB_LD_PATH $KB_DATA_PATH $SYNC_FLAG" >> $ENDLS

#ASYNC MODE
#noting to do

#6 start up
#LOGDETAIL=(`sys_ctl -D $DATA_DIR start 2>&1`)
sys_ctl -D $KB_DATA_PATH start >> $ENDLS &
while [ 1 ]  
do
       rightnum=`ksql -p $KB_PORT -U $KB_USER -d $KB_DATANAME   -c "select 33333;" | grep 33333 | wc -l`
                break
                continue

done



#7 attach pool
    IM_NODE=`ksql -p $KB_POOL_PORT -U $KB_USER  -d $KB_DATANAME -h $KB_POOL_VIP  -c "show pool_nodes;" | grep down | grep -v grep |awk '{print $1}'`
    if [ "$IM_NODE"x != ""x ]
    then
    pcp_attach_node -U $PCP_USER -W $PCP_PASS -h $KB_POOL_VIP -n $IM_NODE >> $ENDLS
    ksql -p $KB_POOL_PORT -U $KB_USER   -d $KB_DATANAME -h $KB_POOL_VIP -c "show pool_nodes;"  >> $ENDLS
    sleep 1
    else
    echo "ALL NODES ARE UP STATUS!" >> $ENDLS
fi

