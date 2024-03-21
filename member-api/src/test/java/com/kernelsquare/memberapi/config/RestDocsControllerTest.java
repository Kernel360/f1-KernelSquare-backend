package com.kernelsquare.memberapi.config;

import static com.kernelsquare.core.constants.RestDocsConstants.*;

import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;

@AutoConfigureRestDocs(uriScheme = URI_SCHEME, uriHost = URI_HOST, uriPort = URI_PORT_MEMBER)
public abstract class RestDocsControllerTest {
}
