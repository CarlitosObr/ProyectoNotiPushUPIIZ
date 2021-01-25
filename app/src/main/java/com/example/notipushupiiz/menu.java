package com.example.notipushupiiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class menu extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;
    private static final String URL = "https://image.flaticon.com/icons/png/512/16/16363.png";
    Bundle b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        b = getIntent().getExtras();
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,R.id.notipush, R.id.salir2)
                .setDrawerLayout(drawer)
                .build();
        //navigationView.setNavigationItemSelectedListener(this);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        actualizaNavHeader();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    public void actualizaNavHeader(){

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView nom_bar = headerView.findViewById(R.id.nombre_barra);
        TextView usu_bar = headerView.findViewById(R.id.usuario_barra);
        ImageView im_barra = headerView.findViewById(R.id.image_barra);
        SharedPreferences shp = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String nombre = shp.getString("tipo","no hay");
        String usuario = shp.getString("nombre","no hay");
        //nom_bar.setText(b.getString("Nombre"));
       // usu_bar.setText(b.getString("Usuario"));
         nom_bar.setText(nombre);
         usu_bar.setText(usuario);
        Picasso.get()
                .load(URL)
                .resize(64,64)
                .centerCrop()
                .into(im_barra);
    }

}