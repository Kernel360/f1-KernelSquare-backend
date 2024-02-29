package com.kernelsquare.domainmysql.domain.coding_meeting.repository.hashtag;

import com.kernelsquare.domainmysql.domain.coding_meeting.entity.CodingMeetingHashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CodingMeetingHashtagRepository extends JpaRepository<CodingMeetingHashtag, Long> {
    @Modifying
    @Query("DELETE FROM CodingMeetingHashtag cmh WHERE cmh.codingMeeting.id = :codingMeetingId")
    void deleteAllByCodingMeetingId(@Param("codingMeetingId") Long codingMeetingId);
}

