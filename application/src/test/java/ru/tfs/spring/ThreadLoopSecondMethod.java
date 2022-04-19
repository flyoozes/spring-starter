package ru.tfs.spring;

public class ThreadLoopSecondMethod implements Runnable {
    SampleService sampleService;

    public ThreadLoopSecondMethod(SampleService firstSampleService) {
        this.sampleService = firstSampleService;
    }

    @Override
    public void run() {
        for (int i = 0; i < 55; i++) {
            try {
                sampleService.justAnotherNothing();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}