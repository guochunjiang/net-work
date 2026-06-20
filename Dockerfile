#FROM maven:3.8-jdk-8 AS builder

#WORKDIR /build
#COPY pom.xml .
#COPY network-common/pom.xml network-common/
#COPY network-dal/pom.xml network-dal/
#COPY network-service/pom.xml network-service/
#COPY network-web/pom.xml network-web/
#COPY network-common/src network-common/src/
#COPY network-dal/src network-dal/src/
#COPY network-service/src network-service/src/
#COPY network-web/src network-web/src/

#RUN mvn package -DskipTests -q

#RUN mkdir -p /app && \
#    unzip network-web/target/network-web-1.0.0-distribution.zip -d /tmp/ && \
#    cp -r /tmp/network-web-1.0.0/* /app/

FROM eclipse-temurin:8-jre

WORKDIR /network-web-1.0.0

COPY /network-web-1.0.0 .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "network-web-1.0.0.jar"]
