package com.sony.engineering.portalcadastro.service;

import com.sony.engineering.portalcadastro.model.User;
import com.sony.engineering.portalcadastro.repository.GenericDao;
import com.sony.engineering.portalcadastro.repository.UserDao;

public class UserService extends GenericServiceImpl<User> {
	
	private UserDao userDao;
	
	public UserService(GenericDao<User> dao) {
		super(dao);
		this.userDao = (UserDao) dao;
	}

}
