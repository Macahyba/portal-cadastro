package com.sony.engineering.portalcadastro.service;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface GenericService<T> {

	T save(T t);
	
	void delete(Integer id);
	
	T edit(T t);
	
	T getOne(Integer id);
	
	List<T> getListByAttr(String attribute, String value);
	
	List<T> getAll();
	
	List<T> getAttrList(String attribute);

}
