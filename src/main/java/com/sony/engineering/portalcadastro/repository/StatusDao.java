package com.sony.engineering.portalcadastro.repository;

import com.sony.engineering.portalcadastro.model.Status;

public interface StatusDao extends GenericDao<Status>{

	Status findDistinctByStatus(String status);
	
}
