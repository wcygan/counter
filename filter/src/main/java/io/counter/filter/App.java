package io.counter.filter;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.kafka.KafkaIO;
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.options.StreamingOptions;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.TypeDescriptors;
import org.apache.kafka.common.serialization.StringDeserializer;

public class App {
    public interface Options extends StreamingOptions {
        @Description("Input text to print.")
        @Default.String("Yay!")
        String getInputText();

        void setInputText(String value);
    }

    public static PCollection<String> buildPipeline(Pipeline pipeline) {
        return pipeline
                .apply("Read from Kafka", KafkaIO.<String, String>read()
                        .withBootstrapServers("kafka.default.svc.cluster.local:9092")
                        .withTopic("packet")
                        .withKeyDeserializer(StringDeserializer.class)
                        .withValueDeserializer(StringDeserializer.class)
                        .withoutMetadata())
                .apply("Print records", MapElements.into(TypeDescriptors.strings())
                        .via(record -> {
                            System.out.println(record.getValue());
                            return record.getValue();
                        }));
    }

    public static void main(String[] args) {
        var options = PipelineOptionsFactory.fromArgs(args).withValidation().as(Options.class);
        var pipeline = Pipeline.create(options);
        App.buildPipeline(pipeline);
        pipeline.run().waitUntilFinish();
    }
}