package br.com.rinhabackend.service;

import br.com.rinhabackend.dto.ExtratoDTO;
import br.com.rinhabackend.dto.SaldoResponseDTO;
import br.com.rinhabackend.dto.TransacaoRequestDTO;

public interface TransacaoService {

    SaldoResponseDTO criarTransacao(Long id, TransacaoRequestDTO request);

    ExtratoDTO obterExtrato(Long id);

}
