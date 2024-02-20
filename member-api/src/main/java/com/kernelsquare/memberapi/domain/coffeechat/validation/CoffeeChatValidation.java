package com.kernelsquare.memberapi.domain.coffeechat.validation;

import com.kernelsquare.core.common_response.error.code.CoffeeChatErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.core.type.ReservationMemberType;
import com.kernelsquare.domainmysql.domain.coffeechat.entity.ChatRoom;
import com.kernelsquare.domainmysql.domain.reservation.entity.Reservation;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdapter;

import java.time.LocalDateTime;

public class CoffeeChatValidation {

    public static void validateChatRoom(ChatRoom chatRoom) {
        LocalDateTime startTime = chatRoom.getExpirationTime().minusMinutes(10000000000L);

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
}
