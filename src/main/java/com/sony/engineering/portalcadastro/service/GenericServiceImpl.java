package com.sony.engineering.portalcadastro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sony.engineering.portalcadastro.repository.GenericDao;

@Service
public abstract class GenericServiceImpl<T> implements GenericService<T> {

	@Autowired
	private GenericDao<T> dao;
	
	public GenericServiceImpl(GenericDao<T> dao) {
		this.dao = dao;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public T save(T t) {
		return dao.save(t);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Integer id) {

		dao.deleteById(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public T edit(T t) {

		return dao.save(t);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public abstract T patch(T t);

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public T findById(Integer id) {

		return dao.findById(id).get();
	}	
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<T> findAll() {

		return dao.findAll();
	}

}
