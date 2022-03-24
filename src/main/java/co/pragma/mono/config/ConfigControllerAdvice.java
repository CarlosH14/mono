package co.pragma.mono.config;

import java.util.ArrayList;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ConfigControllerAdvice extends ResponseEntityExceptionHandler{

    // --------------------------- Excepción Global ---------------------------------
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        ErrorModel apiError = new ErrorModel(
        HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), ex.getMessage());
        return new ResponseEntity<Object>(
        apiError, new HttpHeaders(), apiError.getStatus());
    }
    // ------------------------------------------------------------------------------
    // --------------------------- Parametro Faltante -------------------------------
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
    MissingServletRequestParameterException ex, HttpHeaders headers, 
    HttpStatus status, WebRequest request) {
        String errorName = "Parameter " + "'" + ex.getParameterName() + "'" + " is missing";
        String errorType = "A " + ex.getParameterType() + " must be provided";
        ArrayList<String> error = new ArrayList<String>();
        error.add(errorName); error.add(errorType); 	 
        ErrorModel apiError = 
        new ErrorModel(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(
        apiError, new HttpHeaders(), apiError.getStatus());
    }
    // ------------------------------------------------------------------------------
    // --------------------------- Excepción Global ---------------------------------

}
