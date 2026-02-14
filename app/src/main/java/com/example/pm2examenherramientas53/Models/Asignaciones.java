package com.example.pm2examenherramientas53.Models;

public class Asignaciones {
    private Integer id;
    private Integer herramienta_id;
    private Integer tecnico_id;
    private String fecha_inicio;
    private String fecha_fin;
    private String fecha_devolucion;
    private String notas_entrega;
    private String foto_entrega_uri;
    private String foto_devolucion_uri;

    public Asignaciones() {}

    public Asignaciones(Integer id, Integer herramienta_id, Integer tecnico_id, String fecha_inicio, String fecha_fin, String fecha_devolucion, String notas_entrega, String foto_entrega_uri, String foto_devolucion_uri) {
        this.id = id;
        this.herramienta_id = herramienta_id;
        this.tecnico_id = tecnico_id;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.fecha_devolucion = fecha_devolucion;
        this.notas_entrega = notas_entrega;
        this.foto_entrega_uri = foto_entrega_uri;
        this.foto_devolucion_uri = foto_devolucion_uri;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getHerramienta_id() { return herramienta_id; }
    public void setHerramienta_id(Integer herramienta_id) { this.herramienta_id = herramienta_id; }

    public Integer getTecnico_id() { return tecnico_id; }
    public void setTecnico_id(Integer tecnico_id) { this.tecnico_id = tecnico_id; }

    public String getFecha_inicio() { return fecha_inicio; }
    public void setFecha_inicio(String fecha_inicio) { this.fecha_inicio = fecha_inicio; }

    public String getFecha_fin() { return fecha_fin; }
    public void setFecha_fin(String fecha_fin) { this.fecha_fin = fecha_fin; }

    public String getFecha_devolucion() { return fecha_devolucion; }
    public void setFecha_devolucion(String fecha_devolucion) { this.fecha_devolucion = fecha_devolucion; }

    public String getNotas_entrega() { return notas_entrega; }
    public void setNotas_entrega(String notas_entrega) { this.notas_entrega = notas_entrega; }

    public String getFoto_entrega_uri() { return foto_entrega_uri; }
    public void setFoto_entrega_uri(String foto_entrega_uri) { this.foto_entrega_uri = foto_entrega_uri; }

    public String getFoto_devolucion_uri() { return foto_devolucion_uri; }
    public void setFoto_devolucion_uri(String foto_devolucion_uri) { this.foto_devolucion_uri = foto_devolucion_uri; }
}
