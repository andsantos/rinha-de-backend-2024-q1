package br.com.rinhabackend.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import br.com.rinhabackend.model.Transacao;
import reactor.core.publisher.Flux;

public interface TransacaoRepository
        extends
            ReactiveCrudRepository<Transacao, Long> {

    Flux<Transacao> findByClienteId(int id);
}
