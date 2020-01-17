package com.sony.engineering.portalcadastro.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sony.engineering.portalcadastro.model.Equipment;
import com.sony.engineering.portalcadastro.model.RepairFup;
import com.sony.engineering.portalcadastro.model.SparePart;
import com.sony.engineering.portalcadastro.repository.GenericDao;
import com.sony.engineering.portalcadastro.repository.RepairFupDao;

@Service
public class RepairFupServiceImpl extends GenericServiceImpl<RepairFup> implements RepairFupService{

	public RepairFupServiceImpl(GenericDao<RepairFup> dao) {
		super(dao);
	}

	Logger logger = LoggerFactory.getLogger(RepairFupService.class);
	
	@Autowired
	RepairFupDao repairFupDao;
	
	@Autowired
	SparePartService sparePartService;

	@Autowired
	EquipmentService equipmentService;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public RepairFup save(RepairFup repairFup) {
		
		Set<SparePart> spareParts = repairFup.getSpareParts();
		
		if(repairFup.getId() != null) {
			
			try {
				repairFup = repairFupDao.findById(repairFup.getId()).get();
			} catch (NoSuchElementException e) {
				logger.error("Invalid RepairFup Id!");
				throw new NoSuchElementException();
			}
		} 	
		
		spareParts = sparePartService.saveAll(spareParts);
		repairFup.setSpareParts(spareParts);
		
		return repairFupDao.save(repairFup);
	}	
	
	@Override
	public List<RepairFup> saveAll(List<RepairFup> repairFup) {
		List<RepairFup> newRepairFups = new ArrayList<>();
		
		repairFup.forEach(rf ->{ 
					
			try {
			
				newRepairFups.add(this.save(rf));
			} catch (RuntimeException e) {
				logger.error("Error persisting follow-up");	
				throw new RuntimeException();
			}
		
		});
		
		return newRepairFups;
	}

	@Override
	public RepairFup addEquipmentToSparePart(RepairFup repairFup, Equipment equipment) {
		
		Set<SparePart> spareParts = repairFup.getSpareParts();
		Set<SparePart> newSpareParts = new HashSet<SparePart>();
		
		for (SparePart sparePart : spareParts) {
			
			newSpareParts.add(sparePartService.addEquipmentToSparePart(sparePart, equipment));
		}
		
		repairFup.setSpareParts(newSpareParts);
		return repairFup;
	}

}
