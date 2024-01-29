package com.kernel360.kernelsquare.domain.coffeechat.repository;

import com.kernel360.kernelsquare.domain.coffeechat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CoffeeChatRepository extends JpaRepository<ChatRoom, Long> {

    @Modifying
    @Query("DELETE FROM ChatRoom a WHERE a.id IN (SELECT b.chatRoom.id FROM Reservation b WHERE b.reservationArticle.id = :postId)")
    void deleteChatRoom(@Param("postId") Long postId);

}
