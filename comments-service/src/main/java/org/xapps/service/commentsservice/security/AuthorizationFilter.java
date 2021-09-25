package org.xapps.service.commentsservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.xapps.service.commentsservice.dtos.UserAuthenticated;
import org.xapps.service.commentsservice.utils.ConfigProvider;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    private ConfigProvider configProvider;

    public AuthorizationFilter(AuthenticationManager authenticationManager, ConfigProvider configProvider) {
        super(authenticationManager);
        this.configProvider = configProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authorizationHeader != null && authorizationHeader.startsWith(configProvider.getTokenType())) {
            UsernamePasswordAuthenticationToken auth = getAuthentication(request);
            if(auth != null) {
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        UsernamePasswordAuthenticationToken auth = null;
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = authorizationHeader.replace(configProvider.getTokenType(), "");

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(configProvider.getTokenKey())
                    .parseClaimsJws(token)
                    .getBody();
            String[] subjectData = claims.getSubject().split(configProvider.getAuthoritiesSeparator());
            if(subjectData.length == 2 && subjectData[0] != null && subjectData[1] != null) {
                UserAuthenticated user = new UserAuthenticated(Long.parseLong(subjectData[0]), subjectData[1]);
                String rolesClaim = claims.get(configProvider.getHeaderAuthorities(), String.class);
                user.setRoles(Stream.of(rolesClaim.split(configProvider.getAuthoritiesSeparator())).collect(Collectors.toList()));
                logger.debug("User: " + user);
                Collection<GrantedAuthority> authorities = user.getRoles().stream()
                        .map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                auth = new UsernamePasswordAuthenticationToken(user, null, authorities);
            }
        } catch (Exception ex) {
            logger.info("Token invalid or has expired");
        }

        return auth;
    }
}
