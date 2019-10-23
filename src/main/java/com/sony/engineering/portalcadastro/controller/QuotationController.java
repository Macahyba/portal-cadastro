package com.sony.engineering.portalcadastro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sony.engineering.portalcadastro.model.Quotation;
import com.sony.engineering.portalcadastro.service.CustomerService;
import com.sony.engineering.portalcadastro.service.EquipmentService;
import com.sony.engineering.portalcadastro.service.QuotationService;
import com.sony.engineering.portalcadastro.service.ServiceService;

@Controller
public class QuotationController {

	@Autowired
	EquipmentService equipmentService;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	ServiceService serviceService;
	
	@Autowired
	QuotationService quotationService;
	
	@GetMapping(value = "inserir")
	public String inserirGet(Model model) {
//		
//		model.addAttribute("equipments", equipmentService.getAttrList("name"));
//		model.addAttribute("customers", customerService.getAttrList("name"));
//		model.addAttribute("services", serviceService.getAttrList("name"));
//		
		return "quotation/inserir";
	}
	
	@PostMapping(value = "inserir")
	public String inserirPost(Model model, @ModelAttribute Quotation quotation) {
		
		return null;
	}
	
	@GetMapping(value = "quotations")
	@ResponseBody
	public List<Quotation> getAll(){
	
		return quotationService.findAll();
	}
	
	@PostMapping(value = "quotations")
	@ResponseBody
	public Quotation setQuotation(@RequestBody Quotation quotation) {
		return quotationService.save(quotation);
	}
	
}
