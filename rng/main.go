package main

import (
	"context"
	"github.com/segmentio/kafka-go"
	"github.com/wcygan/counter/generated/go/rng/v1"
	"google.golang.org/protobuf/proto"
	"log"
	"math/rand"
	"net"
	"time"
)

func main() {
	w := &kafka.Writer{
		Addr: kafka.TCP(
			"kafka-controller-0.kafka-controller-headless.default.svc.cluster.local:9092",
			"kafka-controller-1.kafka-controller-headless.default.svc.cluster.local:9092",
			"kafka-controller-2.kafka-controller-headless.default.svc.cluster.local:9092",
		),
		Topic:                  "packet",
		AllowAutoTopicCreation: true,
		Balancer:               &kafka.LeastBytes{},
		Transport: &kafka.Transport{
			Dial: (&net.Dialer{
				Timeout: 10 * time.Second,
			}).DialContext,
		},
	}

	ticker := time.NewTicker(1 * time.Second)
	defer ticker.Stop()

	for range ticker.C {
		packet := rngv1.Packet{Number: rand.Int63()}

		// Serialize the protobuf object to a byte slice
		packetBytes, err := proto.Marshal(&packet)
		if err != nil {
			log.Fatal("failed to serialize packet:", err)
		}

		err = w.WriteMessages(context.Background(),
			kafka.Message{
				Key:   []byte("Key"),
				Value: packetBytes,
			},
		)

		if err != nil {
			log.Println("failed to write messages:", err)
		} else {
			log.Println("Produced:", packet.Number)
		}
	}
}
