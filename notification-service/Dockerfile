FROM openjdk:11-jre-slim

WORKDIR /app

COPY target/notification-service-v0.1.0.jar app.jar

ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC"

EXPOSE 8086

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
