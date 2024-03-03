FROM openjdk:21-ea-1-jdk-oracle
COPY target/winner-system-1.0.0.jar /app/winner-system-1.0.0.jar
ENTRYPOINT ["java","-jar","/app/winner-system-1.0.0.jar"]