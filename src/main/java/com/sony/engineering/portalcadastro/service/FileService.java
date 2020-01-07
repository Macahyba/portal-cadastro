package com.sony.engineering.portalcadastro.service;

import java.io.IOException;

import org.springframework.core.io.Resource;

public interface FileService {

	Resource loadFileAsResource(String fileName);
	
	String generatePdf(Integer id) throws IOException ;
	
}
