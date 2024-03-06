package com.kernelsquare.memberapi.domain.scheduler.manager;

import com.kernelsquare.core.type.MessageType;
import com.kernelsquare.domainmysql.domain.answer.entity.Answer;
import com.kernelsquare.domainmysql.domain.answer.repository.AnswerReader;
import com.kernelsquare.domainmysql.domain.coffeechat.entity.ChatRoom;
import com.kernelsquare.domainmysql.domain.coffeechat.repository.CoffeeChatReader;
import com.kernelsquare.domainmysql.domain.question.entity.Question;
import com.kernelsquare.domainmysql.domain.question.repository.QuestionReader;
import com.kernelsquare.domainmysql.domain.rank.entity.Rank;
import com.kernelsquare.domainmysql.domain.rank.repository.RankReader;
import com.kernelsquare.memberapi.domain.alert.dto.AlertDto;
import com.kernelsquare.memberapi.domain.alert.mapper.AlertDtoMapper;
import com.kernelsquare.memberapi.domain.alert.service.AlertService;
import com.kernelsquare.memberapi.domain.coffeechat.dto.ChatMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SchedulerManagerImpl implements ScheculerManager {
    private final SimpMessageSendingOperations sendingOperations;
    private final CoffeeChatReader coffeeChatReader;
    private final QuestionReader questionReader;
    private final AnswerReader answerReader;
    private final RankReader rankReader;
    private final AlertService alertService;
    private final AlertDtoMapper alertDtoMapper;

    @Override
    @Transactional
    @Scheduled(cron = "0 0/30 * * * *")
    public void disableChatRoom() {
        List<ChatRoom> chatRooms = coffeeChatReader.findActiveChatRooms(true);
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

    @Override
    @Transactional
    @Scheduled(cron = "0 0 17 * * *")
    public void rankAnswer() {
        List<Question> questions = questionReader.findClosedQuestions(false);

        questions.stream()
            .filter(question -> question.getCreatedDate().toLocalDate().isBefore(LocalDate.now().minusDays(7)))
            .forEach(question -> {
                question.closeQuestion();
                List<Answer> answers = answerReader.findAnswersTop3(question.getId());

                Long rankName = 1L;

                for (Answer answer : answers) {
                    Rank rank = rankReader.findRank(rankName);
                    answer.updateRank(rank);

                    alertService.storeAndSendAlert(alertDtoMapper.of(AlertDto.RankAnswerAlert.of(question, answer, rank)));

                    rankName += 1L;
                }
            });
    }
}
