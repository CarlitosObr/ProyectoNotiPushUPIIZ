package com.example.notipushupiiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaSync;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    private ExpandableListView expLV;
    private ExpLVAdapter adapter;
    private ArrayList<String> listCategorias;
    private Map<String, ArrayList<String >> mapChild;
    private static final String URL_TRABAJADOR = "http://sistemas.upiiz.ipn.mx/isc/nopu/api/empleado.php?numempleado=";
    private static final String URL_ALUMNO = "http://sistemas.upiiz.ipn.mx/isc/nopu/api/alumno.php?boleta=";
  private static final String URL_PRO = "http://10.0.0.6/RepoAppsMoviles/Proyecto/NOTIPUSH_API/v1/usuarios.php";
   private static final String URL_i = "http://10.0.0.6/RepoAppsMoviles/Proyecto/NOTIPUSH_API/v1/nopu.png";
  //  private static final String URL_PRO = "http://192.168.1.72/WORK/Repositorios/ProyectoAppsBEIFI/NOTIPUSH_API/v1/usuarios.php";
    //private static final String URL_i = "http://192.168.1.72/WORK/Repositorios/ProyectoAppsBEIFI/NOTIPUSH_API/v1/nopu.png";
    Spinner tipo;
    String tipo_usuar;
    EditText usuario;
    Button continuar;
    RequestQueue rq;
    RequestQueue rq2;
    String numBolEm;
    ImageView nueva;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tipo = findViewById(R.id.spinnerTipo);
        usuario = findViewById(R.id.numero);
        continuar = findViewById(R.id.buttonC);
        nueva = findViewById(R.id.imageViewN);
        Picasso.get()
                .load(URL_i)
                .resize(400,480)
                .into(nueva);
        cargarDatos();
        ArrayAdapter<CharSequence> adaptador = new ArrayAdapter(this,android.R.layout.simple_spinner_item,listCategorias);
        tipo.setAdapter(adaptador);
        checkLogin();
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
                FirebaseMessaging.getInstance().subscribeToTopic("topic_general")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                String msg ="suscrito";
                                if (!task.isSuccessful()) {
                                    msg = "suscrito";
                                }
                                Log.d(TAG, msg);
                                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });

                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                    return;
                                }

                                // Get new FCM registration token
                                 token = task.getResult();

                                // Log and toast
                                String msg = token;
                                Log.d(TAG, msg);
                                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
    }

    private void checkLogin() {
        SharedPreferences shp = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String logueado = shp.getString("logueado","no");

        if(logueado.equals("si")){
            Intent intencion = new Intent(getApplicationContext(), menu.class);
            //intencion.putExtras(mib);
            startActivity(intencion);
            finish();
        }
    }

    private void cargarDatos(){
        listCategorias = new ArrayList<>();
        listCategorias.add("Seleccione");
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

    private void loginTrabajador(String numEm) {
        StringRequest sr = new StringRequest(Request.Method.GET, URL_TRABAJADOR+numEm,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject JSO = new JSONObject(response);
                            String estado = JSO.getString("estado");
                            String programa = JSO.getString("programa");
                            //JSONArray ja = JSO.getJSONArray("login");
                            if(estado.equals("1")){
                                String nombre = JSO.getString("nombre");
                                //Toast.makeText(MainActivity.this,"El empleado es: "+nombre,Toast.LENGTH_LONG).show();
                                obtenerelprograma(programa,nombre,numBolEm,tipo_usuar);
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
                                //Toast.makeText(MainActivity.this,"El alumno es: "+nombre,Toast.LENGTH_LONG).show();
                                obtenerelprograma(programa,nombre,numBolEm,tipo_usuar);
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

    private void obtenerelprograma(final String programa, final String nombre, final String numbol, final String tipo) {
       // rq2 = Volley.newRequestQueue(this);
        //Toast.makeText(MainActivity.this,URL_PRO,Toast.LENGTH_LONG).show();

        StringRequest sr2 = new StringRequest(Request.Method.GET, URL_PRO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject JSO = new JSONObject(response);
                            String estatus = JSO.getString("estatus");
                            //String nombre = JSO.getString("nombre");
                            JSONArray ja = JSO.getJSONArray("programas");
                           // Toast.makeText(MainActivity.this,"AQUÍ ANDO",Toast.LENGTH_LONG).show();
                            if(estatus.equals("1")){
                               // String programa = JSO.getString("programa");
                                //Toast.makeText(MainActivity.this,"PROGRAMA: "+programa,Toast.LENGTH_LONG).show();
                                //obtenerelprograma(programa);
                                String idPrograma = "";
                                for(int i = 0; i<ja.length(); i++){
                                    JSONObject jsob1 = ja.getJSONObject(i);
                                    if(jsob1.getString("Nombre").equals(programa)){
                                        idPrograma = jsob1.getString("idPrograma");
                                    }
                                }
                                //Toast.makeText(MainActivity.this,"IDPROGRAMA: "+idPrograma,Toast.LENGTH_LONG).show();
                                registrarUsuario(nombre,numbol,tipo,idPrograma);

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
        rq.add(sr2);

    }

    public void registrarUsuario(final String nombre, final String numbol, final String tipo, String idPrograma){
            //String url = "http://192.168.1.72/NOTIPUSH_API/v1/usuarios.php";
            HashMap<String, String> parametros = new HashMap();
            parametros.put("idUsuario","");
            parametros.put("nombreCompleto",nombre);
            parametros.put("boleta",numbol);
            parametros.put("token",token);
            parametros.put("tipo",tipo);
            parametros.put("Programa_idPrograma",idPrograma);
       // Toast.makeText(MainActivity.this,"HOLAAA",Toast.LENGTH_LONG).show();
            JsonObjectRequest rqn = new JsonObjectRequest(Request.Method.POST, URL_PRO, new JSONObject(parametros),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                String respuesta = response.getString("estatus");
                                if(respuesta.equals("1")) {
                                    //JSONArray ja = response.getJSONArray("empleados");
                                    Intent intencion = new Intent(getApplicationContext(), menu.class);
                                    Bundle mib = new Bundle();
                                    //mib.putString("ID",ID);
                                    SharedPreferences preferences = getSharedPreferences("credenciales",Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("logueado","si");
                                    editor.putString("boleta",numbol);
                                    editor.putString("nombre",nombre);
                                    editor.putString("tipo",tipo);
                                    editor.commit();
                                    mib.putString("Nombre",tipo);
                                    mib.putString("Usuario",nombre);
                                    mib.putString("Boleta",numbol);
                                    intencion.putExtras(mib);
                                    startActivity(intencion);
                                    finish();
                                }else if(respuesta.equals("0")){
                                    Toast.makeText(MainActivity.this,"Ocurrió un error",Toast.LENGTH_LONG).show();
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
                    }
            );
        rq.add(rqn);
    }
}