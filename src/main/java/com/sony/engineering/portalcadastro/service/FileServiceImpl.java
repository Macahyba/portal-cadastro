package com.sony.engineering.portalcadastro.service;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.sony.engineering.portalcadastro.FileStorageProperties;
import com.sony.engineering.portalcadastro.model.Quotation;

@Service
public class FileServiceImpl implements FileService{

	@Autowired
	private QuotationService quotationService;
	
	private	Path fileStorageLocation;

	private static final String EXTENSION = ".pdf";
	
    @Autowired
    public FileServiceImpl(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }	
	
    public Resource loadFileAsResource(String fileName) {
    	
        try {
        	
            Path filePath = this.fileStorageLocation.resolve(fileName + EXTENSION).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found!");
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File not found!", ex);
        }
    }	
    
    
    public String generatePdf(Integer id) throws Exception {
    	
    	Quotation quotation = quotationService.findById(id);
    	// HANDLE EXCEPTION
    	String src = "<html>\r\n" + 
    			"<head>\r\n" + 
    			"<title>Your Title Here</title>\r\n" + 
    			"</head>\r\n" + 
    			"<body>\r\n" + 
    			"<center><img src=\"clouds.jpg\" align=\"BOTTOM\" ></img> </center>\r\n" + 
    			"\r\n" + 
    			"<a href=\"http://somegreatsite.com\">Link Name</a>\r\n" + 
    			"is a link to another nifty site\r\n" + 
    			"<h1>" + quotation.getId() + "</h1>\r\n" + 
    			"<h2>" + quotation.getLabel() + "</h2>\r\n" + 
    			"Send me mail at <a href=\"mailto:support@yourcompany.com\">\r\n" + 
    			"support@yourcompany.com</a>.\r\n" + 
    			"<p> This is a new paragraph!</p>\r\n" + 
    			"<p> <b>This is a new paragraph!</b></p>\r\n" + 
    			" <b><i>This is a new sentence without a paragraph break, in bold italics.</i></b>\r\n" + 
    			"\r\n" + 
    			"</body>\r\n" + 
    			"</html>";
    	String dest = this.fileStorageLocation.resolve(quotation.getLabel() + EXTENSION).toString();
    	
        try {
        	OutputStream os = new FileOutputStream(dest);
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(src, "/");
            builder.toStream(os);
            builder.run();
            
            return quotation.getLabel();
        } catch (Exception e) {
        	//e.printStackTrace();
        }
    	
        return null;
    }
}
