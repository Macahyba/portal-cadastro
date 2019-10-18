package com.sony.engineering.portalcadastro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sony.engineering.portalcadastro.model.Customer;
import com.sony.engineering.portalcadastro.model.Quotation;
import com.sony.engineering.portalcadastro.repository.GenericDaoImpl;
import com.sony.engineering.portalcadastro.repository.QuotationDao;

@Service
public class QuotationService extends GenericServiceImpl<Quotation>{
	
	@Autowired
	private QuotationDao quotationDao;

	@Autowired
	private CustomerService customerService;
		
	public QuotationService(GenericDaoImpl<Quotation> quotationDao) {
		this.quotationDao = (QuotationDao)quotationDao;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Quotation save(Quotation quotation) {
		
		Customer customer = customerService.getOneByAttr("name", quotation.getCustomer().getName());
		
		if (customer == null) {
			
			customer = customerService.save(quotation.getCustomer());
			quotation.setCustomer(customer);
		}
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
