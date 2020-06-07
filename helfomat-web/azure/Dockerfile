FROM openjdk:11-jdk as builder

WORKDIR /app

COPY . .
RUN chmod +x ./mvnw

RUN ./mvnw package -Phelfomat-web,azure -DskipTests




FROM openjdk:11-jre-slim

COPY --from=builder /app/helfomat-web/target/helfomat.jar /helfomat.jar

ENTRYPOINT ["java","-jar","/helfomat.jar"]