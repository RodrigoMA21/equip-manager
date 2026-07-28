package br.com.projetofinal_grupo1.projetofinal_grupo1.service;

import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.ColaboradorRequestDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.ColaboradorResponseDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.dto.ColaboradorUpdateDTO;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.Colaborador;
import br.com.projetofinal_grupo1.projetofinal_grupo1.model.Endereco;
import br.com.projetofinal_grupo1.projetofinal_grupo1.feign.EnderecoClient;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.ColaboradorRepository;
import br.com.projetofinal_grupo1.projetofinal_grupo1.repository.EnderecoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ColaboradorService {

    private final ColaboradorRepository colaboradorRepository;
    private final EnderecoRepository enderecoRepository;
    private final EnderecoClient enderecoClient;

    public ColaboradorService(ColaboradorRepository colaboradorRepository, EnderecoRepository enderecoRepository, EnderecoClient enderecoClient) {
        this.colaboradorRepository = colaboradorRepository;
        this.enderecoRepository = enderecoRepository;
        this.enderecoClient = enderecoClient;
    }

    public void registrarColaborador(ColaboradorRequestDTO colaboradorRequestDTO){
        Colaborador colaborador = Colaborador.converterParaColaborador(colaboradorRequestDTO);
        Endereco enderecoDoColaborador = enderecoClient.retornarEnderecoPorCep(colaborador.getCep());
        enderecoDoColaborador.setNumero(colaboradorRequestDTO.getNumeroCasa());
        if (colaboradorRequestDTO.getComplemento() != null) {
            enderecoDoColaborador.setComplemento(colaboradorRequestDTO.getComplemento());
        }

        colaborador.setEndereco(enderecoDoColaborador);
        enderecoDoColaborador.setColaborador(colaborador);

        colaboradorRepository.save(colaborador);
        enderecoRepository.save(enderecoDoColaborador);
    }

    public List<ColaboradorResponseDTO> devolverTodosOsColaborador(){
        List<Colaborador>listaDeColaboradores = colaboradorRepository.findAll();
        List<ColaboradorResponseDTO> listaDeColaboradoresResponseDTO = new ArrayList<>();

        for (Colaborador colaborador: listaDeColaboradores){
            listaDeColaboradoresResponseDTO.add(ColaboradorResponseDTO.converterParaColaboradorResponseDTO(colaborador));
        }

        return listaDeColaboradoresResponseDTO;
    }
    public ColaboradorResponseDTO devolverColaboradorPorId(Long id){
        return ColaboradorResponseDTO.converterParaColaboradorResponseDTO(colaboradorRepository.findById(Math.toIntExact(id)).get());
    }

    public void editarNomeEmailDataContratacaoDataRecisaoEspecificacaoEquipamentoDoColaborador(int id, ColaboradorUpdateDTO colaboradorUpdateDTO){
        Colaborador colaborador = colaboradorRepository.findById(Math.toIntExact(id)).get();
        colaborador.atualizarColaborador(colaboradorUpdateDTO);
        colaboradorRepository.editarColaborador(Math.toIntExact(id), colaborador.getNome(), colaborador.getEmail(), colaborador.getCep()
                , colaborador.getDataContratacaoInicio(), colaborador.getDataContratacaoRecisao()
                , colaborador.getEquipamentoEspecificacao());

        Endereco enderecoNovo = enderecoClient.retornarEnderecoPorCep(colaborador.getCep());
        Endereco enderecoDoColaborador = enderecoRepository.findById((int) id).get();

        if(!enderecoNovo.getCep().equals(enderecoDoColaborador.getCep())){
            enderecoRepository.editarEndereco(Math.toIntExact(id), enderecoNovo.getCep(), enderecoNovo.getLogradouro(), enderecoNovo.getComplemento()
                    , enderecoNovo.getUnidade(), enderecoNovo.getBairro(), enderecoNovo.getLocalidade(), enderecoNovo.getUf()
                    , enderecoNovo.getEstado(), enderecoNovo.getRegiao(), enderecoNovo.getIbge(), enderecoNovo.getGia()
                    , enderecoNovo.getDdd(), enderecoNovo.getSiafi());
        }
    }

    public void deletarColaboradorPorId(int id){
        Optional<Colaborador> colaboradorOptional = colaboradorRepository.findById(id);
        if(colaboradorOptional.isPresent()){
            Colaborador colaborador = colaboradorOptional.get();
            colaborador.setAtivo(false);
            colaboradorRepository.save(colaborador);
        }
    }
    public List<ColaboradorResponseDTO> devolverColaboradoresInativos() {
        List<Colaborador> inativos = colaboradorRepository.findByAtivoFalse();
        List<ColaboradorResponseDTO> listaDTO = new ArrayList<>();
        for (Colaborador c : inativos) {
            listaDTO.add(ColaboradorResponseDTO.converterParaColaboradorResponseDTO(c));
        }
        return listaDTO;
    }
    public void reativarColaborador(int id) {
        colaboradorRepository.reativarColaborador(id);
    }

}

