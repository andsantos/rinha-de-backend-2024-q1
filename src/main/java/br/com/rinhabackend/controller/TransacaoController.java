package br.com.rinhabackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.rinhabackend.dto.ExtratoDTO;
import br.com.rinhabackend.dto.SaldoResponseDTO;
import br.com.rinhabackend.dto.TransacaoRequestDTO;
import br.com.rinhabackend.service.TransacaoService;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
public class TransacaoController {
    private final TransacaoService service;

    public TransacaoController(TransacaoService transacaoService) {
        this.service = transacaoService;
    }

    @PostMapping("/clientes/{id}/transacoes")
    public Mono<SaldoResponseDTO> criarTransacao(
            @RequestBody @Valid TransacaoRequestDTO request,
            @PathVariable Long id) {
        return service.criarTransacao(id, request);
    }

    @GetMapping("/clientes/{id}/extrato")
    public Mono<ExtratoDTO> obterExtrato(@PathVariable Long id) {
        return service.obterExtrato(id);
    }
}
