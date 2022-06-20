package com.example.malpicasoft.gastos;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.malpicasoft.AdminSQLiteOpenHelper;
import com.example.malpicasoft.R;
import com.example.malpicasoft.SlidePagerAdapter;
import com.example.malpicasoft.clientes.Clientes;
import com.example.malpicasoft.compras.Compras;
import com.example.malpicasoft.login.Login;
import com.example.malpicasoft.proveedores.Proveedores;
import com.example.malpicasoft.stock.Stock;
import com.example.malpicasoft.usuario.Usuario;
import com.example.malpicasoft.ventas.Ventas;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class Gastos extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gastos);

        // TOOLBAR, NAVIGATION VIEW Y DRAWER TOGGLE
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerUsuario);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        userDate();
        slideFragments();
    }

    private void userDate(){

        // CONSULTA HORA EN BASE SQLITE A TRAVÉS DEL USUARIO RECIBIDO PARA MOSTRARLA EN EL HEADER
        String usuarioActual = getIntent().getStringExtra("user");

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "admin", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT user, hour FROM base WHERE user ='"+usuarioActual+"'", null);

        if(cursor.moveToFirst()){

            // BUSCA AL USUARIO PARA ESTABLECER LA ÚLTIMA CONEXIÓN
            user = cursor.getString(0);
            String hour = cursor.getString(1);

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

    private void slideFragments(){

        // VIEW PAGER CON LOS FRAGMENTS
        List<Fragment> list = new ArrayList<>();

        list.add(new IngresarGastos());
        list.add(new ConsultarGastos());
        list.add(new ModificarGastos());
        list.add(new EliminarGastos());

        ViewPager viewPager = findViewById(R.id.view_pager);
        PagerAdapter pagerAdapter = new SlidePagerAdapter(getSupportFragmentManager(), list);
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        // EVENTOS DEL MENÚ
        if(item.getItemId() == R.id.itemInicio) {

            Intent intent = new Intent(Gastos.this, Usuario.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();

        } else if(item.getItemId() == R.id.itemStock) {

            Intent intent = new Intent(Gastos.this, Stock.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();

        } else if(item.getItemId() == R.id.itemCompras) {

            Intent intent = new Intent(Gastos.this, Compras.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();

        } else if(item.getItemId() == R.id.itemVentas) {

            Intent intent = new Intent(Gastos.this, Ventas.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();

        } else if(item.getItemId() == R.id.itemGastos) {

            drawerLayout.closeDrawer(GravityCompat.START);

        } else if(item.getItemId() == R.id.itemClientes) {

            Intent intent = new Intent(Gastos.this, Clientes.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();

        } else if(item.getItemId() == R.id.itemProveedores) {

            Intent intent = new Intent(Gastos.this, Proveedores.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();

        } else if(item.getItemId() == R.id.itemSalir) {

            Intent intent = new Intent(Gastos.this, Login.class);
            startActivity(intent);
            finish();
        }

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // INFLA MENÚ DE TOOLBAR
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // TOAST CON MENSAJE DE AYUDA
        if(item.getItemId() == R.id.itemAyuda) {
            Toast.makeText(this,"Por favor, envíenos un correo electrónico a malpica-soft@outlook.com y le " +
                    "responderemos a la brevedad!", Toast.LENGTH_LONG).show();
        }
        return false;
    }
}
