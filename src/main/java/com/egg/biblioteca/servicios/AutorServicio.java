package com.egg.biblioteca.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.excepciones.BibliotecaException;
import com.egg.biblioteca.repositorios.AutorRepositorio;

import jakarta.transaction.Transactional;

@Service
public class AutorServicio {
    
    @Autowired
    private AutorRepositorio autorRepositorio;

    @Transactional
    public void crearAutor(String nombre) throws BibliotecaException {
        validarNombre(nombre);
        Autor autor = new Autor();
        autor.setNombre(nombre);

        autorRepositorio.save(autor);
    }

    public List<Autor> listarAutores() {
        List<Autor> autores = new ArrayList<>();
        autores = autorRepositorio.findAll();

        return autores;
    }

    @Transactional
    public void modificarAutor(String id, String nombre) throws BibliotecaException {
        validarNombre(nombre);
        Optional<Autor> autorRta = autorRepositorio.findById(id);

        if(autorRta.isPresent()) {
            Autor autor = autorRta.get();
            autor.setNombre(nombre);

            autorRepositorio.save(autor);
        }
    }

    public Autor getAutor(String id) {
        return autorRepositorio.getReferenceById(id); //Para no usar el getOne que está deprecado.
    }

    private void validarNombre(String name) throws BibliotecaException {
        if(name == null || name.isBlank())
            throw new BibliotecaException("Nombre del autor no puede ser nulo o estar en blanco.");
    }
}
