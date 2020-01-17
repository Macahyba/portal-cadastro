package com.sony.engineering.portalcadastro.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sony.engineering.portalcadastro.model.User;
import com.sony.engineering.portalcadastro.service.UserService;

@RestController
public class UserController {

	Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	UserService userService;
	
	@GetMapping(value = "users")
	public ResponseEntity<List<User>> getAll(){

		return new ResponseEntity<List<User>>(userService.findAll(), HttpStatus.OK);
		
	}
	
	@GetMapping(value = "users/{id}")
	public ResponseEntity<User> getEquipmentById(@PathVariable("id") Integer id){
		
		try {
			return new ResponseEntity<User>(userService.findById(id), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@PostMapping(value = "users")
	public ResponseEntity<User> setEquipment(@RequestBody User user) {
		
		try {
			return new ResponseEntity<User>(userService.save(user), HttpStatus.OK);
		} catch (RuntimeException e) {
			logger.error("Error on creating user: " + e); 
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} 
		
	}
	
	@PutMapping(value = "users/{id}")
	public ResponseEntity<User> updateEquipment(@RequestBody User user, @PathVariable("id") Integer id) {
		
		try {
			user.setId(id);
			return new ResponseEntity<User>(userService.edit(user), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);		
			
		} catch (RuntimeException e) {
			logger.error("Error on updating repair: " + e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}		
	}
	
	@DeleteMapping(value = "users/{id}")
	public ResponseEntity<User> deleteEquipment(@PathVariable("id") Integer id) {
		
		try {
			userService.delete(id);
			return new ResponseEntity<>(HttpStatus.OK);			
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);			
						
		} catch (RuntimeException e) {
			logger.error("Error on patching repair: " + e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}	
	
}
