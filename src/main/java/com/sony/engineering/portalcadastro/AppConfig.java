//package com.sony.engineering.portalcadastro;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.thymeleaf.spring5.SpringTemplateEngine;
//import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
//import org.thymeleaf.spring5.view.ThymeleafViewResolver;
//
//@Configuration
//public class AppConfig implements WebMvcConfigurer{
//	
//    @Bean(name ="templateResolver")	
//    public SpringResourceTemplateResolver thymeleafTemplateResolver() {
//        SpringResourceTemplateResolver templateResolver 
//          = new SpringResourceTemplateResolver();
//        templateResolver.setPrefix("/WEB-INF/views/");
//        templateResolver.setSuffix(".html");
//        templateResolver.setTemplateMode("HTML5");
//        return templateResolver;
//    }
//    @Bean(name ="templateEngine")	    
//    public SpringTemplateEngine getTemplateEngine() {
//    	SpringTemplateEngine templateEngine = new SpringTemplateEngine();
//    	templateEngine.setTemplateResolver(thymeleafTemplateResolver());
//	return templateEngine;
//    }
//    @Bean(name="viewResolver")
//    public ThymeleafViewResolver getViewResolver(){
//    	ThymeleafViewResolver viewResolver = new ThymeleafViewResolver(); 
//    	viewResolver.setTemplateEngine(getTemplateEngine());
//	return viewResolver;
//    }	
//}
