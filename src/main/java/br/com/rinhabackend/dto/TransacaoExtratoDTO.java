package br.com.rinhabackend.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransacaoExtratoDTO {
    private int valor;
    private String tipo;
    private String descricao;

    @JsonProperty(value = "realizada_em")
    private LocalDateTime realizadoEm;

    public TransacaoExtratoDTO(int valor, String tipo, String descricao, LocalDateTime realizadoEm) {
        this.valor = valor;
        this.tipo = tipo;
        this.descricao = descricao;
        this.realizadoEm = realizadoEm;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getRealizadoEm() {
        return realizadoEm;
    }

    public void setRealizadoEm(LocalDateTime realizadoEm) {
        this.realizadoEm = realizadoEm;
    }

}
