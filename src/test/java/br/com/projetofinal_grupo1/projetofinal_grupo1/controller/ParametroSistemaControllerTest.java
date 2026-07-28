package br.com.projetofinal_grupo1.projetofinal_grupo1.controller;

import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.ParametroSistemaRequestDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.ParametroSistemaResponseDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.PrevisaoFaltaResponseDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.UsuarioRepository;
import br.com.projetofinal_grupo1.projetofinal_grupo1.service.ParametroSistemaService;
import br.com.projetofinal_grupo1.projetofinal_grupo1.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ParametroSistemaController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ParametroSistemaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;  // Para converter DTO em JSON

    @MockBean
    private ParametroSistemaService parametrosSistemaService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void listarParametros_DeveRetornar200ComLista() throws Exception {
        ParametroSistemaResponseDTO dto = new ParametroSistemaResponseDTO();
        dto.setIdParametro(1);
        dto.setTempoMedioReposicao(7);
        dto.setTempoMedioConsumoEstoque(5);
        dto.setTempoMedioEnvio(3);
        dto.setTaxaMediaEquipamentosDefeituosos(0.1);
        dto.setEstoqueMinimoSeguranca(2);

        when(parametrosSistemaService.listarTodosParametros()).thenReturn(List.of(dto));

        mockMvc.perform(get("/parametros-sistema"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idParametro").value(1))
                .andExpect(jsonPath("$[0].tempoMedioReposicao").value(7))
                .andExpect(jsonPath("$[0].taxaMediaEquipamentosDefeituosos").value(0.1));
    }

    @Test
    void buscarParametroPorId_QuandoEncontrar_DeveRetornar200() throws Exception {
        ParametroSistemaResponseDTO dto = new ParametroSistemaResponseDTO();
        dto.setIdParametro(1);
        dto.setTempoMedioReposicao(7);
        dto.setTempoMedioConsumoEstoque(5);
        dto.setTempoMedioEnvio(3);
        dto.setTaxaMediaEquipamentosDefeituosos(0.1);
        dto.setEstoqueMinimoSeguranca(2);

        when(parametrosSistemaService.buscarParametroPorId(1)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/parametros-sistema/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idParametro").value(1))
                .andExpect(jsonPath("$.tempoMedioReposicao").value(7));
    }

    @Test
    void buscarParametroPorId_QuandoNaoEncontrar_DeveRetornar404() throws Exception {
        when(parametrosSistemaService.buscarParametroPorId(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/parametros-sistema/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void criarParametro_QuandoValido_DeveRetornar201() throws Exception {
        ParametroSistemaRequestDTO requestDTO = new ParametroSistemaRequestDTO();
        requestDTO.setTempoMedioReposicao(7);
        requestDTO.setTempoMedioConsumoEstoque(5);
        requestDTO.setTempoMedioEnvio(3);
        requestDTO.setTaxaMediaEquipamentosDefeituosos(0.1);
        requestDTO.setEstoqueMinimoSeguranca(2);

        ParametroSistemaResponseDTO responseDTO = new ParametroSistemaResponseDTO();
        responseDTO.setIdParametro(1);
        responseDTO.setTempoMedioReposicao(7);
        responseDTO.setTempoMedioConsumoEstoque(5);
        responseDTO.setTempoMedioEnvio(3);
        responseDTO.setTaxaMediaEquipamentosDefeituosos(0.1);
        responseDTO.setEstoqueMinimoSeguranca(2);

        when(parametrosSistemaService.criarParametro(any())).thenReturn(responseDTO);

        mockMvc.perform(post("/parametros-sistema")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idParametro").value(1))
                .andExpect(jsonPath("$.tempoMedioReposicao").value(7));
    }

    @Test
    void criarParametro_QuandoErro_DeveRetornar400() throws Exception {
        ParametroSistemaRequestDTO requestDTO = new ParametroSistemaRequestDTO();

        when(parametrosSistemaService.criarParametro(any())).thenThrow(new RuntimeException("Erro"));

        mockMvc.perform(post("/parametros-sistema")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void atualizarParametro_QuandoSucesso_DeveRetornar200() throws Exception {
        ParametroSistemaRequestDTO requestDTO = new ParametroSistemaRequestDTO();
        requestDTO.setTempoMedioReposicao(10);
        requestDTO.setTempoMedioConsumoEstoque(8);
        requestDTO.setTempoMedioEnvio(4);
        requestDTO.setTaxaMediaEquipamentosDefeituosos(0.2);
        requestDTO.setEstoqueMinimoSeguranca(3);

        ParametroSistemaResponseDTO responseDTO = new ParametroSistemaResponseDTO();
        responseDTO.setIdParametro(1);
        responseDTO.setTempoMedioReposicao(10);
        responseDTO.setTempoMedioConsumoEstoque(8);
        responseDTO.setTempoMedioEnvio(4);
        responseDTO.setTaxaMediaEquipamentosDefeituosos(0.2);
        responseDTO.setEstoqueMinimoSeguranca(3);

        when(parametrosSistemaService.atualizarParametro(any(Integer.class), any())).thenReturn(responseDTO);

        mockMvc.perform(put("/parametros-sistema/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idParametro").value(1))
                .andExpect(jsonPath("$.tempoMedioReposicao").value(10));
    }

    @Test
    void atualizarParametro_QuandoNaoEncontrar_DeveRetornar404() throws Exception {
        ParametroSistemaRequestDTO requestDTO = new ParametroSistemaRequestDTO();

        when(parametrosSistemaService.atualizarParametro(any(Integer.class), any()))
                .thenThrow(new RuntimeException("Parâmetro não encontrado"));

        mockMvc.perform(put("/parametros-sistema/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void atualizarParametro_QuandoErroValidacao_DeveRetornar400() throws Exception {
        ParametroSistemaRequestDTO requestDTO = new ParametroSistemaRequestDTO();

        when(parametrosSistemaService.atualizarParametro(any(Integer.class), any()))
                .thenThrow(new RuntimeException("Erro qualquer"));

        mockMvc.perform(put("/parametros-sistema/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deletarParametro_QuandoSucesso_DeveRetornar204() throws Exception {
        doNothing().when(parametrosSistemaService).deletarParametro(1);

        mockMvc.perform(delete("/parametros-sistema/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deletarParametro_QuandoNaoEncontrar_DeveRetornar404() throws Exception {
        doThrow(new RuntimeException("Parâmetro não encontrado")).when(parametrosSistemaService).deletarParametro(999);

        mockMvc.perform(delete("/parametros-sistema/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void gerarRelatorioPrevisaoFalta_QuandoSucesso_DeveRetornar200() throws Exception {
        PrevisaoFaltaResponseDTO dtoMock = PrevisaoFaltaResponseDTO.builder()
                .idTipoEquipamento(1)
                .nomeTipoEquipamento("Notebook")
                .numeroSerie("ABC123")
                .quantidadeDisponivelAtual(10)
                .quantidadeEmUsoAtual(5)
                .quantidadeDefeituosaAtual(1)
                .consumoDiarioEstimado(0.5)
                .leadTimeTotalDias(7)
                .estoqueMinimoSeguranca(2)
                .pontoDePedido(4.5)
                .emRiscoDeFalta(false)
                .mensagemAlerta("Estoque suficiente")
                .build();

        when(parametrosSistemaService.gerarRelatorioPrevisaoFalta()).thenReturn(List.of(dtoMock));

        mockMvc.perform(get("/parametros-sistema/relatorio-previsao-falta")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idTipoEquipamento").value(1));
    }

    @Test
    void gerarRelatorioPrevisaoFalta_QuandoErro_DeveRetornar400() throws Exception {
        when(parametrosSistemaService.gerarRelatorioPrevisaoFalta()).thenThrow(new RuntimeException("Erro"));

        mockMvc.perform(get("/parametros-sistema/relatorio-previsao-falta")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
