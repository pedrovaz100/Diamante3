package com.listacompras.api.service;

import com.listacompras.api.dto.MercadoRequestDTO;
import com.listacompras.api.dto.MercadoResponseDTO;
import com.listacompras.api.entity.Mercado;
import com.listacompras.api.repository.MercadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class MercadoService {

    private final MercadoRepository mercadoRepository;

    @Cacheable("mercados")
    public Page<MercadoResponseDTO> listar(Pageable pageable) {
        return mercadoRepository.findAll(pageable).map(this::toResponse);
    }

    @Cacheable(value = "mercado", key = "#id")
    public MercadoResponseDTO buscarPorId(Long id) {
        return toResponse(buscarEntidadePorId(id));
    }

    @Caching(evict = {
        @CacheEvict(value = "mercados", allEntries = true),
        @CacheEvict(value = "mercado", allEntries = true)
    })
    public MercadoResponseDTO criar(MercadoRequestDTO request) {
        Mercado mercado = Mercado.builder()
                .nome(request.getNome())
                .endereco(request.getEndereco())
                .build();
        return toResponse(mercadoRepository.save(mercado));
    }

    @Caching(evict = {
        @CacheEvict(value = "mercados", allEntries = true),
        @CacheEvict(value = "mercado", key = "#id")
    })
    public MercadoResponseDTO atualizar(Long id, MercadoRequestDTO request) {
        Mercado mercado = buscarEntidadePorId(id);
        mercado.setNome(request.getNome());
        mercado.setEndereco(request.getEndereco());
        return toResponse(mercadoRepository.save(mercado));
    }

    @Caching(evict = {
        @CacheEvict(value = "mercados", allEntries = true),
        @CacheEvict(value = "mercado", key = "#id")
    })
    public void deletar(Long id) {
        mercadoRepository.delete(buscarEntidadePorId(id));
    }

    public Mercado buscarEntidadePorId(Long id) {
        return mercadoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Mercado com id " + id + " não encontrado"));
    }

    private MercadoResponseDTO toResponse(Mercado mercado) {
        return MercadoResponseDTO.builder()
                .id(mercado.getId())
                .nome(mercado.getNome())
                .endereco(mercado.getEndereco())
                .build();
    }
}
