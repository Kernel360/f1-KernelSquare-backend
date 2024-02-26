package com.kernelsquare.domainmysql.domain.stream.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class SseRepository {
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    /**
     * 주어진 아이디와 이미터를 저장
     *
     * @param id      - 사용자 아이디.
     * @param emitter - 이벤트 Emitter.
     */
    public void save(Long id, SseEmitter emitter) {
        emitters.put(id, emitter);
    }

    /**
     * 주어진 아이디의 Emitter를 제거
     *
     * @param id - 사용자 아이디.
     */
    public void deleteById(Long id) {
        emitters.remove(id);
    }

    /**
     * 주어진 아이디의 Emitter를 가져옴.
     *
     * @param id - 사용자 아이디.
     * @return SseEmitter - 이벤트 Emitter.
     */
    public SseEmitter get(Long id) {
        return emitters.get(id);
    }

//    private final Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();
//    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();
//
//    public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
//        emitterMap.put(emitterId, sseEmitter);
//        return sseEmitter;
//    }
//
//    public void saveEventCache(String emitterId, Object event) {
//        eventCache.put(emitterId, event);
//    }
//
//    public Map<String, SseEmitter> findAllEmitters() {
//        return new HashMap<>(emitterMap);
//    }
//
//    public Map<String, SseEmitter> findAllEmitterStartWithById(String memberId) {
//        return emitterMap.entrySet().stream()
//            .filter(entry -> entry.getKey().startsWith(memberId))
//            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//    }
//
//    public Map<String, Object> findAllEventCacheStartWithById(String memberId) {
//        return eventCache.entrySet().stream()
//            .filter(entry -> entry.getKey().startsWith(memberId))
//            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//    }
//
//    public void deleteById(String emitterId) {
//        emitterMap.remove(emitterId);
//    }
}
