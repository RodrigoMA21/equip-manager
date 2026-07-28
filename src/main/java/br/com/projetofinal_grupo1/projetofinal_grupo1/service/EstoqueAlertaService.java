package br.com.projetofinal_grupo1.projetofinal_grupo1.service;

import br.com.projetofinal_grupo1.projetofinal_grupo1.model.Alerta;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.Estoque;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.ParametroSistema;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.TipoEquipamento;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.AlertaRepository;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.EstoqueRepository;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.TipoEquipamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class EstoqueAlertaService {

    @Autowired
    TipoEquipamentoRepository tipoEquipamentoRepository;

    @Autowired
    EstoqueRepository estoqueRepository;

    @Autowired
    ParametroSistemaService parametroSistemaService;

    @Autowired
    AlertaRepository alertaRepository;

    @Autowired
    EmailService emailService;

    @Scheduled(fixedRate = 10000) // Roda a cada 10 segundos (ajuste conforme necessário)
    @Transactional
    public void verificarEstoqueCritico() {
        System.out.println("Verificando estoque crítico por tipo de equipamento...");

        ParametroSistema parametros = parametroSistemaService.getLatestParametroSistema()
                .orElseThrow(() -> new RuntimeException("Parâmetros de estoque não configurados. Não é possível verificar alertas."));

        int estoqueMinimoSeguranca = parametros.getEstoqueMinimoSeguranca();

        List<TipoEquipamento> tiposEquipamento = tipoEquipamentoRepository.findAll();

        for (TipoEquipamento tipoEquipamento : tiposEquipamento) {
            List<Estoque> estoques = estoqueRepository.findByTipoEquipamento(tipoEquipamento);

            if (estoques.isEmpty()) {
                System.out.println("Tipo de Equipamento " + tipoEquipamento.getNomeTipo() + " não possui registro de estoque. Ignorando.");
                continue;
            }

            int quantidadeAtual = estoques.stream()
                    .mapToInt(Estoque::getQuantidadeDisponivel)
                    .sum();

            if (quantidadeAtual <= estoqueMinimoSeguranca) {
                Optional<Alerta> alertaExistente = alertaRepository.findTopByTipoEquipamentoAndStatusInOrderByIdDesc(
                        tipoEquipamento, Arrays.asList("PENDENTE", "ENVIADO")
                );

                if (alertaExistente.isEmpty()) {
                    String descricao = String.format(
                            "Estoque do tipo '%s' está em %d unidades, abaixo do mínimo de segurança de %d.",
                            tipoEquipamento.getNomeTipo(), quantidadeAtual, estoqueMinimoSeguranca
                    );
                    Alerta novoAlerta = new Alerta(
                            "ESTOQUE_BAIXO",
                            descricao,
                            "ENVIADO",
                            null,
                            tipoEquipamento
                    );
                    alertaRepository.save(novoAlerta);

                    emailService.enviarAlertaEstoqueBaixo(
                            "admin@example.com",
                            tipoEquipamento.getNomeTipo(),
                            quantidadeAtual,
                            estoqueMinimoSeguranca
                    );
                    System.out.println("Alerta de estoque baixo ENVIADO e registrado para o tipo: " + tipoEquipamento.getNomeTipo());
                } else {
                    System.out.println("Alerta de estoque baixo JÁ ATIVO para o tipo: " + tipoEquipamento.getNomeTipo() + ". Não será reenviado.");
                }
            } else {
                Optional<Alerta> alertaExistente = alertaRepository.findTopByTipoEquipamentoAndStatusInOrderByIdDesc(
                        tipoEquipamento, Arrays.asList("PENDENTE", "ENVIADO")
                );

                if (alertaExistente.isPresent()) {
                    Alerta alertaParaResolver = alertaExistente.get();
                    alertaParaResolver.setStatus("RESOLVIDO");
                    alertaRepository.save(alertaParaResolver);
                    System.out.println("Estoque do tipo " + tipoEquipamento.getNomeTipo() + " normalizado. Alerta " + alertaParaResolver.getId() + " marcado como RESOLVIDO.");
                }
            }
        }
    }

}