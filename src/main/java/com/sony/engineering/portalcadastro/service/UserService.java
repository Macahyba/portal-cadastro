package com.sony.engineering.portalcadastro.service;

import java.util.List;

import com.sony.engineering.portalcadastro.model.User;

public interface UserService extends GenericService<User>{

	User findDistinctByUsername(String username);

	User findById(Integer id);

	List<User> findAll();

	List<User> findDistinctByProfileNot(String profile);
	 
}
