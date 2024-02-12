package io.counter.filter;

import com.rng.v1.Packet;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.kafka.KafkaIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.options.StreamingOptions;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.Values;
import org.apache.kafka.common.serialization.StringDeserializer;

public class App {
    public interface Options extends StreamingOptions {
    }

    public static void main(String[] args) {
        // TODO: Use a different runner for Beam (instead of direct runner)
        var options = PipelineOptionsFactory.fromArgs(args).withValidation().as(Options.class);
        var pipeline = Pipeline.create(options);
        pipeline.apply("Read from Kafka", KafkaIO.<String, Packet>read()
                        .withBootstrapServers("kafka.default.svc.cluster.local:9092")
                        .withTopic("packet")
                        .withKeyDeserializer(StringDeserializer.class)
                        .withValueDeserializer(PacketDeserializer.class)
                        .withoutMetadata())
                .apply(Values.create())
                .apply("Print records", ParDo.of(new DoFn<Packet, Packet>() {
                    @ProcessElement
                    public void processElement(ProcessContext c) {
                        Packet packet = c.element();
                        System.out.println("Packet: " + packet.getNumber());
                        c.output(packet);
                    }
                }));
        pipeline.run().waitUntilFinish();
    }
}