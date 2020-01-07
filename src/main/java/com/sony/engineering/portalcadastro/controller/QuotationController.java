package com.sony.engineering.portalcadastro.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

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

import com.sony.engineering.portalcadastro.model.Quotation;
import com.sony.engineering.portalcadastro.service.CustomerService;
import com.sony.engineering.portalcadastro.service.EquipmentService;
import com.sony.engineering.portalcadastro.service.FileService;
import com.sony.engineering.portalcadastro.service.MailService;
import com.sony.engineering.portalcadastro.service.QuotationService;
import com.sony.engineering.portalcadastro.service.ServiceService;

@Controller
public class QuotationController {

	Logger logger = LoggerFactory.getLogger(QuotationController.class); 
	
	@Autowired
	EquipmentService equipmentService;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	ServiceService serviceService;
	
	@Autowired
	QuotationService quotationService;
	
	@Autowired
	MailService mailService;
	
	@Autowired
	FileService fileService;
	
	@GetMapping(value = "quotations")
	public ResponseEntity<List<Quotation>> getAll(){
	
		return new ResponseEntity<List<Quotation>>(quotationService.findAll(), HttpStatus.OK);
	}
	
	@GetMapping(value = "quotations/{id}")
	public ResponseEntity<Quotation> getQuotation(@PathVariable("id") Integer id){
		
		try {
			
			return new ResponseEntity<Quotation>(quotationService.findById(id), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value = "quotations")
	public ResponseEntity<?> setQuotation(@RequestBody Quotation quotation) {
		
		try {
			
			quotationService.save(quotation);
			fileService.generatePdf(quotation.getId());
			mailService.sendMailNew(quotation);
			return new ResponseEntity<Quotation>(quotation, HttpStatus.CREATED);
		} catch (RuntimeException e) {
			logger.error("Error on creating quotation"); 
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (IOException e) {
			logger.error("Error creating pdf: " + e);
			Map<String, Object> map = new HashMap<String, Object>(){{
				put("quotation", quotation);
				put("warning", "Erro ao criar pdf!");
			}};
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.ACCEPTED); 
		} catch (Exception e) {
			logger.error("Error sendig email: " + e);
			Map<String, Object> map = new HashMap<String, Object>(){{
				put("quotation", quotation);
				put("warning", "Erro ao enviar e-mail!");
			}};
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.ACCEPTED); 
		} 
	}
	
	@PutMapping(value = "quotations/{id}")
	public ResponseEntity<Quotation> updateQuotation(
			@RequestBody Quotation quotation, @PathVariable("id") Integer id) {
		
		quotation.setId(id);
		
		try {
			return new ResponseEntity<Quotation>(quotationService.edit(quotation), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (RuntimeException e) {
			logger.error("Error on updating quotation");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PatchMapping(value = "quotations/{id}")
	public ResponseEntity<Quotation> patchQuotation(
			@RequestBody Quotation quotation, @PathVariable("id") Integer id){

		quotation.setId(id);
		
		try {
			return new ResponseEntity<Quotation>(quotationService.patch(quotation), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);			
		} catch (RuntimeException e) {
			logger.error("Error on patching quotation");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}		
	}
	
	@DeleteMapping(value = "quotations/{id}")
	public ResponseEntity<Object> deleteQuotation(@PathVariable("id") Integer id){
		
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
