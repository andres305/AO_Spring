package com.gestionproyectos.demo.repository;

import com.gestionproyectos.demo.model.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
}
