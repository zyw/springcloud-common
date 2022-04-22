#106MB
#FROM openjdk:8u222-jre
#71MB
#FROM openjdk:8u222-jre-slim
#58MB
FROM openjdk:8u212-jre-alpine
# FROM java:8
VOLUME /tmp
ENV TZ=Asia/Shanghai

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ADD target/*.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar