package com.aluralatam.literalura.repository;

import com.aluralatam.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    @Query("SELECT l from Libro l WHERE l.titulo LIKE %:titulo%")
    Libro buscaLibroPorTitulo(@Param("titulo") String titulo);

    @Query("SELECT DISTINCT l.idioma FROM Libro l")
    List<String> idiomas();

    @Query("SELECT l FROM Libro l WHERE l.idioma = :codigoIdioma")
    List<Libro> librosPorIdioma(@Param("codigoIdioma") String codigoIdioma);

}