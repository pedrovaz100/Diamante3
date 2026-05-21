package com.listacompras.api.repository;

import com.listacompras.api.entity.Mercado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MercadoRepository extends JpaRepository<Mercado, Long> {
}