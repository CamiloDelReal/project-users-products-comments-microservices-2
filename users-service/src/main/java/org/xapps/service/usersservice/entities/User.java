package org.xapps.service.usersservice.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "protected_password")
    private String protectedPassword;

    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private List<Role> roles;

    public User() {
    }

    public User(String firstName, String lastName, String email, String protectedPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.protectedPassword = protectedPassword;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", encryptedPassword='" + protectedPassword + '\'' +
                ", roles=" + roles.stream().map(Role::getName).collect(Collectors.joining(", ")) +
                '}';
    }
}
