package org.github.cmonkey.flow;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class PubSubscriptionImpl implements Flow.Subscription{
    private final ExecutorService executor;
    private Flow.Subscriber subscriber;
    private final AtomicInteger itemValue;
    private AtomicBoolean isCancel;
    private final CompletableFuture<Void> terminated;

    public PubSubscriptionImpl(Flow.Subscriber subscriber,
                               ExecutorService executor,
                               CompletableFuture<Void> terminated){
        this.subscriber = subscriber;
        this.executor = executor;
        this.terminated = terminated;

        itemValue = new AtomicInteger();
        isCancel = new AtomicBoolean(false);
    }

    @Override
    public void request(long n) {

        System.out.println("PubSubscriptionImpl :: request recived to process " + n + " item");
        if (isCancel.get()){
            return;
        }

        if(n < 0){
            executor.execute(() -> subscriber.onError(new IllegalArgumentException()));
        }else{
            publicItem(n);
        }

    }

    private void publicItem(long n) {
        for (int i = 0; i < n; i++) {
            executor.execute(() -> {

                int nextVal = itemValue.incrementAndGet();
                System.out.println("PubSubscriptionImpl Pub Item = " + nextVal);
                subscriber.onNext(nextVal);
            });
            
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
           System.out.println("Encountered Exception = " + e.getMessage());
        }
    }

    @Override
    public void cancel() {
        isCancel.set(true);

        shutdown();
    }

    private void shutdown() {
        System.out.println("PubSubscriptionImpl shutdown executor");
        executor.shutdown();
        Executors.newSingleThreadExecutor().submit(() -> {

            System.out.println("PubSubscriptionImpl shutdown complete");
            terminated.complete(null);
        });
    }
}
