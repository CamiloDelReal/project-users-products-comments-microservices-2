package org.xapps.service.commentsservice.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.xapps.service.commentsservice.utils.ConfigProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private Logger logger = LoggerFactory.getLogger(WebSecurity.class);
    private ConfigProvider configProvider;

    @Autowired
    public WebSecurity(ConfigProvider configProvider) {
        this.configProvider = configProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors().configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration corsConfiguration = new CorsConfiguration();
                corsConfiguration.setAllowedOrigins(List.of(configProvider.getOriginsUrl()));
                corsConfiguration.setAllowedMethods(Arrays.asList(configProvider.getOriginsMethods()));
                corsConfiguration.setAllowedHeaders(Arrays.asList(configProvider.getOriginsHeaders()));
                corsConfiguration.setMaxAge(configProvider.getOriginsMaxAge());
                return corsConfiguration;
            }
        });
        http.headers().frameOptions().disable();
        http.authorizeRequests()
                .antMatchers("/comments/**").permitAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(provideAuthorizationFilter());
    }

    private AuthorizationFilter provideAuthorizationFilter() throws Exception {
        return new AuthorizationFilter(authenticationManager(), configProvider);
    }
}
