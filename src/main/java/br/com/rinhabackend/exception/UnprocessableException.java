package br.com.rinhabackend.exception;

public class UnprocessableException extends RuntimeException {
    private static final long serialVersionUID = 5952619936696721052L;

    public UnprocessableException() {

    }

    public UnprocessableException(String mensagem) {
        super(mensagem);
    }
}
