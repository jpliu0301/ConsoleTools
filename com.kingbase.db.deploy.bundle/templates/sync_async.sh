

KB_PATH=$1
LD_PATH=$2

DATA_DIR=$3
SYNC_FLAG=$4

export PATH=$KB_PATH:$PATH
export LD_LIBRARY_PATH=$LD_PATH:$LD_LIBRARY_PATH


if [ "$SYNC_FLAG"x = ""x ]
then
    SYNC_FLAG=0
fi



if [ $SYNC_FLAG -eq 1 ];then

sed -i "s/[#]*synchronous_standby_names/synchronous_standby_names/g" $DATA_DIR/kingbase.conf
sys_ctl -D $DATA_DIR reload 2>&1
echo "primary async change SYNC successed!" 

else
sed -i "s/synchronous_standby_names/#synchronous_standby_names/g" $DATA_DIR/kingbase.conf
sys_ctl -D $DATA_DIR reload 2>&1 

echo "sync to async successed!"
fi

