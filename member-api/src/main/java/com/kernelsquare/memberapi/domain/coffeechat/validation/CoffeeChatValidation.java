package com.kernelsquare.memberapi.domain.coffeechat.validation;

import com.kernelsquare.core.common_response.error.code.CoffeeChatErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.core.type.AuthorityType;
import com.kernelsquare.core.type.ReservationMemberType;
import com.kernelsquare.domainmysql.domain.authority.entity.Authority;
import com.kernelsquare.domainmysql.domain.coffeechat.entity.ChatRoom;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member_authority.entity.MemberAuthority;
import com.kernelsquare.domainmysql.domain.reservation.entity.Reservation;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdapter;
import com.kernelsquare.memberapi.domain.coffeechat.component.ChatRoomMemberManager;
import com.kernelsquare.memberapi.domain.coffeechat.dto.ChatRoomMember;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class CoffeeChatValidation {

    public static void validateChatRoom(ChatRoom chatRoom) {
        LocalDateTime startTime = chatRoom.getExpirationTime().minusMinutes(30L);

        if (startTime.isAfter(LocalDateTime.now())) {
            throw new BusinessException(CoffeeChatErrorCode.COFFEE_CHAT_ROOM_NOT_ACTIVE);
        }

        if (!chatRoom.getExpirationTime().isAfter(LocalDateTime.now())) {
            throw new BusinessException(CoffeeChatErrorCode.COFFEE_CHAT_ROOM_EXPIRED);
        }
    }

    public static void validateChatRoomActive(ChatRoom chatRoom) {
        if (Boolean.FALSE.equals(chatRoom.getActive())) {
            throw new BusinessException(CoffeeChatErrorCode.COFFEE_CHAT_ROOM_NOT_ACTIVE);
        }
    }

    public static ReservationMemberType validatePermission(Reservation reservation, MemberAdapter memberAdapter) {
        Long menteeId = reservation.getMember().getId();
        Long mentorId = reservation.getReservationArticle().getMember().getId();
        Long memberId = memberAdapter.getMember().getId();

        if (memberId.equals(mentorId)) {
            return ReservationMemberType.MENTOR;
        }

        if (memberId.equals(menteeId)) {
            return ReservationMemberType.MENTEE;
        }

        return  ReservationMemberType.OTHER;
    }

    public static Boolean validateDuplicateEntry(ChatRoomMemberManager manager,
                                              ChatRoom chatRoom, MemberAdapter memberAdapter) {
        String roomKey = chatRoom.getRoomKey();

        Set<ChatRoomMember> chatMemberSet = manager.getChatRoom(roomKey);

        Member member = memberAdapter.getMember();

        ChatRoomMember chatRoomMember = ChatRoomMember.from(member);

        if (chatMemberSet.contains(chatRoomMember)) {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    public static void validateChatRoomCapacity(ChatRoomMemberManager manager, ChatRoom chatRoom) {
        Integer memberCount = manager.countChatRoomMember(chatRoom.getRoomKey());

        if (memberCount >= 2) {
            throw new BusinessException(CoffeeChatErrorCode.COFFEE_CHAT_ROOM_CAPACITY_EXCEEDED);
        }
    }

    public static void validateCoffeeChatRequest(Member sender, Member recipient) {
        if (sender.getId().equals(recipient.getId())) {
            throw new BusinessException(CoffeeChatErrorCode.COFFEE_CHAT_SELF_REQUEST_IMPOSSIBLE);
        }

        List<AuthorityType> recipientAuthorities = recipient.getAuthorities().stream()
            .map(MemberAuthority::getAuthority)
            .map(Authority::getAuthorityType)
            .toList();

        if (!recipientAuthorities.contains(AuthorityType.ROLE_MENTOR)) {
            throw new BusinessException(CoffeeChatErrorCode.COFFEE_CHAT_REQUEST_NOT_VALID);
        }
    }
}
