package com.kernelsquare.memberapi.domain.coffeechat.component;

import com.kernelsquare.core.common_response.error.code.MemberErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.repository.MemberRepository;
import com.kernelsquare.memberapi.domain.coffeechat.dto.ChatRoomMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class ChatRoomMemberManager {
    private final MemberRepository memberRepository;
    private final ConcurrentHashMap<String, List<ChatRoomMember>> chatRoomMap = new ConcurrentHashMap<>();

    public void addChatRoom(String roomKey) {
        chatRoomMap.computeIfAbsent(roomKey, k -> new ArrayList<>());
    }

    public void addChatMember(String roomKey, Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));

        chatRoomMap.computeIfAbsent(roomKey, k -> new ArrayList<>()).add(ChatRoomMember.from(member));
    }

    public List<ChatRoomMember> getChatRoom(String roomKey) {
        return chatRoomMap.getOrDefault(roomKey, new ArrayList<>());
    }

    public void removeChatRoomMember(String roomKey, Long memberId) {
        List<ChatRoomMember> chatRoomMemberList = getChatRoom(roomKey);

        ChatRoomMember member = chatRoomMemberList.stream()
            .filter(chatRoomMember -> chatRoomMember.memberId().equals(memberId))
            .findFirst()
            .get();

        chatRoomMemberList.remove(member);
    }

    public Integer countChatRoomMember(String roomKey) {
        List<ChatRoomMember> chatRoomMemberList = getChatRoom(roomKey);

        return chatRoomMemberList.size();
    }
}
