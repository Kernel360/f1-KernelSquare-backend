// package com.kernel360.kernelsquare.global.jwt;
//
// import java.io.IOException;
//
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.util.StringUtils;
// import org.springframework.web.filter.GenericFilterBean;
//
// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.ServletRequest;
// import jakarta.servlet.ServletResponse;
// import jakarta.servlet.http.HttpServletRequest;
// import lombok.RequiredArgsConstructor;
//
// @RequiredArgsConstructor
// public class JwtFilter extends GenericFilterBean {
// 	private final TokenProvider tokenProvider;
// 	public final String AUTHORIZATION_HEADER = "Authorization";
// 	public final String TOKEN_PREFIX = "Bearer";
//
// 	@Override
// 	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
// 		IOException,
// 		ServletException {
// 		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
// 		String jwt = resolveToken(httpServletRequest);
//
// 		if (StringUtils.hasText(jwt) && tokenProvider.validateAccessToken(jwt)) {
// 			Authentication authentication = tokenProvider.getAuthentication(jwt);
// 			// Security Context에 인증정보 저장
// 			SecurityContextHolder.getContext().setAuthentication(authentication);
// 		}
//
// 		chain.doFilter(request, response);
// 	}
//
// 	/** request header의 토큰 정보 추출 메서드 **/
// 	private String resolveToken(HttpServletRequest request) {
// 		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
// 		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
// 			return bearerToken.substring(TOKEN_PREFIX.length()).trim();
// 		}
// 		return null;
// 	}
// }
