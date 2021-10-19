package com.example.act8_bd_sqlite_osv;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class InfoContacto extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tvNombre;
    private TextView tvApellidos;
    private TextView tvTelefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvNombre = findViewById(R.id.tvNombre);
        tvApellidos = findViewById(R.id.tvApellidos);
        tvTelefono = findViewById(R.id.tvTelefono);

        Bundle parametros = this.getIntent().getExtras();
        if (parametros != null) {
            tvNombre.setText(parametros.getString("nombre"));
            tvApellidos.setText(parametros.getString("apellidos"));
            tvTelefono.setText(parametros.getString("telefono"));
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Información del contacto");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        toolbar.setPadding(35, toolbar.getPaddingTop(), 10, toolbar.getPaddingBottom());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bExportar:
                Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
                contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                contactIntent
                        .putExtra(ContactsContract.Intents.Insert.NAME, tvNombre.getText().toString() + " " + tvApellidos.getText().toString())
                        .putExtra(ContactsContract.Intents.Insert.PHONE, tvTelefono.getText().toString());

                startActivityForResult(contactIntent, 1);
                return true;

            case R.id.bEditar:
                Bundle extras = new Bundle();

                extras.putString("nombre", tvNombre.getText().toString());
                extras.putString("apellidos", tvApellidos.getText().toString());
                extras.putString("telefono", tvTelefono.getText().toString());

                Intent intent = new Intent(getApplicationContext(), EditContacto.class);
                intent.putExtras(extras);

                startActivity(intent);
                finish();
                return true;

            case R.id.bEliminar:
                AgendaSQLiteHelper usdbh = new AgendaSQLiteHelper(this, "agenda", null, 1);
                SQLiteDatabase bd = usdbh.getWritableDatabase();
                bd.execSQL("DELETE FROM contactos WHERE nombre = '" + tvNombre.getText().toString() + "'");
                bd.close();
                Toast.makeText(this, "Se ha eliminado el contacto: " + tvNombre.getText().toString(), Toast.LENGTH_LONG).show();
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void llamar(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + tvTelefono.getText().toString()));

        startActivity(intent);
    }

    public void enviarMensaje(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("sms:" + tvTelefono.getText().toString()));

        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

}
