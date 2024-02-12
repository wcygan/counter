package io.counter.beam;

import com.rng.v1.Packet;
import org.apache.beam.runners.flink.FlinkRunner;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.kafka.KafkaIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.KV;
import org.apache.kafka.common.serialization.LongDeserializer;

public class App {
    public static void main(String[] args) {
        PipelineOptions options = PipelineOptionsFactory.create();
        options.setRunner(FlinkRunner.class);
        Pipeline p = Pipeline.create(options);

        String bootstrapServers = "kafka.default.svc.cluster.local:9092";
        String topic = "packet";

        p.apply(KafkaIO.<Long, Packet>read()
                        .withBootstrapServers(bootstrapServers)
                        .withTopic(topic)
                        .withKeyDeserializer(LongDeserializer.class)
                        .withValueDeserializer(PacketDeserializer.class) // Assuming PacketDeserializer is implemented elsewhere
                        .withoutMetadata())
                .apply(ParDo.of(new DoFn<KV<Long, Packet>, Void>() {
                    @ProcessElement
                    public void processElement(ProcessContext c) {
                        System.out.println(c.element().getValue());
                    }
                }));

        p.run().waitUntilFinish();
    }
}
