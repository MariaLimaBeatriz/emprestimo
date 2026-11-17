package com.example.emprestimo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;

@Entity
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long equipamentoId;
    private Long colaboradorId;
    private LocalDate dataEmprestimo;
    private LocalDate dataPrevistaDevolucao;
    private LocalDate dataDevolucao;

    // Construtor vazio que o JPA exige
    public Emprestimo() {
    }

    // R5: guarda os ids, empresta hoje, prevê hoje + 14
    public Emprestimo(Long equipamentoId, Long colaboradorId, LocalDate hoje) {
        this.equipamentoId = equipamentoId;
        this.colaboradorId = colaboradorId;
        this.dataEmprestimo = hoje;
        this.dataPrevistaDevolucao = hoje.plusDays(14);
    }

    // R6 e a data da R7
    public void devolver(LocalDate data) {
        if (this.dataDevolucao != null) {
            throw new IllegalStateException("Empréstimo já devolvido");
        }
        this.dataDevolucao = data;
    }

    // R8: atrasado se ainda não foi devolvido e a data passada já passou do prazo
    public boolean estaAtrasadoEm(LocalDate data) {
        if (this.dataDevolucao != null) {
            return false;
        }
        return data.isAfter(this.dataPrevistaDevolucao);
    }

    // R8: aparece no JSON como "atrasado"
    public boolean isAtrasado() {
        return estaAtrasadoEm(LocalDate.now());
    }

    // Apenas getters (sem setters, as datas e estados só mudam pelos métodos de negócio)
    public Long getId() {
        return id;
    }

    public Long getEquipamentoId() {
        return equipamentoId;
    }

    public Long getColaboradorId() {
        return colaboradorId;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public LocalDate getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }
}
