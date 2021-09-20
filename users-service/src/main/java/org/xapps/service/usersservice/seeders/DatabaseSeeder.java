package org.xapps.service.usersservice.seeders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.xapps.service.usersservice.entities.Role;
import org.xapps.service.usersservice.entities.User;
import org.xapps.service.usersservice.repositories.RoleRepository;
import org.xapps.service.usersservice.repositories.UserRepository;

import java.util.List;

@Component
public class DatabaseSeeder {

    private BCryptPasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public DatabaseSeeder(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedRoles();
        seedUsers();
    }

    private void seedRoles() {
        if(roleRepository.count() == 0) {
            Role admin = new Role("admin");
            Role user = new Role("user");
            roleRepository.save(admin);
            roleRepository.save(user);
        }
    }

    private void seedUsers() {
        if(userRepository.count() == 0) {
            Role admin = roleRepository.findByName("admin").orElse(null);
            if(admin != null) {
                User user = new User("Admin", "Admin", "admin@mail.com", passwordEncoder.encode("admin"));
                user.setRoles(List.of(admin));
                userRepository.save(user);
            }
        }
    }

}
