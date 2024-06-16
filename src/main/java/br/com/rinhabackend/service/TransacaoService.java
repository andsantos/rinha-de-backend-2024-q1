package br.com.rinhabackend.service;

import br.com.rinhabackend.dto.ExtratoDTO;
import br.com.rinhabackend.dto.SaldoResponseDTO;
import br.com.rinhabackend.dto.TransacaoRequestDTO;
import reactor.core.publisher.Mono;

public interface TransacaoService {

    Mono<SaldoResponseDTO> criarTransacao(Long id, TransacaoRequestDTO request);

    Mono<ExtratoDTO> obterExtrato(Long id);

}
