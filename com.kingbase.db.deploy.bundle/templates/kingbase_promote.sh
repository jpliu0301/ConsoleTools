#!/bin/bash
#source /home/kdb/KingbaseES/V8/kbrc
KB_PATH=$1
KB_LD_PATH=$2

KB_USER=$3
KB_DATANAME=$4
KB_PORT=$5
KB_DATAPATH=$6

export PATH=$KB_PATH:$PATH
export LD_LIBRARY_PATH=$KB_LD_PATH:$LD_LIBRARY_PATH

#/home/kdb/KingbaseES/V8/Server/bin/sys_ctl promote -D /home/kdb/KingbaseES/V8/data

while [ 1 ]  
do
       ksql -p $KB_PORT -U $KB_USER -d $KB_DATANAME   -c "select 33333;" 2>&1
        echo "ksql -p $KB_PORT -U $KB_USER -d $KB_DATANAME   -c \"select 33333;\" "
       rightnum=`ksql -p $KB_PORT -U $KB_USER -d $KB_DATANAME   -c "select 33333;" | grep 33333 | wc -l`
        if [ "$rightnum"x = "1"x ]
        then
                echo kingbase is ok , to prepare execute promote 2>&1
                break
        else
                echo before promote query detail[$rightnum] , try again! 2>&1
                sleep 3
                continue
        fi

done
sys_ctl promote -D $KB_DATAPATH 2>&1

while [ 1 ]  
do
       ksql -p $KB_PORT -U $KB_USER -d $KB_DATANAME   -c "select 33333;" 2>&1
       echo "ksql -p $KB_PORT -U $KB_USER -d $KB_DATANAME   -c \"select 33333;\" "
       rightnum=`ksql -p $KB_PORT -U $KB_USER -d $KB_DATANAME   -c "select 33333;" | grep 33333 | wc -l`
        if [ "$rightnum"x = "1"x ]
        then
                echo after execute promote , kingbase status is ok.  2>&1
                break
        else
                echo after promote query detail[$rightnum] , try again! 2>&1
                sleep 3
                continue
        fi

done