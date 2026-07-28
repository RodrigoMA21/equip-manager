package br.com.projetofinal_grupo1.projetofinal_grupo1.service;

import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.ParametroSistemaRequestDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.ParametroSistemaResponseDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.PrevisaoFaltaResponseDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.exception.BadRequestException;
import br.com.projetofinal_grupo1.projetofinal_grupo1.exception.ResourceNotFoundException;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.Estoque;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.ParametroSistema;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.EstoqueRepository;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.ParametroSistemaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParametroSistemaService {

    private final ParametroSistemaRepository parametrosSistemaRepository;
    private final EstoqueRepository estoqueRepository;

    public ParametroSistemaService(ParametroSistemaRepository parametrosSistemaRepository, EstoqueRepository estoqueRepository) {
        this.parametrosSistemaRepository = parametrosSistemaRepository;
        this.estoqueRepository = estoqueRepository;
    }

    private ParametroSistemaResponseDTO toResponseDTO(ParametroSistema parametro) {
        ParametroSistemaResponseDTO dto = new ParametroSistemaResponseDTO();
        dto.setIdParametro(parametro.getIdParametro());
        dto.setTempoMedioReposicao(parametro.getTempoMedioReposicao());
        dto.setTempoMedioConsumoEstoque(parametro.getTempoMedioConsumoEstoque());
        dto.setTempoMedioEnvio(parametro.getTempoMedioEnvio());
        dto.setTaxaMediaEquipamentosDefeituosos(parametro.getTaxaMediaEquipamentosDefeituosos());
        dto.setEstoqueMinimoSeguranca(parametro.getEstoqueMinimoSeguranca());
        return dto;
    }

    private ParametroSistema toEntity(ParametroSistemaRequestDTO dto) {
        ParametroSistema parametro = new ParametroSistema();
        parametro.setTempoMedioReposicao(dto.getTempoMedioReposicao());
        parametro.setTempoMedioConsumoEstoque(dto.getTempoMedioConsumoEstoque());
        parametro.setTempoMedioEnvio(dto.getTempoMedioEnvio());
        parametro.setTaxaMediaEquipamentosDefeituosos(dto.getTaxaMediaEquipamentosDefeituosos());
        parametro.setEstoqueMinimoSeguranca(dto.getEstoqueMinimoSeguranca());
        return parametro;
    }


    @Transactional
    public ParametroSistemaResponseDTO criarParametro(ParametroSistemaRequestDTO dto) {
        ParametroSistema parametro = toEntity(dto);
        ParametroSistema salvo = parametrosSistemaRepository.save(parametro);
        return toResponseDTO(salvo);
    }

    @Transactional(readOnly = true)
    public Optional<ParametroSistemaResponseDTO> buscarParametroPorId(int id) {
        return parametrosSistemaRepository.findById(id)
                .map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public List<ParametroSistemaResponseDTO> listarTodosParametros() {
        return parametrosSistemaRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ParametroSistemaResponseDTO atualizarParametro(int id, ParametroSistemaRequestDTO dto) {
        // Validação simples (exemplo: valores negativos não permitidos)
        if (dto.getTempoMedioReposicao() < 0
                || dto.getTempoMedioConsumoEstoque() < 0
                || dto.getTempoMedioEnvio() < 0
                || dto.getTaxaMediaEquipamentosDefeituosos() < 0
                || dto.getEstoqueMinimoSeguranca() < 0) {
            throw new BadRequestException("Os valores do parâmetro não podem ser negativos.");
        }

        ParametroSistema existente = parametrosSistemaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parâmetro de estoque não encontrado com o ID: " + id));

        existente.setTempoMedioReposicao(dto.getTempoMedioReposicao());
        existente.setTempoMedioConsumoEstoque(dto.getTempoMedioConsumoEstoque());
        existente.setTempoMedioEnvio(dto.getTempoMedioEnvio());
        existente.setTaxaMediaEquipamentosDefeituosos(dto.getTaxaMediaEquipamentosDefeituosos());
        existente.setEstoqueMinimoSeguranca(dto.getEstoqueMinimoSeguranca());

        ParametroSistema atualizado = parametrosSistemaRepository.save(existente);
        return toResponseDTO(atualizado);
    }

    @Transactional
    public void deletarParametro(int id) {
        if (!parametrosSistemaRepository.existsById(id)) {
            throw new RuntimeException("Parâmetro de estoque não encontrado com o ID: " + id);
        }
        parametrosSistemaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<ParametroSistema> getLatestParametroSistema() {
        return parametrosSistemaRepository.findAll().stream()
                .max((p1, p2) -> Integer.compare(p1.getIdParametro(), p2.getIdParametro()));
    }

    @Transactional(readOnly = true)
    public List<PrevisaoFaltaResponseDTO> gerarRelatorioPrevisaoFalta() {
        ParametroSistema parametros = parametrosSistemaRepository.getLatestParametroSistema()
                .orElseThrow(() -> new RuntimeException("Parâmetros de estoque não configurados."));

        List<Estoque> todosEstoques = estoqueRepository.findAll();
        List<PrevisaoFaltaResponseDTO> relatorio = new ArrayList<>();

        double consumoDiarioBase = (double) parametros.getTempoMedioConsumoEstoque();

        for (Estoque estoque : todosEstoques) {
            if (estoque.getTipoEquipamento() == null) {
                System.out.println("Estoque com ID " + estoque.getId() + " não possui TipoEquipamento associado. Ignorando no relatório.");
                continue;
            }

            double consumoDiarioEstimado = 0;
            if (consumoDiarioBase > 0) {
                consumoDiarioEstimado = (double) estoque.getQuantidadeEmUso() / consumoDiarioBase;
            }

            consumoDiarioEstimado *= (1 + parametros.getTaxaMediaEquipamentosDefeituosos());

            int leadTimeTotalDias = parametros.getTempoMedioReposicao() + parametros.getTempoMedioEnvio();

            double pontoDePedido = (consumoDiarioEstimado * leadTimeTotalDias) + parametros.getEstoqueMinimoSeguranca();

            boolean emRiscoDeFalta = estoque.getQuantidadeDisponivel() < pontoDePedido;
            String mensagemAlerta = emRiscoDeFalta ? "ATENÇÃO: Em risco de falta!" : "Estoque suficiente.";

            relatorio.add(PrevisaoFaltaResponseDTO.builder()

                    .idTipoEquipamento(estoque.getTipoEquipamento().getId())
                    .nomeTipoEquipamento(estoque.getTipoEquipamento().getNomeTipo())
                    .quantidadeDisponivelAtual(estoque.getQuantidadeDisponivel())
                    .quantidadeEmUsoAtual(estoque.getQuantidadeEmUso())
                    .quantidadeDefeituosaAtual(estoque.getQuantidadeDefeituosa())
                    .consumoDiarioEstimado(consumoDiarioEstimado)
                    .leadTimeTotalDias(leadTimeTotalDias)
                    .estoqueMinimoSeguranca(parametros.getEstoqueMinimoSeguranca())
                    .pontoDePedido(pontoDePedido)
                    .emRiscoDeFalta(emRiscoDeFalta)
                    .mensagemAlerta(mensagemAlerta)
                    .build());
        }
        return relatorio;
    }
}
