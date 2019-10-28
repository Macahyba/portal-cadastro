package com.sony.engineering.portalcadastro.service;

import java.util.Set;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sony.engineering.portalcadastro.model.Customer;
import com.sony.engineering.portalcadastro.model.Equipment;
import com.sony.engineering.portalcadastro.model.Quotation;
import com.sony.engineering.portalcadastro.model.Service;
import com.sony.engineering.portalcadastro.model.User;
import com.sony.engineering.portalcadastro.repository.GenericDao;
import com.sony.engineering.portalcadastro.repository.QuotationDao;

@org.springframework.stereotype.Service
public class QuotationServiceImpl extends GenericServiceImpl<Quotation> implements QuotationService{

	@Autowired
	private QuotationDao quotationDao;

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EquipmentService equipmentService;
	
	@Autowired
	private ServiceService serviceService;
		
	public QuotationServiceImpl(GenericDao<Quotation> dao) {
		super(dao);
	}	
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Quotation save(Quotation quotation) {
		
		// CUSTOMER INSERTION
		
		Customer customer = quotation.getCustomer();

		customer = customerService.save(customer);
		
		if (customer == null) {
			throw new RuntimeErrorException(null, "Error on creating customer");
		}
		
		quotation.setCustomer(customer);
				
		
		// USER INSERTION
		
		User user = quotation.getUser();
		
		user = userService.save(user);
		
		if(user == null) {
			throw new RuntimeErrorException(null, "Error on creating user");
		}
		
		quotation.setUser(user);
		
		// EQUIPMENT INSERTION
		
		Set<Equipment> equipments = quotation.getEquipments();
		
		equipments = equipmentService.saveAll(equipments);
		
		if(equipments.isEmpty()) {
			throw new RuntimeErrorException(null, "Error on creating equipments");
		}
		
		quotation.setEquipments(equipments);
		
		// SERVICE INSERTION
		
		Set<Service> services = quotation.getServices();
		
		services = serviceService.saveAll(services);
		
		if(services.isEmpty()) {
			throw new RuntimeErrorException(null, "Error on creating services");
		}	
		
		quotation.setServices(services);
		
		services.forEach(e ->{ serviceService.save(e);});

		

		
	
		return quotationDao.save(quotation);
		
//		
//		
//		quotation.setUser(userService.getOne(quotation.getUser().getId()));
//		
//		Customer customer = customerService.getOneByAttr("name", quotation.getCustomer().getName());
//		
//		if (customer == null) {
//			
//			customer = customerService.save(quotation.getCustomer());
//			
//		}
//		quotation.setCustomer(customer);
//		
//		
//		List<Equipment> equipments = new ArrayList<Equipment>();
//		
//		for(Equipment e: quotation.getEquipments()) {
//			
//			Equipment equipment = equipmentService.getOneByAttr("name", e.getName());
//			
//			if(equipment == null) {
//				equipment = equipmentService.save(e);
//			}
//			equipments.add(equipment);
//		}
//		quotation.setEquipments(equipments);
//
//		
//		List<Service> services = new ArrayList<Service>();
//		
//		for(Service s: quotation.getServices()) {
//			
//			Service service = serviceService.getOneByAttr("name", s.getName());
//			
//			if(service == null) {
//				service  = serviceService.save(s);
//			}
//			services.add(service);
//		}
//		quotation.setServices(services);		
//		
//		
//		quotationDao.save(quotation);
//		return quotation;
	}

	public QuotationDao getQuotationDao() {
		return quotationDao;
	}

	public void setQuotationDao(QuotationDao quotationDao) {
		this.quotationDao = quotationDao;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
}
