package com.example.actionbar_navigationdrawer_osv;

import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import com.example.actionbar_navigationdrawer_osv.oovv.AlarmService;
import com.example.actionbar_navigationdrawer_osv.oovv.Tarea;
import com.example.actionbar_navigationdrawer_osv.oovv.TaskSQLiteHelper;
import com.example.actionbar_navigationdrawer_osv.oovv.TaskSQLiteHelper2;
import com.example.actionbar_navigationdrawer_osv.ui.pickers.DatePickerFragment;
import com.example.actionbar_navigationdrawer_osv.ui.pickers.TimePickerFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class CrearTarea extends AppCompatActivity implements View.OnClickListener {
    private NotificationCompat.Builder mBuilder;
    private NotificationManager notification;
    private AlarmService alarmService = new AlarmService();

    private Toolbar toolbar;
    private EditText etTitulo;
    private EditText etDescripcion;
    private EditText etFechaLimite;
    private EditText etHoraLimite;
    private ImageButton ibFecha;
    private ImageButton ibHora;
    private Switch sRecordatorio;

    private GregorianCalendar gCalendar;
    private String fechaCompleta;
    private String fechaSeleccionada;
    private String horaSeleccionada;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_tarea);

        etTitulo = (EditText) findViewById(R.id.etTitulo);
        etDescripcion = (EditText) findViewById(R.id.etDescripcion);

        etFechaLimite = (EditText) findViewById(R.id.etFechaLimite);
        etFechaLimite.setOnClickListener(this);

        etHoraLimite = (EditText) findViewById(R.id.etHoraLimite);
        etHoraLimite.setOnClickListener(this);

        ibFecha = (ImageButton) findViewById(R.id.ibFecha);
        ibFecha.setOnClickListener(this);
        ibHora = (ImageButton) findViewById(R.id.ibHora);
        ibHora.setOnClickListener(this);

        sRecordatorio = (Switch) findViewById(R.id.sRecordatorio);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Nueva tarea");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        toolbar.setPadding(0, toolbar.getPaddingTop(), 0, toolbar.getPaddingBottom());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_crear_tarea, menu);

        TaskSQLiteHelper usdbh = new TaskSQLiteHelper(this, "agenda", null, 1);
        final SQLiteDatabase db = usdbh.getWritableDatabase();

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bAñadir:
                try {
                    añadirTarea();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.etFechaLimite:
                mostrarDatePickerDialog();
                break;
            case R.id.etHoraLimite:
                mostrarTimePickerDialog();
                break;
            case R.id.ibFecha:
                etFechaLimite.setText("");
                ibFecha.setImageResource(R.drawable.ic_baseline_calendar_today_24);
                break;
            case R.id.ibHora:
                etHoraLimite.setText("");
                ibHora.setImageResource(R.drawable.ic_baseline_schedule_24);
                break;
            default:
                break;
        }
    }

    private void mostrarDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int año, int mes, int dia) {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMMM", Locale.getDefault());

                gCalendar = new GregorianCalendar(año, mes, dia);
                fechaCompleta = año + "/" + mes + "/" + dia;

                fechaSeleccionada = dosDigitos(dia) + "/" + dosDigitos(mes + 1) + "/" + año;

                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, año);
                c.set(Calendar.MONTH, mes);
                c.set(Calendar.DAY_OF_MONTH, dia);

                String diaSemana = sdf.format(c.getTime());
                diaSemana = Character.toUpperCase(diaSemana.charAt(0)) + diaSemana.substring(1, 4) + ", " + dia +
                        " de " + diaSemana.substring(5) + " de " + año;

                sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date date = new Date();
                String fechaActual = sdf.format(date);

                if (fechaSeleccionada.equals(fechaActual)) {
                    etFechaLimite.setText("Hoy.");
                } else {
                    etFechaLimite.setText(diaSemana);
                }
                if (etFechaLimite.getText().toString().trim().isEmpty()) {
                    ibFecha.setImageResource(R.drawable.ic_baseline_calendar_today_24);
                } else {
                    ibFecha.setImageResource(R.drawable.ic_baseline_cancel_24);
                }
            }
        });
        newFragment.show(this.getSupportFragmentManager(), "datePicker");
    }

    private void mostrarTimePickerDialog() {
        TimePickerFragment newFragment = TimePickerFragment.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hora, int minuto) {
                gCalendar.set(Calendar.HOUR_OF_DAY, hora);
                gCalendar.set(Calendar.MINUTE, minuto);
                fechaCompleta += "/" + hora + "/" + minuto;
                horaSeleccionada = dosDigitos(hora) + ":" + dosDigitos(minuto);

                etHoraLimite.setText(horaSeleccionada);

                if (etHoraLimite.getText().toString().trim().isEmpty()) {
                    ibHora.setImageResource(R.drawable.ic_baseline_schedule_24);
                } else {
                    ibHora.setImageResource(R.drawable.ic_baseline_cancel_24);
                }
            }
        });
        newFragment.show(this.getSupportFragmentManager(), "timePicker");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void añadirTarea() throws ParseException {
        //Abrimos la base de datos TAREAS en modo escritura.
        TaskSQLiteHelper usdbh = new TaskSQLiteHelper(this, "bdTareas", null, 1);
        //Obtenemos referencia a la base de datos para poder modificarla.
        SQLiteDatabase db = usdbh.getWritableDatabase();

        //Abrimos la base de datos ALARMAS en modo escritura.
        TaskSQLiteHelper2 usdbh2 = new TaskSQLiteHelper2(this, "bdAlarmas", null, 1);
        //Obtenemos referencia a la base de datos para poder modificarla.
        SQLiteDatabase db2 = usdbh2.getWritableDatabase();

        if (etTitulo.getText().toString().trim().isEmpty() && etFechaLimite.getText().toString().trim().isEmpty()) {
            crearToast("Faltan campos por completar.").show();
            etTitulo.setBackgroundTintList(getResources().getColorStateList(R.color.colorRedLight));
            etFechaLimite.setBackgroundTintList(getResources().getColorStateList(R.color.colorRedLight));
        } else {
            if (etTitulo.getText().toString().trim().isEmpty()) {
                crearToast("Introduce titulo para la tarea.").show();
                etTitulo.setBackgroundTintList(getResources().getColorStateList(R.color.colorRedLight));
            } else {
                etTitulo.setBackgroundTintList(getResources().getColorStateList(R.color.colorGray));
                if (etFechaLimite.getText().toString().trim().isEmpty()) {
                    crearToast("Introduce una fecha límite.").show();
                    etFechaLimite.setBackgroundTintList(getResources().getColorStateList(R.color.colorRedLight));
                } else {
                    etTitulo.setBackgroundTintList(getResources().getColorStateList(R.color.colorGray));
                    crearTarea();
                    db.execSQL("INSERT INTO tareas(titulo, descripcion, fechaLimite, horaLimite, fechaCompleta, usarRecordatorio) " +
                            "VALUES('" + crearTarea().getTitulo() + "', '" + crearTarea().getDescripcion() + "', '" +
                            crearTarea().getFecha() + "', '" + crearTarea().getHora() + "', '" + crearTarea().getFechaCompleta() + "', '" +
                            ((crearTarea().isUsarRecordatorio() == false) ? 0 : 1) + "');");
                    db.close();

                    if (crearTarea().isUsarRecordatorio()) {
                        db2.execSQL("INSERT INTO alarmas(titulo, minutos) VALUES('" + crearTarea().getTitulo().trim() + "', '" + alarmService.getMinutes(gCalendar) + "');");
                        db2.close();
                    }

                    crearToast("Tarea añadida correctamente.").show();

                    finish();
                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Tarea crearTarea() {
        Tarea tarea = new Tarea(etTitulo.getText().toString().trim(), etDescripcion.getText().toString().trim(),
                etFechaLimite.getText().toString().trim(), etHoraLimite.getText().toString().trim(), fechaCompleta, sRecordatorio.isChecked());

        return tarea;
    }

    private Toast crearToast(String mensaje) {
        Toast toast = Toast.makeText(this, mensaje, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 200);
        View view = toast.getView();
        view.getBackground().setColorFilter(Color.rgb(34, 34, 36), PorterDuff.Mode.SRC_IN);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setTextColor(Color.WHITE);
        return toast;
    }

    private String dosDigitos(int n) {
        return (n <= 9) ? ("0" + n) : String.valueOf(n);
    }

}