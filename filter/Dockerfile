FROM openjdk:11 AS builder
COPY gradle ./gradle
COPY build.gradle gradlew ./
RUN ./gradlew dependencies

COPY . .
RUN ./gradlew jar

FROM openjdk:11
COPY --from=builder build/pipeline.jar pipeline.jar
CMD ["java", "-jar", "pipeline.jar"]