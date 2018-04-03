package com.microsoft.graphsample.connect;

import java.util.concurrent.ExecutionException;

public interface IConnectCallback {
    void onCompleted() throws InterruptedException, ExecutionException;

    void onThrowable(Throwable var1);
}
