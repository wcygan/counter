# First stage: Build the Protobuf files using Buf
FROM bufbuild/buf:latest as bufbuild
WORKDIR /app
COPY . .
RUN buf generate proto

# Second stage: Download dependencies
FROM golang:1.21 as godeps
WORKDIR /app
COPY --from=bufbuild /app/generated /app/generated
WORKDIR /app/rng
COPY rng/go.mod rng/go.sum ./
RUN go mod download

# Third stage: Build the Go binary
FROM golang:1.21 as gobuild
WORKDIR /app

# Copy the generated files from the previous stage
COPY --from=bufbuild /app/generated /app/generated

# Copy the downloaded dependencies from the second stage
COPY --from=godeps /go/pkg /go/pkg

# Adjust the WORKDIR to the specific directory where the main.go is located if it's not in the root
WORKDIR /app/rng

# Copy the rest of the code
COPY rng/ ./

# Build the Go binary
RUN CGO_ENABLED=0 GOOS=linux go build -o ../build/rng .

# Final stage: Create the runtime image
FROM alpine:latest
RUN apk --no-cache add ca-certificates
WORKDIR /app

# Copy the compiled binary from the third stage
COPY --from=gobuild /app/build/rng ./rng

CMD ["./rng"]