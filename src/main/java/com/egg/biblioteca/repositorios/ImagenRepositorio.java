package com.egg.biblioteca.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.egg.biblioteca.entidades.Imagen;

public interface ImagenRepositorio extends JpaRepository<Imagen, String> {
    
}
