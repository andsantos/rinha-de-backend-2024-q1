package br.com.rinhabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.rinhabackend.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}
