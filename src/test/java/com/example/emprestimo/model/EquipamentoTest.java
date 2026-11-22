package com.example.emprestimo.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EquipamentoTest {

    @Test
    void emprestarEquipamentoDisponivelFicaIndisponivel() {
        Equipamento equipamento = new Equipamento();

        equipamento.emprestar();

        assertFalse(equipamento.isDisponivel());
    }

    @Test
    void emprestarEquipamentoIndisponivelLancaExcecao() {
        Equipamento equipamento = new Equipamento();
        equipamento.emprestar(); // primeiro empréstimo deixa indisponível


        assertThrows(IllegalStateException.class, () -> equipamento.emprestar());
    }

    @Test
    void devolverEquipamentoFicaDisponivel() {
        Equipamento equipamento = new Equipamento();
        equipamento.emprestar(); // deixa indisponível

        equipamento.devolver();

        assertTrue(equipamento.isDisponivel());
    }
}
