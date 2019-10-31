package com.sony.engineering.portalcadastro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	UserService userService;
	
	@GetMapping(value = "users")
	public List<User> getAll(){

		return userService.findAll();
		
	}
	
	@GetMapping(value = "users/{id}")
	public User getEquipmentById(@PathVariable("id") Integer id){
		
		return userService.findById(id);
	}
	
	@PostMapping(value = "users")
	public User setEquipment(@RequestBody User user) {
		return userService.save(user);
	}
	
	@PutMapping(value = "users/{id}")
	public User updateEquipment(@RequestBody User user, @PathVariable("id") Integer id) {
		user.setId(id);
		return userService.edit(user);
	}
	
	@DeleteMapping(value = "users/{id}")
	public void deleteEquipment(@PathVariable("id") Integer id) {
		userService.delete(id);
	}	
	
}
