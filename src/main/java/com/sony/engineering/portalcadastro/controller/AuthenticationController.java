package com.sony.engineering.portalcadastro.controller;

import com.sony.engineering.portalcadastro.auth.*;
import com.sony.engineering.portalcadastro.model.JwtUserDetails;
import com.sony.engineering.portalcadastro.service.UserService;
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
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@RestController
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUserDetails authUser;

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
    public ResponseEntity<?> saveUser(@RequestBody JwtUserDetails user) throws DataIntegrityViolationException {

        try {
            if (authUser.getProfile() != null && authUser.getProfile().equals("admin")) {
                return ResponseEntity.ok(userDetailsService.save(user).getUser());
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

        } catch (DataIntegrityViolationException e) {
            HashMap<String, Object> back = new HashMap<>();
            back.put("error", e.getCause().getCause().getMessage());
            return new ResponseEntity<>(back, HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(value = "reset/{id}")
    public ResponseEntity<?> resetPassword(@PathVariable("id") Integer id){
        try {
            if ((authUser.getProfile() != null) &&
                    (authUser.getProfile().equals("admin") || Objects.equals(authUser.getId(), id))) {
                userDetailsService.resetPassword(id);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/usersdetails")
    public ResponseEntity<?> getUsers() {
        try {
            if ((authUser.getProfile() != null) && (authUser.getProfile().equals("admin"))) {

                List<JwtUserDetails> users = userDetailsService.getUsers();

                users.forEach(u -> u.setPassword(""));

                return new ResponseEntity<>(users, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/usersdetails/{id}")
    public ResponseEntity<?> getOneUser(@PathVariable("id") Integer id) {
        try {
            if ((authUser.getProfile() != null) &&
                    (authUser.getProfile().equals("admin") || Objects.equals(authUser.getId(), id))) {

                JwtUserDetails user = userDetailsService.findOneUser(id);
                user.setPassword("");

                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping(value = "/usersdetails")
    private ResponseEntity<?> postUserDetail(@RequestBody JwtUserDetails userDetails) {
        try {
            if ((authUser.getProfile() != null) && (authUser.getProfile().equals("admin"))) {
                userDetailsService.save(userDetails);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(value = "/usersdetails/{id}")
    public ResponseEntity<?> patchUserDetails(@RequestBody JwtUserDetails user, @PathVariable("id") Integer id){
        try {
            if ((authUser.getProfile() != null) &&
                    (authUser.getProfile().equals("admin") || Objects.equals(authUser.getId(), id))) {

                user.setId(id);
                user.getUser().setId(id);
                userDetailsService.patch(user);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
