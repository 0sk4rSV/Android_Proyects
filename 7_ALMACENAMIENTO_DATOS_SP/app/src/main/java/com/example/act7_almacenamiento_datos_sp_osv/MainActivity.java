package com.example.act7_almacenamiento_datos_sp_osv;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Calendar c = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    String fechaActual = df.format(c.getTime());

    private TextView ultimoAcceso;
    private RadioButton rbMoodle;
    private RadioButton rbDirecta;
    private RadioButton rbAndroid;
    private RadioGroup rg;
    private RadioButton rbId;
    private EditText curso;
    private TextView cursando;
    private TextView txtGuardado;
    private String cursoMoodle;
    private String cursoDirecta;
    private String cursoAndroid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ultimoAcceso = (TextView) findViewById(R.id.tvFechaGuardado);
        rbMoodle = (RadioButton) findViewById(R.id.rbMoodle);
        rbDirecta = (RadioButton) findViewById(R.id.rbDirecta);
        rbAndroid = (RadioButton) findViewById(R.id.rbAndroid);
        rg = (RadioGroup) findViewById(R.id.radioGroup);
        curso = (EditText) findViewById(R.id.etCurso);
        cursando = (TextView) findViewById(R.id.tvCursoActual);
        txtGuardado = (TextView) findViewById(R.id.tvGuardado);

        SharedPreferences sPref = getSharedPreferences("datos", Context.MODE_PRIVATE);
        cursoMoodle = sPref.getString("cursoMoodle", "");
        cursoDirecta = sPref.getString("cursoDirecta", "");
        cursoAndroid = sPref.getString("cursoAndroid", "");

        if (sPref.getString("ultimoAcceso", "").contains("Se guardó la fecha: ") && hayDatosGuardados()) {
            ultimoAcceso.setText(" Último acceso: " + fechaActual + " ");
            ultimoAcceso.setBackgroundColor(Color.parseColor("#00FF00"));
            txtGuardado.setText("Datos del curso guardados.");
        } else {
            if (cursando.getText().toString().isEmpty()) {
                ultimoAcceso.setText("Último acceso: " + fechaActual + " ");
                ultimoAcceso.setBackgroundColor(Color.parseColor("#00FF00"));
                rbMoodle.setChecked(false);
                rbDirecta.setChecked(false);
                rbAndroid.setChecked(false);
                txtGuardado.setText("No hay datos del curso guardados.");
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void guardarDatos(View view) {
        SharedPreferences sPref = getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();

        if (hayCamposVacios()) {
            errorGuardado();
            Toast tError = Toast.makeText(getApplicationContext(), "Faltan campos por rellenar.", Toast.LENGTH_LONG);
            tError.setGravity(Gravity.CENTER_HORIZONTAL, 0, 800);
            tError.show();
        } else {
            if (rbMoodle.isChecked() == false && rbDirecta.isChecked() == false && rbAndroid.isChecked() == false) {
                errorGuardado();
                Toast tError = Toast.makeText(getApplicationContext(), "No se ha seleccionado ningún curso.", Toast.LENGTH_LONG);
                tError.setGravity(Gravity.CENTER_HORIZONTAL, 0, 800);
                tError.show();
            } else {
                curso.setBackgroundTintList(getResources().getColorStateList(R.color.gray));
                if (curso.getText().toString().isEmpty()) {
                    errorGuardado();
                    curso.setBackgroundTintList(getResources().getColorStateList(R.color.red_light));
                    Toast tError = Toast.makeText(getApplicationContext(), "Falta por rellenar el campo: Curso.", Toast.LENGTH_SHORT);
                    tError.setGravity(Gravity.CENTER_HORIZONTAL, 0, 800);
                    tError.show();
                } else {
                    rbId = (RadioButton) findViewById(rg.getCheckedRadioButtonId());

                    switch (rbId.getText().toString()) {
                        case "AULA MOODLE":
                            cursoMoodle = curso.getText().toString();
                            editor.putString("cursoMoodle", curso.getText().toString());
                            break;
                        case "AULA DIRECTA":
                            cursoDirecta = curso.getText().toString();
                            editor.putString("cursoDirecta", curso.getText().toString());
                            break;
                        case "AULA ANDROID":
                            cursoAndroid = curso.getText().toString();
                            editor.putString("cursoAndroid", curso.getText().toString());
                            break;
                    }
                    editor.putString("ultimoAcceso", ("Se guardó la fecha: " + fechaActual));
                    editor.putBoolean("rbMoodle", rbMoodle.isChecked()).apply();
                    editor.putBoolean("rbDirecta", rbDirecta.isChecked()).apply();
                    editor.putBoolean("rbAndroid", rbAndroid.isChecked()).apply();
                    editor.putString("curso", curso.getText().toString());
                    editor.putString("cursando", cursando.getText().toString());
                    editor.putString("txtGuardado", txtGuardado.getText().toString());
                    editor.commit();

                    c = Calendar.getInstance();
                    ultimoAcceso.setText(" Se guardó la fecha: " + fechaActual + " ");
                    ultimoAcceso.setBackgroundColor(Color.parseColor("#51D1F6"));
                    txtGuardado.setText("Datos del curso guardados.");
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void borrarDatos(View view) {
        Context contexto = this.getApplicationContext();
        SharedPreferences sPref = contexto.getSharedPreferences("datos", Context.MODE_PRIVATE);
        sPref.edit().clear().commit();

        ultimoAcceso.setText(" Último acceso no definido ");
        ultimoAcceso.setBackgroundColor(Color.parseColor("#FF0000"));
        curso.setBackgroundTintList(getResources().getColorStateList(R.color.gray));
        rbMoodle.setChecked(false);
        rbDirecta.setChecked(false);
        rbAndroid.setChecked(false);
        curso.setText("");
        cursando.setText("");
        cursoMoodle = "";
        cursoDirecta = "";
        cursoAndroid = "";
        txtGuardado.setText("No hay datos del curso guardados.");
    }

    private boolean hayCamposVacios() {
        if (curso.getText().toString().isEmpty() && rbMoodle.isChecked() == false && rbDirecta.isChecked() == false && rbAndroid.isChecked() == false) {
            return true;
        }
        return false;
    }

    private boolean hayDatosGuardados() {
        if (cursoMoodle.length() > 0 || cursoDirecta.length() > 0 || cursoAndroid.length() > 0) {
            return true;
        }
        return false;
    }

    private void errorGuardado() {
        Context contexto = this.getApplicationContext();
        SharedPreferences sPref = contexto.getSharedPreferences("datos", Context.MODE_PRIVATE);
        sPref.edit().clear().commit();

        ultimoAcceso.setText(" Último acceso no definido ");
        ultimoAcceso.setBackgroundColor(Color.parseColor("#FF0000"));
        cursando.setText("");
        txtGuardado.setText("No se han podido guardar\nlos datos del curso.");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setCursoGuardado(View view) {
        rbId = (RadioButton) findViewById(rg.getCheckedRadioButtonId());
        switch (rbId.getText().toString()) {
            case "AULA MOODLE":
                if (cursoMoodle.length() > 0) {
                    curso.setBackgroundTintList(getResources().getColorStateList(R.color.gray));
                    curso.setText(cursoMoodle);
                    cursando.setText("Cursando: " + cursoMoodle);
                    txtGuardado.setText("Datos del curso guardados.");
                } else {
                    curso.setText("");
                    cursando.setText("");
                    txtGuardado.setText("No hay datos del curso guardados.");
                }
                break;
            case "AULA DIRECTA":
                if (cursoDirecta.length() > 0) {
                    curso.setBackgroundTintList(getResources().getColorStateList(R.color.gray));
                    curso.setText(cursoDirecta);
                    cursando.setText("Cursando: " + cursoDirecta);
                    txtGuardado.setText("Datos del curso guardados.");
                } else {
                    curso.setText("");
                    cursando.setText("");
                    txtGuardado.setText("No hay datos del curso guardados.");
                }
                break;
            case "AULA ANDROID":
                if (cursoAndroid.length() > 0) {
                    curso.setBackgroundTintList(getResources().getColorStateList(R.color.gray));
                    curso.setText(cursoAndroid);
                    cursando.setText("Cursando: " + cursoAndroid);
                    txtGuardado.setText("Datos del curso guardados.");
                } else {
                    curso.setText("");
                    cursando.setText("");
                    txtGuardado.setText("No hay datos del curso guardados.");
                }
                break;
        }
    }

}