package com.egg.biblioteca.servicios;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.entidades.Editorial;
import com.egg.biblioteca.entidades.Libro;
import com.egg.biblioteca.excepciones.BibliotecaException;
import com.egg.biblioteca.repositorios.AutorRepositorio;
import com.egg.biblioteca.repositorios.EditorialRepositorio;
import com.egg.biblioteca.repositorios.LibroRepositorio;

import jakarta.transaction.Transactional;

@Service
public class LibroServicio {

    @Autowired //Inyección de dependencias. No hace falta inicializar la variable, ya que lo realiza Spring.
    private LibroRepositorio libroRepositorio;
    @Autowired
    private AutorRepositorio autorRepositorio;
    @Autowired
    private EditorialRepositorio editorialRepositorio;
    
    @Transactional
    public void crearLibro(Long isbn, String titulo, Integer ejemplares, String idAutor, String idEditorial) throws BibliotecaException {
        validarDatos(isbn, titulo, ejemplares, idAutor, idEditorial);


        Autor autor = autorRepositorio.findById(idAutor).get();
        Editorial editorial = editorialRepositorio.findById(idEditorial).get();
        Libro libro = new Libro();
        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setEjemplares(ejemplares);
        libro.setAutor(autor);
        libro.setEditorial(editorial);

        libro.setAlta(new Date()); //new Date() arroja el Date correspondiente al momento en el que se instancia.

        libroRepositorio.save(libro);
    }

    public List<Libro> listarLibros() {
        List<Libro> libros = new ArrayList<>();
        libros = libroRepositorio.findAll();

        return libros;
    }

    @Transactional
    public void modificarLibro(Long isbn, String titulo, String idAutor, String idEditorial, int ejemplares) throws BibliotecaException {
        validarDatos(isbn, titulo, ejemplares, idAutor, idEditorial);

        Optional<Libro> rtaLibro = libroRepositorio.findById(isbn);
        //El wrapper optional envuelve el objeto de encontrarlo en lBiblia base de datos, caso contrario, queda vacío.

        Optional<Autor> rtaAutor = autorRepositorio.findById(idAutor);
        Optional<Editorial> rtaEditorial = editorialRepositorio.findById(idEditorial); 

        if(rtaAutor.isEmpty() || rtaEditorial.isEmpty())
            throw new NoSuchElementException("Id de autor o de editorial inválida.");

        Autor autor = new Autor();
        Editorial editorial = new Editorial();

        if (rtaAutor.isPresent())
            autor = rtaAutor.get();
        
        if (rtaEditorial.isPresent())
            editorial = rtaEditorial.get();


        if (rtaLibro.isPresent()) {
            Libro libro = rtaLibro.get();
            libro.setTitulo(titulo);
            libro.setAutor(autor);
            libro.setEditorial(editorial);
            libro.setEjemplares(ejemplares);

            libroRepositorio.save(libro);
        }        
    }

    public Libro getLibro(Long isbn) {
        return libroRepositorio.getReferenceById(isbn); //Para no usar el getOne que está deprecado.
    }

    private void validarDatos(Long isbn, String titulo, Integer ejemplares, String idAutor, String idEditorial) throws BibliotecaException {
        if (isbn == null || isbn < 0) 
            throw new BibliotecaException("Isbn no puede ser nulo o negativo.");
        if (titulo == null || titulo.isBlank())
            throw new BibliotecaException("El titulo no puede ser nulo o estar vacio.");
        if (ejemplares == null || ejemplares < 0) 
            throw new BibliotecaException("Ejemplares no pueden ser nulos o negativos.");
        if (idAutor == null || idAutor.isBlank() || idAutor.isEmpty())
            throw new BibliotecaException("IdAutor no ser nulo o vacio.");
        if (idEditorial == null || idEditorial.isBlank() || idEditorial.isEmpty())
            throw new BibliotecaException("IdEditorial no puede ser nulo o vacio.");
    }
}
