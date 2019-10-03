package com.sony.engineering.portalcadastro.service;

import java.util.List;

public interface GenericService<T> {

	void save(T t);
	
	void delete(Integer id);
	
	void edit(T t);
	
	T getOne(Integer id);
	
	List<T> getAll();
}
