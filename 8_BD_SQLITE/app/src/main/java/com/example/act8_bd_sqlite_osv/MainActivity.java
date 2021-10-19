package com.example.act8_bd_sqlite_osv;

import androidx.appcompat.app.AppCompatActivity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Abrimos la base de datos en modo escritura.
        UsuariosSQLiteHelper usdbh = new UsuariosSQLiteHelper(this, "DBUsuarios", null, 1);

        //Obtenemos referencia a la base de datos para poder modificarla.
        SQLiteDatabase db = usdbh.getWritableDatabase();

        //En caso de abrir de forma correcta la base de datos.
        if (db != null) {
            //Introducimos 5 usuarios de ejemplo.
            for (int i = 1; i <= 5; i++) {
                //Creamos los datos
                int codigo = i;
                String nombre = "Usuario" + i;
                //Introducimos los datos en la tabla Clientes.
                db.execSQL("INSERT INTO Usuarios (codigo, nombre) " + "VALUES (" + codigo + ", '" + nombre + "')");
            }

            //Cerramos la base de datos.
            db.close();
        }
    }

}