package io.counter.beam;

import com.rng.v1.Packet;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        Packet packet = Packet.newBuilder()
                .setNumber(123)
                .build();
        System.out.println(new App().getGreeting());
    }
}
