package com.example.notipushupiiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ExpandableListView expLV;
    private ExpLVAdapter adapter;
    private ArrayList<String> listCategorias;
    private Map<String, ArrayList<String >> mapChild;
    private static final String URL_TRABAJADOR = "http://sistemas.upiiz.ipn.mx/isc/nopu/api/empleado.php?numempleado=";
    private static final String URL_ALUMNO = "http://sistemas.upiiz.ipn.mx/isc/nopu/api/alumno.php?boleta=";
    private static final String URL_PRO = "http://sistemas.upiiz.ipn.mx/isc/nopu/api/alumno.php?boleta=";
    Spinner tipo;
    String tipo_usuar;
    EditText usuario;
    Button continuar;
    RequestQueue rq;
    String numBolEm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tipo = findViewById(R.id.spinnerTipo);
        usuario = findViewById(R.id.numero);
        continuar = findViewById(R.id.buttonC);
        cargarDatos();
        ArrayAdapter<CharSequence> adaptador = new ArrayAdapter(this,android.R.layout.simple_spinner_item,listCategorias);
        tipo.setAdapter(adaptador);
        tipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    tipo_usuar = listCategorias.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        rq = Volley.newRequestQueue(this);
        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saber();
            }
        });
    }
    private void cargarDatos(){
        listCategorias = new ArrayList<>();
        listCategorias.add("Seleccione Auto");
        listCategorias.add("Alumno");
        listCategorias.add("Docente");
        listCategorias.add("PAAE");
    }
    public void saber(){
        numBolEm = usuario.getText().toString();
        if(tipo_usuar.equals("Alumno")){
            //Toast.makeText(MainActivity.this,numBolEm,Toast.LENGTH_LONG).show();
            loginAlumno(numBolEm);
        }else if(tipo_usuar.equals("Docente") || tipo_usuar.equals("PAAE")){
            loginTrabajador(numBolEm);
        }else{
            Toast.makeText(MainActivity.this,"Selecciona el tipo de usuario",Toast.LENGTH_LONG).show();
        }
    }

    private void loginTrabajador(String numBolEm) {
        StringRequest sr = new StringRequest(Request.Method.GET, URL_TRABAJADOR+numBolEm,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject JSO = new JSONObject(response);
                            String estado = JSO.getString("estado");

                            //JSONArray ja = JSO.getJSONArray("login");
                            if(estado.equals("1")){
                                String nombre = JSO.getString("nombre");
                                Toast.makeText(MainActivity.this,"El empleado es: "+nombre,Toast.LENGTH_LONG).show();
                               /* JSONObject jsob1 = ja.getJSONObject(0);
                                String nombre = jsob1.getString("Nombre");
                                String usuario = jsob1.getString("Usuario");
                                String ID = jsob1.getString("ID");
                                Intent intencion = new Intent(getApplicationContext(), MenuActivity.class);
                                Bundle mib = new Bundle();
                                mib.putString("ID",ID);
                                mib.putString("Nombre",nombre);
                                mib.putString("Usuario",usuario);
                                intencion.putExtras(mib);
                                startActivity(intencion);
                                finish();*/
                            }else if(estado.equals("0")){
                                Toast.makeText(MainActivity.this,"No se encontró el usuario",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
        rq.add(sr);
    }

    private void loginAlumno(String boleta) {
        StringRequest sr = new StringRequest(Request.Method.GET, URL_ALUMNO+boleta,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject JSO = new JSONObject(response);
                            String estado = JSO.getString("estado");
                            String nombre = JSO.getString("nombre");
                            //JSONArray ja = JSO.getJSONArray("login");
                            if(estado.equals("1")){
                                String programa = JSO.getString("programa");
                                Toast.makeText(MainActivity.this,"El alumno es: "+nombre,Toast.LENGTH_LONG).show();
                                obtenerelprograma(programa);
                               /* JSONObject jsob1 = ja.getJSONObject(0);
                                String nombre = jsob1.getString("Nombre");
                                String usuario = jsob1.getString("Usuario");
                                String ID = jsob1.getString("ID");
                                Intent intencion = new Intent(getApplicationContext(), MenuActivity.class);
                                Bundle mib = new Bundle();
                                mib.putString("ID",ID);
                                mib.putString("Nombre",nombre);
                                mib.putString("Usuario",usuario);
                                intencion.putExtras(mib);
                                startActivity(intencion);
                                finish();*/
                            }else if(estado.equals("0")){
                                Toast.makeText(MainActivity.this,"No se encontró el usuario",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
        rq.add(sr);
    }

    private void obtenerelprograma(String programa) {
        StringRequest sr = new StringRequest(Request.Method.GET, URL_PRO+""+programa+"",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject JSO = new JSONObject(response);
                            String estatus = JSO.getString("estatus");
                            //String nombre = JSO.getString("nombre");
                            JSONArray ja = JSO.getJSONArray("programas");
                            if(estatus.equals("1")){
                               // String programa = JSO.getString("programa");
                                //Toast.makeText(MainActivity.this,"El alumno es: "+nombre,Toast.LENGTH_LONG).show();
                                //obtenerelprograma(programa);
                                JSONObject jsob1 = ja.getJSONObject(0);
                                String idPrograma = jsob1.getString("idPrograma");

                            }else if(estatus.equals("0")){
                                Toast.makeText(MainActivity.this,"No se encontró el programa",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
        rq.add(sr);
    }
}