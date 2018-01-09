package org.github.cmonkey.flow;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;

public class PublisherSample implements Flow.Publisher<Integer>{
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private PubSubscriptionImpl subscription;
    private final CompletableFuture<Void> terminated = new CompletableFuture<>();
    @Override
    public void subscribe(Flow.Subscriber<? super Integer> subscriber) {
        subscription = new PubSubscriptionImpl(subscriber, executor, terminated) ;
        subscriber.onSubscribe(subscription);
    }
}
