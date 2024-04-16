FROM eclipse-temurin:21.0.2_13-jdk-alpine
COPY target/winner-system-1.0.0-SNAPSHOT.jar /app/winner-system-1.0.0.jar
#COPY target/springboot-timing-ex-0.0.1-SNAPSHOT.jar /app/test.jar
#ENTRYPOINT ["/bin/sh"]
ENTRYPOINT ["java","--enable-preview","-jar","/app/winner-system-1.0.0.jar"]
