package com.example.act2_puntuacion_osv;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    DecimalFormat df = new DecimalFormat("##.##", new DecimalFormatSymbols(Locale.GERMAN));
    private EditText examen;
    private EditText practicas;
    private EditText actitud;
    private int[] notas1Tri = {0, 0, 0};
    private int[] notas2Tri = {0, 0, 0};
    private int[] notas3Tri = {0, 0, 0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        examen = (EditText) findViewById(R.id.etExamen);
        practicas = (EditText) findViewById(R.id.etPracticas);
        actitud = (EditText) findViewById(R.id.etActitud);
    }

    public void calcularNota(View v) {
        try {
            RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup);
            RadioButton rbId = (RadioButton) findViewById(rg.getCheckedRadioButtonId());

            TextView tvNotaFinal = (TextView) findViewById(R.id.tvNotaFinal);
            tvNotaFinal.setText("");

            if (notaValida(examen.getText().toString(), practicas.getText().toString(), actitud.getText().toString())) {
                String cad = "La nota final del \n" + rbId.getText().toString() + " es " + df.format(calculoNota(examen.getText().toString(), practicas.getText().toString(),
                        actitud.getText().toString()));
                TextView texto = (TextView) findViewById(R.id.tvNotaFinal);
                texto.setText(cad);

                switch (rbId.getText().toString()) {
                    case "1ER TRIMESTRE":
                        notas1Tri[0] = Integer.parseInt(examen.getText().toString());
                        notas1Tri[1] = Integer.parseInt(practicas.getText().toString());
                        notas1Tri[2] = Integer.parseInt(actitud.getText().toString());
                        break;
                    case "2º TRIMESTRE":
                        notas2Tri[0] = Integer.parseInt(examen.getText().toString());
                        notas2Tri[1] = Integer.parseInt(practicas.getText().toString());
                        notas2Tri[2] = Integer.parseInt(actitud.getText().toString());
                        break;
                    case "PROYECTO INTEGRADOR":
                        notas3Tri[0] = Integer.parseInt(examen.getText().toString());
                        notas3Tri[1] = Integer.parseInt(practicas.getText().toString());
                        notas3Tri[2] = Integer.parseInt(actitud.getText().toString());
                        break;
                }

            } else {
                Toast tError = Toast.makeText(getApplicationContext(), "Las notas no pueden ser superiores a 10", Toast.LENGTH_SHORT);
                tError.setGravity(Gravity.CENTER_HORIZONTAL, 0, 835);
                tError.show();
            }
        } catch (Exception ex) {
            Toast tError = Toast.makeText(getApplicationContext(), "Faltan campos por rellenar.", Toast.LENGTH_SHORT);
            tError.setGravity(Gravity.CENTER_HORIZONTAL, 0, 835);
            tError.show();
        }
    }

    public double calculoNota(String examen, String practicas, String actitud) {
        double notaFinal = (Double.parseDouble(examen) * 0.7) + (Double.parseDouble(practicas) * 0.2);
        if ((Double.parseDouble(examen) >= 5 && Double.parseDouble(practicas) >= 5)) {
            notaFinal += (Double.parseDouble(actitud) * 0.1);
        }
        return notaFinal;
    }

    public boolean notaValida(String examen, String practicas, String actitud) {
        if (Double.parseDouble(examen) <= 10 && Double.parseDouble(practicas) <= 10 && Double.parseDouble(actitud) <= 10) {
            return true;
        }
        return false;
    }

    private boolean camposVacios() {
        if (examen.getText().toString().isEmpty() && practicas.getText().toString().isEmpty() && actitud.getText().toString().isEmpty()) {
            return true;
        }
        return false;
    }

    public void datos1Trim(View view) {
        if (!camposVacios()) {
            examen.setText(Integer.toString(notas1Tri[0]));
            practicas.setText(Integer.toString(notas1Tri[1]));
            actitud.setText(Integer.toString(notas1Tri[2]));
        }
    }

    public void datos2Trim(View view) {
        if (!camposVacios()) {
            examen.setText(Integer.toString(notas2Tri[0]));
            practicas.setText(Integer.toString(notas2Tri[1]));
            actitud.setText(Integer.toString(notas2Tri[2]));
        }
    }

    public void datos3Trim(View view) {
        if (!camposVacios()) {
            examen.setText(Integer.toString(notas3Tri[0]));
            practicas.setText(Integer.toString(notas3Tri[1]));
            actitud.setText(Integer.toString(notas3Tri[2]));
        }
    }

}