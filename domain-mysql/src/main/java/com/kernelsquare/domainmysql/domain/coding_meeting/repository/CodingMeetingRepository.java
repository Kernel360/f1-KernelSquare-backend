package com.kernelsquare.domainmysql.domain.coding_meeting.repository;

import com.kernelsquare.domainmysql.domain.coding_meeting.entity.CodingMeeting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodingMeetingRepository extends JpaRepository<CodingMeeting, Long> {
    Optional<CodingMeeting> findByCodingMeetingToken(String codingMeetingToken);

    void deleteByCodingMeetingToken(String codingMeetingToken);

    Page<CodingMeeting> findAllByCodingMeetingClosedIsTrue(Pageable pageable);

    Page<CodingMeeting> findAllByCodingMeetingClosedIsFalse(Pageable pageable);

    Page<CodingMeeting> findAllByMemberId(Pageable pageable, Long memberId);

    Boolean existsByMemberIdAndCodingMeetingClosedIsFalse(Long memberId);
}
