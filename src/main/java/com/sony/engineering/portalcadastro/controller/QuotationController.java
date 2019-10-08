package com.sony.engineering.portalcadastro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.sony.engineering.portalcadastro.model.Quotation;

@Controller
public class QuotationController {

	@GetMapping(value = "inserir")
	public String inserirGet(Model model) {
		
		return "quotation/inserir";
	}
	
	@PostMapping(value = "inserir")
	public String inserirPost(Model model, @ModelAttribute Quotation quotation) {
		
		return null;
	}
	
}
