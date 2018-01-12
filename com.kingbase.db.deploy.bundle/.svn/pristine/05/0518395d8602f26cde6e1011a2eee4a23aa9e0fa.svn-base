#!/bin/bash
#source /home/kdb/KingbaseES/V8/kbrc

KB_PATH=$1
KB_LD_PATH=$2

KB_USER=$3
KB_DATANAME=$4
KB_PORT=$5

export PATH=$KB_PATH:$PATH
export LD_LIBRARY_PATH=$KB_LD_PATH:$LD_LIBRARY_PATH

#/home/kdb/KingbaseES/V8/Server/bin/ksql -U SYSTEM -d TEST -p 55433  -c 'checkpoint;'

while [ 1 ]  
do
       
       echo "ksql -p $KB_PORT -U $KB_USER -d $KB_DATANAME   -c \"select 33333;\" "
       ksql -p $KB_PORT -U $KB_USER -d $KB_DATANAME   -c "select 33333;" 2>&1
       rightnum=`ksql -p $KB_PORT -U $KB_USER -d $KB_DATANAME   -c "select 33333;" | grep 33333 |wc -l`
        if [ "$rightnum"x = "1"x ]
        then
                echo kingbase is ok , to prepare execute checkpoint 2>&1
                break
        else
                echo before checkpoint query detail[$rightnum] , try again! 2>&1
                sleep 3
                continue
        fi

done

ksql -U $KB_USER -d $KB_DATANAME -p $KB_PORT -c 'checkpoint;' 2>&1

while [ 1 ]  
do
       
       echo "ksql -p $KB_PORT -U $KB_USER -d $KB_DATANAME   -c \"select 33333;\" "
       ksql -p $KB_PORT -U $KB_USER -d $KB_DATANAME   -c "select 33333;" 2>&1
       rightnum=`ksql -p $KB_PORT -U $KB_USER -d $KB_DATANAME   -c "select 33333;" | grep 33333 |wc -l`
        if [ "$rightnum"x = "1"x ]
        then
                echo after execute checkpoint, kingbase is ok.  2>&1 
                break
        else
                echo after checkpoint query detail[$rightnum] , try again! 2>&1
                sleep 3
                continue
        fi

done


