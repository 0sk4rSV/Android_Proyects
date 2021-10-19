package com.example.actionbar_navigationdrawer_osv;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.actionbar_navigationdrawer_osv.oovv.AlarmReceiver;
import com.example.actionbar_navigationdrawer_osv.oovv.TaskSQLiteHelper;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    //Declaramos una variable de tipo ToolBar, para poder modificar y configurar nuestro ToolBar.
    private Toolbar toolbar;

    private ListView listView;
    private Cursor cursor;
    private SimpleCursorAdapter adapter;

    /* Para hacer funcional nuestro Navigation Drawer empezamos declarando una variable de tipo
    AppBarConfiguration. */
    private AppBarConfiguration mAppBarConfiguration;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Obtenemos la referencia del ToolBar desde el archivo de diseño que tenemos asignado en
        res/layout/activity_main */
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        listView = (ListView) findViewById(R.id.lvTareas);

        /* Para modificar el ToolBar primero tenemos que llamar al método setSupportActionBar y
        pasarle como parámetro nuestro ToolBar. Así lo estableceremos en nuestra Activity. */
        setSupportActionBar(toolbar);

        //Ahora podemos modificar el título y el color del texto que tendrá nuestra AppBar.
        getSupportActionBar().setTitle("Mi lista de tareas");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        /* Declaramos una variable del tipo DrawerLayout para obtener la referencia del DrawerLayout
        que tenemos definido en el archivo de diseño res/layout/activity_main.xml asignado al Activity
        principal. */
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

         /* Declaramos una variable de tipo NavigationView para obtener la referencia del
         Navigation Drawer que tenemos definido en el archivo de diseño res/layout/activity_main.xml
         asignado al Activity principal. */
        NavigationView navigationView = findViewById(R.id.nav_view);

        /* Construimos el objeto que nos permitirá navegar por los fragments correctos cuando
        seleccionemos algún elemento en el menú del Navigation Drawer.
        Debemos hacer coincidir los id de los elementos de dicho menú, con los id de los fragments
        del gráfico de navegación definido en el archivo res/navigation/mobile_navigation.xml */
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_paraHoy, R.id.nav_recordatorios)
                .setDrawerLayout(drawer)
                .build();

        /* Creamos el componenete de tipo NavController que nos permitirá crear un grafo en el que
        estarán conectados todos los Activities y fragments de nuestra aplicación.
        Después tendremos que abrir el archivo navigation/mobile_navigation.xml y seleccionar
        el host fragment que se mostrará por defecto en el Activity principal. */
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //Abrimos la base de datos en modo escritura.
        TaskSQLiteHelper usdbh = new TaskSQLiteHelper(this, "bdTareas", null, 1);
        //Obtenemos referencia a la base de datos para poder modificarla.
        SQLiteDatabase db = usdbh.getWritableDatabase();

        String alarm = Context.ALARM_SERVICE;
        AlarmManager alarmManager = (AlarmManager) getSystemService(alarm);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 123456789, intent, 0);
        int type = AlarmManager.RTC_WAKEUP;
        //Intervalo de tiempo en millisegundos que se ejecutará onReceive en el BroadCast
        long interval = 60000;
        alarmManager.setRepeating(type, System.currentTimeMillis(), interval, pendingIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        /* Vamos a definir e implementar el funcionamiento del SearchView que se encuentra en nuestra AppBar.
        Previamente en el archivo de diseño de nuestra AppBar res/menu/menu_main.xml asignado a nuestra
        Activity principal; tenemos que tener asignado en el elemento que desplegará el SearchView, el atributo
        "app:actionViewClass="android.widget.SearchView".
        Empezamos obteniendo la referencia del elemento que implementará el SearchView y le asignamos la acción. */
        final MenuItem searchItem = menu.findItem(R.id.bBuscar);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setMaxWidth(1000);

        // Le cambiamos al EditText que viene integrado en el Searchview, el color.
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) searchView.findViewById(id);
        textView.setTextColor(Color.WHITE);

        /* Configuramos el SearchView para que cuando se despliegue, obtenga directamente el foco y podamos
        escribir en él sin tener que pulsarlo dos veces; entre otros detalles. */
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

        TaskSQLiteHelper usdbh = new TaskSQLiteHelper(this, "agenda", null, 1);
        final SQLiteDatabase db = usdbh.getWritableDatabase();

        /* Aquí viene la configuración más importante del SearchView.
        Primero le asignamos un listener de accion al SearchViewer.
        Después hacemos uso de dos métodos sobreescritos: */
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            /*El método onQueryTextSubmit() recibe como parámetro el texto que introducimos en el SearchView y al pulsar
            intro o el botón de buscar, se ejecutarán las acciones que estén dentro del método. */
            @Override
            public boolean onQueryTextSubmit(String query) {
                cursor = db.rawQuery("SELECT titulo AS _id, descripcion, fechaLimite, horaLimite FROM tareas " +
                        "WHERE titulo LIKE '%" + query + "%' ORDER BY titulo ASC", null);

                listarListView(cursor);
                return false;
            }

            /* El método onQueryTextChange() recibe como parámetro el texto que introduzcamos en el SearchView y se ejecutará
            automaticamente, cada vez que escribamos un nuevo caracter. */
            @Override
            public boolean onQueryTextChange(String newText) {
                cursor = db.rawQuery("SELECT titulo AS _id, descripcion, fechaLimite, horaLimite FROM tareas " +
                        "WHERE titulo LIKE '%" + newText + "%' ORDER BY titulo ASC", null);

                listarListView(cursor);
                return true;
            }
        });
        return true;
    }

    /* Sobreescribimos el método onOptionsItemSelected() para asignarle una acción mediante
    un switch, a los elementos de acción que tengamos configurados en la AppBar. */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bInformacion:
                Intent i = new Intent(this, Informacion.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* Sobreescribimos el método onSupportNavigateUp() para navegar por los diferentes fragmentos
    y que al pulsar la tecla de retroceso, nos muestre de nuevo el fragment host que se muestra por
    defecto. */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void crearTarea(View view) {
        Intent i = new Intent(this, CrearTarea.class);
        startActivity(i);
    }

    public void listarListView(Cursor cursor) {
        String[] from = {"_id", "descripcion", "fechaLimite", "horaLimite"};
        int[] to = {R.id.tvTitulo, R.id.tvDescripcion, R.id.tvFechaLimite, R.id.tvHoraLimite};

        if (cursor.getCount() > 0) {
            adapter = new SimpleCursorAdapter(getBaseContext(), R.layout.custom_list_view, cursor, from, to, 0);
            ListView simpleListView = (ListView) findViewById(R.id.lvTareas);

            simpleListView.setAdapter(adapter);
        }
    }

}














