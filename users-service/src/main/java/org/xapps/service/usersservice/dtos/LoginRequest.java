package org.xapps.service.usersservice.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class LoginRequest {
    @NotNull(message = "Email cannot be empty")
    @Email(message = "Email has invalid format")
    private String email;

    @NotNull(message = "Password cannot be empty")
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Email = " + email + "\nPassword = " + password;
    }
}
