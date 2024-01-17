package com.kernel360.kernelsquare.domain.coffeechat.entity;

import com.kernel360.kernelsquare.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(nullable = false, name = "room_name", columnDefinition = "varchar(20)")
    private String roomName;

    @Column(nullable = false, name = "active", columnDefinition = "tinyint")
    private Boolean active;

    @Builder
    public ChatRoom(Long id, String roomKey, String roomName) {
        this.id = id;
        this.roomKey = roomKey;
        this.roomName = roomName;
        this.active = false;
    }
}
