package com.sony.engineering.portalcadastro.service;

import com.sony.engineering.portalcadastro.model.User;

public interface UserService extends GenericService<User>{
	
	User validateLogin(User user);
	 
}
