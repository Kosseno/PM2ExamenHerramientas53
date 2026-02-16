package com.example.pm2examenherramientas53;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pm2examenherramientas53.Configuracion.SQLiteConexion;
import com.example.pm2examenherramientas53.Configuracion.Transacciones;

public class Tecnicos extends AppCompatActivity {

    EditText nombre, telefono;
    Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tecnicos);

        nombre = findViewById(R.id.txtnombretecnico);
        telefono = findViewById(R.id.editTextText2);
        btnGuardar = findViewById(R.id.btnguardartecnico);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarTecnico();
            }
        });
    }

    private void guardarTecnico() {
        String nombreStr = nombre.getText().toString().trim();
        String telefonoStr = telefono.getText().toString().trim();

        if (nombreStr.isEmpty()) {
            Toast.makeText(this, "El nombre del técnico es obligatorio", Toast.LENGTH_SHORT).show();
            return;
        }

        if (telefonoStr.length() != 8) {
            Toast.makeText(this, "El teléfono debe tener exactamente 8 dígitos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.dbname, null, Transacciones.dbversion);
            SQLiteDatabase db = conexion.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(Transacciones.t_nombre, nombreStr);
            values.put(Transacciones.t_telefono, telefonoStr);

            long result = db.insert(Transacciones.tbtecnicos, null, values);

            if (result != -1) {
                Toast.makeText(this, "Técnico guardado con éxito", Toast.LENGTH_SHORT).show();
                limpiarCampos();
            } else {
                Toast.makeText(this, "Error al guardar el técnico", Toast.LENGTH_SHORT).show();
            }

            db.close();
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void limpiarCampos() {
        nombre.setText("");
        telefono.setText("");
    }
}
