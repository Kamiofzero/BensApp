package com.ljb.downloadx;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class TaskManager {
    ThreadPoolExecutor threadPoolExecutor;

    private final int maxThreadSum = 1;

    public TaskManager() {
        threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(maxThreadSum);
    }
}
