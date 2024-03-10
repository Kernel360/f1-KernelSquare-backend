package com.kernelsquare.domainmysql.domain.coding_meeting_comment.repository;

import com.kernelsquare.domainmysql.domain.coding_meeting_comment.entity.CodingMeetingComment;

import java.util.List;

public interface CodingMeetingCommentReader {
    CodingMeetingComment findCodingMeetingComment(String codingMeetingCommentToken);
    List<CodingMeetingComment> findAllCodingMeetingComment(Long CodingMeetingId);
}
