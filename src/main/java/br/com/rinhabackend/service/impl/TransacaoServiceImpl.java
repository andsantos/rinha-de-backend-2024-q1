package br.com.rinhabackend.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;

import br.com.rinhabackend.dto.ExtratoDTO;
import br.com.rinhabackend.dto.SaldoDTO;
import br.com.rinhabackend.dto.SaldoResponseDTO;
import br.com.rinhabackend.dto.TransacaoAdapter;
import br.com.rinhabackend.dto.TransacaoExtratoDTO;
import br.com.rinhabackend.dto.TransacaoRequestDTO;
import br.com.rinhabackend.exception.NotFoundException;
import br.com.rinhabackend.exception.UnprocessableException;
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
            TransacaoRequestDTO dto) {
        TransacaoAdapter request = new TransacaoAdapter(dto);
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
                                request.setClienteId(savedCliente.getId());
                                request.setSaldo(savedCliente.getSaldo());
                                request.setLimite(savedCliente.getLimite());
                                return criarTransacao(request)
                                        .flatMap(transacao -> repository
                                                .save(transacao).flatMap(
                                                        savedTransacao -> criarSaldo(
                                                                request)));
                            });
                }).as(transactionalOperator::transactional);

    }

    protected static Mono<Transacao> criarTransacao(TransacaoAdapter request) {
        Transacao transacao = new Transacao();
        transacao.setClienteId(request.getClienteId());
        transacao.setTipo(request.getTipo());
        transacao.setValor(request.getValor());
        transacao.setDescricao(request.getDescricao());
        transacao.setRealizadaEm(LocalDateTime.now());
        return Mono.just(transacao);
    }

    protected static Mono<SaldoResponseDTO> criarSaldo(
            TransacaoAdapter request) {
        return Mono.just(
                new SaldoResponseDTO(request.getSaldo(), request.getLimite()));
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
