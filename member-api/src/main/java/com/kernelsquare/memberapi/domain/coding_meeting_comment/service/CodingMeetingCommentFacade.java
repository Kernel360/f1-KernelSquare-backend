package com.kernelsquare.memberapi.domain.coding_meeting_comment.service;

import com.kernelsquare.domainmysql.domain.coding_meeting_comment.info.CodingMeetingCommentListInfo;
import com.kernelsquare.domainmysql.domain.coding_meeting_comment.service.CodingMeetingCommentService;
import com.kernelsquare.memberapi.domain.coding_meeting_comment.dto.CodingMeetingCommentDto;
import com.kernelsquare.memberapi.domain.coding_meeting_comment.mapper.CodingMeetingCommentDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CodingMeetingCommentFacade {
    private final CodingMeetingCommentService codingMeetingCommentService;
    private final CodingMeetingCommentDtoMapper codingMeetingCommentDtoMapper;

    public List<CodingMeetingCommentDto.FindAllResponse> findAllCodingMeetingComment(String codingMeetingToken) {
        List<CodingMeetingCommentListInfo> allCodingMeetingCommentInfo = codingMeetingCommentService.findAllCodingMeetingComment(codingMeetingToken);
        return allCodingMeetingCommentInfo.stream()
                .map(info -> codingMeetingCommentDtoMapper.toFindAllResponse(info))
                .toList();
    }

    public void createCodingMeetingComment(CodingMeetingCommentDto.CreateRequest request, Long memberId) {
        codingMeetingCommentService.createCodingMeetingComment(codingMeetingCommentDtoMapper.toCreateCommand(request), memberId);
    }

    public void updateCodingMeetingComment(CodingMeetingCommentDto.UpdateRequest request, String codingMeetingToken) {
        codingMeetingCommentService.updateCodingMeetingComment(codingMeetingCommentDtoMapper.toUpdateCommand(request), codingMeetingToken);
    }

    public void deleteCodingMeetingComment(String codingMeetingCommentToken) {
        codingMeetingCommentService.deleteCodingMeetingComment(codingMeetingCommentToken);
    }
}
