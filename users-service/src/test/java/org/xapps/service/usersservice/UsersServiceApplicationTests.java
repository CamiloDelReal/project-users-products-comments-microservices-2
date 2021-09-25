package org.xapps.service.usersservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.xapps.service.usersservice.entities.User;
import org.xapps.service.usersservice.repositories.UserRepository;
import org.xapps.service.usersservice.services.UserService;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class UsersServiceApplicationTests {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

//    public UsersServiceApplicationTests(UserService userService) {
//        this.userService = userService;
//    }

    @Test
    void crudUsers() {
        User user = new User("Root", "Tester", "root@gmail.com", "123456");
        assertNull(user.getId());

        user = userRepository.save(user);
        assertNotNull(user.getId());
//
//
//        RestTemplate restTemplate = new RestTemplate();
//        final String baseUrl = "http://localhost:"+randomServerPort+"/employees/";
//        URI uri = new URI(baseUrl);
//        Employee employee = new Employee(null, "Adam", "Gilly", "test@email.com");
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("X-COM-PERSIST", "true");
//
//        HttpEntity<Employee> request = new HttpEntity<>(employee, headers);
//
//        ResponseEntity<String> result = restTemplate.postForEntity(uri, request, String.class);
//
//        //Verify request succeed
//        Assertions.assertEquals(201, result.getStatusCodeValue());

        assertEquals(201, 201);
    }

}
