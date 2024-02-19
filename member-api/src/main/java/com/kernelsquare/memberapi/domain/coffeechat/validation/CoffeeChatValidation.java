//package com.kernelsquare.memberapi.domain.coffeechat.validation;
//
//import com.kernelsquare.core.common_response.error.exception.BusinessException;
//import com.kernelsquare.domainmysql.domain.authority.entity.Authority;
//import com.kernelsquare.memberapi.domain.auth.dto.MemberAdapter;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//
//import java.lang.reflect.Member;
//
//public class CoffeeChatValidation {
//
//    private void validateMentorAccess(MemberAdapter memberAdapter, EnterCoffeeChatRoomRequest enterCoffeeChatRoomRequest, ChatRoom chatRoom) {
//        if (Long.valueOf(authentication.getName()).equals(enterCoffeeChatRoomRequest.memberId())) {
//            chatRoom.activateRoom(enterCoffeeChatRoomRequest.articleTitle());
//        } else {
//            throw new BusinessException(CoffeeChatErrorCode.MENTOR_MISMATCH);
//        }
//    }
//}
