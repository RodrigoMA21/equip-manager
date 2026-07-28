package br.com.projetofinal_grupo1.projetofinal_grupo1.service;

import br.com.projetofinal_grupo1.projetofinal_grupo1.model.Equipamento;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.Estoque;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.TipoEquipamento;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.EquipamentoRepository;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.EstoqueRepository;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.TipoEquipamentoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EquipamentoService {

    private final EquipamentoRepository equipamentoRepository;
    private final EstoqueRepository estoqueRepository;
    private final TipoEquipamentoRepository tipoEquipamentoRepository;

    public EquipamentoService(EquipamentoRepository equipamentoRepository,
                              EstoqueRepository estoqueRepository,
                              TipoEquipamentoRepository tipoEquipamentoRepository) {
        this.equipamentoRepository = equipamentoRepository;
        this.estoqueRepository = estoqueRepository;
        this.tipoEquipamentoRepository = tipoEquipamentoRepository;
    }

    public List<Equipamento> listarTodos() {
        return equipamentoRepository.findAll();
    }

    public ResponseEntity<Equipamento> buscarPorId(int id) {
        return equipamentoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Transactional
    public Equipamento salvar(Equipamento equipamento) {

        TipoEquipamento tipoEquipamento = tipoEquipamentoRepository.findById(equipamento.getTipoEquipamento().getId())
                .orElseThrow(() -> new RuntimeException("Tipo de equipamento não encontrado com o ID: " + equipamento.getTipoEquipamento().getId()));

        equipamento.setTipoEquipamento(tipoEquipamento);
        equipamento.setDisponivel(true); // marca o novo como disponível


        Equipamento salvo = equipamentoRepository.save(equipamento);


        Estoque estoque = estoqueRepository.findByTipoEquipamentoId(tipoEquipamento.getId())
                .orElse(null);

        if (estoque == null) {
            estoque = new Estoque();
            estoque.setTipoEquipamento(tipoEquipamento);
            estoque.setQuantidadeDisponivel(1);
            estoque.setQuantidadeEmUso(0);
            estoque.setQuantidadeDefeituosa(0);
        } else {
            estoque.setQuantidadeDisponivel(estoque.getQuantidadeDisponivel() + 1);
        }

        estoqueRepository.save(estoque);

        return salvo;
    }

    @Transactional
    public ResponseEntity<Equipamento> atualizar(int id, Equipamento equipamento) {
        return equipamentoRepository.findById(id)
                .map(existing -> {
                    equipamento.setId(id);
                    return ResponseEntity.ok(equipamentoRepository.save(equipamento));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Transactional
    public ResponseEntity<Void> deletar(Integer id) {
        if (!equipamentoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        equipamentoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}