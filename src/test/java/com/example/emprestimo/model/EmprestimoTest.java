package com.example.emprestimo.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EmprestimoTest {


    @Test
    void criarComData14DefineEmprestimoEPrazoPara28() {
        LocalDate hoje = LocalDate.of(2026, 11, 14);
        Emprestimo emprestimo = new Emprestimo(1L, 1L, hoje);

        assertEquals(LocalDate.of(2026, 11, 14), emprestimo.getDataEmprestimo());
        assertEquals(LocalDate.of(2026, 11, 28), emprestimo.getDataPrevistaDevolucao());
    }

    //(R7)
    @Test
    void devolverEmprestimoAbertoRegistraDataDevolucao() {
        Emprestimo emprestimo = new Emprestimo(1L, 1L, LocalDate.of(2026, 11, 14));
        LocalDate dataDevolucao = LocalDate.of(2026, 11, 20);

        emprestimo.devolver(dataDevolucao);

        assertEquals(dataDevolucao, emprestimo.getDataDevolucao());
    }

    //  (R6)
    @Test
    void devolverDuasVezesLancaExcecao() {
        Emprestimo emprestimo = new Emprestimo(1L, 1L, LocalDate.of(2026, 11, 14));
        emprestimo.devolver(LocalDate.of(2026, 11, 20));

        assertThrows(IllegalStateException.class, () -> emprestimo.devolver(LocalDate.of(2026, 11, 21)));
    }

    //  (R8)
    @Test
    void criadoEm27NaoDevolvidoConsultadoEm14EstaAtrasado() {
        Emprestimo emprestimo = new Emprestimo(1L, 1L, LocalDate.of(2026, 10, 27));

        assertTrue(emprestimo.estaAtrasadoEm(LocalDate.of(2026, 11, 14)));
    }

    //Não atrasado
    @Test
    void criadoEm06NaoDevolvidoConsultadoEm14NaoEstaAtrasado() {
        Emprestimo emprestimo = new Emprestimo(1L, 1L, LocalDate.of(2026, 11, 6));

        assertFalse(emprestimo.estaAtrasadoEm(LocalDate.of(2026, 11, 14)));
    }


    @Test
    void criadoEm27DevolvidoEm12ConsultadoEm14NaoEstaAtrasado() {
        Emprestimo emprestimo = new Emprestimo(1L, 1L, LocalDate.of(2026, 10, 27));
        emprestimo.devolver(LocalDate.of(2026, 11, 12));

        assertFalse(emprestimo.estaAtrasadoEm(LocalDate.of(2026, 11, 14)));
    }

    //  Não atrasado (no dia do prazo ainda tem tempo)
    @Test
    void criadoEm31ConsultadoNoDiaDoPrazoNaoEstaAtrasado() {
        Emprestimo emprestimo = new Emprestimo(1L, 1L, LocalDate.of(2026, 10, 31));

        assertFalse(emprestimo.estaAtrasadoEm(LocalDate.of(2026, 11, 14)));
    }
}
