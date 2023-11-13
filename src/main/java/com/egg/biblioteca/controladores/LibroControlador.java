package com.egg.biblioteca.controladores;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.entidades.Editorial;
import com.egg.biblioteca.entidades.Libro;
import com.egg.biblioteca.excepciones.BibliotecaException;
import com.egg.biblioteca.servicios.AutorServicio;
import com.egg.biblioteca.servicios.EditorialServicio;
import com.egg.biblioteca.servicios.LibroServicio;

@Controller
@RequestMapping("/libro") //localhost:8080/libro
public class LibroControlador {
    
    @Autowired
    LibroServicio libroServicio;
    @Autowired
    EditorialServicio editorialServicio;
    @Autowired
    AutorServicio autorServicio;

    @GetMapping("/registrar") //localhost:8080/libro/registrar
    public String registrar(ModelMap modelo) {
        List<Autor> autores = autorServicio.listarAutores();
        List<Editorial> editoriales = editorialServicio.listarEditoriales();

        modelo.addAttribute("autores", autores);
        modelo.addAttribute("editoriales", editoriales);
        //Se agrega el listado de autores y editoriales al modelo, para que la vista pueda renderizar y mostrarlos en sus correspondientes selects.

        return "libro_form";
    }

    @PostMapping("/registro") //localhost:8080/libro/registro
    //Se marcaron como required false los dos parámetros numéricos, ya que en caso de querer enviar al controlador parámetros numéricos en null, se rompe y no ingresan.
    //Se modifican para que en caso de ser nulos, se ingrese igual al método y se maneje adentro la excepción.
    public String registro(@RequestParam(required = false) Long isbn, @RequestParam String titulo, @RequestParam(required = false) Integer ejemplares,
    @RequestParam String idAutor, @RequestParam String idEditorial, ModelMap modelo) {
        try {
            libroServicio.crearLibro(isbn, titulo, ejemplares, idAutor, idEditorial);
            modelo.put( "exito", "El libro fue guardado correctamente.");
            return "index";
        } catch (BibliotecaException | NoSuchElementException e) {
            //Cargamos el modelo con la lista de editoriales y autores para que en caso de un error inicial, acá vuelvan a mostrarse en el select los listados correspondientes.
            List<Autor> autores = autorServicio.listarAutores();
            List<Editorial> editoriales = editorialServicio.listarEditoriales();
            modelo.addAttribute("autores", autores);
            modelo.addAttribute("editoriales", editoriales);

            modelo.put("error", e.getMessage()); 
            return "libro_form"; //En caso de arrojarse una excepción, se vuelve al formulario.
        }
    }

    @GetMapping("/lista")
    public String listar(ModelMap modelo) {
        List<Libro> libros = libroServicio.listarLibros();

        modelo.addAttribute("libros", libros);
        return "libro_list";
    }

    @GetMapping("/modificar/{isbn}")
    public String modificar(@PathVariable Long isbn, ModelMap modelo) {
        modelo.put("libro", libroServicio.getLibro(isbn));

        List<Autor> autores = autorServicio.listarAutores();
        List<Editorial> editoriales = editorialServicio.listarEditoriales();
        modelo.addAttribute("autores", autores);
        modelo.addAttribute("editoriales", editoriales);

        return "libro_modificar";
    }

    @PostMapping("/modificar/{isbn}")
    public String modificar(@PathVariable Long isbn, String titulo, Integer ejemplares, String idAutor, String idEditorial, ModelMap modelo) {
        //Por fuera del bloque try catch porque independientemente de lo que ocurra, necesitamos agregar al modelo los listados de editoriales y autores.

        try {
            libroServicio.modificarLibro(isbn, titulo, idAutor, idEditorial, ejemplares);
            return "redirect:../lista";

        } catch (BibliotecaException | NoSuchElementException e) {    
            List<Autor> autores = autorServicio.listarAutores();
            List<Editorial> editoriales = editorialServicio.listarEditoriales();
            modelo.addAttribute("autores", autores);
            modelo.addAttribute("editoriales", editoriales);        
            
            modelo.put("libro", libroServicio.getLibro(isbn));
            modelo.put("error", e.getMessage());
            
            return "libro_modificar";
        }
    }

}
