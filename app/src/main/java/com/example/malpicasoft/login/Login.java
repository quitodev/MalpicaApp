package com.example.malpicasoft.login;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.malpicasoft.AdminSQLiteOpenHelper;
import com.example.malpicasoft.Dialogs;
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // LIBRERÍA VOLLEY PARA CONSULTAS A BASE DE DATOS
        requestQueue = Volley.newRequestQueue(this);

        // COMPONENTES DE LA VISTA
        final Button buttonLogin = findViewById(R.id.buttonLogin);
        final TextView textHelp = findViewById(R.id.textHelp);
        editUser = findViewById(R.id.editUser);
        editPass = findViewById(R.id.editPass);

        editUser.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {

                    // SI INGRESA AL CAMPO, CAMBIA COLOR DE ÍCONO Y TEXTO
                    editUser.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                    editUser.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_user_orange, 0, 0, 0);

                    if (editUser.getText().toString().equals("Usuario")) {

                        // SI NO SE MODIFICÓ EL TEXTO, LIMPIA EL CAMPO
                        editUser.setText("");
                    }

                } else {

                    // SI SALE DEL CAMPO, RECUPERA COLOR DE ÍCONO Y TEXTO
                    editUser.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorTextHint));
                    editUser.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_user_grey, 0, 0, 0);

                    if (editUser.getText().toString().isEmpty()) {

                        // SI EL CAMPO QUEDÓ VACÍO, LO VUELVE A COMPLETAR
                        editUser.setText("Usuario");
                    }
                }
            }
        });

        editPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {

                    // SI INGRESA AL CAMPO, CAMBIA COLOR DE ÍCONO Y TEXTO
                    editPass.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                    editPass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pass_orange, 0, 0, 0);

                    if (editPass.getText().toString().equals("Contraseña")) {

                        // SI NO SE MODIFICÓ EL TEXTO, LIMPIA EL CAMPO
                        editPass.setText("");
                    }

                } else {

                    // SI SALE DEL CAMPO, RECUPERA COLOR DE ÍCONO Y TEXTO
                    editPass.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorTextHint));
                    editPass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pass_grey, 0, 0, 0);

                    if (editPass.getText().toString().isEmpty()) {

                        // SI EL CAMPO QUEDÓ VACÍO, LO VUELVE A COMPLETAR
                        editPass.setText("Contraseña");
                    }
                }
            }
        });

        // EVENTOS DEL TEXT DE AYUDA
        textHelp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        textHelp.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorTextSelected));
                        mensajeAyuda();
                        break;

                    case MotionEvent.ACTION_UP:
                        textHelp.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                        break;
                }
                return true;
            }
        });

        // EVENTOS DEL BOTÓN LOGIN
        buttonLogin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        buttonLogin.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorTextSelected));
                        dialogProcesando();
                        iniciarSesion();
                        break;

                    case MotionEvent.ACTION_UP:
                        buttonLogin.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                        break;
                }
                return true;
            }
        });
    }

    private void dialogProcesando(){

        // DIALOG CON PROGRESS BAR MIENTRAS CONSULTA A LAS TABLAS
        Dialogs dialogs = new Dialogs(this);
        int layout = R.layout.dialog_procesando;
        dialogs.startResultado(layout);
    }

    private void mensajeError(){

        // TOAST CON MENSAJE DE ERROR
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),"Usuario y/o clave incorrecta!", Toast.LENGTH_SHORT).show();
            }
        }, 2000);
    }


    private void mensajeAyuda() {

        // TOAST DE AYUDA PARA INICIAR SESIÓN
        Toast.makeText(this,"Por favor, envíenos un correo electrónico a malpica-soft@outlook.com y le " +
                "responderemos a la brevedad!", Toast.LENGTH_LONG).show();
    }

    private void iniciarSesion(){

        // OBTIENE LA HORA ACTUAL Y ACTUALIZA HORA DE CONEXIÓN PARA ALMACENAR EN BASE SQLITE
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
        String URL = "http://malpicas.heliohost.org/malpica/login/login_consultar.php?usuario="+editUser.getText().toString()
                +"&clave="+editPass.getText().toString();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

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

                                    // SI NO COINCIDE EL USUARIO Y LA CLAVE INGRESADA, MUESTRA UN MENSAJE DE ERROR
                                    mensajeError();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Por favor, revise su conexión!", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}