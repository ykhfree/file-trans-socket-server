#!/bin/bash
RUN=../lib/file_trans_socket-0.8.jar
CLASSPATH=$RUN
nohup java -Xms512m -Xmx512m -XX:+UseParallelGC -XX:+UseParallelOldGC -classpath $CLASSPATH ykhfree.dev.transfile.server.app.SocketServer 1> /dev/null 2>&1 &
#java -Xms512m -Xmx512m -XX:+UseParallelGC -XX:+UseParallelOldGC -classpath $CLASSPATH ykhfree.dev.transfile.server.app.SocketServer

MyPID=$!
echo "Socket Server start!!"
#echo $MyPID                     # You print to terminal
echo "kill $MyPID" > shutdown.sh  # Write the the command kill pid in shutdown.sh