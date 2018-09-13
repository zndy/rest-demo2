package com.johnny.restdemo2.controller;

import com.johnny.restdemo2.Entity.Request.PayAmount;
import com.johnny.restdemo2.Entity.Response.Status;
import com.johnny.restdemo2.event.CoinInsertEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.johnny.restdemo2.Entity.Response.Status.ResponseType.SUCCESSFUL;

@RestController
@RequestMapping("/payment")
public class CoinChangerController {

    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @GetMapping("/hello")
    public String hello() throws InterruptedException {
        Thread.sleep(10000);
        return "hallo world";
    }

    @GetMapping("/start1")
    public Status start1() {
        return new Status(SUCCESSFUL);
    }

    @PostMapping("/start")
    public Status start(@RequestBody PayAmount amount) {
        return new Status(SUCCESSFUL);
    }

    @GetMapping("/coin-inserted")
    public SseEmitter handleCoinInsert() {
        SseEmitter emitter = new SseEmitter();
        this.emitters.add(emitter);

        emitter.onCompletion(() -> this.emitters.remove(emitter));
        emitter.onTimeout(() -> this.emitters.remove(emitter));

        return emitter;
    }

    @GetMapping("/end-prematurely")
    public void endPrematurely() {
        System.out.println("Customer ended prematurely");
    }

    @GetMapping("/cancel")
    public void cancel() {
        System.out.println("Customer canceled");
    }

    @EventListener
    public void onCoinInserted(CoinInsertEvent event) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        this.emitters.forEach(emitter -> {
            try {
                emitter.send( //Senden des SSE Payloads
                        SseEmitter.event() //Builder für die Events
                                .data(event.getAmount(), MediaType.APPLICATION_JSON_UTF8) //Daten des Events, entspricht dem data: in der SSE Definition
                                .name(event.getName()) //Name des Events, entspricht deem event: in der SSE Definition);
                );
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        });
        this.emitters.removeAll(deadEmitters);
    }

}
