package com.sony.engineering.portalcadastro.service;

import java.util.Set;

import com.sony.engineering.portalcadastro.model.SparePart;

public interface SparePartService extends GenericService<SparePart>{

	Set<SparePart> findDistinctByPartNumber(String partNumber);
	
	Set<SparePart> saveAll(Set<SparePart> spareParts);
	
}
