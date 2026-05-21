package com.listacompras.api.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class ListaCompraResponseDTO {
    private Long id;
    private String nome;
    private LocalDate dataCriacao;
    private MercadoResponseDTO mercado;
    private List<ItemCompraResponseDTO> itens;
}
