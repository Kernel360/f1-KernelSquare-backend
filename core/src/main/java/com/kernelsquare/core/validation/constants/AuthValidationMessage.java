package com.kernelsquare.core.validation.constants;

public class AuthValidationMessage {
	// Not Blank
	public static final String EMAIL_NOT_BLANK = "이메일을 입력해 주세요.";
	public static final String NICKNAME_NOT_BLANK = "닉네임을 입력해 주세요.";
	public static final String PASSWORD_NOT_BLANK = "비밀번호를 입력해 주세요.";

	// Size
	public static final String EMAIL_SIZE = "이메일 길이를 확인해 주세요.";
	public static final String NICKNAME_SIZE = "닉네임 길이를 확인해 주세요.";
	public static final String PASSWORD_SIZE = "비밀번호 길이를 확인해 주세요.";

	// Pattern
	public static final String EMAIL_PATTERN = "이메일에 한글은 허용되지 않습니다.";
	public static final String NICKNAME_PATTERN = "완전한 한글 조합 또는 영문 대소문자만 입력하세요.";
	public static final String PASSWORD_PATTERN = "영문 대소문자, 특수문자, 숫자를 각각 1자 이상 포함하세요.";

	// Email
	public static final String EMAIL = "올바른 이메일을 입력해 주세요";
}
