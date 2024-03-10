package com.kernelsquare.memberapi.domain.coding_meeting.service;
import com.kernelsquare.core.dto.PageResponse;
import com.kernelsquare.domainmysql.domain.coding_meeting.info.CodingMeetingInfo;
import com.kernelsquare.domainmysql.domain.coding_meeting.service.CodingMeetingService;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdapter;
import com.kernelsquare.memberapi.domain.coding_meeting.dto.CodingMeetingDto;
import com.kernelsquare.memberapi.domain.coding_meeting.mapper.CodingMeetingDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CodingMeetingFacade {
    private final CodingMeetingService codingMeetingService;
    private final CodingMeetingDtoMapper codingMeetingDtoMapper;

    public CodingMeetingDto.CreateResponse createCodingMeeting(CodingMeetingDto.CreateRequest request, Long memberId) {
        CodingMeetingInfo.TokenInfo codingMeetingTokenInfo = codingMeetingService.createCodingMeeting(codingMeetingDtoMapper.toCreateCommand(request), memberId);
        return codingMeetingDtoMapper.toCreateResponse(codingMeetingTokenInfo);
    }

    public CodingMeetingDto.FindResponse findCodingMeeting(String codingMeetingToken) {
        CodingMeetingInfo.Info codingMeetingInfo = codingMeetingService.findCodingMeeting(codingMeetingToken);
        return codingMeetingDtoMapper.toFindResponse(codingMeetingInfo);
    }

    public PageResponse findAllCodingMeeting(Pageable pageable, String filterParameter, MemberAdapter memberAdapter) {
        Page<CodingMeetingInfo.ListInfo> allCodingMeetingInfo = codingMeetingService.findAllCodingMeeting(pageable, filterParameter, getMemberId(memberAdapter));
        List<CodingMeetingDto.FindAllResponse> findAllResponses = allCodingMeetingInfo.getContent().stream()
                .map(info -> codingMeetingDtoMapper.toFindAllResponse(info))
                .toList();
        return PageResponse.of(pageable, allCodingMeetingInfo, findAllResponses);
    }

    private Long getMemberId(MemberAdapter memberAdapter) {
        if (Objects.isNull(memberAdapter)) {
            return null;
        }
        return memberAdapter.getMember().getId();
    }

    public void updateCodingMeeting(CodingMeetingDto.UpdateRequest request, String codingMeetingToken) {
        codingMeetingService.updateCodingMeeting(codingMeetingDtoMapper.toUpdateCommand(request), codingMeetingToken);
    }

    public void deleteCodingMeeting(String codingMeetingToken) {
        codingMeetingService.deleteCodingMeeting(codingMeetingToken);
    }

    public void closeCodingMeeting(String codingMeetingToken) {
        codingMeetingService.closeCodingMeeting(codingMeetingToken);
    }
}
