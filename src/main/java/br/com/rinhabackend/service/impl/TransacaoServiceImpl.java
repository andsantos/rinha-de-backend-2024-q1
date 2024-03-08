package br.com.rinhabackend.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

@Service
public class TransacaoServiceImpl implements TransacaoService {
    private final TransacaoRepository repository;
    private final ClienteRepository clienteRepository;

    public TransacaoServiceImpl(TransacaoRepository transacaoRepository, ClienteRepository clienteRepository) {
        this.repository = transacaoRepository;
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    @Override
    public SaldoResponseDTO criarTransacao(Long id, TransacaoRequestDTO request) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado"));

        int saldo = cliente.getSaldo();
        if ("c".equals(request.getTipo())) {
            saldo = saldo + request.getValor();
        } else {
            saldo = saldo - request.getValor();
        }

        if (saldo < (cliente.getLimite() * -1)) {
            throw new UnprocessableException();
        }

        cliente.setSaldo(saldo);

        clienteRepository.save(cliente);

        Transacao transacao = new Transacao();
        transacao.setCliente(cliente);
        transacao.setTipo(request.getTipo());
        transacao.setValor(request.getValor());
        transacao.setDescricao(request.getDescricao());
        transacao.setRealizadaEm(LocalDateTime.now());

        repository.save(transacao);

        return new SaldoResponseDTO(saldo, cliente.getLimite());
    }

    @Override
    public ExtratoDTO obterExtrato(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado"));

        SaldoDTO saldo = new SaldoDTO(cliente.getSaldo(), cliente.getLimite(), LocalDateTime.now());
        List<TransacaoExtratoDTO> transacoes = cliente.getTransacoes().stream().map(TransacaoServiceImpl::entityToDto)
                .toList();

        return new ExtratoDTO(saldo, transacoes);
    }

    protected static TransacaoExtratoDTO entityToDto(Transacao transacao) {
        return new TransacaoExtratoDTO(transacao.getValor(), transacao.getTipo(), transacao.getDescricao(),
                transacao.getRealizadaEm());
    }
}
