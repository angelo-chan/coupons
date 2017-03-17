#!/bin/bash

############## COMMAND LINE VARIABLES ###############
# FIRST ARGUMENT: environment
# Ex: dev | ci | prod
env=$1
# SECOND ARGUMENT: server port
# Ex: 8080 | 8081
serverPort=$2

############### DEFINITION ##########################
projectName=coupons-admin
#destination absolute path. It must be pre created or you can improve this script to create if not exists
destPath=/home/ubuntu/run
destFile=$destPath/$projectName.jar

sourceFile=/home/ubuntu/jars/$projectName*.jar

################ APPLICATION MONITOR ################
logFile=init.log
destLogFile=$destPath/$logFile
# you may change the whatToFind vary on application
whatToFind="Started CouponApplication in "
msgLogFileCreated="$logFile created"
msgBuffer="Buffering: "
msgAppStarted="Application Started... exiting buffer!"

################ PROPERTIES ##########################
properties=--spring.config.active=$env
######################################################

function stopServer(){
    echo " "
    echo "Stopping process on port: $serverPort"
    fuser -n tcp -k $serverPort > redirection &
    echo " "
}

function deleteFiles(){
    echo "Deleting $destFile"
    rm -rf $destFile

    echo "Deleting $destLogFile"
    rm -rf $destLogFile

    echo " "
}

function copyFiles(){
    echo "Copying files from $sourceFile"
    cp $sourceFile $destFile

    echo " "
}

function run(){

   nohup nice java -jar $destFile --server.port=$serverPort $properties $ > $destLogFile 2>&1 &

   echo "COMMAND: nohup nice java -jar $destFile --server.port=$serverPort $properties $ > $destLogFile 2>&1 &"

   echo " "
}

function changeFilePermission(){

    echo "Changing File Permission: chmod 777 $destFile"

    chmod 777 $destFile

    echo " "
}

function watch(){

    tail -f $destLogFile |

        while IFS= read line
            do
                echo "$msgBuffer" "$line"

                if [[ "$line" == *"$whatToFind"* ]]; then
                    echo $msgAppStarted
                    pkill  tail
                fi
        done
}

# 1 - stop server on port ...
stopServer

# 2 - delete destinations folder content
deleteFiles

# 3 - copy files to deploy dir
copyFiles

# 4 - start server
changeFilePermission
run

# 5 - watch loading messages until  ($whatToFind) message is found
watch
