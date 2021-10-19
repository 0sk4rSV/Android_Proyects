package com.example.curso_android;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
    int añoActual = Integer.parseInt(sdf.format(new Date()));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void verValores(View v) {
        try{
        EditText nombre = (EditText) findViewById(R.id.etNombre);
        EditText apellidos = (EditText) findViewById(R.id.etApellidos);
        EditText añoNacimiento = (EditText) findViewById(R.id.etAñoNacimiento);
        if (añoValido(añoNacimiento.getText().toString())){
                String cad = "Mi nombre es " + nombre.getText().toString() + " " + apellidos.getText().toString()
                        + ", tengo " + calculaAños(añoNacimiento.getText().toString()) + " años y voy a aprobar DI sin pestañear.";
                TextView texto = (TextView) findViewById(R.id.tvTexto);
                texto.setText(cad);
            } else {
            Toast tError = Toast.makeText(getApplicationContext(), "Año de nacimineto inválido.", Toast.LENGTH_SHORT);
            tError.setGravity(Gravity.CENTER_HORIZONTAL, 0, 200);
            tError.show();
        }
        } catch (Exception ex) {
            Toast tError = Toast.makeText(getApplicationContext(), "Deben rellenarse todos los datos.", Toast.LENGTH_SHORT);
            tError.setGravity(Gravity.CENTER_HORIZONTAL, 0, 200);
            tError.show();
        }
    }

    public int calculaAños(String strAñoNacimiento) {
        return añoActual - Integer.parseInt(strAñoNacimiento);
    }

    public boolean añoValido(String strAñoNacimiento) {
        int añoNacimiento = Integer.parseInt(strAñoNacimiento);
        if (añoNacimiento <= añoActual && añoNacimiento > 0){
            return true;
        }
        return false;
    }

}