package com.sony.engineering.portalcadastro.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface GenericDao<T> {

	T save(T t);
	
	void delete(Integer id);
	
	T edit(T t);
	
	T getOne(Integer id);
	
	T getOneByAttr(String attribute, String value);
	
	List<T> getListByAttr(String attribute, String value);
	
	List<T> getAll();
	
	List<T> getAttrList(String attribute);
	
}
