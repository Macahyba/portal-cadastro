package com.sony.engineering.portalcadastro.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sony.engineering.portalcadastro.model.Customer;

@Repository
public interface CustomerDao extends GenericDao<Customer>{

	List<Customer> findByCnpj(String cnpj);

	List<Customer> findDistinctByName(String name);

}
