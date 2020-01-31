package com.sony.engineering.portalcadastro.service;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sony.engineering.portalcadastro.repository.GenericDao;

@Service
public abstract class GenericServiceImpl<T> implements GenericService<T> {

	private GenericDao<T> dao;

	@Autowired
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
	public T patch(T t) {
		
		return dao.save(t);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public T findById(Integer id) {

		return dao.findById(id).orElseThrow(NoSuchElementException::new);
	}	
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<T> findAll() {

		return dao.findAll();
	}

	public String[] getNullPropertyNames (Object source) {
	    final BeanWrapper src = new BeanWrapperImpl(source);
	    PropertyDescriptor[] pds = src.getPropertyDescriptors();

	    Set<String> emptyNames = new HashSet<>();
	    for(PropertyDescriptor pd : pds) {
	        Object srcValue = src.getPropertyValue(pd.getName());
	        if ((srcValue == null) || 
	        		(srcValue instanceof HashSet<?> && srcValue.toString().equals("[]"))) {
	        	emptyNames.add(pd.getName());
	        }
	    }
	    String[] result = new String[emptyNames.size()];
	    return emptyNames.toArray(result);
	}
	
	public void merge(Object src, Object target) {
	    BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
	}	
	
}
