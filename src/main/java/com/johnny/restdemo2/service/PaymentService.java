package com.johnny.restdemo2.service;

import com.johnny.restdemo2.event.CoinInsertEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class PaymentService {
    @Autowired
    public ApplicationEventPublisher eventPublisher;

    @Scheduled(fixedRate = 2000)
    public void doSomething() {
        int amount = new Random().nextInt(200);

        this.eventPublisher.publishEvent(new CoinInsertEvent(amount));
    }
}
