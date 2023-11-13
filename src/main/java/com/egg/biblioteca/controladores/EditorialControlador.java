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

import com.egg.biblioteca.entidades.Editorial;
import com.egg.biblioteca.excepciones.BibliotecaException;
import com.egg.biblioteca.servicios.EditorialServicio;

@Controller
@RequestMapping("/editorial") //localhost:8080/editorial
public class EditorialControlador {
    
    @Autowired
    EditorialServicio editorialServicio;

    @GetMapping("/registrar") //localhost:8080/editorial/registrar
    public String registrar() {

        return "editorial_form";
    }

    @PostMapping("/registro") //localhost:8080/editorial/registro
    public String registro(@RequestParam String nombre, ModelMap modelo) {
        try {
            editorialServicio.crearEditorial(nombre);
            modelo.put("exito", "La editorial se guardó correctamente.");
            return "index";
        } catch (BibliotecaException e) {
            modelo.put("error", e.getMessage());
            return "editorial_form";
        }
    }

    @GetMapping("/lista")
    public String listar(ModelMap modelo) {
        List<Editorial> editoriales = editorialServicio.listarEditoriales();

        modelo.put("editoriales", editoriales);
        return "editorial_list";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo) {
        modelo.put("editorial", editorialServicio.getEditorial(id));

        return "editorial_modificar";
    }

    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, String nombre, ModelMap modelo) {
        try {
            editorialServicio.modificarEditorial(id, nombre);
            return "redirect:../lista";
        } catch (BibliotecaException e) {
            modelo.put("error", e.getMessage());
            return "editorial_modificar";
        }
    }

}
