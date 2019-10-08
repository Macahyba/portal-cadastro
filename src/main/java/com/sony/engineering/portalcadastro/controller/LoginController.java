package com.sony.engineering.portalcadastro.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sony.engineering.portalcadastro.model.User;
import com.sony.engineering.portalcadastro.service.UserService;

@Controller
public class LoginController {

	@Autowired
	UserService userService;
	
	@RequestMapping(value = {"/", "login"}, method = RequestMethod.GET)
	public String loginGet(Model model) {
		return "quotation/login";
	}
	
	@PostMapping(value = "login")
	public String loginPost(Model model, @ModelAttribute("user") User user) {
		
		User dbUser = userService.validateLogin(user);
		
		if(dbUser == null) {
			
			model.addAttribute("error", "Usuário ou senha incorretos!");
			return "quotation/login";			
		}
		
		model.addAttribute("user", dbUser);
		return "quotation/home";
	}	
	
//	AJAX CONTROLLER	
//	@PostMapping(value = "login")
//	@ResponseBody
//	public User loginPost(Model model, @RequestBody User user) {
//		
//		User dbUser = userService.validateLogin(user);
//		
//		if(dbUser == null) {
//			
//			model.addAttribute("error", "Usuário ou senha incorretos!");
//			//return "quotation/login";
//			return user;
//		}
//		
//		model.addAttribute("user", dbUser);
//		return dbUser;
//	}		
	
	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public String logout(Model model) {
		
		return "redirect:/";
	}
}
