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
import org.springframework.web.bind.annotation.*;

import com.sony.engineering.portalcadastro.exception.PdfGenerationException;
import com.sony.engineering.portalcadastro.model.Quotation;
import com.sony.engineering.portalcadastro.service.FileService;
import com.sony.engineering.portalcadastro.service.MailService;
import com.sony.engineering.portalcadastro.service.QuotationService;

@RestController
public class QuotationController {

	private Logger logger = LoggerFactory.getLogger(QuotationController.class);

	public final static String APROVADO = "APROVADO";

	@Autowired
	public QuotationController(QuotationService quotationService,
							   MailService mailService,
							   FileService fileService) {
		this.quotationService = quotationService;
		this.mailService = mailService;
		this.fileService = fileService;
	}

	private QuotationService quotationService;
	private MailService mailService;
	private FileService fileService;
	
	@GetMapping(value = "quotations")
	public ResponseEntity<List<Quotation>> getQuotationAll(){
	
		return new ResponseEntity<>(quotationService.findAllActive(), HttpStatus.OK);
	}
	
	@GetMapping(value = "quotations/{id}")
	public ResponseEntity<Quotation> getQuotationOne(@PathVariable("id") Integer id){
		
		try {
			
			return new ResponseEntity<>(quotationService.findById(id), HttpStatus.OK);
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
			mailService.sendMailNew(quotation);
			return new ResponseEntity<>(quotation, HttpStatus.CREATED);
			
		} catch (MessagingException | GeneralSecurityException | IOException e) {

			Map<String, Object> map = mailErrorHandling(quotation, e);
			return new ResponseEntity<>(map, HttpStatus.ACCEPTED);
			
		} catch (IllegalArgumentException e) {

			logger.error("Bad credentials for Gmail: " + e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (RuntimeException e) {

			logger.error("Error on creating quotation: " + e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} 
	}

	@PutMapping(value = "quotations/{id}")
	public ResponseEntity<?> updateQuotation(
			@RequestBody Quotation quotation, @PathVariable("id") Integer id) {
		
		try {
		
			quotation.setId(id);
			quotation = quotationService.edit(quotation);			
			fileService.generatePdf(quotation.getId());
			mailService.sendMailNew(quotation);
			return new ResponseEntity<>(quotation, HttpStatus.OK);
		} catch (PdfGenerationException e) {

			Map<String, Object> map = pdfErrorHandling(quotation, e);
			return new ResponseEntity<>(map, HttpStatus.ACCEPTED);
		} catch (NoSuchElementException e) {

			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (MessagingException | GeneralSecurityException | IOException e) {

			Map<String, Object> map = mailErrorHandling(quotation, e);
			return new ResponseEntity<>(map, HttpStatus.ACCEPTED);
			
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
			if (quotation.getStatus().getStatus().equals(APROVADO)) {
				fileService.generatePdf(quotation.getId());
				mailService.sendMailUpdate(quotation);
			}
			return new ResponseEntity<>(quotation, HttpStatus.OK);
		} catch (PdfGenerationException e) {

			Map<String, Object> map = pdfErrorHandling(quotation, e);
			return new ResponseEntity<>(map, HttpStatus.ACCEPTED);
		} catch (NoSuchElementException e) {

			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (MessagingException | GeneralSecurityException | IOException e) {

			Map<String, Object> map = mailErrorHandling(quotation, e);
			return new ResponseEntity<>(map, HttpStatus.ACCEPTED);
		} catch (RuntimeException e) {

			logger.error("Error on patching quotation: " + e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}		
	}

	private Map<String, Object> pdfErrorHandling(Quotation quotation, Throwable e){
		logger.error("Error creating pdf: " + e);
		Map<String, Object> map = new HashMap<>();
		map.put("quotation", quotation);
		map.put("warning", "Erro ao criar pdf!");
		return map;
	}

	private Map<String, Object> mailErrorHandling(Quotation quotation, Throwable e){
		logger.warn("Error sendig email: " + e);
		Map<String, Object> map = new HashMap<>();
		map.put("quotation", quotation);
		map.put("warning", "Erro ao enviar e-mail!");
		return map;
	}
	
	@DeleteMapping(value = "quotations/{id}")
	public ResponseEntity<Quotation> deleteQuotation(@PathVariable("id") Integer id){
		
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
