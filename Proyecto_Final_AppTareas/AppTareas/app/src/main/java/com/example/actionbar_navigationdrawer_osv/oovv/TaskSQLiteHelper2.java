package com.example.actionbar_navigationdrawer_osv.oovv;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class TaskSQLiteHelper2 extends SQLiteOpenHelper {

    private static final String NOMBRE_TABLA = "alarmas";
    private static final String sqlCreate = "CREATE TABLE IF NOT EXISTS alarmas" +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "titulo TEXT NOT NULL, " +
            "minutos INTEGER NOT NULL);";

    public TaskSQLiteHelper2(@Nullable Context contexto, @Nullable String nombre, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(contexto, "bdAlarmas", factory, version);
    }

    //Este método se ejecutará automaticamente cuando sea necesaria la creación de la base de datos.
    //Es decir, se ejecutará cuando la base de datos todavía no exista.
    //Aquí deben crearse todas las tablas necesarias e insertar los datos iniciales si es necesario.
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Ejecutamos la sentencia SQL para crear la tabla clientes.
        //El método execSQL se limita a ejecutar directamente el código SQL que le pasemos.
        db.execSQL(String.format(sqlCreate));
    }

    //Este método se lanza automáticamente cuando es necesaria una actualización de la estructura
    //de la base de datos o una conversión de los datos.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", NOMBRE_TABLA));
        db.execSQL(sqlCreate);
    }

}
