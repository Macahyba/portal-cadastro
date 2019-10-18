package com.sony.engineering.portalcadastro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sony.engineering.portalcadastro.model.User;
import com.sony.engineering.portalcadastro.repository.GenericDao;
import com.sony.engineering.portalcadastro.repository.UserDao;

@Service
public class UserService extends GenericServiceImpl<User> {
	
	@Autowired	
	private UserDao userDao;
	
	@Autowired
	public UserService(GenericDao<User> dao) {
		this.userDao = (UserDao) dao;
	}

	@Transactional
	public User getUserByLogin(String login) {
		
		return userDao.getUserByLogin(login);
	}	
	
	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public User validateLogin(User user) {
		
		User u = userDao.getUserByLogin(user.getLogin());
		
		if(user.getPassword().equals(u.getPassword())) {
			
			return u;
		}
		
		return null;
	}

}
