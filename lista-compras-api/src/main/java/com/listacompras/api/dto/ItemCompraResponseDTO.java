package com.listacompras.api.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemCompraResponseDTO {
    private Long id;
    private String nome;
    private Integer quantidade;
    private Double preco;
    private Long listaCompraId;
}
