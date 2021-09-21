SHDIR=$(cd "$(dirname "$0")"; pwd)
PIDFILE=$SHDIR/process.pid
echo -n "Stopping process ..."
if [ ! -f "$PIDFILE" ]
then
    echo "no process to stop (could not find file $PIDFILE)"
else
    kill -15 $(cat "$PIDFILE")
    rm -f "$PIDFILE"
    echo STOPPED
fi
exit 0