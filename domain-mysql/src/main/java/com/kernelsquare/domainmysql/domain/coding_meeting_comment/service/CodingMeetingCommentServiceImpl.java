package com.kernelsquare.domainmysql.domain.coding_meeting_comment.service;

import com.kernelsquare.domainmysql.domain.coding_meeting.entity.CodingMeeting;
import com.kernelsquare.domainmysql.domain.coding_meeting.repository.CodingMeetingReader;
import com.kernelsquare.domainmysql.domain.coding_meeting_comment.command.CodingMeetingCommentCommand;
import com.kernelsquare.domainmysql.domain.coding_meeting_comment.entity.CodingMeetingComment;
import com.kernelsquare.domainmysql.domain.coding_meeting_comment.info.CodingMeetingCommentListInfo;
import com.kernelsquare.domainmysql.domain.coding_meeting_comment.repository.CodingMeetingCommentReader;
import com.kernelsquare.domainmysql.domain.coding_meeting_comment.repository.CodingMeetingCommentStore;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.repository.MemberReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CodingMeetingCommentServiceImpl implements CodingMeetingCommentService {
    private final MemberReader memberReader;
    private final CodingMeetingReader codingMeetingReader;
    private final CodingMeetingCommentStore codingMeetingCommentStore;
    private final CodingMeetingCommentReader codingMeetingCommentReader;

    @Override
    public void createCodingMeetingComment(CodingMeetingCommentCommand.CreateCommand command, Long memberId) {
        Member member = memberReader.findMember(memberId);
        CodingMeeting codingMeeting = codingMeetingReader.findCodingMeeting(command.codingMeetingToken());
        CodingMeetingComment initCodingMeetingComment = command.toEntity(member, codingMeeting);
        codingMeetingCommentStore.store(initCodingMeetingComment);
    }

    @Override
    @Transactional
    public void updateCodingMeetingComment(CodingMeetingCommentCommand.UpdateCommand command, String codingMeetingCommentToken) {
        CodingMeetingComment codingMeetingComment = codingMeetingCommentReader.findCodingMeetingComment(codingMeetingCommentToken);
        codingMeetingComment.update(command);
    }

    @Override
    @Transactional
    public void deleteCodingMeetingComment(String codingMeetingCommentToken) {
        codingMeetingCommentStore.delete(codingMeetingCommentToken);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CodingMeetingCommentListInfo> findAllCodingMeetingComment(String codingMeetingToken) {
        CodingMeeting codingMeeting = codingMeetingReader.findCodingMeeting(codingMeetingToken);
        List<CodingMeetingComment> codingMeetingCommentList = codingMeetingCommentReader.findAllCodingMeetingComment(codingMeeting.getId());
        return codingMeetingCommentList.stream().map(CodingMeetingCommentListInfo::of).toList();
    }
}
