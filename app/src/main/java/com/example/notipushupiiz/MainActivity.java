package com.example.notipushupiiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ExpandableListView expLV;
    private ExpLVAdapter adapter;
    private ArrayList<String> listCategorias;
    private Map<String, ArrayList<String >> mapChild;
    Button b;
    Button c;
    Button d;
    EditText sobres;
    EditText prueba;
    EditText et;
    int variablefer;
    int variabledelchileros;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        expLV = findViewById(R.id.expLV);
        listCategorias = new ArrayList<>();
        mapChild = new HashMap<>();

        cargarDatos();

    }
    private void cargarDatos(){
        ArrayList<String> listSeleccionar = new ArrayList<>();

        listCategorias.add("Seleccionar");

        listSeleccionar.add("opcion1");
        listSeleccionar.add("opcion2");
        listSeleccionar.add("opcion3");
        listSeleccionar.add("opcion4");

        mapChild.put(listCategorias.get(0), listSeleccionar);

        adapter = new ExpLVAdapter(this, listCategorias, mapChild);
        expLV.setAdapter(adapter);


    }
}