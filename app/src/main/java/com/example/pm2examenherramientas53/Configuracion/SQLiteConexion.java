package com.example.pm2examenherramientas53.Configuracion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteConexion extends SQLiteOpenHelper
{
    public SQLiteConexion(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Transacciones.CreateTableHerramientas);
        sqLiteDatabase.execSQL(Transacciones.CreateTableTecnicos);
        sqLiteDatabase.execSQL(Transacciones.CreateTableAsignaciones);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Transacciones.DropTableHerramientas);
        db.execSQL(Transacciones.DropTableTecnicos);
        db.execSQL(Transacciones.DropTableAsignaciones);
        onCreate(db);
    }
}
