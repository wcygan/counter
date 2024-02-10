# First stage: Build the Protobuf files using Buf
FROM bufbuild/buf:latest as bufbuild
WORKDIR /app
COPY . .
RUN buf generate proto

# Second stage: Build the Java application using Gradle
FROM gradle:7.3.3-jdk11 as gradlebuild
WORKDIR /app

# Copy the generated files from the previous stage
COPY --from=bufbuild /app/generated /app/generated

# Copy the code in the beam-job directory
COPY beam-job/ ./beam-job/

# Change the working directory to beam-job
WORKDIR /app/beam-job

# Build the Java application
RUN gradle shadowJar

# Final stage: Create the runtime image
FROM openjdk:11-jre-slim
WORKDIR /app

# Copy the compiled JAR from the second stage
COPY --from=gradlebuild /app/beam-job/app/build/libs/beam-job-1.0.jar ./beam-job.jar
#COPY --from=gradlebuild /app/beam-job/build/libs/beam-job-1.0.jar ./beam-job.jar

CMD ["java", "-jar", "./beam-job.jar"]