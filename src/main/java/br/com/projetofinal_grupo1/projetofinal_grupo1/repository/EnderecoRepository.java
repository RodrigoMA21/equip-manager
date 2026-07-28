package br.com.projetofinal_grupo1.projetofinal_grupo1.repository;

import br.com.projetofinal_grupo1.projetofinal_grupo1.model.Endereco;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {
    @Modifying
    @Transactional
    @Query("UPDATE Endereco end SET end.cep = :cep, end.logradouro = :logradouro, end.complemento = :complemento, end.unidade = :unidade," +
            " end.bairro = :bairro, end.localidade = :localidade, end.uf = :uf, end.estado = :estado, end.regiao = :regiao, end.ibge = :ibge," +
            " end.gia = :gia, end.ddd = :ddd, end.siafi = :siafi WHERE end.id = :id")
    void editarEndereco(@Param("id") int id, @Param("cep") String cep, @Param("logradouro") String logradouro
                        , @Param("complemento") String complemento, @Param("unidade") String unidade, @Param("bairro") String bairro
                        , @Param("localidade") String localidade, @Param("uf") String uf, @Param("estado") String estado
                        , @Param("regiao") String regiao, @Param("ibge") String ibge, @Param("gia") String gia, @Param("ddd") String ddd
                        , @Param("siafi") String siafi);
}
