package main

import (
	"fmt"
	rngv1 "github.com/wcygan/counter/generated/go/rng/v1"
	"math/rand"
)

func main() {
	var number int64 = rand.Int63()

	packet := rngv1.Packet{
		Number:  number,
		Message: "Hello",
	}

	fmt.Println("Sending packet: ", packet)
}
