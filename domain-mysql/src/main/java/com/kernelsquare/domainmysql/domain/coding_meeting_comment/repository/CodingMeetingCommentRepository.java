package com.kernelsquare.domainmysql.domain.coding_meeting_comment.repository;

import com.kernelsquare.domainmysql.domain.coding_meeting_comment.entity.CodingMeetingComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CodingMeetingCommentRepository extends JpaRepository<CodingMeetingComment, Long> {
    Optional<CodingMeetingComment> findByCodingMeetingCommentToken(String codingMeetingCommentToken);

    List<CodingMeetingComment> findAllByCodingMeetingId(Long codingMeetingId);

    @Modifying
    @Query("DELETE FROM CodingMeetingComment cmc WHERE cmc.codingMeeting.id = :codingMeetingId")
    void deleteAllByCodingMeetingId(@Param("codingMeetingId") Long codingMeetingId);

    void deleteCodingMeetingCommentByCodingMeetingCommentToken(String codingMeetingCommentToken);
}
