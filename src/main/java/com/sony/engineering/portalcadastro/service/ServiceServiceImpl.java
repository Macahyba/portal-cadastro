package com.sony.engineering.portalcadastro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sony.engineering.portalcadastro.model.Service;
import com.sony.engineering.portalcadastro.repository.GenericDao;
import com.sony.engineering.portalcadastro.repository.ServiceDao;

@org.springframework.stereotype.Service
public class ServiceServiceImpl extends GenericServiceImpl<Service> implements ServiceService{

	@Autowired
	ServiceDao serviceDao;
	
	public ServiceServiceImpl(GenericDao<Service> dao) {
		super(dao);
	}

	@Override
	public List<Service> findByName(String name) {
		return serviceDao.findByName(name);
	}

	@Override
	public List<Service> findByDescription(String description) {
		return serviceDao.findByDescription(description);
	}




}
