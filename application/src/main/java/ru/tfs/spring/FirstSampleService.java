package ru.tfs.spring;

import org.springframework.stereotype.Service;
import ru.tfs.autoconfigure.annotations.Timed;

@Service
@Timed
public class FirstSampleService implements SampleService {

    @Override
    public void justNothing() throws InterruptedException {
        Thread.sleep((long) (Math.random() * 500));
    }

    @Override
    public void justAnotherNothing() throws InterruptedException {
        Thread.sleep((long) (Math.random() * 100));
    }
}
