package com.example.malpicasoft.login;

import android.content.ContentValues;
import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.malpicasoft.AdminSQLiteOpenHelper;
import com.example.malpicasoft.usuario.Usuario;
import com.example.malpicasoft.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class Login extends AppCompatActivity {

    private RequestQueue requestQueue;
    private EditText editUser, editPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // LIBRERÍA VOLLEY PARA CONSULTAS A BASE DE DATOS
        requestQueue = Volley.newRequestQueue(this);

        // COMPONENTES DE LA VISTA
        Button buttonLogin = findViewById(R.id.buttonLogin);
        TextView textHelp = findViewById(R.id.textHelp);
        editUser = findViewById(R.id.editUser);
        editPass = findViewById(R.id.editPass);

        // EVENTOS DEL BOTÓN LOGIN
        textHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mensajeAyuda();
            }
        });

        // EVENTOS DEL BOTÓN LOGIN
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesion();
            }
        });
    }

    private void mensajeAyuda() {

        // TOAST DE AYUDA PARA INICIAR SESIÓN
        Toast.makeText(this,"Por favor, envíenos un correo electrónico a malpica-soft@outlook.com y le " +
                "responderemos a la brevedad!", Toast.LENGTH_LONG).show();
    }

    private void iniciarSesion(){

        // CONSULTA Y ACTUALIZA HORA DE CONEXIÓN PARA ALMACENAR EN BASE SQLITE
        Date date = Calendar.getInstance().getTime();
        String hour = date.toString();
        String horaActual = "" + hour.charAt(11) + hour.charAt(12) + hour.charAt(13) + hour.charAt(14) + hour.charAt(15);
        String usuarioActual = editUser.getText().toString();

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "admin", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT user, hour FROM base WHERE user ='"+usuarioActual+"'", null);

        if(cursor.moveToFirst()){

            // SI ENCUENTRA AL USUARIO INGRESADO, ACTUALIZA LA HORA DE CONEXIÓN
            ContentValues contentValues = new ContentValues();
            contentValues.put("user", usuarioActual);
            contentValues.put("hour", horaActual);
            db.update("base", contentValues, "user='"+usuarioActual+"'",null);
            db.close();

        } else {

            // SI NO ENCUENTRA AL USUARIO INGRESADO, LO AGREGA CON LA HORA DE CONEXIÓN
            ContentValues contentValues = new ContentValues();
            contentValues.put("user", usuarioActual);
            contentValues.put("hour", horaActual);
            db.insert("base", null, contentValues);
            db.close();
        }

        // CONSULTA EL USUARIO Y LA CLAVE OBTENIDA EN LA BASE DE DATOS
        String URL = "http://malpicas.heliohost.org/malpica/login/login_consulta.php?usuario="+editUser.getText().toString()
                +"&clave="+editPass.getText().toString();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("datos");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                LoginSetters loginSetters = new LoginSetters();

                                loginSetters.setUser(jsonObject.optString("usuario"));
                                loginSetters.setPass(jsonObject.optString("clave"));

                                if(loginSetters.getUser().contentEquals(editUser.getText()) &&
                                        loginSetters.getPass().contentEquals(editPass.getText())) {

                                    // SI COINCIDE EL USUARIO Y LA CLAVE INGRESADA, PASA A LA SIGUIENTE ACTIVITY
                                    Intent intent = new Intent(Login.this, Usuario.class);
                                    intent.putExtra("user", editUser.getText().toString());
                                    startActivity(intent);
                                    finish();

                                } else {

                                    // SI NO COINCIDE EL USUARIO Y LA CLAVE INGRESADA, MUESTRA UN TOAST DE ADVERTENCIA
                                    Toast.makeText(getApplicationContext(),"Por favor, revise los datos ingresados!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Por favor, revise su conexión!", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}