package com.example.act8_bd_sqlite_osv;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class UsuariosSQLiteHelper extends SQLiteOpenHelper {
    //Cadena con la sentencia SQL que permite crear la tabla clientes.
    String cadSQL = "CREATE TABLE Usuarios (codigo INTEGER, nombre TEXT)";

    public UsuariosSQLiteHelper(@Nullable Context contexto, @Nullable String nombre, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }

    //Este método se ejecutará automaticamente cuando sea necesaria la creación de la base de datos.
    //Es decir, se ejecutará cuando la base de datos todavía no exista.
    //Aquí deben crearse todas las tablas necesarias e insertar los datos iniciales si es necesario.
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Ejecutamos la sentencia SQL para crear la tabla clientes.
        //El método execSQL se limita a ejecutar directamente el código SQL que le pasemos.
        db.execSQL(cadSQL);
    }
    //Este método se lanza automáticamente cuando es necesaria una actualización de la estructura
    //de la base de datos o una conversión de los datos.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //NOTA: Para simplificar este ejemplo eliminamos la tabla anterior y la creamos de nuevo
        //      con el nuevo formato.
        //      Lo normal sería realizar una migración o traspaso de los datos de la tabla antigua
        //      a la nueva, con la consiguiente complicación que ello conlleva.

        //Eliminamos la versión de la tabla.
        db.execSQL("DROP TABLE IF EXISTS clientes");

        //Creamos la nueva versión de la tabla.
        db.execSQL(cadSQL);
    }

}
