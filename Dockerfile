FROM openjdk:11-jdk AS build
WORKDIR /tmp
COPY . /tmp
RUN chmod +x ./gradlew && ./gradlew clean bootJar

FROM openjdk:11-jdk
WORKDIR /tmp
COPY --from=build /tmp/build/libs/leica_refactoring-0.0.1-SNAPSHOT.jar /tmp/leicablog.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /tmp/leicablog.jar"]