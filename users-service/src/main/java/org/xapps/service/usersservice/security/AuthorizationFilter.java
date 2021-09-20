package org.xapps.service.usersservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.xapps.service.usersservice.entities.User;
import org.xapps.service.usersservice.services.UserService;
import org.xapps.service.usersservice.utils.ConfigProvider;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AuthorizationFilter  extends BasicAuthenticationFilter {

    private Logger logger = LoggerFactory.getLogger(AuthorizationFilter.class);
    private ConfigProvider configProvider;
    private UserService userService;

    public AuthorizationFilter(AuthenticationManager authenticationManager, ConfigProvider configProvider, UserService userService) {
        super(authenticationManager);
        this.configProvider = configProvider;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        logger.info("Authorization header " + authorizationHeader);
        if(authorizationHeader != null && authorizationHeader.startsWith(configProvider.getTokenType())) {
            UsernamePasswordAuthenticationToken auth = getAuthentication(request);
            if(auth != null) {
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authentication = null;
        String authenticationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = authenticationHeader.replace(configProvider.getTokenType(), "");

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(configProvider.getTokenKey())
                    .parseClaimsJws(token)
                    .getBody();
            logger.info("Subject " + claims.getSubject());
            String[] subjectData = claims.getSubject().split(configProvider.getAuthoritiesSeparator());
            if(subjectData.length == 2 && subjectData[0] != null && subjectData[1] != null) {
                logger.info("Email subject " + subjectData[1]);
                User user = userService.getUserByEmail(subjectData[1]);
                logger.info(user.toString());
                if(user != null) {
                    List<GrantedAuthority> authorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
                    authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
                }
            }
        } catch (Exception ex) {
            logger.info("Token invalid or has expired");
        }

        return authentication;
    }
}
