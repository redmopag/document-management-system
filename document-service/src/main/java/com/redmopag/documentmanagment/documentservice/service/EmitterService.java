package com.redmopag.documentmanagment.documentservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class EmitterService {
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter registerEmitter() {
        SseEmitter emitter = new SseEmitter((long) Integer.MAX_VALUE);
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitters.add(emitter);
        return emitter;
    }

    public void notifyEmitter(Object data, String eventName) {
        for (var emitter : emitters) {
            try {
                sendNotification(emitter, data, eventName);
            } catch (Exception e) {
                emitters.remove(emitter);
            }
        }
    }

    private synchronized void sendNotification(SseEmitter emitter, Object data, String eventName)
            throws IOException {
        emitter.send(SseEmitter.event()
                .name(eventName)
                .data(data));
    }
}
