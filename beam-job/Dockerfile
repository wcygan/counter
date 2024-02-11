# First stage: Build the Protobuf files using Buf
FROM bufbuild/buf:latest as bufbuild
WORKDIR /app
COPY . .
RUN buf generate proto

# Second stage: Build the Java application using Gradle
FROM gradle:8.6-jdk11 as gradlebuild
WORKDIR /app
COPY --from=bufbuild /app/generated /app/generated
COPY beam-job/ ./beam-job/
WORKDIR /app/beam-job
RUN gradle run

## Final stage: Create the runtime image
#FROM openjdk:11-jre-slim
#WORKDIR /app
#COPY --from=gradlebuild /app/beam-job/app/build/libs/beam-job-1.0.jar ./beam-job.jar
#CMD ["java", "-jar", "./beam-job.jar"]