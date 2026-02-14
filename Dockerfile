FROM amazoncorretto:25-alpine AS build
WORKDIR /app

COPY . .

RUN ./mvnw clean package

FROM amazoncorretto:25-alpine
WORKDIR /app

COPY --from=build /app/target/finance-app-1.0-SNAPSHOT.jar /app/finance-app-1.0-SNAPSHOT.jar

CMD [ "java", "-jar", "finance-app-1.0-SNAPSHOT.jar"]
