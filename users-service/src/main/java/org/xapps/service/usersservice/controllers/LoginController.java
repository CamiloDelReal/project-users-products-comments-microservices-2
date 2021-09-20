package org.xapps.service.usersservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xapps.service.usersservice.dtos.LoginRequest;
import org.xapps.service.usersservice.dtos.LoginResponse;
import org.xapps.service.usersservice.services.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "security")
public class LoginController {

    private UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        ResponseEntity<LoginResponse> response = null;
        try {
            LoginResponse login = userService.authenticate(loginRequest);
            if (login != null) {
                response = new ResponseEntity<>(login, HttpStatus.OK);
            } else {
                response = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
}
