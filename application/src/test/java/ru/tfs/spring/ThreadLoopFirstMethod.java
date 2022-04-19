package ru.tfs.spring;


public class ThreadLoopFirstMethod implements Runnable {
    FirstSampleService firstSampleService;

    public ThreadLoopFirstMethod(FirstSampleService firstSampleService) {
        this.firstSampleService = firstSampleService;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                firstSampleService.justNothing();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
