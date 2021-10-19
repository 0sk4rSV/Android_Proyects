package com.example.actionbar_navigationdrawer_osv.oovv;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.example.actionbar_navigationdrawer_osv.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class AlarmService extends Service {

    final static String DATA_BASE_NAME = "bdAlarmas";
    private NotificationCompat.Builder mBuilder;
    private NotificationManager notification;
    private SQLiteDatabase db;
    private int tempTime;
    private boolean muestraNotificacion;

    @Override
    public void onCreate() {
        super.onCreate();
        //Crear la notificación
        crearNotificacion();
        int time = getMinutes(new GregorianCalendar());
        tempTime = time;
        muestraNotificacion = true;
        //Inicalizar la base de datos donde se encuentran las alarmas
        TaskSQLiteHelper2 usdbh = new TaskSQLiteHelper2(this, DATA_BASE_NAME, null, 1);
        //Obtenemos referencia a la base de datos para poder modificarla.
        db = usdbh.getWritableDatabase();
    }

    private void crearNotificacion() {
        mBuilder = new NotificationCompat.Builder(this, "001")
                .setSmallIcon(R.drawable.ic_stat_name)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_icon))
                .setContentTitle("Hay tareas pendientes.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        createNotificationChannel();
    }


    /**
     * Este método se ejecuta cuando se inicia o reinicia el proceso, en caso de que
     * Android lo haya finalizado para liberar RAM
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int idArranque) {
        //Creas un contador de tiempo, el valor 10000 es el intervalo de tiempo en que
        //se llamará al método onTick() en milisegundos, un valor menor hace la alarma más
        //precisa pero sobrecarga un poco más al sistema y puede crear una mala experiencia de ususario
        //en dispositivos con poca RAM
        new CountDownTimer(Long.MAX_VALUE, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                //Aquí verificas si hay alguna alarma a esa hora
                showNotification();
            }

            @Override
            public void onFinish() {

            }

        }.start();
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void showNotification() {
        //Obtener la fecha actual en minutos
        int time = getMinutes(new GregorianCalendar());

        Cursor cursor = db.rawQuery("SELECT titulo FROM alarmas WHERE minutos = " + time, null);

        //Comprobar si en la hora actual hay alguna alarma
        if (cursor.moveToFirst() && muestraNotificacion) {
            //Ejecutar alarma
            mBuilder.setContentText(cursor.getString(0));
            notification.notify(0, mBuilder.build());
        }

        cursor.close();
        muestraNotificacion = false;
        if (time != tempTime) {
            tempTime = time;
            muestraNotificacion = true;

        }
    }

    /**
     * Convierte la fecha del GregorianCalendar en un int que representa el total de minutos
     *
     * @param calendar Fecha para convertir en minutos
     * @return Total de minutos que han trasncurrido desde el inicio del año hasta la fecha
     * del GregorianCalendar
     */
    public static int getMinutes(GregorianCalendar calendar) {
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        //Total de minutos que trae un día completo
        int dayMin = 1440;
        int minutes = (day * dayMin);
        minutes += calendar.get(Calendar.MINUTE);
        minutes += (calendar.get(Calendar.HOUR_OF_DAY) * 60);
        return minutes;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "canal1";
            String description = "descripcion1";
            int importance = notification.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("001", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notification = getSystemService(NotificationManager.class);
            notification.createNotificationChannel(channel);
        }
    }

}