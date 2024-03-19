package com.kernelsquare.alertapi.domain.alert.manager;

import com.kernelsquare.domainmongodb.domain.alert.entity.Alert;
import com.kernelsquare.alertapi.domain.alert.handler.SseEmitterHandler;
import com.kernelsquare.alertapi.domain.auth.dto.MemberAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
@RequiredArgsConstructor
public class SseManager {
    private final SseEmitterHandler sseEmitterHandler;

    /* 클라이언트가 구독을 하면 창구 역할을 하는 SseEmitter를 클라이언트에게 보내기 위한 메서드 */
    public SseEmitter subscribe(MemberAdapter memberAdapter) {
        return sseEmitterHandler.createEmitter(memberAdapter.getMember().getId());
    }

    /* 특정 클라이언트에게 메시지를 보내기 위한 메서드 */
    public void send(Alert alert) {
        sseEmitterHandler.sendEmitter(alert);
    }
}
