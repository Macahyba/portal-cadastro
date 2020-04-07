package com.sony.engineering.portalcadastro.service;

import java.util.*;
import com.sony.engineering.portalcadastro.auth.JwtUserDetailsService;
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

	private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserDao userDao;

	@Autowired
	private MailService mailService;

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;

	@Autowired
	public UserServiceImpl(
				GenericDao<User> dao,
				UserDao userDao) {
		super(dao);
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

	private Map<String, Object> mailErrorHandling(User user, Throwable e){
		logger.error("Error sendig email: " + e);
		Map<String, Object> map = new HashMap<>();
		map.put("quotation", user.getUsername());
		map.put("warning", "Erro ao enviar e-mail!");
		return map;
	}

}
