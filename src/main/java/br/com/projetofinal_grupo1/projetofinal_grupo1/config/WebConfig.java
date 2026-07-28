package br.com.projetofinal_grupo1.projetofinal_grupo1.config;

import br.com.projetofinal_grupo1.projetofinal_grupo1.model.IdEquipamentoToColaborador;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new Converter<String, IdEquipamentoToColaborador>() {
            @Override
            public IdEquipamentoToColaborador convert(String source) {
                String[] parts = source.split("-");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Formato esperado: idEquipamento-idColaborador");
                }
                return new IdEquipamentoToColaborador(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            }
        });
    }
}
