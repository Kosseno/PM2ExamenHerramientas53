package com.example.pm2examenherramientas53;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pm2examenherramientas53.Configuracion.SQLiteConexion;
import com.example.pm2examenherramientas53.Configuracion.Transacciones;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistorialdeAsignaciones extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HistorialAdapter adapter;
    private SQLiteConexion conexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historialde_asignaciones);

        conexion = new SQLiteConexion(this, Transacciones.dbname, null, Transacciones.dbversion);
        recyclerView = findViewById(R.id.recyclehistoriadeasignaciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cargarHistorial();
    }

    private void cargarHistorial() {
        List<Map<String, String>> listaHistorial = new ArrayList<>();
        SQLiteDatabase db = conexion.getReadableDatabase();

        // Consulta SQL con JOIN para obtener nombres en lugar de IDs
        String query = "SELECT h.nombre AS herramienta_nombre, t.nombre AS tecnico_nombre, " +
                "a.fecha_inicio, a.fecha_fin, a.fecha_devolucion " +
                "FROM Asignaciones a " +
                "JOIN Herramientas h ON a.herramienta_id = h.id " +
                "JOIN Tecnicos t ON a.tecnico_id = t.id " +
                "ORDER BY a.id DESC";

        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Map<String, String> item = new HashMap<>();
            item.put("herramienta", cursor.getString(cursor.getColumnIndexOrThrow("herramienta_nombre")));
            item.put("tecnico", cursor.getString(cursor.getColumnIndexOrThrow("tecnico_nombre")));
            item.put("inicio", cursor.getString(cursor.getColumnIndexOrThrow("fecha_inicio")));
            item.put("fin", cursor.getString(cursor.getColumnIndexOrThrow("fecha_fin")));
            item.put("devolucion", cursor.getString(cursor.getColumnIndexOrThrow("fecha_devolucion")));
            listaHistorial.add(item);
        }
        cursor.close();

        adapter = new HistorialAdapter(listaHistorial);
        recyclerView.setAdapter(adapter);
    }
}
