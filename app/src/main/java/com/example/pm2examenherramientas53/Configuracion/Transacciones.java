package com.example.pm2examenherramientas53.Configuracion;

public class Transacciones
{
    //DB name
    public static final String dbname = "DBPM01";

    public static final int dbversion = 2;

    //DDL Create Person

    // --- Tablas nuevas ---

    // Tabla Herramientas
    public static final String tbherramientas = "Herramientas";
    public static final String h_id = "id";
    public static final String h_nombre = "nombre";
    public static final String h_descripcion = "descripcion";
    public static final String h_especificaciones = "especificaciones";
    public static final String h_foto_uri = "foto_uri";
    public static final String h_estado = "estado";

    public static final String CreateTableHerramientas = "CREATE TABLE " + tbherramientas + " ( " +
            h_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            h_nombre + " TEXT NOT NULL, " +
            h_descripcion + " TEXT NOT NULL, " +
            h_especificaciones + " TEXT NOT NULL, " +
            h_foto_uri + " TEXT, " +
            h_estado + " TEXT NOT NULL DEFAULT 'DISPONIBLE' )";

    public static final String DropTableHerramientas = "DROP TABLE IF EXISTS " + tbherramientas;

    // Tabla Tecnicos
    public static final String tbtecnicos = "Tecnicos";
    public static final String t_id = "id";
    public static final String t_nombre = "nombre";
    public static final String t_telefono = "telefono";
    public static final String t_especialidad = "especialidad";

    public static final String CreateTableTecnicos = "CREATE TABLE " + tbtecnicos + " ( " +
            t_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            t_nombre + " TEXT NOT NULL, " +
            t_telefono + " TEXT, " +
            t_especialidad + " TEXT )";

    public static final String DropTableTecnicos = "DROP TABLE IF EXISTS " + tbtecnicos;

    // Tabla Asignaciones
    public static final String tbasignaciones = "Asignaciones";
    public static final String a_id = "id";
    public static final String a_herramienta_id = "herramienta_id";
    public static final String a_tecnico_id = "tecnico_id";
    public static final String a_fecha_inicio = "fecha_inicio";
    public static final String a_fecha_fin = "fecha_fin";
    public static final String a_fecha_devolucion = "fecha_devolucion";
    public static final String a_notas_entrega = "notas_entrega";
    public static final String a_foto_entrega_uri = "foto_entrega_uri";
    public static final String a_foto_devolucion_uri = "foto_devolucion_uri";

    public static final String CreateTableAsignaciones = "CREATE TABLE " + tbasignaciones + " ( " +
            a_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            a_herramienta_id + " INTEGER NOT NULL, " +
            a_tecnico_id + " INTEGER NOT NULL, " +
            a_fecha_inicio + " TEXT NOT NULL, " +
            a_fecha_fin + " TEXT NOT NULL, " +
            a_fecha_devolucion + " TEXT, " +
            a_notas_entrega + " TEXT, " +
            a_foto_entrega_uri + " TEXT, " +
            a_foto_devolucion_uri + " TEXT, " +
            "FOREIGN KEY(" + a_herramienta_id + ") REFERENCES " + tbherramientas + "(" + h_id + "), " +
            "FOREIGN KEY(" + a_tecnico_id + ") REFERENCES " + tbtecnicos + "(" + t_id + ") )";

    public static final String DropTableAsignaciones = "DROP TABLE IF EXISTS " + tbasignaciones;
}
