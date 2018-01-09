package org.github.cmonkey.flow;

import java.util.concurrent.*;

public class ProcessorSimple implements Flow.Processor<Integer, String>{
    private Flow.Subscription procSubscription;
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private ProcSubscriptionImpl subscription;
    private long numRequest;
    private ConcurrentLinkedQueue<Integer> resources;
    private final CompletableFuture<Void> terminated = new CompletableFuture<>();

    public ProcessorSimple(){
        numRequest = 0;
        resources = new ConcurrentLinkedQueue<>();
    }
    public void setNewRequest(long n){
        this.numRequest = n;
    }
    @Override
    public void subscribe(Flow.Subscriber subscriber) {
        subscription = new ProcSubscriptionImpl(subscriber, executor, resources, terminated);
        subscriber.onSubscribe(subscription);

    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        System.out.println("ProcessorSimple Subscribed");
        procSubscription = subscription;
        startProcessing();
    }

    private void startProcessing() {
        System.out.println("ProcessorSimple :: started processing " + numRequest + " item");
        procSubscription.request(numRequest);
    }

    @Override
    public void onNext(Integer item) {

        if(null == item){
            throw new NullPointerException();
        }
        resources.add(item);
        System.out.println("ProcessSmple processing item " + item );
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("Processsimple encountered error = " + throwable.getMessage());
    }

    @Override
    public void onComplete() {
        System.out.println("Processorsimple complete");
    }
}
