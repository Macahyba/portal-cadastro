package com.sony.engineering.portalcadastro.service;

import java.util.List;

import com.sony.engineering.portalcadastro.model.User;

public interface UserService extends GenericService<User>{

	User findById(Integer id);

	List<User> findAll();
}
