package com.kernelsquare.domainmysql.domain.coding_meeting.service;

import com.kernelsquare.domainmysql.domain.coding_meeting.command.CodingMeetingCommand;
import com.kernelsquare.domainmysql.domain.coding_meeting.entity.CodingMeeting;
import com.kernelsquare.domainmysql.domain.coding_meeting.info.CodingMeetingInfo;
import com.kernelsquare.domainmysql.domain.coding_meeting.repository.hashtag.CodingMeetingHashtagFactory;
import com.kernelsquare.domainmysql.domain.coding_meeting.repository.location.CodingMeetingLocationFactory;
import com.kernelsquare.domainmysql.domain.coding_meeting.repository.CodingMeetingReader;
import com.kernelsquare.domainmysql.domain.coding_meeting.repository.CodingMeetingStore;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.repository.MemberReader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CodingMeetingServiceImpl implements CodingMeetingService{
    private final MemberReader memberReader;
    private final CodingMeetingStore codingMeetingStore;
    private final CodingMeetingReader codingMeetingReader;
    private final CodingMeetingHashtagFactory codingMeetingHashtagFactory;
    private final CodingMeetingLocationFactory codingMeetingLocationFactory;

    @Override
    @Transactional
    public CodingMeetingInfo.TokenInfo createCodingMeeting(CodingMeetingCommand.CreateCommand command, Long memberId) {
        Member member = memberReader.findMember(memberId);
        CodingMeeting initCodingMeeting = command.toEntity(member);
        CodingMeeting codingMeeting = codingMeetingStore.store(initCodingMeeting, memberId);
        codingMeetingHashtagFactory.store(command, codingMeeting);
        codingMeetingLocationFactory.store(command, codingMeeting);
        return CodingMeetingInfo.TokenInfo.of(codingMeeting);
    }

    @Override
    @Transactional
    public void updateCodingMeeting(CodingMeetingCommand.UpdateCommand command, String codingMeetingToken) {
        CodingMeeting codingMeeting = codingMeetingReader.findCodingMeeting(codingMeetingToken);
        codingMeeting.update(command);
        codingMeetingHashtagFactory.update(command, codingMeeting);
        codingMeetingLocationFactory.update(command, codingMeeting);
    }

    @Override
    @Transactional
    public void closeCodingMeeting(String codingMeetingToken) {
        CodingMeeting codingMeeting = codingMeetingReader.findCodingMeeting(codingMeetingToken);
        codingMeeting.close();
    }

    @Override
    @Transactional
    public void deleteCodingMeeting(String codingMeetingToken) {
        CodingMeeting codingMeeting = codingMeetingReader.findCodingMeeting(codingMeetingToken);
        codingMeetingHashtagFactory.delete(codingMeeting);
        codingMeetingLocationFactory.delete(codingMeeting);
        codingMeetingStore.delete(codingMeetingToken);
    }

    @Override
    @Transactional(readOnly = true)
    public CodingMeetingInfo.Info findCodingMeeting(String codingMeetingToken) {
        CodingMeeting codingMeeting = codingMeetingReader.findCodingMeeting(codingMeetingToken);
        return CodingMeetingInfo.Info.of(codingMeeting);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CodingMeetingInfo.ListInfo> findAllCodingMeeting(Pageable pageable, String filterParameter, Long memberId) {
        Page<CodingMeeting> codingMeetingPage = codingMeetingReader.findAllCodingMeeting(pageable, filterParameter, memberId);
        return codingMeetingPage.map(CodingMeetingInfo.ListInfo::of);
    }
}
