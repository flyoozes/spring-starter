package ru.tfs.spring;

import org.springframework.stereotype.Service;
import ru.tfs.autoconfigure.annotations.Timed;

@Service
public class SecondSampleService implements SampleService {
    @Override
    public void justNothing() throws InterruptedException {
        Thread.sleep((long) (Math.random() * 500));
    }

    @Override
    @Timed
    public void justAnotherNothing() throws InterruptedException {
        Thread.sleep((long) (Math.random() * 300));
    }
}
