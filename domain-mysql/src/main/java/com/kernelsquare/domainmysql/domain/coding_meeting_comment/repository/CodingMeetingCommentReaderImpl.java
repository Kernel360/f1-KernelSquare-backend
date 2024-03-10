package com.kernelsquare.domainmysql.domain.coding_meeting_comment.repository;

import com.kernelsquare.core.common_response.error.code.CodingMeetingCommentErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmysql.domain.coding_meeting_comment.entity.CodingMeetingComment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CodingMeetingCommentReaderImpl implements CodingMeetingCommentReader {
    private final CodingMeetingCommentRepository codingMeetingCommentRepository;

    @Override
    public CodingMeetingComment findCodingMeetingComment(String codingMeetingCommentToken) {
        return codingMeetingCommentRepository.findByCodingMeetingCommentToken(codingMeetingCommentToken)
                .orElseThrow(() -> new BusinessException(CodingMeetingCommentErrorCode.CODIMG_MEETING_COMMENT_NOT_FOUND));
    }

    @Override
    public List<CodingMeetingComment> findAllCodingMeetingComment(Long codingMeetingId) {
        return codingMeetingCommentRepository.findAllByCodingMeetingId(codingMeetingId);
    }
}
