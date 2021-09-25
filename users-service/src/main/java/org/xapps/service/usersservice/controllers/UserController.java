package org.xapps.service.usersservice.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.xapps.service.usersservice.dtos.UserRequest;
import org.xapps.service.usersservice.dtos.UserResponse;
import org.xapps.service.usersservice.services.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated() and hasAuthority('admin')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        logger.info("getAllUsers");
        ResponseEntity<List<UserResponse>> response = null;
        try {
            List<UserResponse> users = userService.getAllUsers();
            response = new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception ex) {
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated() and hasAuthority('admin') or isAuthenticated() and principal.id == #id")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") Long id) {
        logger.info("getUserById id = " + id);
        ResponseEntity<UserResponse> response = null;
        try {
            UserResponse user = userService.getUserById(id);
            if (user != null) {
                response = new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                response = new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
            }
        } catch (Exception ex) {
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        ResponseEntity<UserResponse> response = null;
        try {
            UserResponse user = userService.createUser(userRequest);
            response = new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception ex) {
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated() and hasAuthority('admin') or isAuthenticated() and principal.id == #id")
    public ResponseEntity<UserResponse> editUser(@PathVariable("id") Long id, @Valid @RequestBody UserRequest userRequest) {
        ResponseEntity<UserResponse> response = null;
        try {
            UserResponse user = userService.editUser(id, userRequest);
            if (user != null) {
                response = new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("isAuthenticated() and hasAuthority('admin') or isAuthenticated() and principal.id == #id")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        ResponseEntity<Void> response = null;
        try {
            boolean success = userService.deleteUser(id);
            if(success) {
                response = new ResponseEntity<>(HttpStatus.OK);
            } else {
                response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch(Exception ex) {
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
}
