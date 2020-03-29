package com.sony.engineering.portalcadastro.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sony.engineering.portalcadastro.model.User;
import com.sony.engineering.portalcadastro.service.UserService;

@RestController
public class UserController {

	private Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	User authUser;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	private UserService userService;
	
	@GetMapping(value = "users")
	public ResponseEntity<List<User>> getAll(){

		if (authUser.getProfile() != null && authUser.getProfile().equals("admin")) {
			return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}
	
	@GetMapping(value = "users/{id}")
	public ResponseEntity<User> getUserById(@PathVariable("id") Integer id){
		
		try {
			if ((authUser.getProfile() != null) &&
					(authUser.getProfile().equals("admin") || Objects.equals(authUser.getId(), id))) {
				return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@PostMapping(value = "users")
	public ResponseEntity<User> setUser(@RequestBody User user) {
		
		try {
			if (authUser.getProfile() != null && authUser.getProfile().equals("admin")) {
				return new ResponseEntity<>(userService.save(user), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
		} catch (RuntimeException e) {
			logger.error("Error on creating user: " + e); 
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} 
		
	}
	
	@PutMapping(value = "users/{id}")
	public ResponseEntity<User> putUser(@RequestBody User user, @PathVariable("id") Integer id) {
		
		try {
			if ((authUser.getProfile() != null) &&
					(authUser.getProfile().equals("admin") || Objects.equals(authUser.getId(), id))) {
				user.setId(id);
				return new ResponseEntity<>(userService.edit(user), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);		
			
		} catch (RuntimeException e) {
			logger.error("Error on updating user: " + e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}		
	}

	@PatchMapping(value = "users/{id}")
	public ResponseEntity<User> patchUser(@RequestBody User user, @PathVariable("id") Integer id) {

		try {
			if (authUser.getProfile() != null &&
					(authUser.getProfile().equals("admin") || Objects.equals(authUser.getId(), id))) {
				user.setId(id);
				return new ResponseEntity<>(userService.patch(user), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		} catch (RuntimeException e) {
			logger.error("Error on updating user: " + e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping(value = "users/{id}")
	public ResponseEntity<User> deleteUser(@PathVariable("id") Integer id) {
		
		try {
			if (authUser.getProfile() != null &&
					(authUser.getProfile().equals("admin") || Objects.equals(authUser.getId(), id))) {
				userService.delete(id);
				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);			
						
		} catch (RuntimeException e) {
			logger.error("Error on deleting user: " + e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}	
	
}
