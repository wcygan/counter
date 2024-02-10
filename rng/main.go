package main

import (
	"fmt"
	rngv1 "github.com/wcygan/counter/generated/go/rng/v1"
	"math/rand"
)

func main() {
	packet := rngv1.Packet{
		Number: rand.Int63(),
	}

	fmt.Println("Sending packet:", packet.GetNumber())
}
