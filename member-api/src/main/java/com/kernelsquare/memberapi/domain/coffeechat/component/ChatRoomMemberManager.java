package com.kernelsquare.memberapi.domain.coffeechat.component;

import com.kernelsquare.core.common_response.error.code.MemberErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.repository.MemberRepository;
import com.kernelsquare.memberapi.domain.coffeechat.dto.ChatRoomMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@RequiredArgsConstructor
public class ChatRoomMemberManager {
    private final MemberRepository memberRepository;
    private final ConcurrentHashMap<String, CopyOnWriteArraySet<ChatRoomMember>> chatRoomMap = new ConcurrentHashMap<>();

    public void addChatRoom(String roomKey) {
        chatRoomMap.computeIfAbsent(roomKey, k -> new CopyOnWriteArraySet<>());
    }

    public void addChatMember(String roomKey, Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));

        chatRoomMap.computeIfAbsent(roomKey, k -> new CopyOnWriteArraySet<>()).add(ChatRoomMember.from(member));
    }

    public Set<ChatRoomMember> getChatRoom(String roomKey) {
        return chatRoomMap.getOrDefault(roomKey, new CopyOnWriteArraySet<>());
    }

    public ChatRoomMember removeChatRoomMember(String roomKey, Long memberId) {
        Set<ChatRoomMember> chatRoomMemberList = getChatRoom(roomKey);

        ChatRoomMember chatRoomMember = ChatRoomMember.builder()
            .memberId(memberId)
            .build();

        chatRoomMemberList.remove(chatRoomMember);

        return chatRoomMember;
    }

    public Integer countChatRoomMember(String roomKey) {
        Set<ChatRoomMember> chatRoomMemberList = getChatRoom(roomKey);

        return chatRoomMemberList.size();
    }
}
