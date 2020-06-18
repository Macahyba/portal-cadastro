package com.sony.engineering.portalcadastro.service;

import com.sony.engineering.portalcadastro.model.Quotation;

import java.util.List;

public interface QuotationService extends GenericService<Quotation>{

    List<Quotation> findAllActive();

}
