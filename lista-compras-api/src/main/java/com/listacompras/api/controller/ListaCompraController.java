package com.listacompras.api.controller;

import com.listacompras.api.dto.ListaCompraRequestDTO;
import com.listacompras.api.dto.ListaCompraResponseDTO;
import com.listacompras.api.dto.ListaCompraResumoDTO;
import com.listacompras.api.service.ListaCompraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
@RequestMapping("/listas")
@RequiredArgsConstructor
@Validated
@Tag(name = "Listas de Compras", description = "Operações de gerenciamento de listas de compras")
public class ListaCompraController {

    private final ListaCompraService listaCompraService;

    @GetMapping
    @Operation(summary = "Listar todas as listas de compras",
               description = "Retorna lista paginada de listas de compras. Suporta paginação e ordenação: ?page=0&size=10&sort=nome,asc")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    public Page<EntityModel<ListaCompraResponseDTO>> listar(
            @Parameter(hidden = true) Pageable pageable) {
        return listaCompraService.listar(pageable).map(dto ->
                EntityModel.of(dto,
                        linkTo(methodOn(ListaCompraController.class).buscar(dto.getId())).withSelfRel(),
                        linkTo(methodOn(ListaCompraController.class).listar(pageable)).withRel("listas")
                )
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar lista de compras por ID", description = "Retorna os dados de uma lista específica com seus itens")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista encontrada"),
        @ApiResponse(responseCode = "404", description = "Lista não encontrada")
    })
    public EntityModel<ListaCompraResponseDTO> buscar(
            @Parameter(description = "ID da lista de compras", required = true) @PathVariable Long id) {
        ListaCompraResponseDTO dto = listaCompraService.buscarPorId(id);
        return EntityModel.of(dto,
                linkTo(methodOn(ListaCompraController.class).buscar(id)).withSelfRel(),
                linkTo(methodOn(ListaCompraController.class).listar(Pageable.unpaged())).withRel("listas")
        );
    }

    @PostMapping
    @Operation(summary = "Criar nova lista de compras", description = "Cria uma nova lista de compras associada a um mercado")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Lista criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Mercado não encontrado")
    })
    public ResponseEntity<EntityModel<ListaCompraResponseDTO>> criar(
            @Valid @RequestBody ListaCompraRequestDTO listaCompra) {
        ListaCompraResponseDTO dto = listaCompraService.criar(listaCompra);
        EntityModel<ListaCompraResponseDTO> model = EntityModel.of(dto,
                linkTo(methodOn(ListaCompraController.class).buscar(dto.getId())).withSelfRel(),
                linkTo(methodOn(ListaCompraController.class).listar(Pageable.unpaged())).withRel("listas")
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar lista de compras", description = "Atualiza os dados de uma lista existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista atualizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Lista ou mercado não encontrado")
    })
    public EntityModel<ListaCompraResponseDTO> atualizar(
            @Parameter(description = "ID da lista de compras", required = true) @PathVariable Long id,
            @Valid @RequestBody ListaCompraRequestDTO listaCompra) {
        ListaCompraResponseDTO dto = listaCompraService.atualizar(id, listaCompra);
        return EntityModel.of(dto,
                linkTo(methodOn(ListaCompraController.class).buscar(id)).withSelfRel(),
                linkTo(methodOn(ListaCompraController.class).listar(Pageable.unpaged())).withRel("listas")
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar lista de compras", description = "Remove uma lista de compras e seus itens do sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Lista removida com sucesso"),
        @ApiResponse(responseCode = "404", description = "Lista não encontrada")
    })
    public void deletar(
            @Parameter(description = "ID da lista de compras", required = true) @PathVariable Long id) {
        listaCompraService.deletar(id);
    }

    @GetMapping("/busca/nome")
    @Operation(summary = "Buscar listas por nome", description = "Busca listas de compras pelo nome (case-insensitive, busca parcial)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Resultado da busca")
    })
    public Page<EntityModel<ListaCompraResponseDTO>> buscarPorNome(
            @Parameter(description = "Nome ou parte do nome da lista", required = true)
            @RequestParam @NotBlank String nome,
            @Parameter(hidden = true) Pageable pageable) {
        return listaCompraService.buscarPorNome(nome, pageable).map(dto ->
                EntityModel.of(dto,
                        linkTo(methodOn(ListaCompraController.class).buscar(dto.getId())).withSelfRel()
                )
        );
    }

    @GetMapping("/busca/mercado/{mercadoId}")
    @Operation(summary = "Buscar listas por mercado", description = "Retorna todas as listas de compras associadas a um mercado")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Resultado da busca")
    })
    public Page<EntityModel<ListaCompraResponseDTO>> buscarPorMercado(
            @Parameter(description = "ID do mercado", required = true) @PathVariable Long mercadoId,
            @Parameter(hidden = true) Pageable pageable) {
        return listaCompraService.buscarPorMercado(mercadoId, pageable).map(dto ->
                EntityModel.of(dto,
                        linkTo(methodOn(ListaCompraController.class).buscar(dto.getId())).withSelfRel()
                )
        );
    }

    @GetMapping("/resumo")
    @Operation(summary = "Listar resumo das listas (Projection)",
               description = "Retorna apenas id e nome das listas usando JPA Projection para melhor performance")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Resumo retornado com sucesso")
    })
    public Page<ListaCompraResumoDTO> listarResumo(
            @Parameter(hidden = true) Pageable pageable) {
        return listaCompraService.listarResumo(pageable);
    }
}
