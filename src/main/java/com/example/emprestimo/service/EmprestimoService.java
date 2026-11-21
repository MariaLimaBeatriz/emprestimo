package com.example.emprestimo.service;

import com.example.emprestimo.model.Emprestimo;
import com.example.emprestimo.model.Equipamento;
import com.example.emprestimo.model.TipoEquipamento;
import com.example.emprestimo.repository.ColaboradorRepository;
import com.example.emprestimo.repository.EmprestimoRepository;
import com.example.emprestimo.repository.EquipamentoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmprestimoService {

    private final EmprestimoRepository repository;
    private final EquipamentoRepository  equipamentoRepository;
    private final ColaboradorRepository colaboradorRepository;

    public EmprestimoService(EmprestimoRepository repository, EquipamentoRepository equipamentoRepository, ColaboradorRepository colaboradorRepository) {
        this.repository = repository;
        this.equipamentoRepository = equipamentoRepository;
        this.colaboradorRepository = colaboradorRepository;
    }

    public List<Emprestimo> listar(Boolean atrasados) {
        if (Boolean.TRUE.equals(atrasados)) {
          return repository.findByDataDevolucaoIsNullAndDataPrevistaDevolucaoBefore(LocalDate.now());
      }
         return repository.findAll();
    }

    public Emprestimo buscar(Long id){
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Emprétimo não encontrado"));
    }

    public List<Emprestimo> listarDoColaborador(Long colaboradorId) {
        if (!colaboradorRepository.existsById(colaboradorId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Colaborador não encontrado");
        }
        return repository.findByColaboradorId(colaboradorId);
    }

    @Transactional
    public Emprestimo emprestar(Long equipamentoId, Long colaboradorId) {
        if (!colaboradorRepository.existsById(colaboradorId)) {
            throw new IllegalStateException("Colaborador não encontrado");
        }
        boolean temAtraso = repository.existsByColaboradorIdAndDataDevolucaoIsNullAndDataPrevistaDevolucaoBefore(colaboradorId, LocalDate.now());
        if (temAtraso) {

            throw new IllegalStateException("Colaborador possui imprestimo atrasado");
        }

        Equipamento equipamento = equipamentoRepository.findById(equipamentoId)
                .orElseThrow(() -> new IllegalStateException("Equipamento nao encontrado"));
        equipamento.emprestar();
        // 5. Regra R5: cria o empréstimo com data de hoje e prevê devolução em 14 dias
        Emprestimo emprestimo = new Emprestimo(equipamentoId, colaboradorId, LocalDate.now());
        // 6. Salva os dois
        equipamentoRepository.save(equipamento);
        return repository.save(emprestimo);
    }
    // Regras R6 e R7
    @Transactional
    public Emprestimo devolver(Long id) {
        Emprestimo emprestimo = buscar(id);
        // Regra R6: se já devolvido, a própria entidade lança IllegalStateException
        // Regra R7: registra data de devolução como hoje
        emprestimo.devolver(LocalDate.now());
        // Regra R7: busca o equipamento e volta a deixá-lo disponível
        Equipamento equipamento = equipamentoRepository.findById(emprestimo.getEquipamentoId())
                .orElseThrow(() -> new IllegalStateException("Equipamento não encontrado"));
        equipamento.devolver();
        // Salva os dois
        equipamentoRepository.save(equipamento);
        return repository.save(emprestimo);
    }
}

