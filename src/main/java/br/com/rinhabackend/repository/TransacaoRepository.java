package br.com.rinhabackend.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import br.com.rinhabackend.model.Transacao;
import reactor.core.publisher.Flux;

public interface TransacaoRepository
        extends
            ReactiveCrudRepository<Transacao, Long> {

    @Query("SELECT * FROM transacoes WHERE cliente_Id = :clienteId ORDER BY realizada_Em DESC ")
    Flux<Transacao> findByClienteId(int clienteId);
}
