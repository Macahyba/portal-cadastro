package com.sony.engineering.portalcadastro.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sony.engineering.portalcadastro.model.User;

@Repository
public interface UserDao extends GenericDao<User>{

	List<User> findDistinctByLogin(String login);
	
	List<User> findDistinctByEmail(String email);
	
	List<User> findDistinctByProfileNot(String profile);
	
}
