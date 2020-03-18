package com.sony.engineering.portalcadastro.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.sony.engineering.portalcadastro.exception.PdfGenerationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.sony.engineering.portalcadastro.service.FileService;

@RestController
public class FileController {

    private Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
	public FileController(FileService fileService) {
		this.fileService = fileService;
	}

	private FileService fileService;
	
	@GetMapping(value = "pdf/{id}")
	public ResponseEntity<Resource> generatePdf(@PathVariable("id") Integer id, HttpServletRequest request) {
		
		try {
			
			String fileName = fileService.generatePdf(id);
			
			if (fileName != null){
				
				Resource resource = fileService.loadFileAsResource(fileName);
				
		        String contentType;

	            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
	
		        // Fallback to the default content type if type could not be determined
		        if(contentType == null) {
		            contentType = "application/octet-stream";
		        }
	
		        return ResponseEntity.ok()
		                .contentType(MediaType.parseMediaType(contentType))
		                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
		                .body(resource);	
		        	        
			}
			
		} catch (IOException ex) {
			logger.info("Could not determine file type.");

		} catch (PdfGenerationException e){

			logger.info("Approval User not found!");
		} catch (Exception e) {
			logger.info("Quotation not found!");
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
