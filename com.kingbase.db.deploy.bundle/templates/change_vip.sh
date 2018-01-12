#del VIP

#LOOK HERE!!
#Need manually add DEV NAME, only if all network cards are not same name
#like DEV=eth0
DEV=""



KB_VIRTUAL_IP=$1
KB_VIRTUAL_DEV_NAME=$2
OPRATE=$3


KB_REAL_DEV=$KB_VIRTUAL_DEV_NAME




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
    echo ADD VIP NOW AT `date` ON $KB_REAL_DEV
    /sbin/ip addr add $KB_VIRTUAL_IP dev $KB_REAL_DEV label ${KB_REAL_DEV}:2 2>&1
    #/usr/sbin/arping -s 192.168.211.15 -c 3 -I $
    arping -U $KB_VIRTUAL_IP -I $KB_REAL_DEV -w 1 2>&1
else
    echo oprate vip failed, details vip[$KB_VIRTUAL_IP], dev[$KB_REAL_DEV], oprate[$OPRATE]
fi


