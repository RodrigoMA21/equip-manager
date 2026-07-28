package br.com.projetofinal_grupo1.projetofinal_grupo1.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}