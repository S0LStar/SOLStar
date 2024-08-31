#!/bin/bash

REPOSITORY=/home/ec2-user/back/project/SOLStar
PROJECT_NAME=backend
PROJECT_NAME2=frontend
CONFIG_PATH=/home/ec2-user/back/project/SOLStar/config

export CONFIG_PATH=$CONFIG_PATH

cd $REPOSITORY/$PROJECT_NAME/

echo "> Git pull"
git pull

echo "> 프로젝트 Build 시작"
./gradlew bootJar -x test

echo "> SOLStar 디렉토리로 이동"
cd $REPOSITORY

echo "> Build 파일 복사"
JAR_NAME=$(ls $REPOSITORY/$PROJECT_NAME/build/libs/ | grep jar | tail -n 1)
cp $REPOSITORY/$PROJECT_NAME/build/libs/$JAR_NAME $REPOSITORY/

echo "> 현재 구동중인 애플리케이션 PID 확인"
CURRENT_PID=$(pgrep -f ${PROJECT_NAME}.*.jar)

echo "현재 구동중인 애플리케이션 pid : $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
        echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
        echo "> kill -15 $CURRENT_PID"
        kill -15 $CURRENT_PID
        sleep 5

        CURRENT_PID_AFTER_KILL=$(pgrep -f ${PROJECT_NAME})
        if [ -z $CURRENT_PID_AFTER_KILL ]; then
                echo "> Application 종료 완료"
        else
                echo "> kill Application 시행"
                kill -9 $CURRENT_PID_AFTER_KILL
                sleep 5
        fi
fi

echo "> 새 애플리케이션 배포"
nohup java -jar $REPOSITORY/$JAR_NAME > $REPOSITORY/nohup.out 2>&1 &

echo "> run client project"
cd $REPOSITORY/$PROJECT_NAME2

echo "> pm2 kill"
pm2 delete static-page-server-3000 || true

echo "> git pull"
git pull

echo "> npm build"
npm install
npm run build

echo "> pm2 build"
pm2 serve build/ 3000 --spa --name "static-page-server-3000"

