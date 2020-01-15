package com.sony.engineering.portalcadastro.service;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sony.engineering.portalcadastro.model.Status;
import com.sony.engineering.portalcadastro.repository.GenericDao;
import com.sony.engineering.portalcadastro.repository.StatusDao;

@Service
public class StatusServiceImpl extends GenericServiceImpl<Status> implements StatusService{

	@Autowired
	StatusDao statusDao;
	
	Logger logger = LoggerFactory.getLogger(EquipmentServiceImpl.class);
	
	public StatusServiceImpl(GenericDao<Status> dao) {
		super(dao);
	}

	@Override
	public Status save(Status status) {
		
		if(status.getId() != null) {
			
			try {
				status = statusDao.findById(status.getId()).get();
			} catch (NoSuchElementException e) {
				logger.error("Invalid Status Id!");
				throw new NoSuchElementException();
			}
			
		} else {

			status = statusDao.findDistinctByStatus(status.getStatus());
		}
		
		return statusDao.save(status);
	}
	
	@Override
	public Status findDistinctByStatus(String status) {
		return statusDao.findDistinctByStatus(status);
	}

}
