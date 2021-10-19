package com.example.act8_bd_sqlite_osv;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AgendaSQLiteHelper extends SQLiteOpenHelper {

    private static String NOMBRE_BD = "agenda", NOMBRE_TABLA = "contactos";

    public AgendaSQLiteHelper(@Nullable Context contexto, @Nullable String nombre, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(contexto, NOMBRE_BD, factory, version);
    }

    //Este método se ejecutará automaticamente cuando sea necesaria la creación de la base de datos.
    //Es decir, se ejecutará cuando la base de datos todavía no exista.
    //Aquí deben crearse todas las tablas necesarias e insertar los datos iniciales si es necesario.
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Ejecutamos la sentencia SQL para crear la tabla clientes.
        //El método execSQL se limita a ejecutar directamente el código SQL que le pasemos.
        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s(nombre TEXT, apellidos TEXT, telefono TEXT PRIMARY KEY)", NOMBRE_TABLA));
    }

    //Este método se lanza automáticamente cuando es necesaria una actualización de la estructura
    //de la base de datos o una conversión de los datos.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.format("DROP TABLE IF EXISTS CREATE TABLE contactos"));
        onCreate(db);
    }

}
