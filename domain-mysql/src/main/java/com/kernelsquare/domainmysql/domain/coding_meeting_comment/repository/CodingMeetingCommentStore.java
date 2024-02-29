package com.kernelsquare.domainmysql.domain.coding_meeting_comment.repository;

import com.kernelsquare.domainmysql.domain.coding_meeting_comment.entity.CodingMeetingComment;

public interface CodingMeetingCommentStore {
    CodingMeetingComment store(CodingMeetingComment initCodingMeetingComment);
    void delete(String codingMeetingCommentToken);
    void deleteAll(Long codingMeetingId);
}
