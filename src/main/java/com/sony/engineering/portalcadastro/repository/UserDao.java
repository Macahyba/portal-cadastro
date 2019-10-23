package com.sony.engineering.portalcadastro.repository;

import org.springframework.stereotype.Repository;

import com.sony.engineering.portalcadastro.model.User;

@Repository
public interface UserDao extends GenericDao<User>{

	User findByLogin(String login);
	
}
