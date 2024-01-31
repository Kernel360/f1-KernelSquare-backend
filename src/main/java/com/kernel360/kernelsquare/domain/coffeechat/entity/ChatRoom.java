package com.kernel360.kernelsquare.domain.coffeechat.entity;

import com.kernel360.kernelsquare.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "ChatRoom")
@Getter
@Table(name = "chat_room")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "room_key", columnDefinition = "varchar(50)")
    private String roomKey;

    @Column(name = "room_name", columnDefinition = "varchar(20)")
    private String roomName;

    @Column(nullable = false, name = "active", columnDefinition = "tinyint")
    private Boolean active;

    @Column(nullable = false, name = "expiration_time", columnDefinition = "datetime")
    private LocalDateTime expirationTime;

    @Builder
    public ChatRoom(Long id, String roomKey, LocalDateTime expirationTime) {
        this.id = id;
        this.roomKey = roomKey;
        this.roomName = null;
        this.active = false;
        this.expirationTime = expirationTime;
    }

    public void activateRoom(String roomName) {
        this.roomName = roomName;
        this.active = true;
    }

    public void deactivateRoom() {
        this.active = false;
    }
}
