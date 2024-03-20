package com.kernelsquare.alertapi.config;

import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;

import static com.kernelsquare.core.constants.RestDocsConstants.*;

@AutoConfigureRestDocs(uriScheme = URI_SCHEME, uriHost = URI_HOST, uriPort = URI_PORT_ALERT)
public abstract class RestDocsControllerTest {
}
