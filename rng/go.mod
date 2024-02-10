module github.com/wcygan/counter/rng

go 1.21.5

require github.com/wcygan/counter/generated/go v0.0.0

require google.golang.org/protobuf v1.32.0 // indirect

replace github.com/wcygan/counter/generated/go => ../generated/go
