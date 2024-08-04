package br.com.rinhabackend.exception;

public class NotFoundException extends RuntimeException {
    private static final long serialVersionUID = 3031796252071493431L;

    public NotFoundException(String mensagem) {
        super(mensagem);
    }
}
