package com.egg.biblioteca.entidades;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class Imagen {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String mime;
    private String nombre;

    @Lob @Basic(fetch = FetchType.LAZY) //Lob --> informa que este campo puede manejar muchos datos. Puede ser pesado.
    // FecthType.Lazy --> indica que se va a cargar sólo cuando pidamos acceder a este atributo (getContenido()).
    //Por defecto todos los atributos están marcados como Eager, y se cargaran cuando accedamos a cualquier instancia de esta clase.
    //Aún con la anotación Lob tuve que aumentar el tamaño aceptado por la columna contenido en la DB (ALTER TABLE imagen MODIFY contenido LONGBLOB;)
    private byte[] contenido;

    public Imagen() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMime() {
        return this.mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public byte[] getContenido() {
        return this.contenido;
    }

    public void setContenido(byte[] contenido) {
        this.contenido = contenido;
    }

}
