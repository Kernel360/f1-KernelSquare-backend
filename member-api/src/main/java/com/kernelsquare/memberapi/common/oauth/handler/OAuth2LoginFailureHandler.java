package com.kernelsquare.memberapi.common.oauth.handler;

//@Component
//@RequiredArgsConstructor
//public class OAuth2LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
//
//    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
//
//    @Override
//    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//        String targetUrl = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
//                .map(Cookie::getValue)
//                .orElse(("/"));
//
//        exception.printStackTrace();
//
//        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
//                .queryParam("error", exception.getLocalizedMessage())
//                .build().toUriString();
//
//        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
//
//        getRedirectStrategy().sendRedirect(request, response, targetUrl);
//    }
//}

