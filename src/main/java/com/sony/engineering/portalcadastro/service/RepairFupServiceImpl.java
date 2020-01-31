package com.sony.engineering.portalcadastro.service;

import java.util.ArrayList;
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
import com.sony.engineering.portalcadastro.model.Repair;
import com.sony.engineering.portalcadastro.model.RepairFup;
import com.sony.engineering.portalcadastro.model.SparePart;
import com.sony.engineering.portalcadastro.repository.GenericDao;
import com.sony.engineering.portalcadastro.repository.RepairFupDao;

@Service
public class RepairFupServiceImpl extends GenericServiceImpl<RepairFup> implements RepairFupService{

	public RepairFupServiceImpl(GenericDao<RepairFup> dao) {
		super(dao);
	}

	private Logger logger = LoggerFactory.getLogger(RepairFupService.class);
	private RepairFupDao repairFupDao;
	private SparePartService sparePartService;

	@Autowired
	public RepairFupServiceImpl(GenericDao<RepairFup> dao,
								RepairFupDao repairFupDao,
								SparePartService sparePartService) {
		super(dao);
		this.repairFupDao = repairFupDao;
		this.sparePartService = sparePartService;
	}

	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public RepairFup save(RepairFup repairFup) {
		
		Set<SparePart> spareParts = repairFup.getSpareParts();
		
		if(repairFup.getId() != null) {

			repairFup = repairFupDao.findById(repairFup.getId()).orElseThrow(() -> {
				logger.error("Invalid RepairFup Id!");
				throw new NoSuchElementException();
			});
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
	
	public void repairFupsCheckId(Repair repair) {

		repair.getRepairFups().forEach(rp ->{
			
			if(rp.getId() != null) {
				logger.error("Not allowed to update follow-up!");
				throw new NoSuchElementException();	
			}
		});
		
	}		
	
	@Override
	public void addEquipmentToRepairFups(List<RepairFup> repairFups, Equipment equipment) {
		repairFups.forEach(rp-> rp.setEquipment(equipment));
	}

}
