package com.kernelsquare.memberapi.domain.alert.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SseEmitterHandler {
    private static final Long DEFAULT_TIMEOUT = 60 * 1000L;
    private final ConcurrentHashMap<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public void addEmitter(Long memberId, SseEmitter emitter) {
        emitters.put(memberId, emitter);
    }

    public SseEmitter getEmitter(Long memberId) {
        return emitters.get(memberId);
    }

    public void deleteEmitter(Long memberId) {
        emitters.remove(memberId);
    }

    public SseEmitter createEmitter(Long memberId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);

        addEmitter(memberId, emitter);

        emitter.onCompletion(() -> deleteEmitter(memberId));
        emitter.onTimeout(() -> deleteEmitter(memberId));

        return emitter;
    }

    public void sendEmitter(Long memberId, Object message, String eventName) {
        SseEmitter emitter = getEmitter(memberId);

        if (Objects.nonNull(emitter)) {
            try {
                emitter.send(SseEmitter.event().id(memberId.toString()).name(eventName).data(message));
            } catch (IOException e) {
                deleteEmitter(memberId);
                emitter.completeWithError(e);
            }
        }
    }
}
