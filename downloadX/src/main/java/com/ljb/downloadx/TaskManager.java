package com.ljb.downloadx;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class TaskManager {
    ThreadPoolExecutor executor;

    private final int maxThreadSum = 1;

    public TaskManager() {
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(maxThreadSum);
    }

    public int getRunningTaskCount() {
        return executor.getActiveCount();
    }

    public boolean isFullTask() {
        return executor.getActiveCount() == maxThreadSum;

    }


    public void addTask(Task task) {
        executor.execute(task);
    }

    public void removeTask(Task task) {
        executor.remove(task);
    }

    public void destroy() {
        executor.shutdown();
    }


}
