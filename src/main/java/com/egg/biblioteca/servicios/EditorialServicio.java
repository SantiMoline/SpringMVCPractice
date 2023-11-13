package com.egg.biblioteca.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egg.biblioteca.entidades.Editorial;
import com.egg.biblioteca.excepciones.BibliotecaException;
import com.egg.biblioteca.repositorios.EditorialRepositorio;

import jakarta.transaction.Transactional;

@Service
public class EditorialServicio {
    
    @Autowired
    private EditorialRepositorio editorialRepositorio;

    @Transactional
    public void crearEditorial(String nombre) throws BibliotecaException {
        validarNombre(nombre);

        Editorial editorial = new Editorial();
        editorial.setNombre(nombre);

        editorialRepositorio.save(editorial);
    }

    public List<Editorial> listarEditoriales() {
        List<Editorial> editoriales = new ArrayList<>();
        editoriales = editorialRepositorio.findAll();

        return editoriales;
    }

    @Transactional
    public void modificarEditorial(String id, String nombre) throws BibliotecaException {
        validarNombre(nombre);
        Optional<Editorial> editorialRta = editorialRepositorio.findById(id);

        if(editorialRta.isPresent()) {
            Editorial editorial = editorialRta.get();
            editorial.setNombre(nombre);

            editorialRepositorio.save(editorial);
        }
    }

    public Editorial getEditorial(String id) {
        return editorialRepositorio.getReferenceById(id); //Para no usar el getOne que está deprecado.
    }

    private void validarNombre(String nombre) throws BibliotecaException {
        if (nombre == null || nombre.isBlank())
            throw new BibliotecaException("Nombre del a editorial no puede ser nulo o estar en blanco.");
    }
}
