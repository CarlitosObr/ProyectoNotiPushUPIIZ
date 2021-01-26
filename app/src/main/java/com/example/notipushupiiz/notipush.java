package com.example.notipushupiiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link notipush#newInstance} factory method to
 * create an instance of this fragment.
 */
public class notipush extends Fragment implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

   // private static final String URL_saberID = "http://10.0.0.6/RepoAppsMoviles/Proyecto/NOTIPUSH_API/v1/usuarios.php?boleta=";
   // private static final String URL_mostrar = "http://10.0.0.6/RepoAppsMoviles/Proyecto/NOTIPUSH_API/v1//notificaciones.php?idUsuario=";
    private static final String URL_saberID = "http://192.168.1.72/WORK/Repositorios/ProyectoAppsBEIFI/NOTIPUSH_API/v1/usuarios.php?boleta=";
    private static final String URL_mostrar = "http://192.168.1.72/WORK/Repositorios/ProyectoAppsBEIFI/NOTIPUSH_API/v1/notificaciones.php?idUsuario=";
    View v;
    RequestQueue rq;
    ListView lista;
    ArrayList<notificacion> notificaciones= new ArrayList<>();
    String boleta;
    Adaptador ad;

    public notipush() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment notipush.
     */
    // TODO: Rename and change types and number of parameters
    public static notipush newInstance(String param1, String param2) {
        notipush fragment = new notipush();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_notipush, container, false);

        //nombre = v.findViewById(R.id.Nombre_barra);
       // usuario = v.findViewById(R.id.usuario_barra);
        rq = Volley.newRequestQueue(v.getContext());
        lista=v.findViewById(R.id.listita);
        SharedPreferences shp = getActivity().getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        boleta = shp.getString("boleta","no hay");
        /*Bundle mib = getActivity().getIntent().getExtras();
        if(mib!=null){
            boleta = mib.getString("Boleta");
        }*/
        obtenerID();
        //Toast.makeText(getActivity().getApplicationContext(),notificaciones.size(),Toast.LENGTH_LONG).show();
        ad = new Adaptador(getActivity().getApplicationContext(),notificaciones,R.layout.diseno_lista);
        lista.setAdapter(ad);
        lista.setOnItemClickListener(this);
        lista.setOnItemLongClickListener(this);
        return v;
    }
    public void obtenerID(){

        JsonObjectRequest jsr = new JsonObjectRequest(Request.Method.GET, URL_saberID+boleta, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String respuesta = response.getString("estatus");
                            if(respuesta.equals("1")) {
                                JSONArray ja = response.getJSONArray("usuarios");
                                JSONObject jsonOb = ja.getJSONObject(0);
                                String id = jsonOb.getString("idUsuario");
                                //Toast.makeText(getActivity().getApplicationContext(),id,Toast.LENGTH_LONG).show();
                                listar(id);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
        );
        rq.add(jsr);
    }
    public void listar(final String id){

        JsonObjectRequest jsr = new JsonObjectRequest(Request.Method.GET, URL_mostrar+id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String respuesta = response.getString("estatus");
                            //Toast.makeText(getActivity().getApplicationContext(),respuesta,Toast.LENGTH_LONG).show();
                            if(respuesta.equals("1")) {
                                JSONArray ja = response.getJSONArray("notificaciones");
                                JSONObject jsonObIn = ja.getJSONObject(0);
                                String idP = jsonObIn.getString("idNotificacion");
                                String usuario = jsonObIn.getString("titulo");
                                String clave = jsonObIn.getString("descripcion");
                                //Toast.makeText(getActivity().getApplicationContext(),usuario,Toast.LENGTH_LONG).show();
                                notificaciones.add(new notificacion(idP, usuario, clave));
                                ad.notifyDataSetChanged();
                                for (int i = 1; i < ja.length(); i++) {
                                    JSONObject jsonOb = ja.getJSONObject(i);
                                    String id = jsonOb.getString("idNotificacion");
                                    if(idP.equals(id)){

                                    }else{

                                         usuario = jsonOb.getString("titulo");
                                         clave = jsonOb.getString("descripcion");

                                        notificaciones.add(new notificacion(id, usuario, clave));
                                        ad.notifyDataSetChanged();
                                        idP = jsonOb.getString("idNotificacion");
                                    }
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
        );
        rq.add(jsr);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle mib = new Bundle();
        mib.putString("titulo",notificaciones.get(position).getTitulo());
        mib.putString("descripcion",notificaciones.get(position).getDescripcion());
        Intent intencion = new Intent(getActivity().getApplicationContext(),Detalle.class);
        intencion.putExtras(mib);
        getActivity().startActivityFromFragment(this,intencion,0);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }
}