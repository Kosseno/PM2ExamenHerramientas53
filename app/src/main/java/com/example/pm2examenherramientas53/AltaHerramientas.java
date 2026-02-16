package com.example.pm2examenherramientas53;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.pm2examenherramientas53.Configuracion.SQLiteConexion;
import com.example.pm2examenherramientas53.Configuracion.Transacciones;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AltaHerramientas extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PICK_IMAGE = 2;
    private static final int PERMISSION_REQUEST_CODE = 100;

    EditText nombre, descripcion, especificaciones;
    ImageView foto;
    Button btnFoto, btnGuardar, btnTecnicos, btnAsignacion;

    String currentPhotoPath;
    Uri photoURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_altaherramientas);

        nombre = findViewById(R.id.txtnombrealtaherramientas);
        descripcion = findViewById(R.id.txtdescripcionaltaherramientas);
        especificaciones = findViewById(R.id.txtespecificaciontecnica);
        foto = findViewById(R.id.imagefotoalta);
        btnFoto = findViewById(R.id.btnfoto);
        btnGuardar = findViewById(R.id.btnguardar);
        btnTecnicos = findViewById(R.id.btntecnicos);
        btnAsignacion = findViewById(R.id.btnasignacion);

        // Establecer placeholder inicial
        foto.setImageResource(R.drawable.placeholder);

        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissions()) {
                    showImagePickDialog();
                } else {
                    requestPermissions();
                }
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarHerramienta();
            }
        });
        
        btnTecnicos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AltaHerramientas.this, Tecnicos.class);
                startActivity(intent);
            }
        });

        btnAsignacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AltaHerramientas.this, Asignaciones.class);
                startActivity(intent);
            }
        });
    }

    private boolean checkPermissions() {
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showImagePickDialog();
            } else {
                Toast.makeText(this, "Permissions Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showImagePickDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar Imagen");
        builder.setItems(new CharSequence[]{"Tomar Foto", "Elegir de Galería"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        dispatchTakePictureIntent();
                        break;
                    case 1:
                        dispatchPickImageIntent();
                        break;
                }
            }
        });
        builder.show();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.pm2examenherramientas53.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void dispatchPickImageIntent() {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageIntent.setType("image/*");
        startActivityForResult(pickImageIntent, REQUEST_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                foto.setImageURI(photoURI);
            } else if (requestCode == REQUEST_PICK_IMAGE && data != null && data.getData() != null) {
                try {
                    photoURI = saveImageToInternalStorage(data.getData());
                    foto.setImageURI(photoURI);
                } catch (IOException e) {
                    Toast.makeText(this, "Error al procesar la imagen", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private Uri saveImageToInternalStorage(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        File file = createImageFile();
        OutputStream outputStream = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
        return Uri.fromFile(file);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void guardarHerramienta() {
        String nombreStr = nombre.getText().toString().trim();
        String descripcionStr = descripcion.getText().toString().trim();
        String especificacionesStr = especificaciones.getText().toString().trim();

        if (nombreStr.isEmpty() || descripcionStr.isEmpty() || especificacionesStr.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }
        
        String regex = "^[a-zA-Z0-9\\s]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(nombreStr);

        if (nombreStr.length() < 3 || !matcher.matches()) {
            Toast.makeText(this, "El nombre debe tener al menos 3 caracteres y solo puede contener letras, números y espacios.", Toast.LENGTH_LONG).show();
            return;
        }


        if (photoURI == null) {
            Toast.makeText(this, "Debe seleccionar una foto", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.dbname, null, Transacciones.dbversion);
            SQLiteDatabase db = conexion.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(Transacciones.h_nombre, nombreStr);
            values.put(Transacciones.h_descripcion, descripcionStr);
            values.put(Transacciones.h_especificaciones, especificacionesStr);
            values.put(Transacciones.h_foto_uri, photoURI.toString());

            long result = db.insert(Transacciones.tbherramientas, null, values);

            if (result != -1) {
                Toast.makeText(this, "Herramienta guardada con éxito", Toast.LENGTH_SHORT).show();
                limpiarCampos();
            } else {
                Toast.makeText(this, "Error al guardar la herramienta", Toast.LENGTH_SHORT).show();
            }

            db.close();

        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    private void limpiarCampos() {
        nombre.setText("");
        descripcion.setText("");
        especificaciones.setText("");
        foto.setImageResource(R.drawable.placeholder); 
        photoURI = null; // Se resetea la URI para obligar a elegir foto real
    }
}
