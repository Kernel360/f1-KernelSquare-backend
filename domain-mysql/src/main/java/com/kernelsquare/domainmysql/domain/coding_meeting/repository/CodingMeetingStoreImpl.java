package com.kernelsquare.domainmysql.domain.coding_meeting.repository;

import com.kernelsquare.core.common_response.error.code.CodingMeetingErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmysql.domain.coding_meeting.entity.CodingMeeting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CodingMeetingStoreImpl implements CodingMeetingStore{
    private final CodingMeetingRepository codingMeetingRepository;
    @Override
    public CodingMeeting store(CodingMeeting initCodingMeeting, Long memberId) {
        if (codingMeetingRepository.existsByMemberIdAndCodingMeetingClosedIsFalse(memberId)) {
            throw new BusinessException(CodingMeetingErrorCode.CODING_MEETING_ALREADY_EXIST);
        }
        return codingMeetingRepository.save(initCodingMeeting);
    }

    @Override
    public void delete(String codingMeetingToken) {
        codingMeetingRepository.deleteByCodingMeetingToken(codingMeetingToken);
    }
}
