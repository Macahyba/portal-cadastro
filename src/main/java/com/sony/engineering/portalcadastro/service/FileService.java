package com.sony.engineering.portalcadastro.service;


import org.springframework.core.io.Resource;

import com.sony.engineering.portalcadastro.exception.PdfGenerationException;

public interface FileService {

	Resource loadFileAsResource(String fileName);
	
	String generatePdf(Integer id) throws PdfGenerationException;
	
}
