package br.com.projetofinal_grupo1.projetofinal_grupo1.service;

import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.EquipamentoToColaboradorRequestDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.EquipamentoToColaboradorResponseDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.Colaborador;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.Equipamento;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.EquipamentoToColaborador;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.IdEquipamentoToColaborador;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.ColaboradorRepository;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.EquipamentoRepository;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.EquipamentoToColaboradorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EquipamentoToColaboradorService {

    private final EquipamentoToColaboradorRepository equipamentoToColaboradorRepository;
    private final EquipamentoRepository equipamentoRepository;
    private final ColaboradorRepository colaboradorRepository;
    private final PrevisaoEntregaService previsaoEntregaService;

    public EquipamentoToColaboradorService(
            EquipamentoToColaboradorRepository equipamentoToColaboradorRepository,
            EquipamentoRepository equipamentoRepository,
            ColaboradorRepository colaboradorRepository,
            PrevisaoEntregaService previsaoEntregaService) {
        this.equipamentoToColaboradorRepository = equipamentoToColaboradorRepository;
        this.equipamentoRepository = equipamentoRepository;
        this.colaboradorRepository = colaboradorRepository;
        this.previsaoEntregaService = previsaoEntregaService;
    }

    public List<EquipamentoToColaboradorResponseDTO> listarTodos() {
        return equipamentoToColaboradorRepository.findAll()
                .stream()
                .map(EquipamentoToColaboradorResponseDTO::new)
                .toList();
    }

    public ResponseEntity<EquipamentoToColaboradorResponseDTO> buscarPorId(IdEquipamentoToColaborador id) {
        return equipamentoToColaboradorRepository.findById(id)
                .map(e -> ResponseEntity.ok(new EquipamentoToColaboradorResponseDTO(e)))
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<EquipamentoToColaboradorResponseDTO> salvar(EquipamentoToColaboradorRequestDTO dto) {
        IdEquipamentoToColaborador id = dto.getChaveCompostaEquipamentoColaborador();
        Equipamento equipamento = equipamentoRepository.findById(id.getIdEquipamento())
                .orElseThrow(() -> new RuntimeException("Equipamento não encontrado"));

        if (!equipamento.getDisponivel()) {
            throw new IllegalStateException("Equipamento não está disponível para empréstimo");
        }

        equipamento.setDisponivel(false);
        equipamentoRepository.save(equipamento);

        EquipamentoToColaborador entity = dto.toEntity();
        EquipamentoToColaborador salvo = equipamentoToColaboradorRepository.save(entity);

        return ResponseEntity.status(201).body(new EquipamentoToColaboradorResponseDTO(salvo));
    }

    public ResponseEntity<EquipamentoToColaboradorResponseDTO> atualizar(IdEquipamentoToColaborador id, EquipamentoToColaboradorRequestDTO dto) {
        if (!equipamentoToColaboradorRepository.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        EquipamentoToColaborador atualizado = dto.toEntity();
        atualizado.setId(id);
        EquipamentoToColaborador salvo = equipamentoToColaboradorRepository.save(atualizado);
        return ResponseEntity.ok(new EquipamentoToColaboradorResponseDTO(salvo));
    }

    public ResponseEntity<Void> deletar(IdEquipamentoToColaborador id) {
        if (!equipamentoToColaboradorRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        equipamentoToColaboradorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Void> registrarDevolucao(IdEquipamentoToColaborador id) {
        Optional<EquipamentoToColaborador> opt = equipamentoToColaboradorRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        EquipamentoToColaborador etc = opt.get();

        if (etc.getDataDevolucao() != null) {
            return ResponseEntity.badRequest().build();
        }

        etc.setDataDevolucao(LocalDate.now());
        equipamentoToColaboradorRepository.save(etc);

        equipamentoRepository.findById(id.getIdEquipamento()).ifPresent(equipamento -> {
            equipamento.setDisponivel(true);
            equipamentoRepository.save(equipamento);
        });

        return ResponseEntity.ok().build();
    }
}

