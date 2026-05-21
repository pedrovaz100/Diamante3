package com.listacompras.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MercadoRequestDTO {

    @NotBlank(message = "O nome do mercado é obrigatório")
    private String nome;

    @NotBlank(message = "O endereço do mercado é obrigatório")
    private String endereco;
}
