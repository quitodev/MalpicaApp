package com.example.malpicasoft.usuario;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.malpicasoft.Dialogs;
import com.example.malpicasoft.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class UsuarioFragment extends Fragment {

    String fechaActual, datoDia, datoMes, datoAno, datoUsuario;
    Button buttonModificar, buttonGuardar;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    int counter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        dialogProcesando();

        requestQueue = Volley.newRequestQueue(getContext());

        obtenerFecha();
        obtenerVentas();
        obtenerCompras();
        obtenerGastos();
        obtenerDatos();

        View root = inflater.inflate(R.layout.fragment_usuario, container, false);
        return root;
    }

    // DIALOG CON PROGRESS BAR MIENTRAS REALIZA LAS CONSULTAS A LAS TABLAS
    public void dialogProcesando(){

        final Dialogs dialogs = new Dialogs(getActivity());
        dialogs.startProcesando();

        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                counter++;

                if(counter == 30) {
                    timer.cancel();
                    dialogs.endProcesando();
                    counter = 0;
                }
            }
        };
        timer.schedule(timerTask,0,100);
    }

    // OBTIENE EL MES ACTUAL PARA EFECTUAR LAS CONSULTAS EN LAS DISTINTAS TABLAS
    public void obtenerFecha() {

        Calendar calendar = Calendar.getInstance();
        fechaActual = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());

        String[] arrayFecha = fechaActual.split("/");
        datoDia = arrayFecha[0];
        datoMes = arrayFecha[1];
        datoAno = arrayFecha[2];

        if(datoMes.length() == 1){
            datoMes = "0" + datoMes;
        }
    }

    public void obtenerVentas(){

        // OBTIENE EL TOTAL DE VENTAS REGISTRADAS EN EL MES ACTUAL
        String URL = "http://malpicas.heliohost.org/malpica/usuario/inicio_consulta_ventas.php?mes=" + datoMes;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("totalventas");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER ESTABLECE EL TOTAL DE LAS VENTAS
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                UsuarioSetters usuarioSetters = new UsuarioSetters();
                                usuarioSetters.setTotalVentas(jsonObject.getString("total_ventas"));
                                String totalVentas = usuarioSetters.getTotalVentas();
                                String totalVentas1 = totalVentas;

                                if(totalVentas1.equals("null")){
                                    totalVentas1 = totalVentas1.replace("null","0");
                                }

                                // PARSEO DEL TOTAL AL FORMATO DOUBLE Y LO MUESTRA EN EL TEXT VIEW
                                double totalVentas2 = Double.parseDouble(totalVentas1);
                                DecimalFormat decimalFormat = new DecimalFormat("######0.00");
                                String totalVentas3 = "$ " + decimalFormat.format(totalVentas2).replace(".",",");
                                TextView textVentas = getActivity().findViewById(R.id.textVentas1);
                                textVentas.setText(totalVentas3);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Por favor, revise su conexión!", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void obtenerCompras(){

        // OBTIENE EL TOTAL DE COMPRAS REGISTRADAS EN EL MES ACTUAL
        String URL = "http://malpicas.heliohost.org/malpica/usuario/inicio_consulta_compras.php?mes=" + datoMes;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("totalcompras");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER ESTABLECE EL TOTAL DE LAS COMPRAS
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                UsuarioSetters usuarioSetters = new UsuarioSetters();
                                usuarioSetters.setTotalCompras(jsonObject.getString("total_compras"));
                                String totalCompras = usuarioSetters.getTotalCompras();
                                String totalCompras1 = totalCompras;

                                if(totalCompras1.equals("null")){
                                    totalCompras1 = totalCompras1.replace("null","0");
                                }

                                // PARSEO DEL TOTAL AL FORMATO DOUBLE Y LO MUESTRA EN EL TEXT VIEW
                                double totalCompras2 = Double.parseDouble(totalCompras1);
                                DecimalFormat decimalFormat = new DecimalFormat("######0.00");
                                String totalCompras3 = "$ " + decimalFormat.format(totalCompras2).replace(".",",");
                                TextView textCompras = getActivity().findViewById(R.id.textCompras1);
                                textCompras.setText(totalCompras3);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Por favor, revise su conexión!", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void obtenerGastos(){

        // OBTIENE EL TOTAL DE GASTOS REGISTRADAS EN EL MES ACTUAL
        String URL = "http://malpicas.heliohost.org/malpica/usuario/inicio_consulta_gastos.php?mes=" + datoMes;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("totalgastos");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER ESTABLECE EL TOTAL DE LOS GASTOS
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                UsuarioSetters usuarioSetters = new UsuarioSetters();
                                usuarioSetters.setTotalGastos(jsonObject.getString("total_gastos"));
                                String totalGastos = usuarioSetters.getTotalGastos();
                                String totalGastos1 = totalGastos;

                                if(totalGastos1.equals("null")){
                                    totalGastos1 = totalGastos1.replace("null","0");
                                }

                                // PARSEO DEL TOTAL AL FORMATO DOUBLE Y LO MUESTRA EN EL TEXT VIEW
                                double totalGastos2 = Double.parseDouble(totalGastos1);
                                DecimalFormat decimalFormat = new DecimalFormat("######0.00");
                                String totalGastos3 = "$ " + decimalFormat.format(totalGastos2).replace(".",",");
                                TextView textGastos = getActivity().findViewById(R.id.textGastos1);
                                textGastos.setText(totalGastos3);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Por favor, revise su conexión!", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void obtenerDatos(){

        // RECIBE EL USUARIO Y LO UTILIZA PARA CONSULTAR LOS DATOS DEL CLIENTE
        datoUsuario = getActivity().getIntent().getStringExtra("user");

        String URL = "http://malpicas.heliohost.org/malpica/usuario/inicio_consulta_datos.php?usuario=" + datoUsuario;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("datos");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER ESTABLECE EL TOTAL DE LOS GASTOS
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                UsuarioSetters usuarioSetters = new UsuarioSetters();
                                usuarioSetters.setLocal(jsonObject.getString("local"));
                                usuarioSetters.setDireccion(jsonObject.getString("direccion"));
                                usuarioSetters.setLocalidad(jsonObject.getString("localidad"));
                                usuarioSetters.setCuit(jsonObject.getString("cuit"));
                                usuarioSetters.setCondicion(jsonObject.getString("condicion"));
                                usuarioSetters.setEmail(jsonObject.getString("email"));
                                usuarioSetters.setTelefono(jsonObject.getString("telefono"));
                                usuarioSetters.setEstado(jsonObject.getString("estado"));
                                usuarioSetters.setFechaAlta(jsonObject.getString("fecha_alta"));

                                String local = usuarioSetters.getLocal();
                                String direccion = usuarioSetters.getDireccion();
                                String localidad = usuarioSetters.getLocalidad();
                                String cuit = usuarioSetters.getCuit();
                                String condicion = usuarioSetters.getCondicion();
                                String email = usuarioSetters.getEmail();
                                String telefono = usuarioSetters.getTelefono();
                                String estado = usuarioSetters.getEstado();
                                String fechaAlta = usuarioSetters.getFechaAlta() + "20";

                                final EditText editLocal = getActivity().findViewById(R.id.editLocal);
                                final EditText editDireccion = getActivity().findViewById(R.id.editDireccion);
                                final EditText editLocalidad = getActivity().findViewById(R.id.editLocalidad);
                                final EditText editCuit = getActivity().findViewById(R.id.editCuit);
                                final EditText editCondicion = getActivity().findViewById(R.id.editCondicion);
                                final EditText editEmail = getActivity().findViewById(R.id.editEmail);
                                final EditText editTelefono = getActivity().findViewById(R.id.editTelefono);
                                TextView textEstado = getActivity().findViewById(R.id.textEstado1);
                                TextView textFechaAlta = getActivity().findViewById(R.id.textFechaAlta1);

                                editLocal.setText(local);
                                editDireccion.setText(direccion);
                                editLocalidad.setText(localidad);
                                editCuit.setText(cuit);
                                editCondicion.setText(condicion);
                                editEmail.setText(email);
                                editTelefono.setText(telefono);
                                textEstado.setText(estado);
                                textFechaAlta.setText(fechaAlta);

                                // EVENTOS DE LOS BOTONES MODIFICAR Y GUARDAR
                                buttonModificar = getActivity().findViewById(R.id.buttonModificar);
                                buttonModificar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        editLocal.setFocusableInTouchMode(true);
                                        editDireccion.setFocusableInTouchMode(true);
                                        editLocalidad.setFocusableInTouchMode(true);
                                        editCuit.setFocusableInTouchMode(true);
                                        editCondicion.setFocusableInTouchMode(true);
                                        editEmail.setFocusableInTouchMode(true);
                                        editTelefono.setFocusableInTouchMode(true);
                                        editLocal.requestFocus();

                                        buttonModificar.setVisibility(View.INVISIBLE);
                                    }
                                });

                                buttonGuardar = getActivity().findViewById(R.id.buttonGuardar);
                                buttonGuardar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        modificarDatos();

                                        editLocal.setFocusableInTouchMode(false);
                                        editDireccion.setFocusableInTouchMode(false);
                                        editLocalidad.setFocusableInTouchMode(false);
                                        editCuit.setFocusableInTouchMode(false);
                                        editCondicion.setFocusableInTouchMode(false);
                                        editEmail.setFocusableInTouchMode(false);
                                        editTelefono.setFocusableInTouchMode(false);

                                        editLocal.setFocusable(false);
                                        editDireccion.setFocusable(false);
                                        editLocalidad.setFocusable(false);
                                        editCuit.setFocusable(false);
                                        editCondicion.setFocusable(false);
                                        editEmail.setFocusable(false);
                                        editTelefono.setFocusable(false);

                                        buttonModificar.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Por favor, revise su conexión!", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void modificarDatos(){

        // SE HABILITAN LOS EDIT TEXT PARA MODIFICAR Y ALMACENAR LOS DATOS DEL CLIENTE EN LA BASE DE MYSQL
        final EditText editLocal = getActivity().findViewById(R.id.editLocal);
        final EditText editDireccion = getActivity().findViewById(R.id.editDireccion);
        final EditText editLocalidad = getActivity().findViewById(R.id.editLocalidad);
        final EditText editCuit = getActivity().findViewById(R.id.editCuit);
        final EditText editCondicion = getActivity().findViewById(R.id.editCondicion);
        final EditText editEmail = getActivity().findViewById(R.id.editEmail);
        final EditText editTelefono = getActivity().findViewById(R.id.editTelefono);

        String URL = "http://malpicas.heliohost.org/malpica/usuario/inicio_update_datos.php?usuario=" + datoUsuario;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), "Modificación exitosa!", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> datosUsuario = new HashMap<>();
                datosUsuario.put("local",editLocal.getText().toString());
                datosUsuario.put("direccion",editDireccion.getText().toString());
                datosUsuario.put("localidad",editLocalidad.getText().toString());
                datosUsuario.put("cuit",editCuit.getText().toString());
                datosUsuario.put("condicion",editCondicion.getText().toString());
                datosUsuario.put("email",editEmail.getText().toString());
                datosUsuario.put("telefono",editTelefono.getText().toString());
                return datosUsuario;
            }
        };
        requestQueue.add(stringRequest);
    }
}