package com.aluralatam.literalura.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private int fecha_nacimiento;
    private int fecha_fallecimiento;

    @ManyToMany(mappedBy = "autores", fetch = FetchType.EAGER)
    private List<Libro> libro;

    public Autor() {
    }

    public Autor(String nombre, int fecha_nacimiento, int fecha_fallecimiento, List<Libro> libro) {
        this.nombre = nombre;
        this.fecha_nacimiento = fecha_nacimiento;
        this.fecha_fallecimiento = fecha_fallecimiento;
        this.libro = libro;
    }
    public Autor(String nombre, int fecha_nacimiento, int fecha_fallecimiento) {
        this.nombre = nombre;
        this.fecha_nacimiento = fecha_nacimiento;
        this.fecha_fallecimiento = fecha_fallecimiento;
    }

    public Autor(Autor autor, List<Libro> libro) {
        this.nombre = autor.getNombre();
        this.fecha_nacimiento = autor.getFecha_nacimiento();
        this.fecha_fallecimiento = autor.getFecha_fallecimiento();
        this.libro = libro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(int fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public int getFecha_fallecimiento() {
        return fecha_fallecimiento;
    }

    public void setFecha_fallecimiento(int fecha_fallecimiento) {
        this.fecha_fallecimiento = fecha_fallecimiento;
    }

    public List<Libro> getLibro() {
        return libro;
    }

    public void setLibro(List<Libro> libro) {
        this.libro = libro;
    }

    @Override
    public String toString() {
        String librosJoin = "[" + this.getLibro().stream().map(Libro::getTitulo).collect(Collectors.joining(", ")) + "]";

        return "Autor: " + this.getNombre() + "\n" +
                "Fecha de nacimiento: " + this.getFecha_nacimiento() + "\n" +
                "Fecha de fallecimiento: " + this.getFecha_fallecimiento() + "\n" +
                "Libros:  " + librosJoin + "\n";
    }
}