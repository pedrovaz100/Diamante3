package com.listacompras.api.controller;

import com.listacompras.api.dto.MercadoRequestDTO;
import com.listacompras.api.dto.MercadoResponseDTO;
import com.listacompras.api.service.MercadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/mercados")
@RequiredArgsConstructor
@Tag(name = "Mercados", description = "Operações de gerenciamento de mercados e lojas")
public class MercadoController {

    private final MercadoService mercadoService;

    @GetMapping
    @Operation(summary = "Listar todos os mercados",
               description = "Retorna lista paginada de mercados. Suporta paginação e ordenação: ?page=0&size=10&sort=nome,asc")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    public Page<EntityModel<MercadoResponseDTO>> listar(
            @Parameter(hidden = true) Pageable pageable) {
        return mercadoService.listar(pageable).map(dto ->
                EntityModel.of(dto,
                        linkTo(methodOn(MercadoController.class).buscar(dto.getId())).withSelfRel(),
                        linkTo(methodOn(MercadoController.class).listar(pageable)).withRel("mercados")
                )
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar mercado por ID", description = "Retorna os dados de um mercado específico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Mercado encontrado"),
        @ApiResponse(responseCode = "404", description = "Mercado não encontrado")
    })
    public EntityModel<MercadoResponseDTO> buscar(
            @Parameter(description = "ID do mercado", required = true) @PathVariable Long id) {
        MercadoResponseDTO dto = mercadoService.buscarPorId(id);
        return EntityModel.of(dto,
                linkTo(methodOn(MercadoController.class).buscar(id)).withSelfRel(),
                linkTo(methodOn(MercadoController.class).listar(Pageable.unpaged())).withRel("mercados")
        );
    }

    @PostMapping
    @Operation(summary = "Criar novo mercado", description = "Cria um novo mercado com nome e endereço")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Mercado criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<EntityModel<MercadoResponseDTO>> criar(
            @Valid @RequestBody MercadoRequestDTO mercado) {
        MercadoResponseDTO dto = mercadoService.criar(mercado);
        EntityModel<MercadoResponseDTO> model = EntityModel.of(dto,
                linkTo(methodOn(MercadoController.class).buscar(dto.getId())).withSelfRel(),
                linkTo(methodOn(MercadoController.class).listar(Pageable.unpaged())).withRel("mercados")
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar mercado", description = "Atualiza os dados de um mercado existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Mercado atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Mercado não encontrado")
    })
    public EntityModel<MercadoResponseDTO> atualizar(
            @Parameter(description = "ID do mercado", required = true) @PathVariable Long id,
            @Valid @RequestBody MercadoRequestDTO mercado) {
        MercadoResponseDTO dto = mercadoService.atualizar(id, mercado);
        return EntityModel.of(dto,
                linkTo(methodOn(MercadoController.class).buscar(id)).withSelfRel(),
                linkTo(methodOn(MercadoController.class).listar(Pageable.unpaged())).withRel("mercados")
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar mercado", description = "Remove um mercado do sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Mercado removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Mercado não encontrado")
    })
    public void deletar(
            @Parameter(description = "ID do mercado", required = true) @PathVariable Long id) {
        mercadoService.deletar(id);
    }
}
