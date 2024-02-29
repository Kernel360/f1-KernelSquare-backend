package com.kernelsquare.domainmysql.domain.coding_meeting_comment.service;

import com.kernelsquare.domainmysql.domain.coding_meeting_comment.command.CodingMeetingCommentCommand;
import com.kernelsquare.domainmysql.domain.coding_meeting_comment.info.CodingMeetingCommentListInfo;

import java.util.List;

public interface CodingMeetingCommentService {
    void createCodingMeetingComment(CodingMeetingCommentCommand.CreateCommand command, Long memberId);
    void updateCodingMeetingComment(CodingMeetingCommentCommand.UpdateCommand command, String codingMeetingCommentToken);
    void deleteCodingMeetingComment(String codingMeetingCommentToken);
    List<CodingMeetingCommentListInfo> findAllCodingMeetingComment(String codingMeetingToken);
}
