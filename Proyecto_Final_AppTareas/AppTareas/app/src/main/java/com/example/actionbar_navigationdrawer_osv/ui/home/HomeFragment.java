package com.example.actionbar_navigationdrawer_osv.ui.home;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.actionbar_navigationdrawer_osv.EditarTarea;
import com.example.actionbar_navigationdrawer_osv.R;
import com.example.actionbar_navigationdrawer_osv.oovv.TaskSQLiteHelper;

public class HomeFragment extends Fragment {
    private Cursor cursor;
    private ListView listView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Creamos el fragment a partir del archivo de diseño layout/fragment_home.xml
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        listView = (ListView) view.findViewById(R.id.lvTareas);

        TaskSQLiteHelper usdbh = new TaskSQLiteHelper(getContext(), "bdTareas", null, 1);
        //Obtenemos referencia a la base de datos para poder modificarla.
        final SQLiteDatabase db = usdbh.getWritableDatabase();

        cursor = db.rawQuery("SELECT titulo AS _id, descripcion, fechaLimite, horaLimite FROM tareas ORDER BY _id ASC", null);


        String[] from = {"_id", "descripcion", "fechaLimite", "horaLimite"};
        int[] to = {R.id.tvTitulo, R.id.tvDescripcion, R.id.tvFechaLimite, R.id.tvHoraLimite};

        if (cursor.getCount() > 0) {
            SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(getContext(), R.layout.custom_list_view, cursor, from, to, 0);
            ListView simpleListView = (ListView) view.findViewById(R.id.lvTareas);

            simpleListView.setAdapter(cursorAdapter);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Bundle extras = new Bundle();

                TextView tvTitulo = (TextView) view.findViewById(R.id.tvTitulo);
                TextView tvDescripcion = (TextView) view.findViewById(R.id.tvDescripcion);
                TextView tvFechaLimite = (TextView) view.findViewById(R.id.tvFechaLimite);
                TextView tvHoraLimite = (TextView) view.findViewById(R.id.tvHoraLimite);

                cursor = db.rawQuery("SELECT usarRecordatorio FROM tareas " +
                        "WHERE titulo LIKE '%" + tvTitulo.getText().toString() + "%'", null);

                extras.putString("titulo", tvTitulo.getText().toString());
                extras.putString("descripcion", tvDescripcion.getText().toString());
                extras.putString("fechaLimite", tvFechaLimite.getText().toString());
                extras.putString("horaLimite", tvHoraLimite.getText().toString());

                while (cursor.moveToNext()) {
                    extras.putBoolean("recordatorio", (cursor.getInt(0) == 0) ? false : true);
                }
                cursor.close();

                Intent intent = new Intent(getContext(), EditarTarea.class);
                intent.putExtras(extras);

                startActivity(intent);
            }
        });

        return view;
    }

}