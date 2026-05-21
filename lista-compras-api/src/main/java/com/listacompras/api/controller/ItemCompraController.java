package com.listacompras.api.controller;

import com.listacompras.api.dto.ItemCompraRequestDTO;
import com.listacompras.api.dto.ItemCompraResponseDTO;
import com.listacompras.api.service.ItemCompraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/itens")
@RequiredArgsConstructor
@Validated
@Tag(name = "Itens de Compra", description = "Operações de gerenciamento de itens dentro das listas de compras")
public class ItemCompraController {

    private final ItemCompraService itemCompraService;

    @GetMapping
    @Operation(summary = "Listar todos os itens de compra",
               description = "Retorna lista paginada de itens. Suporta paginação e ordenação: ?page=0&size=10&sort=nome,asc")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    public Page<EntityModel<ItemCompraResponseDTO>> listar(
            @Parameter(hidden = true) Pageable pageable) {
        return itemCompraService.listar(pageable).map(dto ->
                EntityModel.of(dto,
                        linkTo(methodOn(ItemCompraController.class).buscar(dto.getId())).withSelfRel(),
                        linkTo(methodOn(ItemCompraController.class).listar(pageable)).withRel("itens")
                )
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar item por ID", description = "Retorna os dados de um item específico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Item encontrado"),
        @ApiResponse(responseCode = "404", description = "Item não encontrado")
    })
    public EntityModel<ItemCompraResponseDTO> buscar(
            @Parameter(description = "ID do item de compra", required = true) @PathVariable Long id) {
        ItemCompraResponseDTO dto = itemCompraService.buscarPorId(id);
        return EntityModel.of(dto,
                linkTo(methodOn(ItemCompraController.class).buscar(id)).withSelfRel(),
                linkTo(methodOn(ItemCompraController.class).listar(Pageable.unpaged())).withRel("itens")
        );
    }

    @PostMapping
    @Operation(summary = "Criar novo item de compra", description = "Adiciona um novo item a uma lista de compras existente. Preço máximo: R$10.000,00. Quantidade máxima: 1000.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Item criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou regra de negócio violada"),
        @ApiResponse(responseCode = "404", description = "Lista de compras não encontrada")
    })
    public ResponseEntity<EntityModel<ItemCompraResponseDTO>> criar(
            @Valid @RequestBody ItemCompraRequestDTO itemCompra) {
        ItemCompraResponseDTO dto = itemCompraService.criar(itemCompra);
        EntityModel<ItemCompraResponseDTO> model = EntityModel.of(dto,
                linkTo(methodOn(ItemCompraController.class).buscar(dto.getId())).withSelfRel(),
                linkTo(methodOn(ItemCompraController.class).listar(Pageable.unpaged())).withRel("itens")
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar item de compra", description = "Atualiza os dados de um item existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Item atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Item não encontrado")
    })
    public EntityModel<ItemCompraResponseDTO> atualizar(
            @Parameter(description = "ID do item de compra", required = true) @PathVariable Long id,
            @Valid @RequestBody ItemCompraRequestDTO itemCompra) {
        ItemCompraResponseDTO dto = itemCompraService.atualizar(id, itemCompra);
        return EntityModel.of(dto,
                linkTo(methodOn(ItemCompraController.class).buscar(id)).withSelfRel(),
                linkTo(methodOn(ItemCompraController.class).listar(Pageable.unpaged())).withRel("itens")
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar item de compra", description = "Remove um item de uma lista de compras")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Item removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Item não encontrado")
    })
    public void deletar(
            @Parameter(description = "ID do item de compra", required = true) @PathVariable Long id) {
        itemCompraService.deletar(id);
    }

    @GetMapping("/busca/nome")
    @Operation(summary = "Buscar itens por nome", description = "Busca itens pelo nome (case-insensitive, busca parcial)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Resultado da busca")
    })
    public Page<EntityModel<ItemCompraResponseDTO>> buscarPorNome(
            @Parameter(description = "Nome ou parte do nome do item", required = true)
            @RequestParam @NotBlank String nome,
            @Parameter(hidden = true) Pageable pageable) {
        return itemCompraService.buscarPorNome(nome, pageable).map(dto ->
                EntityModel.of(dto,
                        linkTo(methodOn(ItemCompraController.class).buscar(dto.getId())).withSelfRel()
                )
        );
    }

    @GetMapping("/busca/preco")
    @Operation(summary = "Buscar itens por faixa de preço", description = "Retorna itens cujo preço esteja entre precoMin e precoMax")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Resultado da busca"),
        @ApiResponse(responseCode = "400", description = "Faixa de preço inválida")
    })
    public Page<EntityModel<ItemCompraResponseDTO>> buscarPorFaixaPreco(
            @Parameter(description = "Preço mínimo", required = true) @RequestParam @NotNull @Positive Double precoMin,
            @Parameter(description = "Preço máximo", required = true) @RequestParam @NotNull @Positive Double precoMax,
            @Parameter(hidden = true) Pageable pageable) {
        return itemCompraService.buscarPorFaixaPreco(precoMin, precoMax, pageable).map(dto ->
                EntityModel.of(dto,
                        linkTo(methodOn(ItemCompraController.class).buscar(dto.getId())).withSelfRel()
                )
        );
    }

    @GetMapping("/busca/quantidade-minima")
    @Operation(summary = "Buscar itens por quantidade mínima", description = "Retorna itens com quantidade maior ou igual ao valor informado")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Resultado da busca")
    })
    public Page<EntityModel<ItemCompraResponseDTO>> buscarPorQuantidadeMinima(
            @Parameter(description = "Quantidade mínima", required = true) @RequestParam @NotNull @Positive Integer quantidade,
            @Parameter(hidden = true) Pageable pageable) {
        return itemCompraService.buscarPorQuantidadeMinima(quantidade, pageable).map(dto ->
                EntityModel.of(dto,
                        linkTo(methodOn(ItemCompraController.class).buscar(dto.getId())).withSelfRel()
                )
        );
    }
}
