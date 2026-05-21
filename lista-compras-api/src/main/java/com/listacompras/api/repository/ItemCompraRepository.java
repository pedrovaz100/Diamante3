package com.listacompras.api.repository;

import com.listacompras.api.entity.ItemCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemCompraRepository extends JpaRepository<ItemCompra, Long> {

    Page<ItemCompra> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    Page<ItemCompra> findByPrecoBetween(Double precoMin, Double precoMax, Pageable pageable);

    Page<ItemCompra> findByQuantidadeGreaterThanEqual(Integer quantidade, Pageable pageable);
}
