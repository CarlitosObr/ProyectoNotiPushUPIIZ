package com.example.notipushupiiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Detalle extends AppCompatActivity {
    TextView titu;
    TextView desc;
    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
        titu = findViewById(R.id.tituloTV);
        desc = findViewById(R.id.descripTV);
        back = findViewById(R.id.regresaya);
        Bundle mib = getIntent().getExtras();
        String titulin = mib.getString("titulo");
        String descri = mib.getString("descripcion");
        titu.setText(titulin);
        desc.setText(descri);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intencion = new Intent(getApplicationContext(), menu.class);

             //   intencion.putExtras(mib);
                finish();
            }
        });
    }
}