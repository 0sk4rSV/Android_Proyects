package com.example.actionbar_navigationdrawer_osv;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Informacion extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Información del desarrollador");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void abrirLinkedin(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.imagen_click));
        Uri uri = Uri.parse("https://www.linkedin.com/in/%C3%B3scar-su%C3%A1rez-vargas-b1ab3a175/");
        Intent i = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(i);
    }

}