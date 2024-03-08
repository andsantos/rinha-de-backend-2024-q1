package br.com.rinhabackend.exception;

public class ObjectAlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = -2775381149424453483L;

    public ObjectAlreadyExistsException(String mensagem) {
        super(mensagem);
    }
}
