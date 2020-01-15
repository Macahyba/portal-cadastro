package com.sony.engineering.portalcadastro.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sony.engineering.portalcadastro.exception.PdfGenerationException;
import com.sony.engineering.portalcadastro.model.Quotation;
import com.sony.engineering.portalcadastro.service.FileService;
import com.sony.engineering.portalcadastro.service.MailService;
import com.sony.engineering.portalcadastro.service.QuotationService;

@Controller
public class QuotationController {

	Logger logger = LoggerFactory.getLogger(QuotationController.class); 
	
	@Autowired
	QuotationService quotationService;
	
	@Autowired
	MailService mailService;
	
	@Autowired
	FileService fileService;
	
	@GetMapping(value = "quotations")
	public ResponseEntity<List<Quotation>> getQuotationAll(){
	
		return new ResponseEntity<List<Quotation>>(quotationService.findAll(), HttpStatus.OK);
	}
	
	@GetMapping(value = "quotations/{id}")
	public ResponseEntity<Quotation> getQuotationOne(@PathVariable("id") Integer id){
		
		try {
			
			return new ResponseEntity<Quotation>(quotationService.findById(id), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value = "quotations")
	public ResponseEntity<?> postQuotation(@RequestBody Quotation quotation) {
		
		try {
			
			quotationService.save(quotation);
			//mailService.sendMailNew(quotation);
			return new ResponseEntity<Quotation>(quotation, HttpStatus.CREATED);
			
//		} catch (MessagingException | GeneralSecurityException | IOException e) {
//			logger.error("Error sendig email: " + e);
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("quotation", quotation);
//			map.put("warning", "Erro ao enviar e-mail!");			
//			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.ACCEPTED); 	
			
		} catch (RuntimeException e) {
			logger.error("Error on creating quotation: " + e); 
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} 
	}

	@PutMapping(value = "quotations/{id}")
	public ResponseEntity<?> updateQuotation(
			@RequestBody Quotation quotation, @PathVariable("id") Integer id) {
		
		quotation.setId(id);
		
		try {
			
			quotation = quotationService.edit(quotation);			
			fileService.generatePdf(quotation.getId());
			mailService.sendMailNew(quotation);
			return new ResponseEntity<Quotation>(quotation, HttpStatus.OK);
		} catch (PdfGenerationException e) {
			logger.error("Error creating pdf: " + e);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("quotation", quotation);
			map.put("warning", "Erro ao criar pdf!");			
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.ACCEPTED); 			
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			
		} catch (MessagingException | GeneralSecurityException | IOException e) {
			logger.error("Error sendig email: " + e);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("quotation", quotation);
			map.put("warning", "Erro ao enviar e-mail!");			
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.ACCEPTED); 			
			
		} catch (RuntimeException e) {
			logger.error("Error on updating quotation: " + e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PatchMapping(value = "quotations/{id}")
	public ResponseEntity<?> patchQuotation(
			@RequestBody Quotation quotation, @PathVariable("id") Integer id){

		quotation.setId(id);
		
		try {
			
			quotation = quotationService.patch(quotation);			
			fileService.generatePdf(quotation.getId());
			//mailService.sendMailUpdate(quotation);
			return new ResponseEntity<Quotation>(quotation, HttpStatus.OK);
		} catch (PdfGenerationException e) {
			logger.error("Error creating pdf: " + e);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("quotation", quotation);
			map.put("warning", "Erro ao criar pdf!");
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.ACCEPTED); 			
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			
//		} catch (MessagingException | GeneralSecurityException | IOException e) {
//			logger.error("Error sendig email: " + e);
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("quotation", quotation);
//			map.put("warning", "Erro ao enviar e-mail!");
//			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.ACCEPTED); 						
		} catch (RuntimeException e) {
			logger.error("Error on patching quotation: " + e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}		
	}
	
	@DeleteMapping(value = "quotations/{id}")
	public ResponseEntity<?> deleteQuotation(@PathVariable("id") Integer id){
		
		try {
			quotationService.delete(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (EmptyResultDataAccessException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);			
		} catch (RuntimeException e) {
			logger.error("Error on deleting quotation");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
}
