package com.sony.engineering.portalcadastro.service;

import java.util.List;
import java.util.NoSuchElementException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sony.engineering.portalcadastro.model.User;
import com.sony.engineering.portalcadastro.repository.GenericDao;
import com.sony.engineering.portalcadastro.repository.UserDao;

@Service
public class UserServiceImpl extends GenericServiceImpl<User> implements UserService{

	Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
	
	@Autowired
	private UserDao userDao;
	
	public UserServiceImpl(GenericDao<User> dao) {
		super(dao);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public User save(User user) {
		
		if(user.getId() != null) {
			
			try {
				user = userDao.findById(user.getId()).get();
			} catch (NoSuchElementException e) {
				logger.error("Invalid User Id!");
				throw new NoSuchElementException();
			}
			
		} else {
			
			try {

				List<User> findUser = userDao.findDistinctByEmail(user.getEmail());
				
				if(!findUser.isEmpty()) {
					user = findUser.get(0);
				}
				
			} catch (IncorrectResultSizeDataAccessException e){
				logger.error("Duplicate user e-mail detected, please chech the DB");
				throw new IncorrectResultSizeDataAccessException(1);
			}
			
		}

		return userDao.save(user);
	}
	
	public User validateLogin(User user) {
		
		try {
			List<User> u = userDao.findDistinctByLogin(user.getLogin());
		
			if(user.getPassword().equals(u.get(0).getPassword())) {
				
				return u.get(0);
			}
			
		} catch (NullPointerException e) {
			
			return null;
		}
		
		return null;
	}

}
