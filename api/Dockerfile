FROM eclipse-temurin:17-jre-alpine
COPY build/libs/screw-your-neighbor-server-0.0.1-SNAPSHOT.jar app.jar
USER 1000
ENTRYPOINT ["java","-jar","/app.jar"]
