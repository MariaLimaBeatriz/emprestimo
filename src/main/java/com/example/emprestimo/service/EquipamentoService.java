package com.example.emprestimo.service;

import com.example.emprestimo.model.Equipamento;
import com.example.emprestimo.model.TipoEquipamento;
import com.example.emprestimo.repository.EmprestimoRepository;
import com.example.emprestimo.repository.EquipamentoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EquipamentoService {

    private final EquipamentoRepository repository;
    private final EmprestimoRepository emprestimoRepository;

    public EquipamentoService(EquipamentoRepository repository, EmprestimoRepository emprestimoRepository) {
        this.repository = repository;
        this.emprestimoRepository = emprestimoRepository;
    }

    public List<Equipamento> listar(TipoEquipamento tipo) {
        if (tipo == null) {
            return repository.findAll();
        }
        return repository.findByTipo(tipo);
    }

    public Equipamento buscar(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipamento não encontrado"));
    }

    // Regras R1 e R2
    public Equipamento cadastrar(Equipamento equipamento) {
        if (repository.existsByPatrimonio(equipamento.getPatrimonio())) {
            throw new IllegalStateException("Patrimônio já cadastrado");
        }
        // R2: nasce disponível independente do JSON
        equipamento.setDisponivel(true);
        return repository.save(equipamento);
    }

    // Atualização mantendo patrimonio e status de disponibilidade
    public Equipamento atualizar(Long id, Equipamento dados) {
        Equipamento existente = buscar(id);
        existente.setNome(dados.getNome());
        existente.setTipo(dados.getTipo());
        return repository.save(existente);
    }

    // Regra R9: não remove se já tiver histórico de empréstimo
    public void remover(Long id) {
        Equipamento existente = buscar(id);
        if (emprestimoRepository.existsByEquipamentoId(id)) {
            throw new IllegalStateException("Equipamento possui histórico de empréstimos");
        }
        repository.delete(existente);
    }

}
