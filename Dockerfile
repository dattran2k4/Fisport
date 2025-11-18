FROM openjdk:21

ARG FILE_JAR=target/*.jar

COPY ${FILE_JAR} fisport.jar

ENTRYPOINT ["java", "-jar", "fisport.jar"]

EXPOSE 8080