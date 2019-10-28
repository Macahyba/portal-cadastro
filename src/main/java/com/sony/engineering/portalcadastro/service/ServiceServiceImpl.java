package com.sony.engineering.portalcadastro.service;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sony.engineering.portalcadastro.model.Service;
import com.sony.engineering.portalcadastro.repository.GenericDao;
import com.sony.engineering.portalcadastro.repository.ServiceDao;

@org.springframework.stereotype.Service
public class ServiceServiceImpl extends GenericServiceImpl<Service> implements ServiceService{

	Logger logger = LoggerFactory.getLogger(ServiceServiceImpl.class);
	
	@Autowired
	ServiceDao serviceDao;
	
	public ServiceServiceImpl(GenericDao<Service> dao) {
		super(dao);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Service save(Service service) {
		
		if(service.getId() != null) {
			
			try {
				service = serviceDao.findById(service.getId()).get();
			} catch (NoSuchElementException e) {
				logger.error("Invalid Service Id!");
				throw new NoSuchElementException();
			}
		} else {
			try {
				
				List<Service> findEquipment = serviceDao.findDistinctByName(service.getName());
				
				if(!findEquipment.isEmpty()) {
					service = findEquipment.get(0);
				}
				
			} catch (IncorrectResultSizeDataAccessException e){
				logger.error("Duplicate Service detected, please chech the DB");
				throw new IncorrectResultSizeDataAccessException(1);
			}
		}
		
		return serviceDao.save(service);
	}	
	
	@Override
	public Set<Service> saveAll(Set<Service> services){
		
		Set<Service> newServices = new HashSet<>();
		
		services.forEach(sv ->{ 
					
			try {
			
				newServices.add(this.save(sv));
			} catch (RuntimeException e) {
				logger.error("Error persisting service");	
				throw new RuntimeException();
			}
		
		});
		
		return newServices;
	}
	
	@Override
	public List<Service> findDistinctByName(String name) {
		return serviceDao.findDistinctByName(name);
	}

	@Override
	public List<Service> findDistinctByDescription(String description) {
		return serviceDao.findDistinctByDescription(description);
	}


}
