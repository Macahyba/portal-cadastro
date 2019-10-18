package com.sony.engineering.portalcadastro.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sony.engineering.portalcadastro.model.Customer;
import com.sony.engineering.portalcadastro.model.Equipment;
import com.sony.engineering.portalcadastro.model.Quotation;
import com.sony.engineering.portalcadastro.model.Service;
import com.sony.engineering.portalcadastro.repository.GenericDaoImpl;
import com.sony.engineering.portalcadastro.repository.QuotationDao;

@org.springframework.stereotype.Service
public class QuotationService extends GenericServiceImpl<Quotation>{
	
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
		
	public QuotationService(GenericDaoImpl<Quotation> quotationDao) {
		this.quotationDao = (QuotationDao)quotationDao;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Quotation save(Quotation quotation) {
		
		
		quotation.setUser(userService.getOne(quotation.getUser().getId()));
		
		Customer customer = customerService.getOneByAttr("name", quotation.getCustomer().getName());
		
		if (customer == null) {
			
			customer = customerService.save(quotation.getCustomer());
			
		}
		quotation.setCustomer(customer);
		
		
		List<Equipment> equipments = new ArrayList<Equipment>();
		
		for(Equipment e: quotation.getEquipments()) {
			
			Equipment equipment = equipmentService.getOneByAttr("name", e.getName());
			
			if(equipment == null) {
				equipment = equipmentService.save(e);
			}
			equipments.add(equipment);
		}
		quotation.setEquipments(equipments);

		
		List<Service> services = new ArrayList<Service>();
		
		for(Service s: quotation.getServices()) {
			
			Service service = serviceService.getOneByAttr("name", s.getName());
			
			if(service == null) {
				service  = serviceService.save(s);
			}
			services.add(service);
		}
		quotation.setServices(services);		
		
		
		quotationDao.save(quotation);
		return quotation;
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
