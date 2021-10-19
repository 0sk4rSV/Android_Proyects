package com.example.act6_osv;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FichaAlumActivity extends AppCompatActivity {

    private String aCampos[];
    private EditText nombre;
    private EditText apellido1;
    private EditText apellido2;
    private EditText direccion;
    private EditText codPostal;
    private EditText numTel;
    private EditText email;
    private EditText usuario;
    private EditText clave;
    private TextView mensaje;
    private String cad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficha);

        nombre = (EditText) findViewById(R.id.etNombre);
        apellido1 = (EditText) findViewById(R.id.etApellido1);
        apellido2 = (EditText) findViewById(R.id.etApellido2);
        direccion = (EditText) findViewById(R.id.etDireccion);
        codPostal = (EditText) findViewById(R.id.etCodPostal);
        numTel = (EditText) findViewById(R.id.etNumTel);
        email = (EditText) findViewById(R.id.etCorreo);
        usuario = (EditText) findViewById(R.id.etUsuario);
        clave = (EditText) findViewById(R.id.etClave);
        mensaje = (TextView) findViewById(R.id.tvDatosFicha);

        SharedPreferences sharedPref = getSharedPreferences("datos", Context.MODE_PRIVATE);
        nombre.setText(sharedPref.getString("nombre", ""));
        apellido1.setText(sharedPref.getString("apellido1", ""));
        apellido2.setText(sharedPref.getString("apellido2", ""));
        direccion.setText(sharedPref.getString("direccion", ""));
        codPostal.setText(sharedPref.getString("codPostal", ""));
        numTel.setText(sharedPref.getString("numTel", ""));
        email.setText(sharedPref.getString("email", ""));
        usuario.setText(sharedPref.getString("usuario", ""));
        clave.setText(sharedPref.getString("clave", ""));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void validarFicha(View view) {

        mensaje.setText("");
        aCampos = new String[]{nombre.getText().toString(), apellido1.getText().toString(), direccion.getText().toString(),
                codPostal.getText().toString(), numTel.getText().toString(), numTel.getText().toString(),
                email.getText().toString(), usuario.getText().toString(), clave.getText().toString()};

        SharedPreferences preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString("nombre", nombre.getText().toString());
        editor.putString("apellido1", apellido1.getText().toString());
        editor.putString("apellido2", apellido2.getText().toString());
        editor.putString("direccion", direccion.getText().toString());
        editor.putString("codPostal", codPostal.getText().toString());
        editor.putString("numTel", numTel.getText().toString());
        editor.putString("email", email.getText().toString());
        editor.putString("usuario", usuario.getText().toString());
        editor.putString("clave", clave.getText().toString());
        editor.commit();

        if (camposVacio(aCampos) == false) {
            if (validaCodPostal(codPostal.getText().toString()) == false) {
                Toast tErrorVal = Toast.makeText(getApplicationContext(), "El Código postal debe tener 5 dígitos.", Toast.LENGTH_SHORT);
                codPostal.setBackgroundTintList(getResources().getColorStateList(R.color.red_light));
                tErrorVal.setGravity(Gravity.CENTER_HORIZONTAL, 0, 850);
                tErrorVal.show();
            } else {
                codPostal.setBackgroundTintList(getResources().getColorStateList(R.color.gray));
                if (validaEmail(email.getText().toString()) == false) {
                    Toast tErrorVal = Toast.makeText(getApplicationContext(), "El e-mail tiene un formato incorrecto.", Toast.LENGTH_SHORT);
                    email.setBackgroundTintList(getResources().getColorStateList(R.color.red_light));
                    tErrorVal.setGravity(Gravity.CENTER_HORIZONTAL, 0, 850);
                    tErrorVal.show();
                } else {
                    email.setBackgroundTintList(getResources().getColorStateList(R.color.gray));
                    if (validaUsuario(usuario.getText().toString()) == false) {
                        Toast tErrorVal = Toast.makeText(getApplicationContext(), "El usuario debe tener 8 dígitos.", Toast.LENGTH_SHORT);
                        usuario.setBackgroundTintList(getResources().getColorStateList(R.color.red_light));
                        tErrorVal.setGravity(Gravity.CENTER_HORIZONTAL, 0, 850);
                        tErrorVal.show();
                    } else {
                        usuario.setBackgroundTintList(getResources().getColorStateList(R.color.gray));
                        if (validaClave(clave.getText().toString()) == false) {
                            Toast tErrorVal = Toast.makeText(getApplicationContext(), "La clave debe tener 8 dígitos.", Toast.LENGTH_SHORT);
                            clave.setBackgroundTintList(getResources().getColorStateList(R.color.red_light));
                            tErrorVal.setGravity(Gravity.CENTER_HORIZONTAL, 0, 850);
                            tErrorVal.show();
                        } else {
                            clave.setBackgroundTintList(getResources().getColorStateList(R.color.gray));
                            cad = ("Ficha del alumno " + nombre.getText().toString() + "\n" + apellido1.getText().toString() + " " + apellido2.getText().toString() + " \nguardada correctamente.");
                            mensaje = (TextView) findViewById(R.id.tvDatosFicha);
                            mensaje.setText(cad);
                        }
                    }
                }
            }
        } else {
            Toast tError = Toast.makeText(getApplicationContext(), "Faltan campos por rellenar.", Toast.LENGTH_SHORT);
            tError.setGravity(Gravity.CENTER_HORIZONTAL, 0, 850);
            tError.show();
        }
    }

    public void volverAtras(View view) {
        Intent intent = new Intent(FichaAlumActivity.this, MainActivity.class);
        intent.putExtra("texto", cad);
        startActivity(intent);
    }

    public boolean camposVacio(String[] aCampos) {
        for (int i = 0; i < aCampos.length; i++) {
            if (aCampos[i].isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public boolean validaCodPostal(String codPostal) {
        if (codPostal.length() == 5) {
            return true;
        }
        return false;
    }

    public boolean validaEmail(String email) {
        Pattern pattern = Pattern.compile("^([0-9a-zA-Z]+[-._+&])*[0-9a-zA-Z]+@([-0-9a-zA-Z]+[.])+[a-zA-Z]{2,6}$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean validaUsuario(String usuario) {
        if (usuario.length() == 8) {
            return true;
        }
        return false;
    }

    public boolean validaClave(String clave) {
        if (clave.length() == 8) {
            return true;
        }
        return false;
    }

}