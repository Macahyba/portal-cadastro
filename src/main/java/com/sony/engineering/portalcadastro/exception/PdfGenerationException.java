package com.sony.engineering.portalcadastro.exception;

public class PdfGenerationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PdfGenerationException() {
		
	}
	
	public PdfGenerationException(Throwable e) {
		super(e);
	}
	
    public PdfGenerationException(String message, Throwable cause) {
        super(message, cause);
    }	
	
}
