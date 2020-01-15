package com.sony.engineering.portalcadastro.service;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;


import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.sony.engineering.portalcadastro.FileStorageProperties;
import com.sony.engineering.portalcadastro.exception.PdfGenerationException;
import com.sony.engineering.portalcadastro.model.Quotation;
import com.sony.engineering.portalcadastro.model.Service;

@org.springframework.stereotype.Service
public class FileServiceImpl implements FileService{

	@Autowired
	private QuotationService quotationService;
	
	private	Path fileStorageLocation;
	
    @Autowired
    private HttpServletRequest request;	

	private static final String EXTENSION = ".pdf";
	
    @Autowired
    public FileServiceImpl(FileStorageProperties fileStorageProperties) throws PdfGenerationException  {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new PdfGenerationException("Could not create the directory where the uploaded files will be stored.", ex);
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
    
    
    public String generatePdf(Integer id) throws PdfGenerationException {
    	
    	Quotation quotation = quotationService.findById(id);
    	
    	String serverPath = request.getScheme() + "://" + request.getLocalName() + ":" + 
    						request.getLocalPort() + request.getContextPath();
    	
    	String dataAtual = DateTimeFormatter.ofPattern("DD/MM/YYYY")
    										.format(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
    	
    	Float valorDesconto = quotation.getTotalPrice()* (quotation.getTotalDiscount()/100);
    	
    	Float valorLiquido = quotation.getTotalPrice() * (1 - quotation.getTotalDiscount()/100);
    	
    	Integer numOrdem = 1;
    	
    	String src = "<!DOCTYPE html>" +
        "<html lang=\"pt-br\">"+
        "   <head>"+
        "       <meta charset=\"utf-8\"></meta>" +
        "       <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"></meta>" +
        "       <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"></meta>" +
        "       <title>Report</title>" +
        "       <link rel=\"stylesheet\" type=\"text/css\" href=\"" + 
        serverPath + "/css/pdf.css\"></link>" +        
        "   </head>" +
        "   <body>" +

		"<div class=\"container-fluid small \">" +
            "           <div class=\"row mb-3\">"+
            "               <div class=\"col\">"+
            "                   <img src=\"" + serverPath + "/img/logo.png\"></img>"+
            "                   <h6 class=\"font-weight-bold\">Sony Brasil Ltda.</h6>"+
            "                   <ul class=\"list-unstyled\">"+
            "                       <li>Rua Werner Siemens, 111 | Condomínio e-Business Park | Prédio 1</li>"+
            "                       <li>CEP: 05069-010 | Lapa | São Paulo | SP | Brasil</li>"+
            "                       <li>Tel.: (11) 2196-9000 - Fax.: 2196-9186</li>"+
            "                   </ul>"+
            "                   <ul class=\"list-unstyled mb-1\">"+
            "                       <li class=\"font-weight-bold font-italic\">" + quotation.getCustomer().getFullName() + "</li>"+
            "                       <li class=\"font-weight-bold font-italic\">" + quotation.getContact().getDepartment() + "</li>"+
            "                   </ul>"+
            "               </div>"+
            "           </div>"+
            "           <div class=\"row font-weight-bold font-italic\">"+
            "                   Data: " + dataAtual +
            "           </div>"+
            "           <div class=\"row font-weight-bold font-italic\">"+
            "                   Cotação: " + quotation.getLabel()+
            "           </div>"+            
            "           <div class=\"row\">"+
            "               <div class=\"col\">"+
            "                   <table class=\"main-table text-center\">"+
            "                       <thead>"+
            "                           <tr>"+
            "                               <th>ITEM</th>"+
            "                               <th>MODELO</th>"+
            "                               <th>DESCRIÇÃO</th>"+
            "                               <th>VALOR UNITÁTIO</th>"+
            "                               <th>DESCONTO</th>"+
            "                               <th>V. LÍQUIDO</th>"+
            "                           </tr>"+
            "                       </thead>"+
            "                       <tbody>"+
            "                           <tr>"+
            "                               <td class=\"servico\" colspan=\"6\">SERVIÇO - VALOR NACIONAL - R$</td>"+
            "                           </tr>";
    	
    		for (Service service : quotation.getServices()) {
		    
                src +=
                "                           <tr>"+      
                "                               <td>" + numOrdem.toString() + "</td>"+     	
                "                               <td>" + service.getName() + "</td>"+     	
                "                               <td>" + service.getDescription() + "</td>"+     	
                "                               <td>" + String.format("%.2f", service.getPrice()) + "</td>"+     	  	
                "                               <td>" + String.format("%.2f", quotation.getTotalDiscount()) + "%</td>"+     	
                "                               <td>" + String.format("%.2f", valorLiquido)  + "</td>"+     	
                "                           </tr>";
                numOrdem++;
    		}
   				
            src +=    				
            "                       </tbody>"+
            "                       <tfoot>"+
            "                           <tr class=\"resumo font-weight-bold\">"+
            "                               <td></td>"+
            "                               <td></td>"+
            "                               <td>Valor total R$</td>"+
            "                               <td>" + String.format("%.2f", quotation.getTotalPrice()) + "</td>"+
            "                               <td>" + String.format("%.2f", valorDesconto) + "</td>"+
            "                               <td>" + String.format("%.2f", valorLiquido) + "</td>"+
            "                           </tr>"+
            "                       </tfoot>"+
            "                   </table>"+
            "               </div>"+
            "           </div>"+
            "           <div class=\"row h-25\">"+
            "           	<div class=\"col\">"+            
            "               	<div class=\"left\">"+
            "                   	<ul class=\"list-unstyled\">"+
            "                       	<li class=\"font-weight-bold\">Condições Gerais:</li>"+
            "                       	<li>Validade da proposta: 30 dias.</li>"+
            "                       	<li>Pagamento: 28 ddl</li>"+
            "                       	<li>Preço com frete CIF incluso</li>"+
            "                       	<li>Todos os impostos inclusos</li>"+
            "                       	<li>Prazo de Entrega: 10 dias apos a chegada das peças</li>"+
            "                       	<li></li>"+
            "                       	<li>Atenciosamente,</li>"+
            "                   	</ul>"+
            "               	</div>"+           
            "               </div>"+
            "           	<div class=\"col\">"+
            "               	<div class=\"right font-weight-bold text-primary\">"+
            "                   	<ul class=\"list-unstyled\">"+
            "                       	<li>Valor Total Nacional - R$ " + String.format("%.2f", quotation.getTotalPrice()) + "</li>"+
            "                       	<li>Desconto Especial - R$ " + String.format("%.2f", valorDesconto) + "</li>"+
            "                       	<li>Valor Total Liquido - R$ " + String.format("%.2f", valorLiquido) + "</li>"+
            "                   	</ul>"+
            "               	</div>"+
            "               </div>"+            
            "           </div>"+
            "           <div class=\"row\">"+
            "           	<div class=\"col\">"+            
            "	            	<div class=\"left\">"+
            "   	            	<ul class=\"list-unstyled\">"+
            "       	                <li>_____________________________________________________________</li>"+
            "           	            <li>" + quotation.getApprovalUser().getName() + "</li>"+
            "               	        <li>Professional Solutions Brasil | " + quotation.getApprovalUser().getRole() + "</li>"+
            "                   	    <li>Ph. " + quotation.getApprovalUser().getPhone() + "</li>"+
            "                  		 </ul>"+
            "               	</div>"+
            "               </div>"+
            "           	<div class=\"col\">"+            
            "               	<div class=\"right\">"+
            "                   	<ul class=\"list-unstyled\">"+
            "                       	<li>_____________________________________________________________</li>"+
            "                       	<li>" + quotation.getContact().getName() + "</li>"+
            "                       	<li>" + quotation.getContact().getDepartment() + "</li>"+
            "                       	<li>Data: " + dataAtual + "</li>"+
            "                   	</ul>"+
            "               	</div>"+
            "               </div>"+           
            "           </div>"+
            "       </div>"+
            "   </body>"+
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
        	throw new PdfGenerationException(e);
        }
    	
    }
}
