package br.com.rinhabackend.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import br.com.rinhabackend.model.Cliente;

public interface ClienteRepository
        extends
            ReactiveCrudRepository<Cliente, Long> {

}
