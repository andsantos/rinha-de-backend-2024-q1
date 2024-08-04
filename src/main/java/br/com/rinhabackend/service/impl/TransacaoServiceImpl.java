package br.com.rinhabackend.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;

import br.com.rinhabackend.dto.ExtratoDTO;
import br.com.rinhabackend.dto.SaldoDTO;
import br.com.rinhabackend.dto.SaldoResponseDTO;
import br.com.rinhabackend.dto.TransacaoExtratoDTO;
import br.com.rinhabackend.dto.TransacaoRequestDTO;
import br.com.rinhabackend.exception.NotFoundException;
import br.com.rinhabackend.exception.UnprocessableException;
import br.com.rinhabackend.model.Cliente;
import br.com.rinhabackend.model.Transacao;
import br.com.rinhabackend.repository.ClienteRepository;
import br.com.rinhabackend.repository.TransacaoRepository;
import br.com.rinhabackend.service.TransacaoService;
import reactor.core.publisher.Mono;

@Service
public class TransacaoServiceImpl implements TransacaoService {
    private final TransacaoRepository repository;
    private final ClienteRepository clienteRepository;
    private final TransactionalOperator transactionalOperator;

    public TransacaoServiceImpl(TransacaoRepository transacaoRepository,
            ClienteRepository clienteRepository,
            TransactionalOperator transactional) {
        this.repository = transacaoRepository;
        this.clienteRepository = clienteRepository;
        this.transactionalOperator = transactional;
    }

    @Override
    public Mono<SaldoResponseDTO> criarTransacao(Long id,
            TransacaoRequestDTO request) {
        return clienteRepository.findById(id)
                .switchIfEmpty(Mono
                        .error(new NotFoundException("Cliente não encontrado")))
                .flatMap(cliente -> {
                    int saldo = cliente.getSaldo();

                    if ("c".equals(request.getTipo())) {
                        saldo = saldo + request.getValor();
                    } else {
                        saldo = saldo - request.getValor();
                    }

                    if (saldo < (cliente.getLimite() * -1)) {
                        return Mono.error(new UnprocessableException());
                    }

                    cliente.setSaldo(saldo);

                    return clienteRepository.save(cliente)
                            .flatMap(savedCliente -> {
                                return criarTransacao(request, savedCliente);
                            });

                }).as(transactionalOperator::transactional);

    }

    protected Mono<SaldoResponseDTO> criarTransacao(TransacaoRequestDTO request,
            Cliente cliente) {
        Transacao transacao = new Transacao();
        transacao.setClienteId(cliente.getId());
        transacao.setTipo(request.getTipo());
        transacao.setValor(request.getValor());
        transacao.setDescricao(request.getDescricao());
        transacao.setRealizadaEm(LocalDateTime.now());
        return repository.save(transacao).flatMap((t) -> {
            return Mono.just(new SaldoResponseDTO(cliente.getSaldo(),
                    cliente.getLimite()));
        });
    }

    @Override
    public Mono<ExtratoDTO> obterExtrato(Long id) {
        return clienteRepository.findById(id)
                .switchIfEmpty(Mono
                        .error(new NotFoundException("Cliente não encontrado")))
                .flatMap(cliente -> {
                    SaldoDTO saldo = new SaldoDTO(cliente.getSaldo(),
                            cliente.getLimite(), LocalDateTime.now());
                    return repository.findByClienteId(cliente.getId())
                            .map(TransacaoServiceImpl::entityToDto)
                            .collectList()
                            .map(transacoes -> new ExtratoDTO(saldo,
                                    transacoes));
                });
    }

    protected static TransacaoExtratoDTO entityToDto(Transacao transacao) {
        return new TransacaoExtratoDTO(transacao.getValor(),
                transacao.getTipo(), transacao.getDescricao(),
                transacao.getRealizadaEm());
    }
}
