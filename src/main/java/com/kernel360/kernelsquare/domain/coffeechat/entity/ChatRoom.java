package com.kernel360.kernelsquare.domain.coffeechat.entity;

import com.kernel360.kernelsquare.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "chatroom")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(nullable = false, name = "room_name", columnDefinition = "varchar(20)")
    private String roomName;

    @Builder
    public ChatRoom(String id, String roomName) {
        this.id = id;
        this.roomName = roomName;
    }
}
