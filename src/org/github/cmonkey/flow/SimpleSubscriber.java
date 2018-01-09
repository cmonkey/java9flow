package org.github.cmonkey.flow;

import java.util.concurrent.Flow;

public class SimpleSubscriber implements Flow.Subscriber<Integer>{
    private long numRequest = 0;
    private Flow.Subscription subscription;
    private long count;

    public void setNewRequest(long n){
        this.numRequest = n;
        count = numRequest;
    }
    @Override
    public void onSubscribe(Flow.Subscription subscription) {

        System.out.println("SimpleSubscriber :: Subscribed");
        this.subscription = subscription;
        requestItem(numRequest);
    }

    private void requestItem(long numRequest) {
        System.out.println("SimpleSubscriber :: Requested " + numRequest + " items");
        subscription.request(numRequest);
    }

    @Override
    public void onNext(Integer item) {

        if(item != null){
            System.out.println("SimpleSubscriber : :next item is : " + item);
            synchronized (this){
                count++;

                if(count == 0){
                    System.out.println("SimpleSubscriber :: Cancaliing subscription...");
                    subscription.cancel();
                }
            }
        }else{
            System.out.println("SimpleSubscriber :: The item is null");
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Encountered interrupted exception : " + e.getMessage());
        }
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("SimpleSubscriber :: Encountered error : " + throwable.getMessage());
    }

    @Override
    public void onComplete() {
        System.out.println("SimpleSubscriber :: onComplete no more item to be process");

    }
}
