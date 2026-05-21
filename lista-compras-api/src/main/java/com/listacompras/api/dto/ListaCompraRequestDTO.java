package com.listacompras.api.dto;

import com.listacompras.api.validation.NotFutureDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ListaCompraRequestDTO {

    @NotBlank(message = "O nome da lista é obrigatório")
    private String nome;

    @NotNull(message = "A data de criação é obrigatória")
    @NotFutureDate(message = "A data de criação não pode ser no futuro")
    private LocalDate dataCriacao;

    @NotNull(message = "O mercado é obrigatório")
    private Long mercadoId;
}
