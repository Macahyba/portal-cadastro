package com.sony.engineering.portalcadastro.service;

import org.springframework.core.io.Resource;

public interface FileService {

	Resource loadFileAsResource(String fileName);
	
	String generatePdf(Integer id) throws Exception ;
	
}
