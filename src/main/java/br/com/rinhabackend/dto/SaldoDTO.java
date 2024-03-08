package br.com.rinhabackend.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SaldoDTO {
    private int total;

    @JsonProperty(value = "data_extrato")
    private LocalDateTime dataExtrato;

    private int limite;

    public SaldoDTO(int total, int limite, LocalDateTime dataExtrato) {
        this.total = total;
        this.limite = limite;
        this.dataExtrato = dataExtrato;
    }

    public int getTotal() {
        return total;
    }

    public LocalDateTime getDataExtrato() {
        return dataExtrato;
    }

    public int getLimite() {
        return limite;
    }

}
