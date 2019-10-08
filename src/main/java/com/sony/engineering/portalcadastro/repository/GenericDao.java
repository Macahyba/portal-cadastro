package com.sony.engineering.portalcadastro.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface GenericDao<T> {

	void save(T t);
	
	void delete(Integer id);
	
	void edit(T t);
	
	T getOne(Integer id);
	
	List<T> getAll();
	
}
