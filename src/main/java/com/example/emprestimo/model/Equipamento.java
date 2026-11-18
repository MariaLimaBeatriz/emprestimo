package com.example.emprestimo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity// Diz para o banco que essa classe é uma tabela
public class Equipamento {

    @Id // Chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O patrimônio é obrigatório")
    @Column(nullable = false, unique = true)
    private String patrimonio;
    @Column(nullable = false)
    private String nome;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEquipamento tipo;
    @Column(nullable = false)
    private boolean disponivel = true;

    public Equipamento() { }

    public void emprestar(){
        if (this.disponivel) {
            throw new IllegalStateException("Equipamento indisponível");
        }
        this.disponivel = false;
    }

    public void devolver() {
        this.disponivel = true;
    }


    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPatrimonio(String patrimonio) {
        this.patrimonio = patrimonio;
    }

    public void setTipo(TipoEquipamento tipo) {
        this.tipo = tipo;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getPatrimonio() {
        return patrimonio;
    }

    public TipoEquipamento getTipo() {
        return tipo;
    }
}
