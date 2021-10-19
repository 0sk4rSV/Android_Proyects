package com.example.act8_bd_sqlite_osv;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CrearContacto extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText etNombre;
    private EditText etApellidos;
    private EditText etTelefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear);

        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        etNombre = findViewById(R.id.etNombreCrea);
        etApellidos = findViewById(R.id.etApellidoCrea);
        etTelefono = findViewById(R.id.etTelefonoCrea);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Añadir contacto");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        toolbar.setPadding(340, toolbar.getPaddingTop(), 10, toolbar.getPaddingBottom());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void añadirContacto(View v) {
        AgendaSQLiteHelper usdbh = new AgendaSQLiteHelper(this, "agenda", null, 1);
        SQLiteDatabase bd = usdbh.getWritableDatabase();

        if (camposVacios() == false) {
            if (etNombre.getText().toString().isEmpty()) {
                Toast.makeText(this, "El campo nombre está vacío.", Toast.LENGTH_LONG).show();
                etNombre.setBackgroundTintList(getResources().getColorStateList(R.color.red_light));
            } else {
                etNombre.setBackgroundTintList(getResources().getColorStateList(R.color.gray));
                if (etTelefono.getText().toString().isEmpty()) {
                    Toast.makeText(this, "El campo teléfono está vacío.", Toast.LENGTH_LONG).show();
                    etTelefono.setBackgroundTintList(getResources().getColorStateList(R.color.red_light));
                } else {
                    if (etTelefono.getText().toString().length() < 9) {
                        Toast.makeText(this, "El campo teléfono debe tener 9 dígitos.", Toast.LENGTH_LONG).show();
                        etTelefono.setBackgroundTintList(getResources().getColorStateList(R.color.red_light));
                    } else {
                        try {
                            etTelefono.setBackgroundTintList(getResources().getColorStateList(R.color.gray));

                            bd.execSQL("INSERT INTO contactos(nombre, apellidos, telefono) VALUES('" + etNombre.getText().toString() + "', '" +
                                    etApellidos.getText().toString() + "', '" + etTelefono.getText().toString() + "')");
                            bd.close();
                            Toast.makeText(this, "Se ha guardado el contacto.", Toast.LENGTH_LONG).show();

                            finish();
                            Intent i = new Intent(this, MainActivity.class);
                            startActivity(i);
                        } catch (SQLiteConstraintException ex) {
                            Toast.makeText(this, "Ya existe un contacto con ese teléfono.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        } else {
            Toast.makeText(this, "El contacto está vacío.", Toast.LENGTH_LONG).show();
            etNombre.setBackgroundTintList(getResources().getColorStateList(R.color.red_light));
            etTelefono.setBackgroundTintList(getResources().getColorStateList(R.color.red_light));
        }
    }

    public void cancelar(View view) {
        onBackPressed();
        finish();
    }

    private boolean camposVacios() {
        if (etNombre.getText().toString().isEmpty() && etTelefono.getText().toString().isEmpty()) {
            return true;
        }
        return false;
    }

}
