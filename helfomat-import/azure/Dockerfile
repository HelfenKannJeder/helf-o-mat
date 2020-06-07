FROM openjdk:11-jdk as builder

WORKDIR /app

COPY . .
RUN chmod +x ./mvnw

RUN ./mvnw package -Phelfomat-import -DskipTests -pl helfomat-import -am




FROM openjdk:11-jre-slim

COPY --from=builder /app/helfomat-import/target/helfomat-import.jar /helfomat-import.jar

ENTRYPOINT ["java","-jar","/helfomat-import.jar"]