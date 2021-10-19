package com.example.act8_bd_sqlite_osv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        listView = (ListView) findViewById(R.id.lvContactos);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Contactos");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        toolbar.setPadding(40, toolbar.getPaddingTop(), 25, toolbar.getPaddingBottom());

        //Abrimos la base de datos en modo escritura.
        AgendaSQLiteHelper usdbh = new AgendaSQLiteHelper(this, "agenda", null, 1);
        //Obtenemos referencia a la base de datos para poder modificarla.
        SQLiteDatabase db = usdbh.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT nombre AS _id, apellidos, telefono FROM contactos ORDER BY nombre ASC", null);

        String[] from = {"_id", "apellidos", "telefono"};
        int[] to = {R.id.tvNombre, R.id.tvApellidos, R.id.tvTelefono};

        if (cursor.getCount() > 0) {
            SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.custom_list, cursor, from, to, 0);
            ListView simpleListView = (ListView) findViewById(R.id.lvContactos);
            simpleListView.setAdapter(cursorAdapter);

            TextView tvSinContactos = (TextView) findViewById(R.id.tvSinContactos);
            tvSinContactos.setVisibility(View.INVISIBLE);
            TextView tvPulsa = (TextView) findViewById(R.id.tvPulsa);
            tvPulsa.setVisibility(View.INVISIBLE);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Bundle extras = new Bundle();

                TextView tvNombre = (TextView) view.findViewById(R.id.tvNombre);
                TextView tvApellidos = (TextView) view.findViewById(R.id.tvApellidos);
                TextView tvTelefono = (TextView) view.findViewById(R.id.tvTelefono);

                extras.putString("nombre", tvNombre.getText().toString());
                extras.putString("apellidos", tvApellidos.getText().toString());
                extras.putString("telefono", tvTelefono.getText().toString());

                Intent intent = new Intent(getApplicationContext(), InfoContacto.class);
                intent.putExtras(extras);

                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem searchItem = menu.findItem(R.id.bSearch);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) searchView.findViewById(id);
        textView.setTextColor(Color.WHITE);

        searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                searchView.setIconifiedByDefault(true);
                searchView.setFocusable(true);
                searchView.setIconified(false);
                searchView.requestFocusFromTouch();
                return false;
            }
        });

        AgendaSQLiteHelper usdbh = new AgendaSQLiteHelper(this, "agenda", null, 1);
        final SQLiteDatabase db = usdbh.getWritableDatabase();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Cursor cursor = db.rawQuery("SELECT nombre AS _id, apellidos, telefono FROM contactos WHERE nombre LIKE '%" + query +
                        "%' OR apellidos LIKE '%" + query + "%' OR telefono LIKE '" + query + "%' ORDER BY nombre ASC", null);

                String[] from = {"_id", "apellidos", "telefono"};
                int[] to = {R.id.tvNombre, R.id.tvApellidos, R.id.tvTelefono};

                if (cursor.getCount() > 0) {
                    SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.custom_list, cursor, from, to, 0);
                    ListView simpleListView = (ListView) findViewById(R.id.lvContactos);
                    simpleListView.setAdapter(cursorAdapter);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Cursor cursor = db.rawQuery("SELECT nombre AS _id, apellidos, telefono FROM contactos WHERE nombre LIKE '%" + newText +
                        "%' OR apellidos LIKE '%" + newText + "%' OR telefono LIKE '" + newText + "%' ORDER BY nombre ASC", null);

                String[] from = {"_id", "apellidos", "telefono"};
                int[] to = {R.id.tvNombre, R.id.tvApellidos, R.id.tvTelefono};

                if (cursor.getCount() > 0) {
                    SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.custom_list, cursor, from, to, 0);
                    ListView simpleListView = (ListView) findViewById(R.id.lvContactos);
                    simpleListView.setAdapter(cursorAdapter);
                }
                return true;
            }
        });
        return true;
    }

    public void añadirContacto(View view) {
        Intent i = new Intent(this, CrearContacto.class);
        startActivity(i);
    }

    public void abrirAppContactos(View view) {
        Intent intent  = new Intent();
        intent.setComponent(new ComponentName("com.android.contacts", "com.android.contacts.DialtactsContactsEntryActivity"));
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.addCategory("android.intent.category.DEFAULT");
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}