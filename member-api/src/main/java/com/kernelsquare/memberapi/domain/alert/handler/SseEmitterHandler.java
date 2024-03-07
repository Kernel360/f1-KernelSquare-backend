package com.kernelsquare.memberapi.domain.alert.handler;

import com.kernelsquare.domainmongodb.domain.alert.entity.Alert;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SseEmitterHandler {
    private static final Long DEFAULT_TIMEOUT = 60 * 60 * 1000L;
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
        emitter.onTimeout(emitter::complete);

        return emitter;
    }

    public void sendEmitter(Alert alert) {
        SseEmitter emitter = getEmitter(Long.valueOf(alert.getRecipientId()));

        if (Objects.nonNull(emitter)) {
            try {
                emitter.send(SseEmitter.event()
                    .id(alert.getRecipientId())
                    .name(alert.getAlertType().name())
                    .data(alert, MediaType.APPLICATION_JSON));
            } catch (IOException e) {
                deleteEmitter(Long.valueOf(alert.getRecipientId()));
                emitter.completeWithError(e);
            }
        }
    }
}
