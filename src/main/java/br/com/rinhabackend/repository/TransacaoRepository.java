package br.com.rinhabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.rinhabackend.model.Transacao;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

}
