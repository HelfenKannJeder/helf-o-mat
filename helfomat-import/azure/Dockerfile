FROM eclipse-temurin:17-jdk as builder

WORKDIR /app

COPY . .
RUN chmod +x ./mvnw

RUN ./mvnw package -Phelfomat-import -DskipTests -pl helfomat-import -am




FROM eclipse-temurin:17-jre

COPY --from=builder /app/helfomat-import/target/helfomat-import.jar /helfomat-import.jar

ENTRYPOINT ["java","-jar","/helfomat-import.jar"]