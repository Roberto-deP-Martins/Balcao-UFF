package br.uff.balcao_uff.commons.util.response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

public class ResponseFormatter {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Método para criar uma resposta padrão.
     * @param status Código de status HTTP.
     * @param message Mensagem personalizada.
     * @return Map com a resposta formatada.
     */
    public static Map<String, Object> createResponse(HttpStatus status, String message) {
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("timestamp", LocalDateTime.now().format(FORMATTER));
        responseBody.put("status", status.value());
        responseBody.put("message", message);
        return responseBody;
    }

    /**
     * Método para criar uma resposta padrão de sucesso.
     * @param message Mensagem personalizada.
     * @return Map com a resposta formatada para sucesso.
     */
    public static Map<String, Object> createSuccessResponse(String message) {
        return createResponse(HttpStatus.OK, message);
    }

    /**
     * Método para criar uma resposta padrão de erro.
     * @param message Mensagem personalizada.
     * @return Map com a resposta formatada para erro.
     */
    public static Map<String, Object> createErrorResponse(String message) {
        return createResponse(HttpStatus.BAD_REQUEST, message);
    }

}
