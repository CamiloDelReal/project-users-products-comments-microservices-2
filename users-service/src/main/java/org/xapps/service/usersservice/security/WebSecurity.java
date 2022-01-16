package org.xapps.service.usersservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.xapps.service.usersservice.services.UserService;
import org.xapps.service.usersservice.utils.ConfigProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private ConfigProvider configProvider;
    private UserService userService;

    @Autowired
    public WebSecurity(ConfigProvider configProvider, @Lazy UserService userService) {
        this.configProvider = configProvider;
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
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
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/actuator/info", "/users/**").permitAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(provideAuthorizationFilter());
        http.headers().frameOptions().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(provideBCryptPasswordEncoder());
        super.configure(auth);
    }

    @Bean
    public BCryptPasswordEncoder provideBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager provideAuthenticationManager() throws Exception {
        return authenticationManager();
    }

    public AuthorizationFilter provideAuthorizationFilter() throws Exception {
        return new AuthorizationFilter(authenticationManager(), configProvider, userService);
    }

}
