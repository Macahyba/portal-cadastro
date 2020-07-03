package com.sony.engineering.portalcadastro.service;

import java.util.HashSet;

import java.util.NoSuchElementException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import com.sony.engineering.portalcadastro.model.SparePart;
import com.sony.engineering.portalcadastro.repository.GenericDao;
import com.sony.engineering.portalcadastro.repository.SparePartDao;

@Service
public class SparePartServiceImpl extends GenericServiceImpl<SparePart> implements SparePartService{
	
	private Logger logger = LoggerFactory.getLogger(SparePartServiceImpl.class);

	private SparePartDao sparePartDao;

	@Autowired
	public SparePartServiceImpl(GenericDao<SparePart> dao, SparePartDao sparePartDao) {
		super(dao);
		this.sparePartDao = sparePartDao;
	}

	public SparePartServiceImpl(GenericDao<SparePart> dao) {
		super(dao);
	}

	@Override
	public Set<SparePart> findDistinctByPartNumber(String partNumber) {
		return sparePartDao.findDistinctByPartNumber(partNumber);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public SparePart save(SparePart sparePart) {

		if(sparePart.getId() != null) {

			sparePart = sparePartDao.findById(sparePart.getId()).<NoSuchElementException>orElseThrow(() -> {
				logger.error("Invalid SparePart Id!");
				throw new NoSuchElementException();
			});

		} else {
			
			try {

				Set<SparePart> findSparePart = sparePartDao.findDistinctByPartNumber(sparePart.getPartNumber());
				
				if(!findSparePart.isEmpty()) {
					sparePart = findSparePart.iterator().next();
				}
				
			} catch (IncorrectResultSizeDataAccessException e){
				logger.error("Duplicate SparePart detected, please check the DB");
				throw new IncorrectResultSizeDataAccessException(1);
			}
			
		}

		return sparePartDao.save(sparePart);		
	}
	
	@Override
	public Set<SparePart> saveAll(Set<SparePart> spareParts) {
		Set<SparePart> newSpareParts = new HashSet<>();
		
		spareParts.forEach(eq ->{ 
					
			try {
			
				newSpareParts.add(this.save(eq));
			} catch (RuntimeException e) {				
				logger.error("Error persisting spareParts");	
				throw new RuntimeException();
			}
		
		});
		
		return newSpareParts;
	}

}
