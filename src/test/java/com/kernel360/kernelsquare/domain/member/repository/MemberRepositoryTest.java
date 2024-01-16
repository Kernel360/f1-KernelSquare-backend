package com.kernel360.kernelsquare.domain.member.repository;

import com.kernel360.kernelsquare.domain.auth.dto.SignUpRequest;
import com.kernel360.kernelsquare.domain.level.entity.Level;
import com.kernel360.kernelsquare.domain.level.repository.LevelRepository;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.global.common_response.error.code.LevelErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.code.MemberErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.exception.BusinessException;
import com.kernel360.kernelsquare.global.config.JpaAuditingConfig;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("회원 레포지토리 통합 테스트")
@Transactional
@DataJpaTest
public class MemberRepositoryTest {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private LevelRepository levelRepository;
    private Member member;
    private Level level_01;
    private Level level_02;

    @BeforeEach
    void setUp() {
        level_01 = createTestLevel(1L, 1L, 200L);
        level_01.setCreatedDate();
        levelRepository.save(level_01);

        level_02 = createTestLevel(2L, 2L, 500L);
        level_02.setCreatedDate();
        levelRepository.save(level_02);

        member = createTestMember(level_01);
        member.setCreatedDate();
        memberRepository.save(member);
    }

    @Test
    @DisplayName("회원 경험치 변경 테스트")
    void updateExperienceByValue() {
        //given
        Long experienceValue = 100L;
        Long memberId = member.getId();

        //when
        memberRepository.updateExperienceByValue(memberId, experienceValue);
        entityManager.clear();

        Member newUpdatedMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));

        //then
        assertThat(newUpdatedMember.getExperience()).isEqualTo(experienceValue);
    }

    @Test
    @DisplayName("회원 경험치 누적 테스트")
    void updateExperienceByDifference() {
        //given
        Long experienceDifference = 100L;
        Long memberId = member.getId();
        Long memberExperience = member.getExperience();

        //when
        memberRepository.updateExperienceByDifference(memberId, experienceDifference);
        entityManager.clear();

        Member newUpdatedMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));

        //then
        assertThat(newUpdatedMember.getExperience()).isEqualTo(memberExperience + experienceDifference);
    }


    private Member createTestMember(Level level) {
        return Member
                .builder()
                .id(1L)
                .nickname("hongjugwang")
                .email("jugwang@naver.com")
                .password("hashedPassword")
                .level(level)
                .experience(10000L)
                .introduction("hi, i'm hongjugwang.")
                .imageUrl("s3:qwe12fasdawczx")
                .build();
    }

    private Level createTestLevel(Long levelId, Long levelName, Long levelUpperLimit) {
        return Level
                .builder()
                .id(levelId)
                .name(levelName)
                .imageUrl("S1:testLevel0" + levelId)
                .levelUpperLimit(levelUpperLimit)
                .build();
    }
}



//
//
//public class MemberAnswerVoteRepositoryTest {
//    @Autowired
//    private MemberAnswerVoteRepository memberAnswerVoteRepository;
//
//    private Member member;
//    private Answer answer;
//    private Question question;
//    private MemberAnswerVote memberAnswerVote;
//
//    @BeforeEach
//    void setUp() {
//        member = createTestMember();
//        question = createTestQuestion();
//        answer = createTestAnswer(member, question);
//        memberAnswerVote = createTestVote(member, answer);
//    }
//
//    @Test
//    @DisplayName("투표 탐색 정상 작동 테스트")
//    void findByMemberIdAndAnswerId() {
//        //given
//        MemberAnswerVote savedMemberAnswerVote = memberAnswerVoteRepository.save(memberAnswerVote);
//
//        //when
//        MemberAnswerVote newMemberAnswerVote = memberAnswerVoteRepository.findByMemberIdAndAnswerId(
//                savedMemberAnswerVote.getMember().getId(),
//                savedMemberAnswerVote.getAnswer().getId()
//        ).orElseThrow(() -> new BusinessException(MemberAnswerVoteErrorCode.MEMBER_ANSWER_VOTE_NOT_FOUND));
//
//        //then
//        assertThat(newMemberAnswerVote).isEqualTo(memberAnswerVote);
//    }
//
//    private Question createTestQuestion() {
//        return Question
//                .builder()
//                .title("Test Question")
//                .content("Test Content")
//                .imageUrl("S3:TestImage")
//                .closedStatus(false)
//                .build();
//    }
//
//    private Member createTestMember() {
//        return Member
//                .builder()
//                .id(1L)
//                .nickname("hongjugwang")
//                .email("jugwang@naver.com")
//                .password("hashedPassword")
//                .experience(10000L)
//                .introduction("hi, i'm hongjugwang.")
//                .imageUrl("s3:qwe12fasdawczx")
//                .build();
//    }
//
//    private Answer createTestAnswer(Member member, Question question) {
//        return Answer
//                .builder()
//                .id(1L)
//                .content("Test Answer")
//                .voteCount(1L)
//                .imageUrl("S3:TestAnswer")
//                .member(member)
//                .question(question)
//                .build();
//    }
//
//    private MemberAnswerVote createTestVote(Member member, Answer answer) {
//        return MemberAnswerVote
//                .builder()
//                .member(member)
//                .answer(answer)
//                .status(1)
//                .build();
//    }
//}
