FROM bufbuild/buf:latest as bufbuild
WORKDIR /app
COPY buf.gen.yaml ./
COPY generated ./generated
COPY proto ./proto
RUN buf generate proto

FROM openjdk:11 AS builder
WORKDIR /app/filter
COPY filter/gradle ./gradle
COPY filter/gradlew ./gradlew
COPY filter/build.gradle ./build.gradle
RUN ./gradlew dependencies

COPY --from=bufbuild /app/generated/java ../generated/java
COPY filter/src ./src
RUN ./gradlew jar

FROM openjdk:11
WORKDIR /app
COPY --from=builder /app/filter/build/pipeline.jar ./pipeline.jar
CMD ["java", "-jar", "pipeline.jar"]