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

	private Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

	private UserDao userDao;

	@Autowired
	public UserServiceImpl(GenericDao<User> dao, UserDao userDao) {
		super(dao);
		this.userDao = userDao;
	}

	public UserServiceImpl(GenericDao<User> dao) {
		super(dao);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public User save(User user) {
		
		if(user.getId() != null) {

				user = userDao.findById(user.getId()).orElseThrow(() -> {
					logger.error("Invalid User Id!");
					throw new NoSuchElementException();
				});

		} else {
			
			try {

				User findUser = userDao.findDistinctByUsername(user.getUsername());

				if (findUser != null){
					user = findUser;
				}
				
			} catch (IncorrectResultSizeDataAccessException e) {
				logger.error("Duplicate username detected, please check the DB");
				throw new IncorrectResultSizeDataAccessException(1);
			}

		}

		return userDao.save(user);
	}

	@Override
	public User patch(User user){

		User userDb = userDao.findById(user.getId())
				.orElseThrow(() -> new NoSuchElementException("Invalid User Id!"));

		merge(user, userDb);
		return userDao.save(userDb);
	}

	@Override
	public User findById(Integer id){

		return userDao.findById(id).orElseThrow(() -> {
			logger.error("Invalid User Id!");
			throw new NoSuchElementException();
		});
	}

	public User findDistinctByUsername(String username){
		return userDao.findDistinctByUsername((username));
	}

	@Override
	public List<User> findDistinctByProfileNot(String profile) {
		return userDao.findDistinctByProfileNot(profile);
	}

}
