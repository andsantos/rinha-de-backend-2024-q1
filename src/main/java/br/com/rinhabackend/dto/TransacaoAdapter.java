package br.com.rinhabackend.dto;

public class TransacaoAdapter {
    private Integer valor;

    private String tipo;

    private String descricao;

    private Integer saldo;

    private Integer clienteId;

    private Integer limite;

    public TransacaoAdapter(TransacaoRequestDTO request) {
        this.valor = request.getValor();
        this.tipo = request.getTipo();
        this.descricao = request.getDescricao();
    }

    public Integer getSaldo() {
        return saldo;
    }

    public void setSaldo(Integer saldo) {
        this.saldo = saldo;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public Integer getLimite() {
        return limite;
    }

    public void setLimite(Integer limite) {
        this.limite = limite;
    }

    public Integer getValor() {
        return valor;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescricao() {
        return descricao;
    }
}
