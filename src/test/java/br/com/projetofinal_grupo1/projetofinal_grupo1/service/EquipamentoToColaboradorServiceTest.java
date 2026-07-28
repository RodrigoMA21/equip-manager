package br.com.projetofinal_grupo1.projetofinal_grupo1.service;

import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.EquipamentoToColaboradorRequestDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.EquipamentoToColaboradorResponseDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.Equipamento;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.EquipamentoToColaborador;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.IdEquipamentoToColaborador;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.ColaboradorRepository;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.EquipamentoRepository;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.EquipamentoToColaboradorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EquipamentoToColaboradorServiceTest {

    private EquipamentoToColaboradorRepository equipamentoToColaboradorRepository;
    private EquipamentoRepository equipamentoRepository;
    private ColaboradorRepository colaboradorRepository;
    private PrevisaoEntregaService previsaoEntregaService;
    private EquipamentoToColaboradorService equipamentoToColaboradorService;

    @BeforeEach
    void setUp() {
        equipamentoToColaboradorRepository = mock(EquipamentoToColaboradorRepository.class);
        equipamentoRepository = mock(EquipamentoRepository.class);
        colaboradorRepository = mock(ColaboradorRepository.class);
        previsaoEntregaService = mock(PrevisaoEntregaService.class);

        equipamentoToColaboradorService = new EquipamentoToColaboradorService(
                equipamentoToColaboradorRepository,
                equipamentoRepository,
                colaboradorRepository,
                previsaoEntregaService
        );
    }

    @Test
    void testListarTodos() {
        EquipamentoToColaborador etColaborador = new EquipamentoToColaborador();
        when(equipamentoToColaboradorRepository.findAll()).thenReturn(List.of(etColaborador));

        List<EquipamentoToColaboradorResponseDTO> resultado = equipamentoToColaboradorService.listarTodos();

        assertEquals(1, resultado.size());
        verify(equipamentoToColaboradorRepository).findAll();
    }

    @Test
    void testBuscarPorId_Encontrado() {
        IdEquipamentoToColaborador id = new IdEquipamentoToColaborador(1, 2);
        EquipamentoToColaborador etColaborador = new EquipamentoToColaborador();
        etColaborador.setId(id);

        when(equipamentoToColaboradorRepository.findById(id)).thenReturn(Optional.of(etColaborador));

        ResponseEntity<EquipamentoToColaboradorResponseDTO> response = equipamentoToColaboradorService.buscarPorId(id);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testBuscarPorId_NaoEncontrado() {
        IdEquipamentoToColaborador id = new IdEquipamentoToColaborador(1, 2);

        when(equipamentoToColaboradorRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<EquipamentoToColaboradorResponseDTO> response = equipamentoToColaboradorService.buscarPorId(id);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testSalvar_ComEquipamentoDisponivel() {
        Equipamento equipamento = new Equipamento();
        equipamento.setId(1);
        equipamento.setDisponivel(true);

        EquipamentoToColaboradorRequestDTO dto = mock(EquipamentoToColaboradorRequestDTO.class);
        IdEquipamentoToColaborador id = new IdEquipamentoToColaborador(1, 1);
        when(dto.getChaveCompostaEquipamentoColaborador()).thenReturn(id);

        EquipamentoToColaborador etColaborador = new EquipamentoToColaborador();
        when(dto.toEntity()).thenReturn(etColaborador);

        when(equipamentoRepository.findById(1)).thenReturn(Optional.of(equipamento));
        when(equipamentoToColaboradorRepository.save(any())).thenReturn(etColaborador);

        ResponseEntity<EquipamentoToColaboradorResponseDTO> response = equipamentoToColaboradorService.salvar(dto);

        assertEquals(201, response.getStatusCodeValue());
        assertFalse(equipamento.getDisponivel());
        verify(equipamentoRepository).save(equipamento);
    }

    @Test
    void testSalvar_ComEquipamentoIndisponivel() {
        Equipamento equipamento = new Equipamento();
        equipamento.setId(1);
        equipamento.setDisponivel(false);

        EquipamentoToColaboradorRequestDTO dto = mock(EquipamentoToColaboradorRequestDTO.class);
        IdEquipamentoToColaborador id = new IdEquipamentoToColaborador(1, 1);
        when(dto.getChaveCompostaEquipamentoColaborador()).thenReturn(id);

        when(equipamentoRepository.findById(1)).thenReturn(Optional.of(equipamento));

        assertThrows(IllegalStateException.class, () -> equipamentoToColaboradorService.salvar(dto));
    }

    @Test
    void testAtualizar_Encontrado() {
        IdEquipamentoToColaborador id = new IdEquipamentoToColaborador(1, 2);
        LocalDate entrega = LocalDate.now();
        LocalDate devolucao = entrega.plusDays(5);
        LocalDate previsao = entrega.plusDays(3);

        EquipamentoToColaboradorRequestDTO dto = mock(EquipamentoToColaboradorRequestDTO.class);

        EquipamentoToColaborador atualizado = new EquipamentoToColaborador();
        atualizado.setId(id);
        atualizado.setDataEntrega(entrega);
        atualizado.setDataDevolucao(devolucao);
        atualizado.setPrevisaoEntrega(previsao);

        when(dto.toEntity()).thenReturn(atualizado);
        when(equipamentoToColaboradorRepository.findById(id))
                .thenReturn(Optional.of(new EquipamentoToColaborador()));
        when(equipamentoToColaboradorRepository.save(any()))
                .thenReturn(atualizado); // 👈 importante!

        ResponseEntity<EquipamentoToColaboradorResponseDTO> response = equipamentoToColaboradorService.atualizar(id, dto);

        assertEquals(200, response.getStatusCodeValue());

        EquipamentoToColaboradorResponseDTO body = response.getBody();
        assertNotNull(body);
        assertEquals(entrega, body.getDataEntrega());
        assertEquals(devolucao, body.getDataDevolucao());
        assertEquals(previsao, body.getPrevisaoEntrega());
        assertEquals(id, body.getChaveCompostaEquipamentoColaborador());
    }


    @Test
    void testAtualizar_NaoEncontrado() {
        IdEquipamentoToColaborador id = new IdEquipamentoToColaborador(1, 2);
        EquipamentoToColaboradorRequestDTO dto = mock(EquipamentoToColaboradorRequestDTO.class);

        when(equipamentoToColaboradorRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<EquipamentoToColaboradorResponseDTO> response = equipamentoToColaboradorService.atualizar(id, dto);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testDeletar_Encontrado() {
        IdEquipamentoToColaborador id = new IdEquipamentoToColaborador(1, 2);
        when(equipamentoToColaboradorRepository.existsById(id)).thenReturn(true);

        ResponseEntity<Void> response = equipamentoToColaboradorService.deletar(id);

        assertEquals(204, response.getStatusCodeValue());
        verify(equipamentoToColaboradorRepository).deleteById(id);
    }

    @Test
    void testDeletar_NaoEncontrado() {
        IdEquipamentoToColaborador id = new IdEquipamentoToColaborador(1, 2);
        when(equipamentoToColaboradorRepository.existsById(id)).thenReturn(false);

        ResponseEntity<Void> response = equipamentoToColaboradorService.deletar(id);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testRegistrarDevolucao_Sucesso() {
        IdEquipamentoToColaborador id = new IdEquipamentoToColaborador(1, 2);
        EquipamentoToColaborador etColaborador = new EquipamentoToColaborador();
        etColaborador.setId(id);
        etColaborador.setDataDevolucao(null);

        Equipamento equipamento = new Equipamento();
        equipamento.setId(1);

        when(equipamentoToColaboradorRepository.findById(id)).thenReturn(Optional.of(etColaborador));
        when(equipamentoRepository.findById(id.getIdEquipamento())).thenReturn(Optional.of(equipamento));

        ResponseEntity<Void> response = equipamentoToColaboradorService.registrarDevolucao(id);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(etColaborador.getDataDevolucao());
        verify(equipamentoRepository).save(equipamento);
    }

    @Test
    void testRegistrarDevolucao_JaDevolvido() {
        IdEquipamentoToColaborador id = new IdEquipamentoToColaborador(1, 2);
        EquipamentoToColaborador etColaborador = new EquipamentoToColaborador();
        etColaborador.setDataDevolucao(LocalDate.now());

        when(equipamentoToColaboradorRepository.findById(id)).thenReturn(Optional.of(etColaborador));

        ResponseEntity<Void> response = equipamentoToColaboradorService.registrarDevolucao(id);

        assertEquals(400, response.getStatusCodeValue());
    }
}
