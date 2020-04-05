package com.sony.engineering.portalcadastro.controller;

import com.sony.engineering.portalcadastro.auth.*;
import com.sony.engineering.portalcadastro.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private User authUser;

    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {

        try {
            authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

            final JwtUserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

            final String token = jwtTokenUtil.generateToken(userDetails);

            return ResponseEntity.ok(new JwtResponse(token));
        } catch (Exception e) {
            HashMap<String, Object> back = new HashMap<>();
            back.put("error", e.toString());
            return new ResponseEntity<>(back, HttpStatus.UNAUTHORIZED);
        }

    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> saveUser(@RequestBody User user) throws DataIntegrityViolationException {

        try {
            if (authUser.getProfile() != null && authUser.getProfile().equals("admin")) {
                User newUser = userDetailsService.save(user);
                newUser.setPassword("");
                return ResponseEntity.ok(newUser);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

        } catch (DataIntegrityViolationException e) {
            HashMap<String, Object> back = new HashMap<>();
            back.put("error", e.getCause().getCause().getMessage());
            return new ResponseEntity<>(back, HttpStatus.BAD_REQUEST);
        }
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
