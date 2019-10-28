package com.sony.engineering.portalcadastro.service;

import java.util.List;
import java.util.Set;

import com.sony.engineering.portalcadastro.model.Service;

public interface ServiceService extends GenericService<Service>{

	List<Service> findDistinctByName(String name);

	List<Service> findDistinctByDescription(String description);

	Set<Service> saveAll(Set<Service> services);
}
