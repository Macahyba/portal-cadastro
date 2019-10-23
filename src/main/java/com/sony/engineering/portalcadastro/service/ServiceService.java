package com.sony.engineering.portalcadastro.service;

import java.util.List;

import com.sony.engineering.portalcadastro.model.Service;

public interface ServiceService extends GenericService<Service>{

	List<Service> findByName(String name);

	List<Service> findByDescription(String description);

}
