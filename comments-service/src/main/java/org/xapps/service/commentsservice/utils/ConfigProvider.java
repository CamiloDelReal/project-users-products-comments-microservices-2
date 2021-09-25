package org.xapps.service.commentsservice.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ConfigProvider {
    @Value("${security.claims.header-authorities}")
    private String headerAuthorities;

    @Value("${security.claims.separator}")
    private String authoritiesSeparator;

    @Value("${security.token.key}")
    private String tokenKey;

    @Value("${security.token.type}")
    private String tokenType;

    @Value("${security.token.header-name}")
    private String tokenHeader;

    @Value("${security.token.validity}")
    private Long validity;
}
