FROM openjdk:8
ADD target/oracledatabase-0.0.1-SNAPSHOT.jar oracledatabase-0.0.1-SNAPSHOT.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "oracledatabase-0.0.1-SNAPSHOT.jar"]