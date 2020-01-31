package com.sony.engineering.portalcadastro.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.sony.engineering.portalcadastro.model.User;
import com.sony.engineering.portalcadastro.service.UserService;

@Controller
public class LoginController {

	@Autowired
	public LoginController(UserService userService) {
		this.userService = userService;
	}

	private UserService userService;
	
	@GetMapping(value = {"/", "login"})
	public String loginGet() {
		return "quotation/login";
	}
	
	@PostMapping(value = "login")
	public String loginPost(Model model, @ModelAttribute("user") User user, HttpSession session) {
		
		User dbUser = userService.validateLogin(user);
		
		if(dbUser == null) {
			
			model.addAttribute("error", "Usuário ou senha incorretos!");
			return "quotation/login";			
		}
		
		session.setAttribute("user", dbUser);
		return "redirect:/home";
	}	
	
	@GetMapping(value = "home")
	public String home() {
		return "/quotation/home";
	}		
	
	@GetMapping(value = "logout")
	public String logout() {
		
		return "redirect:/";
	}
}
