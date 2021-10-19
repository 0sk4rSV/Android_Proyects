package com.example.act2_puntuacion_osv;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {

    DecimalFormat df = new DecimalFormat("##,##");
    private EditText examen;
    private EditText practicas;
    private EditText actitud;
    private Spinner spinner;
    private int notasPMDM[] = new int[3];
    private int notasDI[] = new int[3];
    private int notasAD[] = new int[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        examen = (EditText) findViewById(R.id.etExamen);
        practicas = (EditText) findViewById(R.id.etPracticas);
        actitud = (EditText) findViewById(R.id.etActitud);
        spinner = (Spinner) findViewById(R.id.sModulos);
    }

    public void calcularNota(View v) {

        try {
            String sModulo = spinner.getSelectedItem().toString();
            TextView tvNotaFinal = (TextView) findViewById(R.id.tvNotaFinal);
            tvNotaFinal.setText("");

            if (notaValida(examen.getText().toString(), practicas.getText().toString(), actitud.getText().toString())) {
                String cad = "La nota final del módulo\n" + sModulo + " es " + df.format(calculoNota(examen.getText().toString(), practicas.getText().toString(),
                        actitud.getText().toString()));
                TextView texto = (TextView) findViewById(R.id.tvNotaFinal);
                texto.setText(cad);
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

    public void guardarNota(View view) {
        String sModulo = spinner.getSelectedItem().toString();
        Toast tMensaje = Toast.makeText(getApplicationContext(), "Notas de " + sModulo + " guardadas", Toast.LENGTH_SHORT);
        tMensaje.setGravity(Gravity.CENTER_HORIZONTAL, 0, 835);
        try {
        switch (sModulo) {
            case "PMDM":
                notasPMDM[0] = Integer.parseInt(examen.getText().toString());
                notasPMDM[1] = Integer.parseInt(practicas.getText().toString());
                notasPMDM[2] = Integer.parseInt(actitud.getText().toString());
                tMensaje.show();
                break;
            case "DI":
                notasDI[0] = Integer.parseInt(examen.getText().toString());
                notasDI[1] = Integer.parseInt(practicas.getText().toString());
                notasDI[2] = Integer.parseInt(actitud.getText().toString());
                tMensaje.show();
                break;
            case "AC":
                notasAD[0] = Integer.parseInt(examen.getText().toString());
                notasAD[1] = Integer.parseInt(practicas.getText().toString());
                notasAD[2] = Integer.parseInt(actitud.getText().toString());
                tMensaje.show();
                break;
        }
        } catch (Exception ex) {
            Toast tError = Toast.makeText(getApplicationContext(), "Faltan campos por rellenar.", Toast.LENGTH_SHORT);
            tError.setGravity(Gravity.CENTER_HORIZONTAL, 0, 835);
            tError.show();
        }
    }

    public void cargarNota(View view) {
        String sModulo = spinner.getSelectedItem().toString();
        Toast tMensaje = Toast.makeText(getApplicationContext(), "Notas de " + sModulo + " cargadas", Toast.LENGTH_SHORT);
        tMensaje.setGravity(Gravity.CENTER_HORIZONTAL, 0, 835);
            switch (sModulo) {
                case "PMDM":
                    examen.setText(Integer.toString(notasPMDM[0]));
                    practicas.setText(Integer.toString(notasPMDM[1]));
                    actitud.setText(Integer.toString(notasPMDM[2]));
                    tMensaje.show();
                    break;
                case "DI":
                    examen.setText(Integer.toString(notasDI[0]));
                    practicas.setText(Integer.toString(notasDI[1]));
                    actitud.setText(Integer.toString(notasDI[2]));
                    tMensaje.show();
                    break;
                case "AD":
                    examen.setText(Integer.toString(notasAD[0]));
                    practicas.setText(Integer.toString(notasAD[1]));
                    actitud.setText(Integer.toString(notasAD[2]));
                    tMensaje.show();
                    break;
            }
    }
}
