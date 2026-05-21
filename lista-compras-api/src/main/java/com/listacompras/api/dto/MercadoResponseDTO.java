package com.listacompras.api.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MercadoResponseDTO {
    private Long id;
    private String nome;
    private String endereco;
}
