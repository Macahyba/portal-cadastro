package com.sony.engineering.portalcadastro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sony.engineering.portalcadastro.model.User;
import com.sony.engineering.portalcadastro.repository.GenericDao;
import com.sony.engineering.portalcadastro.repository.ServiceDao;
import com.sony.engineering.portalcadastro.repository.UserDao;

@Service
public class UserServiceImpl extends GenericServiceImpl<User> implements UserService{

	@Autowired
	private UserDao userDao;
	
	public UserServiceImpl(GenericDao<User> dao) {
		super(dao);
	}

	public User validateLogin(User user) {
		
		User u = userDao.findByLogin(user.getLogin());
		
		if(user.getPassword().equals(u.getPassword())) {
			
			return u;
		}
		
		return null;
	}

}
