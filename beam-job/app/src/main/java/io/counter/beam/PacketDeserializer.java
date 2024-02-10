package io.counter.beam;

import com.google.protobuf.InvalidProtocolBufferException;
import com.rng.v1.Packet;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class PacketDeserializer implements Deserializer<Packet> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // Nothing to configure
    }

    @Override
    public Packet deserialize(String topic, byte[] data) {
        try {
            return Packet.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            throw new SerializationException("Error deserializing value", e);
        }
    }

    @Override
    public void close() {
        // Nothing to close
    }
}