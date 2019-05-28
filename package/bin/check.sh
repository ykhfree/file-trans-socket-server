#!/bin/bash

#crontab을 대비하여 미리 실행 환경으로 이동한다.
cd /home/ykhfree/socket-server/bin

PGM_NAME=file_trans_socket

Cnt=`ps -ef|grep $PGM_NAME|grep -v grep|grep -v vi|wc -l`
PROCESS=`ps -ef|grep $PGM_NAME|grep -v grep|grep -v vi|awk '{print $2}'`

if [ $Cnt -gt 0 ]
then
    echo "$PGM_NAME is running."
else
    RUN=../lib/file_trans_socket-0.8.jar
    CLASSPATH=$RUN
	nohup java -Xms512m -Xmx512m -XX:+UseParallelGC -XX:+UseParallelOldGC -classpath $CLASSPATH ykhfree.dev.transfile.server.app.SocketServer 1> /dev/null 2>&1 &
	MyPID=$!
	echo "Socket Server start!!"
	echo "kill $MyPID" > shutdown.sh
fi