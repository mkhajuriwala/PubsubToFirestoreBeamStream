package com.kloudbuddy.stream;

import com.google.firestore.v1.Document;
import com.google.firestore.v1.Write;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.firestore.FirestoreIO;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;

public class App {
    public static void main(String[] args){
        RuntimeOptions options =
                PipelineOptionsFactory.fromArgs(args).withValidation().as(RuntimeOptions.class);
        options.setStreaming(true);
        Pipeline pipeline = Pipeline.create(options);
        pipeline
                .apply("Read PubSub Messages", PubsubIO.readStrings().fromSubscription(options.getInputTopicSubscription()))
                .apply(ParDo.of(new DoFn<String, Document>() {
                    @ProcessElement
                    public void processElement(ProcessContext processContext) {
                        processContext.output(Document.newBuilder().setName(processContext.getPipelineOptions().as(RuntimeOptions.class).getFirestoreProject()
                                        +"databases/(default)/"+processContext.getPipelineOptions().as(RuntimeOptions.class).getFirestoreCollection()
                                        +processContext.element())
                                .build());
                    }
                }))
                .apply(ParDo.of(new DoFn<Document, Write>() {
                    @ProcessElement
                    public void processElement(ProcessContext processContext) {
                        processContext.output(Write.newBuilder().setUpdate(processContext.element()).build());
                    }
                }))
                .apply("Write to Firestore", FirestoreIO.v1().write().batchWrite().build());
        pipeline.run().waitUntilFinish();
    }
}
