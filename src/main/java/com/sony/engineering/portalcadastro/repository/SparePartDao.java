package com.sony.engineering.portalcadastro.repository;

import java.util.List;

import com.sony.engineering.portalcadastro.model.SparePart;

public interface SparePartDao extends GenericDao<SparePart>{

	List<SparePart> findDistinctByPartNumber(String partNumber);
}
