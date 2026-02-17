FROM eclipse-temurin:17-jdk as builder

WORKDIR /app

COPY . .
RUN chmod +x ./mvnw

RUN ./mvnw package -Phelfomat-web,azure -DskipTests -pl helfomat-web -am




FROM eclipse-temurin:17-jre

COPY --from=builder /app/helfomat-web/target/helfomat.jar /helfomat.jar

ENTRYPOINT ["java","-jar","/helfomat.jar"]