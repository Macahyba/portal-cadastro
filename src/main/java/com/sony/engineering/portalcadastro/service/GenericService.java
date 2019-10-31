package com.sony.engineering.portalcadastro.service;

import java.util.List;

public interface GenericService<T> {

	T save(T t);
	
	void delete(Integer id);
	
	T edit(T t);
	
	T patch(T t);
	
	List<T> findAll();

	T findById(Integer id);
	
}
