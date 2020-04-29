package com.example.malpicasoft.clientes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.malpicasoft.AdminSQLiteOpenHelper;
import com.example.malpicasoft.R;
import com.example.malpicasoft.SlidePagerAdapter;
import com.example.malpicasoft.compras.Compras;
import com.example.malpicasoft.gastos.Gastos;
import com.example.malpicasoft.proveedores.Proveedores;
import com.example.malpicasoft.stock.Stock;
import com.example.malpicasoft.usuario.Usuario;
import com.example.malpicasoft.ventas.Ventas;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class Clientes extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    String user, hour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        // TOOLBAR, NAVIGATION VIEW Y DRAWER TOGGLE
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerUsuario);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        userDate();
        slideFragments();
    }

    // CONSULTA HORA A TRAVÉS DEL USUARIO RECIBIDO PARA MOSTRAR EN HEADER
    public void userDate(){

        String usuarioActual = getIntent().getStringExtra("user");

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "admin", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT user, hour FROM base WHERE user ='"+usuarioActual+"'", null);

        if(cursor.moveToFirst()){

            user = cursor.getString(0);
            hour = cursor.getString(1);

            String usuario = "Hola " + user + "!";
            String hora = "Última conexión: Hoy, " + hour + ".";

            View header = navigationView.getHeaderView(0);
            TextView textUser = header.findViewById(R.id.textUser);
            TextView textHour = header.findViewById(R.id.textHour);
            textUser.setText(usuario);
            textHour.setText(hora);

            db.close();

        } else {

            db.close();
        }
    }

    // VIEW PAGER
    public void slideFragments(){

        List<Fragment> list = new ArrayList<>();

        list.add(new IngresarClientes());
        list.add(new ConsultarClientes());
        list.add(new ModificarClientes());
        list.add(new EliminarClientes());

        viewPager = findViewById(R.id.view_pager);
        pagerAdapter = new SlidePagerAdapter(getSupportFragmentManager(),list);
        viewPager.setAdapter(pagerAdapter);
    }

    // EVENTOS DEL MENÚ
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.itemInicio) {

            Intent intent = new Intent(Clientes.this, Usuario.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();

        } else if(item.getItemId() == R.id.itemStock) {

            Intent intent = new Intent(Clientes.this, Stock.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();

        } else if(item.getItemId() == R.id.itemCompras) {

            Intent intent = new Intent(Clientes.this, Compras.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();

        } else if(item.getItemId() == R.id.itemVentas) {

            Intent intent = new Intent(Clientes.this, Ventas.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();

        } else if(item.getItemId() == R.id.itemGastos) {

            Intent intent = new Intent(Clientes.this, Gastos.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();

        } else if(item.getItemId() == R.id.itemClientes) {

            drawerLayout.closeDrawer(GravityCompat.START);

        } else if(item.getItemId() == R.id.itemProveedores) {

            Intent intent = new Intent(Clientes.this, Proveedores.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();

        } else if(item.getItemId() == R.id.itemSalir) {

            finish();
        }

        return false;
    }
}