package com.sony.engineering.portalcadastro.service;

import com.sony.engineering.portalcadastro.model.Status;

public interface StatusService extends GenericService<Status>{

	Status findDistinctByStatus(String status);
	
}
