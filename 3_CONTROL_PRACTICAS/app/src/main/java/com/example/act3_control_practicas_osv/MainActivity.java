package com.example.act3_control_practicas_osv;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    DecimalFormat df = new DecimalFormat("##,##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void cbSeleccion(View v1) {
        EditText etP1 = (EditText) findViewById(R.id.etP1);
        EditText etP2 = (EditText) findViewById(R.id.etP2);
        EditText etP3 = (EditText) findViewById(R.id.etP3);

        if (((CheckBox) findViewById(R.id.cbP1)).isChecked()) {
            etP1.setEnabled(true);
        } else {
            etP1.setEnabled(false);
            etP1.setText("0");
        }

        if (((CheckBox) findViewById(R.id.cbP2)).isChecked()) {
            etP2.setEnabled(true);
        } else {
            etP2.setEnabled(false);
            etP2.setText("0");
        }

        if (((CheckBox) findViewById(R.id.cbP3)).isChecked()) {
            etP3.setEnabled(true);
        } else {
            etP3.setEnabled(false);
            etP3.setText("0");
        }
    }

    public void calcularMedia(View v2) {
        try {
            EditText notaP1 = (EditText) findViewById(R.id.etP1);
            EditText notaP2 = (EditText) findViewById(R.id.etP2);
            EditText notaP3 = (EditText) findViewById(R.id.etP3);

            TextView tvNotaFinal = (TextView) findViewById(R.id.tvNotaMedia);
            tvNotaFinal.setText("");

            if (notaValida(notaP1.getText().toString(), notaP2.getText().toString(), notaP3.getText().toString())) {
                String cad = "PUNTUAIÓN DEL ALUMNO: " + df.format(calculoNota(notaP1.getText().toString(), notaP2.getText().toString(),
                        notaP3.getText().toString(), cantChecks()));
                TextView texto = (TextView) findViewById(R.id.tvNotaMedia);
                texto.setText(cad);
            } else {
                Toast tError = Toast.makeText(getApplicationContext(), "Las notas no pueden ser superiores a 10", Toast.LENGTH_SHORT);
                tError.setGravity(Gravity.CENTER_HORIZONTAL, 0, 600);
                tError.show();
            }
        } catch (Exception ex) {
            Toast tError = Toast.makeText(getApplicationContext(), "Faltan campos por rellenar.", Toast.LENGTH_SHORT);
            tError.setGravity(Gravity.CENTER_HORIZONTAL, 0, 600);
            tError.show();
        }
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v2.getApplicationWindowToken(), 0);
    }

    public int cantChecks() {
        int count = 0;

        if (((CheckBox) findViewById(R.id.cbP1)).isChecked()) {
            count++;
        }

        if (((CheckBox) findViewById(R.id.cbP2)).isChecked()) {
            count++;
        }

        if (((CheckBox) findViewById(R.id.cbP3)).isChecked()) {
            count++;
        }
        return count;
    }

    public double calculoNota(String notaP1, String notaP2, String notaP3, int dividendo) {
        double notaFinal = (Integer.parseInt(notaP1) + Integer.parseInt(notaP2) + Integer.parseInt(notaP3)) / dividendo;
        return notaFinal;
    }

    public boolean notaValida(String examen, String practicas, String actitud) {
        if (Double.parseDouble(examen) <= 10 && Double.parseDouble(practicas) <= 10 && Double.parseDouble(actitud) <= 10) {
            return true;
        }
        return false;
    }

}