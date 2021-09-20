package org.xapps.service.usersservice.dtos;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserRequest {
    @NotNull(message = "First name cannot be empty")
    @Size(min = 3, message = "First name must not be less than 3 characters")
    private String firstName;

    private String lastName;

    @Email(message = "Not a valid email format")
    @NotNull(message = "Email cannot be empty")
    private String email;

    @NotNull(message = "Password cannot be empty")
    @Size(min = 3, message = "Password cannot be less than 3 characters")
    private String password;

    public UserRequest() {
    }

    public UserRequest(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
}
