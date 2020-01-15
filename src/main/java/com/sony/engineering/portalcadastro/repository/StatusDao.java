package com.sony.engineering.portalcadastro.repository;

import com.sony.engineering.portalcadastro.model.Status;

public interface StatusDao extends GenericDao<Status>{

	public Status findDistinctByStatus(String status);
	
}
