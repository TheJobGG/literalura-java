package com.aluralatam.literalura.repository;

import com.aluralatam.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    @Query("Select a from Autor a WHERE :anio BETWEEN a.fecha_nacimiento AND a.fecha_fallecimiento")
    List<Autor> autoresVivosEnAnio(@Param("anio") int anio);
}
