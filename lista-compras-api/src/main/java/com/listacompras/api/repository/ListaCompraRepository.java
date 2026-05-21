package com.listacompras.api.repository;

import com.listacompras.api.entity.ListaCompra;
import com.listacompras.api.repository.projection.ListaCompraResumoProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListaCompraRepository extends JpaRepository<ListaCompra, Long> {

    Page<ListaCompra> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    Page<ListaCompra> findByMercadoId(Long mercadoId, Pageable pageable);

    Page<ListaCompraResumoProjection> findAllProjectedBy(Pageable pageable);
}
