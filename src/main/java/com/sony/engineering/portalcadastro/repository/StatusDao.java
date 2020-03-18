package com.sony.engineering.portalcadastro.repository;

import com.sony.engineering.portalcadastro.model.Status;

import java.util.List;

public interface StatusDao extends GenericDao<Status>{

	Status findDistinctByStatus(String status);

	List<Status> findAllByOrderByIdAsc();

}
