REPOSITORY=/refact-leica-back
PROJECT_NAME=refact-leica-back

echo "> Git Pull 받는중"

git pull origin main

echo "> 프로젝트 Build 시작"

./gradlew build

echo "> 현재 구동중인 애플리케이션 pid 확인"

CURRENT_PID=$(pgrep -f ${PROJECT_NAME}.*.jar)

echo "현재 구동 중인 애플리케이션 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
    echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
    echo "> kill -9 $CURRENT_PID"
    kill -9 $CURRENT_PID
    sleep 3
fi

echo "> 새 애플리케이션 배포"

cd build/libs

nohup java -jar -Dspring.config.location=/home/ubuntu/final-leicablog/refact-leica-back/application.yml leica_refactoring-0.0.1-SNAPSHOT.jar & > app.log 2>&1 &
