package br.com.projetofinal_grupo1.projetofinal_grupo1.service;

import br.com.projetofinal_grupo1.projetofinal_grupo1.model.Estoque;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.TipoEquipamento;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.EquipamentoRepository;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.EstoqueRepository;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.TipoEquipamentoRepository;
import org.springframework.stereotype.Service;

@Service
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final TipoEquipamentoRepository tipoEquipamentoRepository;
    private final EquipamentoRepository equipamentoRepository;

    public EstoqueService(EstoqueRepository estoqueRepository, TipoEquipamentoRepository tipoEquipamentoRepository, EquipamentoRepository equipamentoRepository) {
        this.estoqueRepository = estoqueRepository;
        this.tipoEquipamentoRepository = tipoEquipamentoRepository;
        this.equipamentoRepository = equipamentoRepository;
    }

    public Estoque atualizarEstoque(int idEstoque, Estoque estoqueAtualizado, int idTipoEquipamento) {
        Estoque estoqueExistente = estoqueRepository.findById(idEstoque)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado"));

        estoqueExistente.setQuantidadeDisponivel(estoqueAtualizado.getQuantidadeDisponivel());
        estoqueExistente.setQuantidadeEmUso(estoqueAtualizado.getQuantidadeEmUso());
        estoqueExistente.setQuantidadeDefeituosa(estoqueAtualizado.getQuantidadeDefeituosa());

        // Atualiza tipo, se necessário (opcional)
        if (estoqueExistente.getTipoEquipamento().getId() != idTipoEquipamento) {
            TipoEquipamento tipo = tipoEquipamentoRepository.findById(idTipoEquipamento)
                    .orElseThrow(() -> new RuntimeException("TipoEquipamento não encontrado"));
            estoqueExistente.setTipoEquipamento(tipo);
        }

        return estoqueRepository.save(estoqueExistente);
    }
}