package com.sony.engineering.portalcadastro.repository;

import org.springframework.stereotype.Repository;

import com.sony.engineering.portalcadastro.model.Quotation;

import java.util.List;

@Repository
public interface QuotationDao extends GenericDao<Quotation> {

    Quotation findFirstByOrderByIdDesc();

    List<Quotation> findByActiveEquals(Boolean value);

}
