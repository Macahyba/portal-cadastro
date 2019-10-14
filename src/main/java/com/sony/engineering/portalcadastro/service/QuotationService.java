package com.sony.engineering.portalcadastro.service;

import org.springframework.stereotype.Service;

import com.sony.engineering.portalcadastro.model.Quotation;
import com.sony.engineering.portalcadastro.repository.GenericDao;
import com.sony.engineering.portalcadastro.repository.QuotationDao;

@Service
public class QuotationService extends GenericServiceImpl<Quotation>{

	QuotationDao quotationDao;
	
	public QuotationService(GenericDao<Quotation> dao) {
		super(dao);
		this.quotationDao = (QuotationDao)dao;
	}

	public QuotationDao getQuotationDao() {
		return quotationDao;
	}

	public void setQuotationDao(QuotationDao quotationDao) {
		this.quotationDao = quotationDao;
	}

	
}
