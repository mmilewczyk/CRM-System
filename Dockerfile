FROM openjdk:8
ENV POSTGRES_PASSWORD=root
ADD target/oracledatabase-0.0.1-SNAPSHOT.jar oracledatabase-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "oracledatabase-0.0.1-SNAPSHOT.jar"]