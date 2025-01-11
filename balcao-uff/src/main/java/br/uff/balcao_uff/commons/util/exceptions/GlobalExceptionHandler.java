package br.uff.balcao_uff.commons.util.exceptions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String TIMESTAMP = "timestamp";
    private static final String STATUS = "status";
    private static final String ERROR = "error";
    private static final String MESSAGE = "message";

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private Map<String, Object> createResponseBody(HttpStatus status, String error, String message) {
    	Map<String, Object> body = new LinkedHashMap<>();
    	body.put(TIMESTAMP, LocalDateTime.now().format(FORMATTER));
    	body.put(STATUS, status.value());
    	body.put(ERROR, error);
    	body.put(MESSAGE, message);
    	return body;
    }
    
    
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Object> handleSecurityException(SecurityException ex) {
        Map<String, Object> body = createResponseBody(HttpStatus.FORBIDDEN, 
        												"Forbidden", 
        												ex.getMessage());
        
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> body = createResponseBody(HttpStatus.BAD_REQUEST, 
        												"Bad Request", 
        												ex.getMessage());
        
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SameUserException.class)
    public ResponseEntity<Object> handleSameUserException(SameUserException ex) {
        Map<String, Object> body = createResponseBody(HttpStatus.BAD_REQUEST, 
        												"Bad Request", 
        												ex.getMessage());
        
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(TransactionUpdateException.class)
    public ResponseEntity<Object> handleTransactionUpdateException(TransactionUpdateException ex) {
        Map<String, Object> body = createResponseBody(HttpStatus.INTERNAL_SERVER_ERROR, 
                                                      "Internal Server Error", 
                                                      ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(ConversaNotFoundException.class)
    public ResponseEntity<Object> handleConversaNotFoundException(ConversaNotFoundException ex) {
        Map<String, Object> body = createResponseBody(HttpStatus.NOT_FOUND, 
                                                      "Conversa Não Encontrada", 
                                                      ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsuarioNaoInteressadoException.class)
    public ResponseEntity<Object> handleUsuarioNaoInteressadoException(UsuarioNaoInteressadoException ex) {
        Map<String, Object> body = createResponseBody(HttpStatus.FORBIDDEN, 
                                                      "Acesso Negado", 
                                                      ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

}
