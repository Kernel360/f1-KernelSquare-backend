package com.kernelsquare.memberapi.domain.scheduler;

import com.kernelsquare.core.common_response.error.code.RankErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.core.type.MessageType;
import com.kernelsquare.domainmysql.domain.answer.entity.Answer;
import com.kernelsquare.domainmysql.domain.answer.repository.AnswerRepository;
import com.kernelsquare.domainmysql.domain.coffeechat.entity.ChatRoom;
import com.kernelsquare.domainmysql.domain.coffeechat.repository.CoffeeChatRepository;
import com.kernelsquare.domainmysql.domain.question.entity.Question;
import com.kernelsquare.domainmysql.domain.question.repository.QuestionRepository;
import com.kernelsquare.domainmysql.domain.rank.entity.Rank;
import com.kernelsquare.domainmysql.domain.rank.repository.RankRepository;
import com.kernelsquare.memberapi.domain.coffeechat.dto.ChatMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SchedulerService {
    private final CoffeeChatRepository coffeeChatRepository;
    private final SimpMessageSendingOperations sendingOperations;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final RankRepository rankRepository;

    @Transactional
    @Scheduled(cron = "0 0/30 * * * *")
    public void disableRoom() {
        List<ChatRoom> chatRooms = coffeeChatRepository.findAllByActive(true);
        chatRooms.forEach(chatRoom -> {
            chatRoom.deactivateRoom();

            ChatMessageResponse message = ChatMessageResponse.builder()
                .type(MessageType.EXPIRE)
                .roomKey(chatRoom.getRoomKey())
                .sender("system")
                .message("채팅방 사용 시간이 만료되었습니다.")
                .sendTime(LocalDateTime.now())
                .build();

            sendingOperations.convertAndSend("/topic/chat/room/" + message.getRoomKey(), message);
        });
    }

    @Transactional
    @Scheduled(cron = "0 0 2 * * *")
    public void rankAnswer() {
        List<Question> questions = questionRepository.findAllByClosedStatus(false);

        questions.stream()
            .filter(question -> question.getCreatedDate().toLocalDate().isBefore(LocalDate.now().minusDays(7)))
            .forEach(question -> {
                question.updateClosedStatus();
                List<Answer> answers = answerRepository.findAnswersByQuestionIdSortedByVoteCount(question.getId());

                Long rankName = 1L;

                for (Answer answer : answers) {
                    Rank rank = rankRepository.findByName(rankName)
                        .orElseThrow(() -> new BusinessException(RankErrorCode.RANK_NOT_FOUND));
                    answer.updateRank(rank);

                    rankName += 1L;
                }
            });
    }
}
