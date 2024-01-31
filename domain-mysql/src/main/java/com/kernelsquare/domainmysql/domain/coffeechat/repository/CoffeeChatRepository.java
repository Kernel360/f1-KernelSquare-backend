package com.kernelsquare.domainmysql.domain.coffeechat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kernelsquare.domainmysql.domain.coffeechat.entity.ChatRoom;

public interface CoffeeChatRepository extends JpaRepository<ChatRoom, Long> {
	@Modifying
	@Query("DELETE FROM ChatRoom a WHERE a.id IN (SELECT b.chatRoom.id FROM Reservation b WHERE b.reservationArticle.id = :postId)")
	void deleteChatRoom(@Param("postId") Long postId);

	Optional<ChatRoom> findByRoomKey(String roomKey);

	List<ChatRoom> findAllByActive(Boolean active);
}
