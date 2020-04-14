package com.sony.engineering.portalcadastro.service;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;


import javax.servlet.http.HttpServletRequest;

import com.openhtmltopdf.util.XRLog;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;


import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.sony.engineering.portalcadastro.config.FileStorageProperties;
import com.sony.engineering.portalcadastro.exception.PdfGenerationException;
import com.sony.engineering.portalcadastro.model.Quotation;
import com.sony.engineering.portalcadastro.model.Service;

@org.springframework.stereotype.Service
public class FileServiceImpl implements FileService{

	private QuotationService quotationService;
	private RepairService repairService;
	private	Path fileStorageLocation;
    private HttpServletRequest request;	

	private static final String PDF = ".pdf";
    private static final String CSV = ".csv";

	@Autowired
    public FileServiceImpl(FileStorageProperties fileStorageProperties,
                           QuotationService quotationService,
                           RepairService repairService,
                           HttpServletRequest request) throws PdfGenerationException   {
        this.quotationService = quotationService;
        this.repairService = repairService;
        this.request = request;
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
        	
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
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

        XRLog.setLoggingEnabled(false);
    	Quotation quotation = quotationService.findById(id);

    	if(quotation.getApprovalUser() == null){
    	    throw new PdfGenerationException("Approval User not found!", new Throwable());
        }

        String serverPath = request.getScheme() + "://localhost:" +
    						request.getLocalPort() + request.getContextPath();

    	String dataAtual = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    										.format(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
    	
    	Float valorDesconto = quotation.getTotalPrice()* (quotation.getTotalDiscount()/100);
    	
    	int numOrdem = 1;

        StringBuilder src = new StringBuilder();

        src.append("<!DOCTYPE html>")
            .append("<html lang=\"pt-br\">")
            .append("   <head>")
            .append("       <meta charset=\"utf-8\"></meta>")
            .append("       <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"></meta>")
            .append("       <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"></meta>")
            .append("       <title>Report</title>")
            .append("       <link rel=\"stylesheet\" type=\"text/css\" href=\"")
            .append(serverPath).append("/css/pdf.css\"></link>")
            .append("   </head>")
            .append("   <body>")
            .append("<div class=\"container-fluid small \">")
            .append("           <div class=\"row mb-3\">")
            .append("               <div class=\"col\">")
            .append("                   <img src=\"").append(serverPath).append("/images/logo.png\"></img>")
            .append("                   <h6 class=\"font-weight-bold\">Sony Brasil Ltda.</h6>")
            .append("                   <ul class=\"list-unstyled\">")
            .append("                       <li>Rua Werner Siemens, 111 | Condomínio e-Business Park | Prédio 1</li>")
            .append("                       <li>CEP: 05069-010 | Lapa | São Paulo | SP | Brasil</li>")
            .append("                       <li>Tel.: (11) 2196-9000 - Fax.: 2196-9186</li>")
            .append("                       <li>CNPJ: 43.447.044/0004-10</li>")
            .append("                   </ul>")
            .append("                   <ul class=\"list-unstyled mb-1\">")
            .append("                       <li class=\"font-weight-bold font-italic\">").append(quotation.getCustomer().getFullName()).append("</li>")
            .append("                       <li class=\"font-weight-bold font-italic\">").append(quotation.getContact().getDepartment()).append("</li>")
            .append("                   </ul>")
            .append("               </div>")
            .append("           </div>")
            .append("           <div class=\"row font-weight-bold font-italic\">")
            .append("                   Data: ").append(dataAtual)
            .append("           </div>")
            .append("           <div class=\"row font-weight-bold font-italic\">")
            .append("                   Cotação: ").append(quotation.getLabel()).append(" - ").append(quotation.getEquipment().getName())
                .append(", Número de Série ").append(quotation.getEquipment().getSerialNumber())
            .append("           </div>")
            .append("           <div class=\"row\">")
            .append("               <div class=\"col\">")
            .append("                   <table class=\"main-table text-center\">")
            .append("                       <thead>")
            .append("                           <tr>")
            .append("                               <th>ITEM</th>")
            .append("                               <th>MODELO</th>")
            .append("                               <th>DESCRIÇÃO</th>")
            .append("                               <th>VALOR UNITÁTIO</th>")
            .append("                               <th>DESCONTO</th>")
            .append("                               <th>V. LÍQUIDO</th>")
            .append("                           </tr>")
            .append("                       </thead>")
            .append("                       <tbody>")
            .append("                           <tr>")
            .append("                               <td class=\"servico\" colspan=\"6\">SERVIÇO - VALOR NACIONAL - R$</td>")
            .append("                           </tr>");

    		for (Service service : quotation.getServices()) {

                src.append("                           <tr>")
                    .append("                               <td>").append(numOrdem).append("</td>")
                    .append("                               <td>").append(quotation.getEquipment().getName()).append("</td>")
                    .append("                               <td>").append(service.getDescription()).append("</td>")
                    .append("                               <td>").append(String.format("%.2f", service.getPrice())).append("</td>");
                    if (quotation.getTotalDiscount() == 0){
                        src.append("                               <td>-</td>");
                    } else {
                        src.append("                               <td>").append(String.format("%.2f", quotation.getTotalDiscount())).append("%</td>");
                    }
                    src.append("                               <td>").append(String.format("%.2f", this.getValorLiquido(service.getPrice(), quotation.getTotalDiscount()))).append("</td>")
                    .append("                           </tr>");
                numOrdem++;
    		}

            src.append("                       </tbody>")
                .append("                       <tfoot>")
                .append("                           <tr class=\"resumo font-weight-bold\">")
                .append("                               <td></td>")
                .append("                               <td></td>")
                .append("                               <td>Valor total R$</td>")
                .append("                               <td>").append(String.format("%.2f", quotation.getTotalPrice())).append("</td>");
                if (valorDesconto == 0){
                    src.append("                               <td>-</td>");
                } else {
                    src.append("                               <td>").append(String.format("%.2f", valorDesconto)).append("</td>");
                }
                src.append("                               <td>").append(String.format("%.2f", this.getValorLiquido(quotation.getTotalPrice(), quotation.getTotalDiscount()))).append("</td>")
                .append("                           </tr>")
                .append("                       </tfoot>")
                .append("                   </table>")
                .append("               </div>")
                .append("           </div>")
                .append("           <div class=\"row h-25\">")
                .append("           	<div class=\"col\">")
                .append("               	<div class=\"left\">")
                .append("                   	<ul class=\"list-unstyled\">")
                .append("                       	<li class=\"font-weight-bold\">Condições Gerais:</li>")
                .append("                       	<li>Validade da proposta: 30 dias.</li>")
                .append("                       	<li>Pagamento: 28 ddl</li>")
                .append("                       	<li>Preço com frete CIF incluso</li>")
                .append("                       	<li>Todos os impostos inclusos</li>")
                .append("                       	<li>Prazo de Entrega: 10 dias apos a chegada das peças</li>")
                .append("                       	<li style=\"color: red;\">Obs: Este orçamento se refere apenas ao serviço de mão de obra, não estando incluidas peças ou quaisquer outras partes necessárias para o reparo.</li>")
                .append("                       	<li></li>")
                .append("                       	<br></br>")
                .append("                       	<li>Atenciosamente,</li>")
                .append("                   	</ul>")
                .append("               	</div>")
                .append("               </div>")
                .append("           	<div class=\"col\">")
                .append("               	<div class=\"right font-weight-bold text-primary\">")
                .append("                   	<ul class=\"list-unstyled\">")
                .append("                       	<li>Valor Total Nacional - R$ ").append(String.format("%.2f", quotation.getTotalPrice())).append("</li>");
                if (valorDesconto != 0){
                    src.append("                       	<li>Desconto Especial - R$ ").append(String.format("%.2f", valorDesconto)).append("</li>");
                }
                src.append("                       	<li>Valor Total Liquido - R$ ").append(String.format("%.2f", this.getValorLiquido(quotation.getTotalPrice(), quotation.getTotalDiscount()))).append("</li>")
                .append("                   	</ul>")
                .append("               	</div>")
                .append("               </div>")
                .append("           </div>")
                .append("           <div class=\"row low\">")
                .append("               <div class=\"col\">")
                .append("	           	    <div class=\"left\">")
                .append("   	           	    <ul class=\"list-unstyled\">")
                .append("       	                <li>_____________________________________________________________</li>")
                .append("                           <li>").append(quotation.getApprovalUser().getFullName()).append("</li>")
                .append("              	            <li>Professional Solutions Brasil | ").append(quotation.getApprovalUser().getRole()).append("</li>")
                .append("                  	        <li>Ph. ").append(quotation.getApprovalUser().getPhone()).append("</li>")
                .append("              		    </ul>")
                .append("              	    </div>")
                .append("               </div>")
                .append("          	    <div class=\"col\">")
                .append("              	    <div class=\"right\">")
                .append("                  	    <ul class=\"list-unstyled\">")
                .append("                     	    <li>_____________________________________________________________</li>")
                .append("                      	    <li>").append(quotation.getContact().getName()).append("</li>")
                .append("                      	    <li>").append(quotation.getContact().getDepartment()).append("</li>")
                .append("                      	    <li>Data: ").append(dataAtual).append("</li>")
                .append("                  	    </ul>")
                .append("              	    </div>")
                .append("               </div>")
                .append("           </div>")
                .append("       </div>")
                .append("   </body>")
                .append("</html>");

    	String dest = this.fileStorageLocation.resolve(quotation.getLabel() + PDF).toString();

        try {
        	OutputStream os = new FileOutputStream(dest);
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(src.toString(), "/");
            builder.toStream(os);
            builder.run();
            return quotation.getLabel() + PDF;
        } catch (Exception e) {
        	throw new PdfGenerationException(e);
        }
    	
    }

    private Float getValorLiquido(Float valor, Float discount){
        return valor * (1 - discount/100);
    }

    @Override
    public String generateQuotationsCsv() throws IOException {

        List<Quotation> quotations = quotationService.findAll();

        String[] HEADERS = { "Id", "Nome", "Equipamento", "Serial Number",
                            "Valor", "Desconto", "Status", "Data de Criacao"};


        String dest = this.fileStorageLocation.resolve("quotations" + CSV).toString();

        FileWriter out = new FileWriter(dest);
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT
                .withHeader(HEADERS))) {
            quotations.forEach((q) -> {
                try {
                    printer.printRecord(q.getLabel(), q.getCustomer().getName(), q.getEquipment().getName(),
                                            q.getEquipment().getSerialNumber(), q.getTotalPrice(), q.getTotalDiscount(),
                                            q.getStatus().getStatus(), q.getCreationDate());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        return "quotations" + CSV;
    }

    @Override
    public String generateRepairsCsv() throws IOException {

//        List<Repair> repairs = repairService.findAll();
//
//        String[] HEADERS = { "Notificacao SAP", "Nome", "Equipamento", "Serial Number",
//                "Valor", "Desconto", "Status", "Data de Criacao"};
//
//        FileWriter out = new FileWriter("test.csv");
//        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT
//                .withHeader(HEADERS))) {
//            quotations.forEach((q) -> {
//                try {
//                    printer.printRecord(q.getLabel(), q.getCustomer().getName(), q.getEquipment().getName(),
//                            q.getEquipment().getSerialNumber(), q.getTotalPrice(), q.getTotalDiscount(),
//                            q.getStatus().getStatus(), q.getCreationDate());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });
//        }

        return null;
    }
}
