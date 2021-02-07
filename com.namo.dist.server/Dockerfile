From openjdk:15
COPY files /usr/src/myserver/files
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /usr/src/myserver/staticfileserver.jar
WORKDIR /usr/src/myserver
CMD java  -XX:MinRAMPercentage=50 -XX:MaxRAMPercentage=80 -XshowSettings:vm -jar staticfileserver.jar
