package org.xapps.service.productsservice.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserAuthenticated {
    private Long id;
    private String email;
    private List<String> roles;

    public UserAuthenticated() {
    }

    public UserAuthenticated(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public UserAuthenticated(Long id, String email, List<String> roles) {
        this.id = id;
        this.email = email;
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "UserAuthenticated {" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", roles=" + String.join(", ", roles) +
                '}';
    }
}
