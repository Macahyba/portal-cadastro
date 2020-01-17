package com.sony.engineering.portalcadastro.service;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.management.RuntimeErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sony.engineering.portalcadastro.model.Customer;
import com.sony.engineering.portalcadastro.model.Equipment;
import com.sony.engineering.portalcadastro.model.Repair;
import com.sony.engineering.portalcadastro.model.RepairFup;
import com.sony.engineering.portalcadastro.model.Status;
import com.sony.engineering.portalcadastro.model.User;
import com.sony.engineering.portalcadastro.repository.GenericDao;
import com.sony.engineering.portalcadastro.repository.RepairDao;

@Service
public class RepairServiceImpl extends GenericServiceImpl<Repair> implements RepairService{

	Logger logger = LoggerFactory.getLogger(RepairFupServiceImpl.class);
	
	@Autowired
	RepairDao repairDao;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	StatusService statusService;
	
	@Autowired
	EquipmentService equipmentService;
	
	@Autowired
	RepairFupService repairFupService;
	
	public RepairServiceImpl(GenericDao<Repair> dao) {
		super(dao);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Repair save(Repair repair) {
		
		customerInsertion(repair);
		
		contactInsertion(repair);
		
		userInsertion(repair);
		
		statusInsertion(repair);		
		
		equipmentInsertion(repair);	
		
		// SAVE
		
		return repairDao.save(repair);
		
	}

	public Repair patch(Repair repair) {

		
		try {
		
			Repair repairDb = repairDao.findById(repair.getId()).get();
			
			repairFupsCheckId(repair);
			
			repairFupsInsertion(repair, repairDb);
			
			merge(repair, repairDb);
			return this.save(repairDb);
			
		} catch (Exception e) {
			throw new RuntimeErrorException(null, e.getMessage());
		}
		
	}

	public RepairDao getRepairDao() {
		return repairDao;
	}

	public void setRepairDao(RepairDao repairDao) {
		this.repairDao = repairDao;
	}
	
	public static String[] getNullPropertyNames (Object source) {
	    final BeanWrapper src = new BeanWrapperImpl(source);
	    PropertyDescriptor[] pds = src.getPropertyDescriptors();

	    Set<String> emptyNames = new HashSet<String>();
	    for(PropertyDescriptor pd : pds) {
	        Object srcValue = src.getPropertyValue(pd.getName());
	        if (srcValue == null) emptyNames.add(pd.getName());
	    }
	    String[] result = new String[emptyNames.size()];
	    return emptyNames.toArray(result);
	}
	
	public static void merge(Object src, Object target) {
	    BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
	}		
	
	private void customerInsertion(Repair repair) {

		
		Customer customer = repair.getCustomer();
		
		if (customer == null) {
			throw new RuntimeErrorException(null, "Error on creating customer");
		}
		
		customer = customerService.save(customer);
		repair.setCustomer(customer);
	}
	
	private void contactInsertion(Repair repair) {

		repair.setContact(repair.getCustomer().getContacts().iterator().next());
	}
	
	private void userInsertion(Repair repair) {
		
		User user = repair.getUser();
		
		if(user == null) {
			throw new RuntimeErrorException(null, "Error on creating user");
		}
		
		user = userService.save(user);
		repair.setUser(user);		
	}
	
	private void statusInsertion(Repair repair) {
		
		Status status = repair.getStatus();
		
		try {

			status = statusService.findById(status.getId());
		} catch (Exception e) {
			throw new RuntimeErrorException(null, "Status not found!");
		}
	
		repair.setStatus(status);
		
	}	
	
	private void equipmentInsertion(Repair repair) {
		
		Equipment equipment = repair.getEquipment();
		
		if(equipment == null) {
			throw new RuntimeErrorException(null, "Error on creating equipment");
		}
		
		equipment = equipmentService.save(equipment);
		repair.setEquipment(equipment);
		
	}		

	private void repairFupsInsertion(Repair repair, Repair repairDb) {
		List<RepairFup> repairFups = repairDb.getRepairFups();
		
		repair = addEquipmentToSparePart(repair, repairDb.getEquipment());		
		
		if (!repairFups.isEmpty()) {
			
			repair.addRepairFupsTop(repairFups);
		}	
		
		repairFupService.saveAll(repair.getRepairFups());
		
	}	
	
	private Repair addEquipmentToSparePart(Repair repair, Equipment equipment) {

		List<RepairFup> repairFups = repair.getRepairFups();
		
		List<RepairFup> newRepairFups = new ArrayList<RepairFup>();
		
		for (RepairFup repairFup : repairFups) {
			
			newRepairFups.add(repairFupService.addEquipmentToSparePart(repairFup, equipment));
		}
		
		repair.setRepairFups(newRepairFups);		
		return repair;
	}

	private void repairFupsCheckId(Repair repair) {

		repair.getRepairFups().forEach(rp ->{
			
			if(rp.getId() != null) {
				logger.error("Not allowed to update follow-up!");
				throw new NoSuchElementException();	
			}
		});
		
	}	
	
}
