package com.suchorski.messageboards.api.controlleradvice;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Pattern;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;

import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class Handlers {

    @ExceptionHandler(ConstraintViolationException.class)
    public void handleDataIntegrityViolationException(ConstraintViolationException exception,
            ServletWebRequest webRequest) throws IOException {
        final var regex = "CONSTRAINT\\s+`([^`]+)`\\s+FOREIGN\\s+KEY\\s+\\(`[^`]+`\\)\\s+REFERENCES\\s+`[^`]+`\\s+\\(`[^`]+`\\)";
        final var pattern = Pattern.compile(regex);
        final var matcher = pattern.matcher(exception.getSQLException().getMessage());
        final var constraintName = Optional
                .ofNullable(exception.getConstraintName())
                .orElse(matcher.find() ? matcher.group(1) : "unknown");
        getResponse(webRequest).sendError(HttpStatus.CONFLICT.value(),
                CONSTRAINTS.message(constraintName.toLowerCase()));
    }

    private HttpServletResponse getResponse(ServletWebRequest webRequest) throws IOException {
        if (webRequest.getResponse() == null) {
            throw new IOException();
        }
        return webRequest.getResponse();
    }

    public class CONSTRAINTS {

        public static String message(String constraint) {
            switch (constraint) {
                case "uq_allocation":
                    return "Alocação do usuário já existe. Favor realizar a alteração.";
                case "fk_message_board":
                    return "Não é possível excluir quadros de avisos com mensagens.";
                default:
                    return "Erro desconhecido. Favor contatar o suporte.";
            }
        }
    }

}
