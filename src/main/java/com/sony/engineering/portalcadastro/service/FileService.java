package com.sony.engineering.portalcadastro.service;


import org.springframework.core.io.Resource;

import com.sony.engineering.portalcadastro.exception.PdfGenerationException;

import java.io.IOException;

public interface FileService {

	Resource loadFileAsResource(String fileName);
	
	String generatePdf(Integer id) throws PdfGenerationException;

	String generateQuotationsCsv() throws IOException;

	String generateRepairsCsv() throws IOException;

}
