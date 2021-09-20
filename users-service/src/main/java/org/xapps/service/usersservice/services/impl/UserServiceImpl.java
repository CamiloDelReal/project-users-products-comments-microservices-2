package org.xapps.service.usersservice.services.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.xapps.service.usersservice.dtos.LoginRequest;
import org.xapps.service.usersservice.dtos.LoginResponse;
import org.xapps.service.usersservice.dtos.UserRequest;
import org.xapps.service.usersservice.dtos.UserResponse;
import org.xapps.service.usersservice.entities.Role;
import org.xapps.service.usersservice.entities.User;
import org.xapps.service.usersservice.repositories.RoleRepository;
import org.xapps.service.usersservice.repositories.UserRepository;
import org.xapps.service.usersservice.services.UserService;
import org.xapps.service.usersservice.utils.ConfigProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private BCryptPasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private ModelMapper modelMapper;
    private AuthenticationManager authenticationManager;
    private ConfigProvider configProvider;

    @Autowired
    public UserServiceImpl(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository,
                           ModelMapper modelMapper, @Lazy AuthenticationManager authenticationManager, ConfigProvider configProvider) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
        this.configProvider = configProvider;
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponse> response;
        if (!users.isEmpty()) {
            response = users.stream().map(it -> modelMapper.map(it, UserResponse.class)).collect(Collectors.toList());
        } else {
            response = new ArrayList<>();
        }
        return response;
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        UserResponse response = null;
        if (user != null) {
            response = modelMapper.map(user, UserResponse.class);
        }
        return response;
    }

    @Override
    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        return user;
    }

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        User user = modelMapper.map(userRequest, User.class);
        user.setProtectedPassword(passwordEncoder.encode(userRequest.getPassword()));
        Role userRole = roleRepository.findByName("user").orElse(null);
        if (userRole != null) {
            user.setRoles(List.of(userRole));
        }
        userRepository.save(user);
        UserResponse response = modelMapper.map(user, UserResponse.class);
        return response;
    }

    @Override
    public UserResponse editUser(Long id, UserRequest userRequest) {
        User user = userRepository.findById(id).orElse(null);
        UserResponse response = null;
        if (user != null) {
            user.setFirstName(userRequest.getFirstName());
            user.setLastName(userRequest.getLastName());
            user.setEmail(userRequest.getEmail());
            userRepository.save(user);
            response = modelMapper.map(user, UserResponse.class);
        }
        return response;
    }

    @Override
    public boolean deleteUser(Long id) {
        boolean success = false;
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            userRepository.delete(user);
            success = true;
        }
        return false;
    }

    @Override
    public LoginResponse authenticate(LoginRequest loginRequest) {
        logger.info("authenticate login = " + loginRequest);
        LoginResponse response = null;
        UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(credentials);
        } catch (Exception ex) {
            logger.info("Invalid credentials");
        }

        if (authentication != null) {
            User user = userRepository.findByEmail(loginRequest.getEmail()).orElse(null);

            Collection<GrantedAuthority> authorities = null;
            if (user != null) {
                authorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());

                SecurityContextHolder.getContext().setAuthentication(authentication);   // Test without calling it

                String rolesClaims = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(configProvider.getAuthoritiesSeparator()));
                String subject = String.join(configProvider.getAuthoritiesSeparator(), String.valueOf(user.getId()), user.getEmail());
                logger.info("Roles claims = " + rolesClaims);

                Claims claims = Jwts.claims();
                claims.setSubject(subject);
                claims.put(configProvider.getHeaderAuthorities(), rolesClaims);

                long timestamp = System.currentTimeMillis();

                String token = Jwts.builder()
                        .setSubject(user.getEmail())
                        .setClaims(claims)
                        .setIssuer("Users Products Comments Microservices")
                        .setIssuedAt(new Date(timestamp))
                        .setExpiration(new Date(timestamp + configProvider.getValidity()))
                        .signWith(SignatureAlgorithm.HS512, configProvider.getTokenKey())
                        .compact();

                response = new LoginResponse(user.getEmail(), token, configProvider.getTokenType(), AuthorityUtils.authorityListToSet(authorities));
            }
        }

        return response;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException(email);
        } else {
            Collection<GrantedAuthority> authorities = null;
            authorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getProtectedPassword(), true, true, true, true, authorities);
        }
    }
}
