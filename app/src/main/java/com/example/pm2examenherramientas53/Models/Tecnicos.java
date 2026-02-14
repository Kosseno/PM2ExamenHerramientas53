package com.example.pm2examenherramientas53.Models;

public class Tecnicos {
    private Integer id;
    private String nombre;
    private String telefono;
    private String especialidad;

    public Tecnicos() {}

    public Tecnicos(Integer id, String nombre, String telefono, String especialidad) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.especialidad = especialidad;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
}
