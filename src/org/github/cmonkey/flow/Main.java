package org.github.cmonkey.flow;

public class Main {

    public static void main(String[] args) {
        PublisherSample publisher = new PublisherSample();
        SimpleSubscriber subscriber = new SimpleSubscriber();
        ProcessorSimple processor = new ProcessorSimple();

        subscriber.setNewRequest(4);
        processor.setNewRequest(10);

        publisher.subscribe(processor);
        processor.subscribe(subscriber);
    }
}
