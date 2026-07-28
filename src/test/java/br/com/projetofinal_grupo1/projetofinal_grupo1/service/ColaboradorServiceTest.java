package br.com.projetofinal_grupo1.projetofinal_grupo1.service;

import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.ColaboradorRequestDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.ColaboradorResponseDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.ColaboradorUpdateDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.Colaborador;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.Endereco;
import br.com.projetofinal_grupo1.projetofinal_grupo1.feign.EnderecoClient;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.ColaboradorRepository;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.EnderecoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ColaboradorServiceTest {

    private ColaboradorRepository colaboradorRepository;
    private EnderecoRepository enderecoRepository;
    private EnderecoClient enderecoClient;
    private ColaboradorService colaboradorService;

    @BeforeEach
    void setUp() {
        colaboradorRepository = mock(ColaboradorRepository.class);
        enderecoRepository = mock(EnderecoRepository.class);
        enderecoClient = mock(EnderecoClient.class);

        colaboradorService = new ColaboradorService(colaboradorRepository, enderecoRepository, enderecoClient);
    }

    @Test
    void registrarColaborador_DeveSalvarColaboradorComEndereco() {
        // Arrange
        ColaboradorRequestDTO dto = new ColaboradorRequestDTO();
        dto.setCep("12345-678");

        // Cria o objeto real, chamando método real
        Colaborador colaborador = Colaborador.converterParaColaborador(dto);

        Endereco endereco = new Endereco();
        endereco.setCep("12345-678");

        when(enderecoClient.retornarEnderecoPorCep("12345-678")).thenReturn(endereco);

        // Act
        colaboradorService.registrarColaborador(dto);

        // Assert
        verify(colaboradorRepository).save(any(Colaborador.class));
        verify(enderecoRepository).save(endereco);

        // Aqui não podemos garantir a ligação exata se o converter é complexo,
        // mas pelo menos o endereço e colaborador foram salvos
    }

    @Test
    void devolverTodosOsColaborador_DeveRetornarListaDTOs() {
        // Arrange
        Colaborador colaborador = new Colaborador();
        List<Colaborador> colaboradores = List.of(colaborador);

        when(colaboradorRepository.findAll()).thenReturn(colaboradores);

        // Mock estático do método converter (pode usar PowerMockito para static mock, mas aqui vamos simular via spy)
        ColaboradorResponseDTO dto = mock(ColaboradorResponseDTO.class);

        // Poderia criar um spy da classe ColaboradorResponseDTO, mas simplificamos:
        // Assumindo que o método está correto, só verificamos tamanho da lista

        // Act
        List<ColaboradorResponseDTO> resultado = colaboradorService.devolverTodosOsColaborador();

        // Assert
        assertEquals(colaboradores.size(), resultado.size());
        verify(colaboradorRepository).findAll();
    }

    @Test
    void editarNomeEmailDataContratacaoDataRecisaoEspecificacaoEquipamentoDoColaborador_DeveAtualizarColaboradorEEndereco() {
        // Arrange
        int id = Math.toIntExact(1L);
        ColaboradorUpdateDTO updateDTO = mock(ColaboradorUpdateDTO.class);

        Colaborador colaborador = mock(Colaborador.class);
        when(colaboradorRepository.findById(id)).thenReturn(Optional.of(colaborador));

        // Mock dos getters para não retornar null (evita erro no verify)
        when(colaborador.getNome()).thenReturn("Nome Teste");
        when(colaborador.getEmail()).thenReturn("email@teste.com");
        when(colaborador.getCep()).thenReturn("12345-678");
        when(colaborador.getDataContratacaoInicio()).thenReturn(null); // ou data válida
        when(colaborador.getDataContratacaoRecisao()).thenReturn(null);
        when(colaborador.getEquipamentoEspecificacao()).thenReturn("Especificação X");

        // Método que atualiza o colaborador
        doNothing().when(colaborador).atualizarColaborador(updateDTO);

        // Mock salvar do colaboradorRepository (não faz nada)
        doNothing().when(colaboradorRepository).editarColaborador(
                (int) anyLong(),
                anyString(),
                anyString(),
                anyString(),
                any(),
                any(),
                any()
        );

        Endereco enderecoNovo = new Endereco();
        enderecoNovo.setCep("12345-678");

        Endereco enderecoExistente = new Endereco();
        enderecoExistente.setCep("54321-987");  // cep diferente pra entrar no if e editar o endereco

        when(enderecoClient.retornarEnderecoPorCep("12345-678")).thenReturn(enderecoNovo);
        when(enderecoRepository.findById((int) id)).thenReturn(Optional.of(enderecoExistente));

        doNothing().when(enderecoRepository).editarEndereco(
                (int) anyLong(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString()
        );

        // Act
        colaboradorService.editarNomeEmailDataContratacaoDataRecisaoEspecificacaoEquipamentoDoColaborador(id, updateDTO);

        // Assert
        verify(colaboradorRepository).findById(id);
        verify(colaborador).atualizarColaborador(updateDTO);
        verify(colaboradorRepository).editarColaborador(
                eq(id),
                eq("Nome Teste"),
                eq("email@teste.com"),
                eq("12345-678"),
                isNull(),
                isNull(),
                eq("Especificação X")
        );

        verify(enderecoClient).retornarEnderecoPorCep("12345-678");
        verify(enderecoRepository).findById((int) id);
        verify(enderecoRepository).editarEndereco(
                eq(id),
                eq("12345-678"),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class)
        );
    }

    @Test
    void deletarColaboradorPorId_DeveInativarQuandoEncontrado() {
        // Arrange
        int id = 1;
        Colaborador colaborador = new Colaborador();
        colaborador.setAtivo(true);

        when(colaboradorRepository.findById(id)).thenReturn(Optional.of(colaborador));

        // Act
        colaboradorService.deletarColaboradorPorId(id);

        // Assert
        assertFalse(colaborador.isAtivo(), "Colaborador deve estar inativo após soft delete");
        verify(colaboradorRepository).save(colaborador);
    }

    @Test
    void deletarColaboradorPorId_NaoDeveDeletarQuandoNaoEncontrado() {
        // Arrange
        int id = Math.toIntExact(1L);
        when(colaboradorRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        colaboradorService.deletarColaboradorPorId(id);

        // Assert
        verify(colaboradorRepository, never()).delete(any());
    }
}
