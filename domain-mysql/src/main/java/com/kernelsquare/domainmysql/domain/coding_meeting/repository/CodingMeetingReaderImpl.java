package com.kernelsquare.domainmysql.domain.coding_meeting.repository;

import com.kernelsquare.core.common_response.error.code.CodingMeetingErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmysql.domain.coding_meeting.entity.CodingMeeting;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CodingMeetingReaderImpl implements CodingMeetingReader{

    private final CodingMeetingRepository codingMeetingRepository;

    @Override
    public CodingMeeting findCodingMeeting(String codingMeetingToken) {
        return codingMeetingRepository.findByCodingMeetingToken(codingMeetingToken)
                .orElseThrow(() -> new BusinessException(CodingMeetingErrorCode.CODING_MEETING_NOT_FOUND));
    }

    @Override
    public Page<CodingMeeting> findAllCodingMeeting(Pageable pageable) {
        return codingMeetingRepository.findAll(pageable);
    }
}
