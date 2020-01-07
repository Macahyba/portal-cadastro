package com.sony.engineering.portalcadastro.service;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.sony.engineering.portalcadastro.model.Quotation;

public interface MailService {
	
	void sendMailNew(Quotation quotation) throws IOException, GeneralSecurityException, AddressException, MessagingException ;
	
	void sendMailUpdate(Quotation quotation) throws IOException, GeneralSecurityException, AddressException, MessagingException ;

}
