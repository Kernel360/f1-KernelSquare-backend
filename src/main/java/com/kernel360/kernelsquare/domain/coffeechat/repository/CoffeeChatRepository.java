package com.kernel360.kernelsquare.domain.coffeechat.repository;

import com.kernel360.kernelsquare.domain.coffeechat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoffeeChatRepository extends JpaRepository<ChatRoom, Long> {
}
