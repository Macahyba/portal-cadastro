package com.sony.engineering.portalcadastro.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.*;
import javax.servlet.http.HttpServletRequest;

import com.sony.engineering.portalcadastro.exception.MailSendException;
import com.sony.engineering.portalcadastro.model.JwtUserDetails;
import com.sony.engineering.portalcadastro.auth.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import com.sony.engineering.portalcadastro.config.FileStorageProperties;
import com.sony.engineering.portalcadastro.config.MailProperties;
import com.sony.engineering.portalcadastro.model.Quotation;
import com.sony.engineering.portalcadastro.model.User;

@Service
public class MailServiceImpl implements MailService {
	
	private String username;
	private static String tokenDir;
	private static String credentials;
	private ResourceLoader resourceLoader;

	private JwtUserDetailsService userService;
	private FileStorageProperties fsp;
	private HttpServletRequest request;
	private JwtUserDetails authUser;

	@Autowired
	public MailServiceImpl(MailProperties mailProperties,
						   JwtUserDetailsService userService,
						   FileStorageProperties fsp,
						   HttpServletRequest request,
						   JwtUserDetails authUser,
						   ResourceLoader resourceLoader) {
		this.userService = userService;
		this.fsp = fsp;
		this.request = request;
		this.authUser = authUser;
		this.username = mailProperties.getUsername();
		tokenDir = mailProperties.getTokenDir();
		credentials = mailProperties.getCredentials();
		this.resourceLoader = resourceLoader;
	}

	private static final String APPLICATION_NAME = "portalOrcamento";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    
    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_COMPOSE);

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {

		final java.util.logging.Logger buggyLogger = java.util.logging.Logger.getLogger(FileDataStoreFactory.class.getName());
		buggyLogger.setLevel(java.util.logging.Level.SEVERE);

        // Load client secrets.
        Resource banner = resourceLoader.getResource(credentials);
		InputStream in = banner.getInputStream();
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + credentials);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(tokenDir)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    private void sendMailNewUnfiltered(Quotation quotation) throws IOException, GeneralSecurityException, MessagingException {

        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        List<JwtUserDetails> users = userService.findDistinctByProfileNot("user");
        
        String subject = "Novo orçamento em Portal Orçamento ✔";
        String bodyText = "Aviso.<br>Novo orcamento <b>" + quotation.getLabel()+ "</b> em http://" +
									request.getServerName() +".";
        List<String> to = new ArrayList<>();
        
        users.forEach(dest -> to.add(dest.getUser().getEmail()));
        to.add(username);
        MimeMessage mail = createEmail(to, username, subject, bodyText);
        sendMessage(service, username, mail);

    }

	@Override
	public void sendMailNew(Quotation quotation) throws MailSendException {

		try {
			sendMailNewUnfiltered(quotation);
		} catch (MessagingException | GeneralSecurityException | IOException e) {
			throw new MailSendException(e);
		}
	}

	private void sendMailUpdateUnfiltered(Quotation quotation) throws IOException, GeneralSecurityException, MessagingException  {

        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        File file = new File(fsp.getDir() + "/" + quotation.getLabel() + ".pdf");

        User userAprov = quotation.getApprovalUser();
        User user = quotation.getUser();
        
        String subject = "Orcamento aprovado em Portal Orçamento ✔";
        String bodyText = "Aviso.<br>Orcamento <b>" + quotation.getLabel()+ "</b> aprovado em http://" +
								request.getServerName() +".";
        List<String> to = new ArrayList<>();

		to.add(userAprov.getEmail());
		to.add(user.getEmail());
        to.add(username);
        MimeMessage mail = createEmailWithAttachment(to, username, subject, bodyText, file);
        sendMessage(service, username, mail);

	}

	@Override
	public void sendMailUpdate(Quotation quotation) throws MailSendException {

		try {
			sendMailUpdateUnfiltered(quotation);
		} catch (MessagingException | GeneralSecurityException | IOException e) {
			throw new MailSendException(e);
		}
	}

	private void sendMailResetUnfiltered(JwtUserDetails user) throws IOException, GeneralSecurityException, AddressException, MessagingException {
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
				.setApplicationName(APPLICATION_NAME)
				.build();

		String subject = "Reset de senha ✔";
		String bodyText = "Aviso.<br>Sua senha foi resetada para <b>" + user.getPassword() + "</b>" +
				"<br>Favor alterar após o login";
		List<String> to = new ArrayList<>();

		to.add(user.getUser().getEmail());
		MimeMessage mail = createEmail(to, username, subject, bodyText);
		sendMessage(service, username, mail);
	}

	@Override
	public void sendMailReset(JwtUserDetails user) throws MailSendException {

    	try {
    		sendMailResetUnfiltered(user);
		} catch (MessagingException | GeneralSecurityException | IOException e) {
    		throw new MailSendException(e);
		}
	}

	private static MimeMessage prepareEmail(List<String> to,
								            String from,
								            String subject)
					            		throws MessagingException {

		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		MimeMessage email = new MimeMessage(session);

		email.setFrom(new InternetAddress(from));

		for (String dest : to) {
			email.addRecipient(javax.mail.Message.RecipientType.TO,
					new InternetAddress(dest));
		}

		email.setSubject(subject);
		
		return email;

	}


    private static MimeMessage createEmail(List<String> to,
						            String from,
						            String subject,
						            String bodyText)
            		throws MessagingException {

		if (from == null ) {
			throw new MessagingException("'from' field is undefined.");
		}

		MimeMessage email = prepareEmail(to, from, subject);
		
		MimeBodyPart mimeBodyPart = new MimeBodyPart();
		mimeBodyPart.setContent(bodyText, "text/html");
		
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(mimeBodyPart);
		email.setContent(multipart);
		
		return email;
	}	
        
    private static MimeMessage createEmailWithAttachment(List<String> to,
											            String from,
											            String subject,
											            String bodyText,
											            File file)
							            			throws MessagingException {

		if (from == null ) {
			throw new MessagingException("'from' field is undefined.");
		}

		MimeMessage email = prepareEmail(to, from, subject);    	
		
		MimeBodyPart mimeBodyPart = new MimeBodyPart();
		mimeBodyPart.setContent(bodyText, "text/html");
		
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(mimeBodyPart);
		
		mimeBodyPart = new MimeBodyPart();
		DataSource source = new FileDataSource(file);
		
		mimeBodyPart.setDataHandler(new DataHandler(source));
		mimeBodyPart.setFileName(file.getName());
		
		multipart.addBodyPart(mimeBodyPart);
		email.setContent(multipart);
		
		return email;
    }    
  
    private static Message createMessageWithEmail(MimeMessage emailContent)
            throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        Message message = new com.google.api.services.gmail.model.Message();
        message.setRaw(encodedEmail);
        return message;
    }   
    
    private static void sendMessage(Gmail service,
	            String userId,
	            MimeMessage emailContent)
					throws MessagingException, IOException {
		Message message = createMessageWithEmail(emailContent);
		service.users().messages().send(userId, message).execute();

	}    

}
