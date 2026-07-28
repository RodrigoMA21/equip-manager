package br.com.projetofinal_grupo1.projetofinal_grupo1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UsuarioResponseDTO {
    private int id;
    private String email;
    private boolean emailConfirmado;
    private List<String> authorities;

}
