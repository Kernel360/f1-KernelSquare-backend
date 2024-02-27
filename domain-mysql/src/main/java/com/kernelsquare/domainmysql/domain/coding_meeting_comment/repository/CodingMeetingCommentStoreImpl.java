package com.kernelsquare.domainmysql.domain.coding_meeting_comment.repository;

import com.kernelsquare.domainmysql.domain.coding_meeting_comment.entity.CodingMeetingComment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CodingMeetingCommentStoreImpl implements CodingMeetingCommentStore {
    private final CodingMeetingCommentRepository codingMeetingCommentRepository;
    @Override
    public CodingMeetingComment store(CodingMeetingComment initCodingMeetingComment) {
        return codingMeetingCommentRepository.save(initCodingMeetingComment);
    }

    @Override
    public void delete(String codingMeetingCommentToken) {
        codingMeetingCommentRepository.deleteCodingMeetingCommentByCodingMeetingCommentToken(codingMeetingCommentToken);
    }

    @Override
    public void deleteAll(Long codingMeetingId) {
        codingMeetingCommentRepository.deleteAllByCodingMeetingId(codingMeetingId);
    }
}
