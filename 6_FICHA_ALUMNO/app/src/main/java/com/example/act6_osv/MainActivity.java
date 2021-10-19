package com.example.act6_osv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String cad;
    private TextView mensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mensaje = (TextView) findViewById(R.id.tvDatosFicha);
        cad = mensaje.getText().toString();
        recibirDatos();
    }

    public void abrirFichaAlu(View view) {
        Intent intent = new Intent (view.getContext(), FichaAlumActivity.class);
        startActivityForResult(intent, 0);
    }

    public void recibirDatos() {
        Bundle parametro = this.getIntent().getExtras();
        System.out.println("es:"+parametro);
        if (parametro != null) {
            String cad = getIntent().getStringExtra("texto");
            TextView mensaje = (TextView) findViewById(R.id.tvDatosFicha);
            mensaje.setText(cad);
        } else {
            TextView mensaje = (TextView) findViewById(R.id.tvDatosFicha);
            mensaje.setText("No hay datos en la ficha");
        }
    }
}