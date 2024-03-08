package br.com.rinhabackend.exception;

public class BadRequestException extends RuntimeException {
    private static final long serialVersionUID = -913821645324323489L;
    public static final String MSG_ERRO_REQUISICAO_INVALIDA = "Requisição inválida";

    public BadRequestException() {
        super(MSG_ERRO_REQUISICAO_INVALIDA);
    }

    public BadRequestException(String mensagem) {
        super(mensagem);
    }
}
