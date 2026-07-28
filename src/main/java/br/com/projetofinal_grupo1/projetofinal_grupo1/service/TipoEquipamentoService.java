package br.com.projetofinal_grupo1.projetofinal_grupo1.service;

import br.com.projetofinal_grupo1.projetofinal_grupo1.model.TipoEquipamento;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.TipoEquipamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TipoEquipamentoService {

    private final TipoEquipamentoRepository tipoEquipamentoRepository;

    public TipoEquipamentoService(TipoEquipamentoRepository tipoEquipamentoRepository) {
        this.tipoEquipamentoRepository = tipoEquipamentoRepository;
    }

    @Transactional
    public TipoEquipamento criarTipoEquipamento(String nomeTipo) {
        if (nomeTipo == null || nomeTipo.trim().isEmpty()) {
            throw new RuntimeException("Nome do tipo de equipamento é obrigatório");
        }

        String nomeNormalizado = nomeTipo.trim().toUpperCase();

        boolean jaExiste = tipoEquipamentoRepository.existsByNomeTipoIgnoreCase(nomeTipo.trim());
        if (jaExiste) {
            throw new RuntimeException("Tipo de equipamento já existe");
        }

        TipoEquipamento novo = new TipoEquipamento();
        novo.setNomeTipo(nomeTipo.trim());

        return tipoEquipamentoRepository.save(novo);
    }

    @Transactional(readOnly = true) // Para operações de leitura
    public List<TipoEquipamento> listarTodosTiposEquipamento() {
        return tipoEquipamentoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<TipoEquipamento> buscarTipoEquipamentoPorId(int id) {
        return tipoEquipamentoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<TipoEquipamento> buscarTipoEquipamentoPorNome(String nomeTipo) {
        return tipoEquipamentoRepository.findByNomeTipo(nomeTipo);
    }

    @Transactional
    public TipoEquipamento atualizarTipoEquipamento(int id, TipoEquipamento tipoEquipamentoAtualizado) {
        return tipoEquipamentoRepository.findById(id)
                .map(tipoExistente -> {
                    tipoExistente.setNomeTipo(tipoEquipamentoAtualizado.getNomeTipo());
                    // Adicione outras propriedades se houver
                    return tipoEquipamentoRepository.save(tipoExistente);
                })
                .orElseThrow(() -> new RuntimeException("Tipo de equipamento não encontrado com o ID: " + id));
    }

    @Transactional
    public void deletarTipoEquipamento(int id) {
        if (!tipoEquipamentoRepository.existsById(id)) {
            throw new RuntimeException("Tipo de equipamento não encontrado com o ID: " + id);
        }
        tipoEquipamentoRepository.deleteById(id);
    }
}
