package br.com.projetofinal_grupo1.projetofinal_grupo1.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}