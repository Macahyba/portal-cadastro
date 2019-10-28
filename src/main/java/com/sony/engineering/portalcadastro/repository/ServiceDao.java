package com.sony.engineering.portalcadastro.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sony.engineering.portalcadastro.model.Service;

@Repository
public interface ServiceDao extends GenericDao<Service>{

	List<Service> findDistinctByDescription(String description);

	List<Service> findDistinctByName(String name);

}
