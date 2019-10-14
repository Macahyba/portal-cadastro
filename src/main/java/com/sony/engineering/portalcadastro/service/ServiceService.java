package com.sony.engineering.portalcadastro.service;

import com.sony.engineering.portalcadastro.model.Service;
import com.sony.engineering.portalcadastro.repository.GenericDao;
import com.sony.engineering.portalcadastro.repository.ServiceDao;

@org.springframework.stereotype.Service
public class ServiceService extends GenericServiceImpl<Service>{

	private ServiceDao serviceDao;
	
	public ServiceService(GenericDao<Service> dao) {
		super(dao);
		this.serviceDao = (ServiceDao)dao;
	}

	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

}
