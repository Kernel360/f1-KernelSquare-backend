package com.kernel360.kernelsquare.global.filter;

import java.io.IOException;
import java.util.logging.Logger;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

public class JWTCheckFilter implements Filter {

	private final Logger LOG =
		Logger.getLogger(JWTCheckFilter.class.getName());

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
		IOException,
		ServletException {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (null != authentication) {
			//todo : 어떤 내용 담을지..
			LOG.info("Authentication success");
		}
		chain.doFilter(request, response);
	}
}
