package com.egg.biblioteca.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.excepciones.BibliotecaException;
import com.egg.biblioteca.servicios.AutorServicio;

@Controller
@RequestMapping("/autor") //localhost:8080/autor
public class AutorControlador {

    @Autowired
    private AutorServicio autorServicio;

    @GetMapping("/registrar") //localhost:8080/autor
    public String registrar(){
        
        return "autor_form";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, ModelMap modelo) { //El nombre de la variable recibida por parámetro es igual al nombre del atributo name del input en el html correspondiente.
        try {
            autorServicio.crearAutor(nombre);
            modelo.put("exito", "El autor se guardo correctamente.");
            return "index";
        } catch (BibliotecaException e) {
            modelo.put("error", e.getMessage());
            return "autor_form"; //Si hay un error, se vuelve a la página del formulario.
        }
    }

    @GetMapping("/lista")
    public String listar(ModelMap modelo) {
        List<Autor> autores = autorServicio.listarAutores();

        modelo.addAttribute("autores", autores);
        return "autor_list";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo) {
        modelo.put("autor", autorServicio.getAutor(id));
        
        return "autor_modificar";
    }

    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, String nombre, ModelMap modelo) {
        try {
            autorServicio.modificarAutor(id, nombre);
            return "redirect:../lista";
        } catch (BibliotecaException e) {
            modelo.put("error", e.getMessage());
            return "autor.modificar";
        }

    }

    
}
