package br.com.projetofinal_grupo1.projetofinal_grupo1.feign;

import br.com.projetofinal_grupo1.projetofinal_grupo1.model.Endereco;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "endereco-client", url = "http://viacep.com.br/ws")
public interface EnderecoClient {

    @GetMapping("{cep}/json")
    Endereco retornarEnderecoPorCep(@PathVariable(name = "cep") String cep);

}