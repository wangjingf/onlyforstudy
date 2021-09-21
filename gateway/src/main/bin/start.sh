SHDIR=$(cd "$(dirname "$0")"; pwd)
echo current path:$SHDIR
PIDFILE=$SHDIR/process.pid
JAVA_OPTS="$JAVA_OPTS $JAVA_OPTS_TRAINING"
if [ -f $PIDFILE ]; then
    if kill -0 `cat $PIDFILE` > /dev/null 2>&1; then
        echo server already running as process `cat $PIDFILE`. 
        exit 0
    fi
fi

mkdir -p /export/servers/nginx/logs/live1.jd.local



# 如果使用到nginx需要启动nginx，不要reload，因为nginx默认是不启动的，需要使用sudo执行
# 注：首先创建client_body文件
mkdir -p /dev/shm/nginx_temp/client_body
sudo /export/servers/nginx/sbin/nginx -c /export/servers/nginx/conf/nginx.conf

LOGFILE="nohup.out"

nohup /export/servers/jdk1.8.0_20/bin/java $JAVA_OPTS \
-cp .:$SHDIR/../conf:$SHDIR/../lib/* io.study.gateway.gateway.GatewayStarter > $LOGFILE &



if [ $? -eq 0 ] 
then
    if /bin/echo -n $! > "$PIDFILE"
    then
	    tail -n 100 $LOGFILE
    else
        echo FAILED TO WRITE PID
        exit 1
    fi
else
    echo "server nohup failed!"
    exit 1
fi
