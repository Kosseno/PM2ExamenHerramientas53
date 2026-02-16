package com.example.pm2examenherramientas53.Models;

public class HerramientaVista {
    private final Herramientas herramienta;
    private final String tecnicoAsignado;
    private final String fechaFin;
    private final String fechaDevolucion;

    public HerramientaVista(Herramientas herramienta, String tecnicoAsignado, String fechaFin, String fechaDevolucion) {
        this.herramienta = herramienta;
        this.tecnicoAsignado = tecnicoAsignado;
        this.fechaFin = fechaFin;
        this.fechaDevolucion = fechaDevolucion;
    }

    public Herramientas getHerramienta() { return herramienta; }
    public String getTecnicoAsignado() { return tecnicoAsignado; }
    public String getFechaFin() { return fechaFin; }
    public String getFechaDevolucion() { return fechaDevolucion; }
}
