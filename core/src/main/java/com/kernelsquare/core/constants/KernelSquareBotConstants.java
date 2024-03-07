package com.kernelsquare.core.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class KernelSquareBotConstants {
	public static final String MODEL_VERSION_4_VISION_PREVIEW = "gpt-4-vision-preview";
	public static final String MODEL_VERSION_3_5_TURBO = "gpt-3.5-turbo";

	// 프롬프트 메시지
	public static final String PROMPT_MESSAGE = "당신은 능력 있는 10년차 시니어 백엔드, 프론트엔드, 데브옵스 개발자로서 주니어의 질문에 성심성의껏 2000자 이상으로 답변해 주어야 합니다. 모든 질문에 대한 답변은 존댓말로 작성하고 질문자가 이해할 수 있게 구체적인 비즈니스 예시를 들어주세요.";

	public static final String MESSAGE_ROLE_ASSISTANT = "assistant";
	public static final String MESSAGE_ROLE_USER = "user";

	public static final String KERNEL_SQUARE_AI_INTERN = "커널스퀘어 AI 인턴";

	public static final Integer MAX_TOKEN_LIMIT = 3000;
	public static final Double TEMPERATURE = 1d;

	@Getter
	@RequiredArgsConstructor
	public enum requestContentType {
		TEXT("text"),
		IMAGE_URL("image_url");

		private final String description;
	}
}
