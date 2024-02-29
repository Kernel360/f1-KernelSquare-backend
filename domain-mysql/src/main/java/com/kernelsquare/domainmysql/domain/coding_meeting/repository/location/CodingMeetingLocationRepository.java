package com.kernelsquare.domainmysql.domain.coding_meeting.repository.location;

import com.kernelsquare.domainmysql.domain.coding_meeting.entity.CodingMeetingLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CodingMeetingLocationRepository extends JpaRepository<CodingMeetingLocation, Long> {
    @Modifying
    @Query("DELETE FROM CodingMeetingLocation cml WHERE cml.codingMeeting.id = :codingMeetingId")
    void deleteAllByCodingMeetingId(@Param("codingMeetingId") Long codingMeetingId);
}
