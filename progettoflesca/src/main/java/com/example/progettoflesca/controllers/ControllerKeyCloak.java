package com.example.progettoflesca.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//@EnableWebSecurity

/*public class ControllerKeyCloak {
    @GetMapping
    //@PreAuthorize("hasRole('client_user')")
    public ResponseEntity hello(){
        System.out.println("ciaoooo");
        return new ResponseEntity("Hello from Spring boot & Keycloak", HttpStatus.OK);
    }
    @GetMapping("/hello-2")
    //@PreAuthorize("hasRole('client_admin')")
    public String hello2(){
        return "Hello from Spring boot & Keycloak - ADMIN";
    }
}*/

@RestController
@RequestMapping("/api/v1/demo")
public class ControllerKeyCloak {

    @GetMapping()
    //@PreAuthorize("hasRole('client_user')")
    public ResponseEntity hello(){
        System.out.println("ciaoooo");
        return new ResponseEntity("Hello from Spring boot & Keycloak", HttpStatus.OK);
    }
    @GetMapping(path = "/hello-2")
    //@PreAuthorize("hasRole('client_admin')")
    public String hello2() {
        return "Hello from Spring boot & Keycloak - ADMIN";
    }
}
