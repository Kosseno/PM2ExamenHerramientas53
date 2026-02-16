package com.example.pm2examenherramientas53;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pm2examenherramientas53.Configuracion.SQLiteConexion;
import com.example.pm2examenherramientas53.Configuracion.Transacciones;
import com.example.pm2examenherramientas53.Models.Herramientas;
import com.example.pm2examenherramientas53.Models.HerramientaVista;
import com.example.pm2examenherramientas53.Models.Tecnicos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Asignaciones extends AppCompatActivity implements HerramientaAdapter.OnHerramientaListener {

    private RecyclerView recyclerView;
    private HerramientaAdapter adapter;
    private SQLiteConexion conexion;
    private List<HerramientaVista> listaHerramientas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignaciones);

        conexion = new SQLiteConexion(this, Transacciones.dbname, null, Transacciones.dbversion);
        recyclerView = findViewById(R.id.recycleviewlistaherramientas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SearchView searchView = findViewById(R.id.searchbarherramientas);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null) adapter.filter(newText);
                return true;
            }
        });

        cargarHerramientas();
    }

    private void cargarHerramientas() {
        listaHerramientas = new ArrayList<>();
        SQLiteDatabase db = conexion.getReadableDatabase();

        String query = "SELECT h.*, a.fecha_fin, a.fecha_devolucion, t.nombre as tecnico_nombre " +
                "FROM Herramientas h " +
                "LEFT JOIN Asignaciones a ON h.id = a.herramienta_id AND a.fecha_devolucion IS NULL " +
                "LEFT JOIN Tecnicos t ON a.tecnico_id = t.id";

        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Herramientas h = new Herramientas(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                    cursor.getString(cursor.getColumnIndexOrThrow("descripcion")),
                    cursor.getString(cursor.getColumnIndexOrThrow("especificaciones")),
                    cursor.getString(cursor.getColumnIndexOrThrow("foto_uri")),
                    cursor.getString(cursor.getColumnIndexOrThrow("estado"))
            );
            listaHerramientas.add(new HerramientaVista(
                    h,
                    cursor.getString(cursor.getColumnIndexOrThrow("tecnico_nombre")),
                    cursor.getString(cursor.getColumnIndexOrThrow("fecha_fin")),
                    cursor.getString(cursor.getColumnIndexOrThrow("fecha_devolucion"))
            ));
        }
        cursor.close();

        // Ordenar: fecha_fin ascendente (nulos al final)
        Collections.sort(listaHerramientas, (o1, o2) -> {
            if (o1.getFechaFin() == null && o2.getFechaFin() == null) return 0;
            if (o1.getFechaFin() == null) return 1;
            if (o2.getFechaFin() == null) return -1;
            return o1.getFechaFin().compareTo(o2.getFechaFin());
        });

        adapter = new HerramientaAdapter(listaHerramientas, this, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onHerramientaLongClick(HerramientaVista h) {
        if (!"DISPONIBLE".equalsIgnoreCase(h.getHerramienta().getEstado())) {
            Toast.makeText(this, "Herramienta ya ASIGNADA", Toast.LENGTH_SHORT).show();
            return;
        }
        mostrarDialogoAsignacion(h.getHerramienta());
    }

    private void mostrarDialogoAsignacion(Herramientas herramienta) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_asignacion, null);

        Spinner spinnerTecnicos = view.findViewById(R.id.spinner_tecnicos_dialog);
        EditText txtInicio = view.findViewById(R.id.txt_fecha_inicio_dialog);
        EditText txtFin = view.findViewById(R.id.txt_fecha_fin_dialog);

        List<Tecnicos> tecnicos = obtenerTecnicos();
        List<String> nombresTecnicos = new ArrayList<>();
        for (Tecnicos t : tecnicos) nombresTecnicos.add(t.getNombre());
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresTecnicos);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTecnicos.setAdapter(spinnerAdapter);

        txtInicio.setOnClickListener(v -> mostrarDatePicker(txtInicio));
        txtFin.setOnClickListener(v -> mostrarDatePicker(txtFin));

        builder.setView(view)
                .setTitle("Asignar: " + herramienta.getNombre())
                .setPositiveButton("Confirmar", (dialog, which) -> {
                    int pos = spinnerTecnicos.getSelectedItemPosition();
                    if (pos >= 0) {
                        realizarAsignacion(herramienta, tecnicos.get(pos), txtInicio.getText().toString(), txtFin.getText().toString());
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void mostrarDatePicker(EditText et) {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) -> {
            et.setText(String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, day));
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private List<Tecnicos> obtenerTecnicos() {
        List<Tecnicos> list = new ArrayList<>();
        Cursor cursor = conexion.getReadableDatabase().query("Tecnicos", null, null, null, null, null, "nombre");
        while (cursor.moveToNext()) {
            list.add(new Tecnicos(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                    cursor.getString(cursor.getColumnIndexOrThrow("telefono")),
                    cursor.getString(cursor.getColumnIndexOrThrow("especialidad"))
            ));
        }
        cursor.close();
        return list;
    }

    private void realizarAsignacion(Herramientas h, Tecnicos t, String inicio, String fin) {
        if (inicio.isEmpty() || fin.isEmpty()) {
            Toast.makeText(this, "Las fechas son obligatorias", Toast.LENGTH_SHORT).show();
            return;
        }
        if (fin.compareTo(inicio) < 0) {
            Toast.makeText(this, "Fecha fin no puede ser menor a inicio", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirmar Asignación")
                .setMessage("¿Confirmar asignación de " + h.getNombre() + " a " + t.getNombre() + " del " + inicio + " al " + fin + "?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    SQLiteDatabase db = conexion.getWritableDatabase();
                    db.beginTransaction();
                    try {
                        ContentValues valuesA = new ContentValues();
                        valuesA.put("herramienta_id", h.getId());
                        valuesA.put("tecnico_id", t.getId());
                        valuesA.put("fecha_inicio", inicio);
                        valuesA.put("fecha_fin", fin);
                        db.insert("Asignaciones", null, valuesA);

                        ContentValues valuesH = new ContentValues();
                        valuesH.put("estado", "ASIGNADA");
                        db.update("Herramientas", valuesH, "id=?", new String[]{String.valueOf(h.getId())});

                        db.setTransactionSuccessful();
                        Toast.makeText(this, "Asignación exitosa", Toast.LENGTH_SHORT).show();
                        cargarHerramientas();
                    } catch (Exception e) {
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    } finally {
                        db.endTransaction();
                        db.close();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
