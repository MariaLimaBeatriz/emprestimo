package com.example.emprestimo.repository;

import com.example.emprestimo.model.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    // Histórico da pessoa
    List<Emprestimo> findByColaboradorId(Long colaboradorId);

    // R9: verifica se o equipamento tem histórico de empréstimo
    boolean existsByEquipamentoId(Long equipamentoId);

    // R8, filtro de atrasados
    List<Emprestimo> findByDataDevolucaoIsNullAndDataPrevistaDevolucaoBefore(LocalDate data);

    // R10: verifica se o colaborador tem empréstimo atrasado
    boolean existsByColaboradorIdAndDataDevolucaoIsNullAndDataPrevistaDevolucaoBefore(Long colaboradorId, LocalDate data);
}
