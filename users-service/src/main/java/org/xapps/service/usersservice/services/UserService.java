package org.xapps.service.usersservice.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.xapps.service.usersservice.dtos.LoginRequest;
import org.xapps.service.usersservice.dtos.LoginResponse;
import org.xapps.service.usersservice.dtos.UserRequest;
import org.xapps.service.usersservice.dtos.UserResponse;
import org.xapps.service.usersservice.entities.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);

    User getUserByEmail(String email);

    UserResponse createUser(UserRequest userRequest);

    UserResponse editUser(Long id, UserRequest userRequest);

    boolean deleteUser(Long id);

    LoginResponse authenticate(LoginRequest loginRequest);
}
