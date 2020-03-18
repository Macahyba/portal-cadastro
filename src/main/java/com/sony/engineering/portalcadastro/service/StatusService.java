package com.sony.engineering.portalcadastro.service;

import com.sony.engineering.portalcadastro.model.Status;

import java.util.List;

public interface StatusService extends GenericService<Status>{

	Status findDistinctByStatus(String status);

	List<Status> findAllByOrderByIdAsc();
	
}
