package com.kernelsquare.memberapi.domain.chatgpt.dto;

import static com.kernelsquare.core.constants.KernelSquareBotConstants.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kernelsquare.core.util.ImageUtils;
import com.kernelsquare.domainmysql.domain.question.entity.Question;

import lombok.Builder;

public class ChatGptDto {

	@Builder
	public record CreateAnswerRequest(
		String model,
		Double temperature,
		Integer max_tokens,
		List<Message> messages
	) {
		public static CreateAnswerRequest of(Question question) {
			if (Objects.isNull(question.getImageUrl())) {
				return CreateAnswerRequest.builder()
					.model(MODEL_VERSION_3_5_TURBO)
					.messages(List.of(
							Message.builder()
								.role(MESSAGE_ROLE_ASSISTANT)
								.content(PROMPT_MESSAGE)
								.build(),
							Message.builder()
								.role(MESSAGE_ROLE_USER)
								.content(question.getContent())
								.build()
						)
					)
					.temperature(TEMPERATURE)
					.max_tokens(MAX_TOKEN_LIMIT)
					.build();
			}
			return CreateAnswerRequest.builder()
				.model(MODEL_VERSION_4_VISION_PREVIEW)
				.messages(List.of(
					Message.builder()
						.role(MESSAGE_ROLE_ASSISTANT)
						.content(PROMPT_MESSAGE)
						.build(),
					Message.<List<Content>>builder()
						.role(MESSAGE_ROLE_USER)
						.content(List.of(
							Content.builder()
								.type(requestContentType.TEXT.getDescription())
								.text(question.getContent())
								.imageUrl(null)
								.build(),
							Content.builder()
								.type(requestContentType.IMAGE_URL.getDescription())
								.text(null)
								.imageUrl(ImageUrl.builder()
									.url(ImageUtils.makeImageUrl(question.getImageUrl()))
									.build())
								.build()
						))
						.build()
				))
				.temperature(TEMPERATURE)
				.max_tokens(MAX_TOKEN_LIMIT)
				.build();
		}

	}

	public record CreateAnswerResponse(List<Choice<String>> choices) {

		public Optional<String> getText() {
			if (Objects.isNull(choices) || choices.isEmpty()) {
				return Optional.empty();
			}
			return Optional.of(choices.get(0).message.content);
		}

		record Choice<T>(Message<T> message) {
		}
	}

	@Builder
	private record Message<T>(
		T content,
		String role
	) {
	}

	@Builder
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private record Content(
		String type,
		String text,
		ImageUrl imageUrl
	) {
	}

	@Builder
	private record ImageUrl(
		String url
	) {
	}
}
