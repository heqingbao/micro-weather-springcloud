FROM openjdk:8-jdk-alpine
MAINTAINER qingbao.ho@gmail.com

COPY build/libs/weather-data-service-1.0.jar /weather-data-service.jar
ENTRYPOINT ["java", "-jar", "/weather-data-service.jar"]