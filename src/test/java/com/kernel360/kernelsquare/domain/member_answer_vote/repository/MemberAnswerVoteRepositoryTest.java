package com.kernel360.kernelsquare.domain.member_answer_vote.repository;

import com.kernel360.kernelsquare.domain.answer.entity.Answer;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.member_answer_vote.entity.MemberAnswerVote;
import com.kernel360.kernelsquare.domain.question.entity.Question;
import com.kernel360.kernelsquare.global.common_response.error.code.MemberAnswerVoteErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.exception.BusinessException;
import com.kernel360.kernelsquare.global.config.JpaAuditingConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("투표 레포지토리 통합 테스트")
@DataJpaTest
@Import(JpaAuditingConfig.class)
public class MemberAnswerVoteRepositoryTest {
    @Autowired
    private MemberAnswerVoteRepository memberAnswerVoteRepository;

    private Member member;
    private Answer answer;
    private Question question;
    private MemberAnswerVote memberAnswerVote;

    @BeforeEach
    void setUp() {
        member = createTestMember();
        question = createTestQuestion();
        answer = createTestAnswer(member, question);
        memberAnswerVote = createTestVote(member, answer);
    }

    @Test
    @DisplayName("투표 탐색 정상 작동 테스트")
    void findByMemberIdAndAnswerId() {
        //given
        MemberAnswerVote savedMemberAnswerVote = memberAnswerVoteRepository.save(memberAnswerVote);

        //when
        MemberAnswerVote newMemberAnswerVote = memberAnswerVoteRepository.findByMemberIdAndAnswerId(
                savedMemberAnswerVote.getMember().getId(),
                savedMemberAnswerVote.getAnswer().getId()
        ).orElseThrow(() -> new BusinessException(MemberAnswerVoteErrorCode.MEMBER_ANSWER_VOTE_NOT_FOUND));

        //then
        assertThat(newMemberAnswerVote).isEqualTo(memberAnswerVote);
    }

    private Question createTestQuestion() {
        return Question
                .builder()
                .title("Test Question")
                .content("Test Content")
                .imageUrl("S3:TestImage")
                .closedStatus(false)
                .build();
    }

    private Member createTestMember() {
        return Member
                .builder()
                .id(1L)
                .nickname("hongjugwang")
                .email("jugwang@naver.com")
                .password("hashedPassword")
                .experience(10000L)
                .introduction("hi, i'm hongjugwang.")
                .imageUrl("s3:qwe12fasdawczx")
                .build();
    }

    private Answer createTestAnswer(Member member, Question question) {
        return Answer
                .builder()
                .id(1L)
                .content("Test Answer")
                .voteCount(1L)
                .imageUrl("S3:TestAnswer")
                .member(member)
                .question(question)
                .build();
    }

    private MemberAnswerVote createTestVote(Member member, Answer answer) {
        return MemberAnswerVote
                .builder()
                .member(member)
                .answer(answer)
                .status(1)
                .build();
    }
}
