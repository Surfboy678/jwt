package com.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private JwtUtilities jwtUtilities;

    @GetMapping("/hello")
    public String hello(){
        return "Hello authenticated user";
    }

    @PostMapping(value = "/auth")
    public ResponseEntity<AuthenticationResponse> createToken(@RequestBody UserDto userDto){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(),userDto.getPassword()));
        UserDetails retrievedUser = userDetailsServiceImpl.loadUserByUsername(userDto.getUsername());
        //TODO: stworzyc maszynke do generowania jwt;
        String token = jwtUtilities.generateToken(retrievedUser);
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }
}
