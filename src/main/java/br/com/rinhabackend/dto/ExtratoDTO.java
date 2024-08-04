package br.com.rinhabackend.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExtratoDTO {
    private SaldoDTO saldo;

    @JsonProperty(value = "ultimas_transacoes")
    private List<TransacaoExtratoDTO> transacoes;

    public ExtratoDTO(SaldoDTO saldo, List<TransacaoExtratoDTO> transacoes) {
        this.saldo = saldo;
        this.transacoes = transacoes;
    }

    public SaldoDTO getSaldo() {
        return saldo;
    }

    public void setSaldo(SaldoDTO saldo) {
        this.saldo = saldo;
    }

    public List<TransacaoExtratoDTO> getTransacoes() {
        return transacoes;
    }

    public void setTransacoes(List<TransacaoExtratoDTO> transacoes) {
        this.transacoes = transacoes;
    }
}
