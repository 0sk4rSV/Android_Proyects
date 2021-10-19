package com.example.act8_bd_sqlite_osv;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class EditContacto extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText etNombre;
    private EditText etApellidos;
    private EditText etTelefono;
    private Bundle extras;
    private Bundle parametros;
    private String nombre, apellidos, telefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        toolbar = (Toolbar) findViewById(R.id.toolbarEdit);
        etNombre = findViewById(R.id.etEditNombre);
        etApellidos = findViewById(R.id.etEditApellidos);
        etTelefono = findViewById(R.id.etEditTelefono);

        parametros = this.getIntent().getExtras();
        if (parametros != null) {
            etNombre.setText(parametros.getString("nombre"));
            etApellidos.setText(parametros.getString("apellidos"));
            etTelefono.setText(parametros.getString("telefono"));
            nombre = etNombre.getText().toString();
            apellidos = etApellidos.getText().toString();
            telefono = etTelefono.getText().toString();
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Editar contacto");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        toolbar.setPadding(340, toolbar.getPaddingTop(), 10, toolbar.getPaddingBottom());
    }

    public void cancelar(View view) {
        extras = new Bundle();

        extras.putString("nombre", nombre);
        extras.putString("apellidos", apellidos);
        extras.putString("telefono", telefono);

        Intent intent = new Intent(getApplicationContext(), InfoContacto.class);
        intent.putExtras(extras);

        startActivity(intent);
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void actualizar(View view) {
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
                            bd.execSQL("UPDATE contactos SET nombre = '" + etNombre.getText().toString() + "', apellidos = '" + etApellidos.getText().toString() + "', " +
                                    "telefono = '" + etTelefono.getText().toString() + "' WHERE telefono = '" + parametros.getString("telefono") + "'");
                            bd.close();

                            extras = new Bundle();

                            extras.putString("nombre", etNombre.getText().toString());
                            extras.putString("apellidos", etApellidos.getText().toString());
                            extras.putString("telefono", etTelefono.getText().toString());

                            Intent intent = new Intent(getApplicationContext(), InfoContacto.class);
                            intent.putExtras(extras);

                            startActivity(intent);
                            finish();
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

    private boolean camposVacios() {
        if (etNombre.getText().toString().isEmpty() && etTelefono.getText().toString().isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        extras = new Bundle();

        extras.putString("nombre", etNombre.getText().toString());
        extras.putString("apellidos", etApellidos.getText().toString());
        extras.putString("telefono", etTelefono.getText().toString());

        Intent intent = new Intent(getApplicationContext(), InfoContacto.class);
        intent.putExtras(extras);

        startActivity(intent);
        finish();
    }

}
