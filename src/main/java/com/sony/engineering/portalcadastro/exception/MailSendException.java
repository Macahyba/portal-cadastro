package com.sony.engineering.portalcadastro.exception;

import java.util.Map;

public class MailSendException extends Exception {

    private static final long serialVersionUID = 1L;

    private Map errorMap;

    public MailSendException(Map errorMap) {
        this.errorMap = errorMap;
    }

    public MailSendException(Throwable e) {
        super(e);
    }

    public MailSendException(String message, Throwable cause) {
        super(message, cause);
    }

    public Map getErrorMap() {
        return errorMap;
    }
}
