package com.example.pm2examenherramientas53.Models;

public class Herramientas {
    private Integer id;
    private String nombre;
    private String descripcion;
    private String especificaciones;
    private String foto_uri;
    private String estado;

    public Herramientas() {}

    public Herramientas(Integer id, String nombre, String descripcion, String especificaciones, String foto_uri, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.especificaciones = especificaciones;
        this.foto_uri = foto_uri;
        this.estado = estado;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEspecificaciones() { return especificaciones; }
    public void setEspecificaciones(String especificaciones) { this.especificaciones = especificaciones; }

    public String getFoto_uri() { return foto_uri; }
    public void setFoto_uri(String foto_uri) { this.foto_uri = foto_uri; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
