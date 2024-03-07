package com.kernelsquare.memberapi.domain.chatgpt.service;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kernelsquare.core.common_response.error.code.KernelSquareBotErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.core.constants.KernelSquareBotConstants;
import com.kernelsquare.domainmysql.domain.answer.entity.Answer;
import com.kernelsquare.domainmysql.domain.answer.repository.AnswerRepository;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.repository.MemberRepository;
import com.kernelsquare.domainmysql.domain.question.entity.Question;
import com.kernelsquare.domainmysql.domain.question.repository.QuestionRepository;
import com.kernelsquare.memberapi.domain.chatgpt.client.ChatGptClient;
import com.kernelsquare.memberapi.domain.chatgpt.dto.ChatGptDto;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@RequiredArgsConstructor
public class ChatGptServiceImpl implements ChatGptService {

	private final ChatGptClient chatGptClient;
	private final AnswerRepository answerRepository;
	private final QuestionRepository questionRepository;
	private final MemberRepository memberRepository;

	@Value("${chatgpt.rest-api-key}")
	private String apiKey;

	@Value("${chatgpt.chat-api-url}")
	private String chatApiUrl;

	@SneakyThrows({URISyntaxException.class})
	@Transactional
	@Override
	public void createChatGptAnswer(Long questionId) {
		if (answerRepository.existsByMemberNicknameAndQuestionId(KernelSquareBotConstants.KERNEL_SQUARE_AI_INTERN, questionId)) {
			throw new BusinessException(KernelSquareBotErrorCode.ANSWER_ALREADY_EXIST);
		}

		Question question = questionRepository.findById(questionId)
			.orElseThrow(() -> new BusinessException(KernelSquareBotErrorCode.QUESTION_NOT_FOUND));
		Member member = memberRepository.findByNickname(KernelSquareBotConstants.KERNEL_SQUARE_AI_INTERN)
			.orElseThrow(() -> new BusinessException(KernelSquareBotErrorCode.KERNEL_SQUARE_BOT_NOT_FOUND));

		ChatGptDto.CreateAnswerRequest answerRequest = ChatGptDto.CreateAnswerRequest.of(question);

		ChatGptDto.CreateAnswerResponse chatGptResponse = chatGptClient.getChatGptResponse(new URI(chatApiUrl),
			"Bearer " + apiKey, answerRequest);

		String response = chatGptResponse.getText()
			.orElseThrow(() -> new BusinessException(KernelSquareBotErrorCode.EMPTY_ANSWER_RESPONSE));

		Answer answer = Answer.builder()
			.question(question)
			.imageUrl(null)
			.voteCount(0L)
			.content(response)
			.member(member)
			.build();

		answerRepository.save(answer);
	}
}
