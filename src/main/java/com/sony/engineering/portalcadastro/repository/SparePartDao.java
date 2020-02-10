package com.sony.engineering.portalcadastro.repository;

import java.util.Set;

import com.sony.engineering.portalcadastro.model.SparePart;

public interface SparePartDao extends GenericDao<SparePart>{

	Set<SparePart> findDistinctByPartNumber(String partNumber);
}
