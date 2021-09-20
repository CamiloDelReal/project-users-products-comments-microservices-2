package org.xapps.service.usersservice.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class LoginResponse {
    private String email;
    private String token;
    private String tokenType;
    private Set<String> roles;

    public LoginResponse() {
    }

    public LoginResponse(String email, String token, String tokenType, Set<String> roles) {
        this.email = email;
        this.token = token;
        this.tokenType = tokenType;
        this.roles = roles;
    }
}
