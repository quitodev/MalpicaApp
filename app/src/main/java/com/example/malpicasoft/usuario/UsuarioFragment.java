package com.example.malpicasoft.usuario;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class UsuarioFragment extends Fragment {

    private String datoHoraActual, datoFechaActual, datoDia, datoMes, datoAno, datoUsuario;
    private RequestQueue requestQueue;
    private JsonObjectRequest jsonObjectRequest;
    private int counter;
    private double totalVentas, totalCompras, totalGastos, totalBalance;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_usuario, container, false);

        // LIBRERÍA VOLLEY PARA CONSULTAS A BASES DE DATOS
        requestQueue = Volley.newRequestQueue(getContext());

        dialogProcesando();
        dateFragments();
        consultarUsuario();
        actualizarIngreso();

        // COMPONENTES DE LA VISTA
        final EditText editComercio = root.findViewById(R.id.editComercio);
        final EditText editDireccion = root.findViewById(R.id.editDireccion);
        final EditText editLocalidad = root.findViewById(R.id.editLocalidad);
        final EditText editCuit = root.findViewById(R.id.editCuit);
        final EditText editCondicion = root.findViewById(R.id.editCondicion);
        final EditText editEmail = root.findViewById(R.id.editEmail);
        final EditText editTelefono = root.findViewById(R.id.editTelefono);

        final Button buttonModificar = root.findViewById(R.id.buttonModificar);
        final Button buttonGuardar = root.findViewById(R.id.buttonGuardar);

        // EVENTOS DEL BOTÓN MODIFICAR
        buttonModificar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        buttonModificar.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSelected));
                        buttonModificar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_yellow, 0, 0, 0);

                        editComercio.setFocusableInTouchMode(true);
                        editDireccion.setFocusableInTouchMode(true);
                        editLocalidad.setFocusableInTouchMode(true);
                        editCuit.setFocusableInTouchMode(true);
                        editCondicion.setFocusableInTouchMode(true);
                        editEmail.setFocusableInTouchMode(true);
                        editTelefono.setFocusableInTouchMode(true);

                        editComercio.requestFocusFromTouch();

                        buttonModificar.setVisibility(View.INVISIBLE);
                        break;

                    case MotionEvent.ACTION_UP:
                        buttonModificar.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                        buttonModificar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_orange, 0, 0, 0);
                        break;
                }
                return true;
            }
        });

        // EVENTOS DEL BOTÓN GUARDAR
        buttonGuardar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        buttonGuardar.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSelected));
                        buttonGuardar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_yellow, 0, 0, 0);

                        dialogProcesando();
                        modificarDatos();

                        editComercio.setFocusableInTouchMode(false);
                        editDireccion.setFocusableInTouchMode(false);
                        editLocalidad.setFocusableInTouchMode(false);
                        editCuit.setFocusableInTouchMode(false);
                        editCondicion.setFocusableInTouchMode(false);
                        editEmail.setFocusableInTouchMode(false);
                        editTelefono.setFocusableInTouchMode(false);

                        editComercio.setFocusable(false);
                        editDireccion.setFocusable(false);
                        editLocalidad.setFocusable(false);
                        editCuit.setFocusable(false);
                        editCondicion.setFocusable(false);
                        editEmail.setFocusable(false);
                        editTelefono.setFocusable(false);
                        break;

                    case MotionEvent.ACTION_UP:
                        buttonGuardar.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                        buttonGuardar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_orange, 0, 0, 0);
                        break;
                }
                return true;
            }
        });

        // EVENTOS AL CAMBIAR DE CAMPOS
        editComercio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, RECUPERA COLOR DE TEXTO
                    editComercio.setTextColor(ContextCompat.getColor(getContext(), R.color.colorText));

                } else {

                    // SI INGRESA AL CAMPO, CAMBIA COLOR DE TEXTO
                    editComercio.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSelected));
                }
            }
        });
        editDireccion.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, RECUPERA COLOR DE TEXTO
                    editDireccion.setTextColor(ContextCompat.getColor(getContext(), R.color.colorText));

                } else {

                    // SI INGRESA AL CAMPO, CAMBIA COLOR DE TEXTO
                    editDireccion.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSelected));
                }
            }
        });
        editLocalidad.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, RECUPERA COLOR DE TEXTO
                    editLocalidad.setTextColor(ContextCompat.getColor(getContext(), R.color.colorText));

                } else {

                    // SI INGRESA AL CAMPO, CAMBIA COLOR DE TEXTO
                    editLocalidad.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSelected));
                }
            }
        });
        editCuit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, RECUPERA COLOR DE TEXTO
                    editCuit.setTextColor(ContextCompat.getColor(getContext(), R.color.colorText));

                } else {

                    // SI INGRESA AL CAMPO, CAMBIA COLOR DE TEXTO
                    editCuit.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSelected));
                }
            }
        });
        editCondicion.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, RECUPERA COLOR DE TEXTO
                    editCondicion.setTextColor(ContextCompat.getColor(getContext(), R.color.colorText));

                } else {

                    // SI INGRESA AL CAMPO, CAMBIA COLOR DE TEXTO
                    editCondicion.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSelected));
                }
            }
        });
        editEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, RECUPERA COLOR DE TEXTO
                    editEmail.setTextColor(ContextCompat.getColor(getContext(), R.color.colorText));

                } else {

                    // SI INGRESA AL CAMPO, CAMBIA COLOR DE TEXTO
                    editEmail.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSelected));
                }
            }
        });
        editTelefono.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, RECUPERA COLOR DE TEXTO
                    editTelefono.setTextColor(ContextCompat.getColor(getContext(), R.color.colorText));

                } else {

                    // SI INGRESA AL CAMPO, CAMBIA COLOR DE TEXTO
                    editTelefono.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSelected));
                }
            }
        });

        return root;
    }

    private void dateFragments() {

        // OBTIENE LA FECHA Y HORA ACTUAL
        Date date = Calendar.getInstance().getTime();
        String hour = date.toString();
        datoHoraActual = "" + hour.charAt(11) + hour.charAt(12) + hour.charAt(13) + hour.charAt(14) + hour.charAt(15);

        Calendar calendar = Calendar.getInstance();
        String fechaActual = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());

        String[] arrayFecha = fechaActual.split("/");
        datoDia = arrayFecha[0];
        datoMes = arrayFecha[1];
        datoAno = arrayFecha[2];

        if (datoDia.length() == 1) {
            datoDia = "0" + datoDia;
        }

        if (datoMes.length() == 1) {
            datoMes = "0" + datoMes;
        }

        datoFechaActual = datoDia + "/" + datoMes + "/" + datoAno;
    }

    private void dialogProcesando(){

        // DIALOG CON PROGRESS BAR MIENTRAS CONSULTA A LAS TABLAS
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

    private void dialogError(){

        // DIALOG CON MENSAJE DE ERROR
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Dialogs dialogs = new Dialogs(getActivity());
                int layout = R.layout.dialog_error;
                dialogs.startResultado(layout);
            }
        }, 3000);
    }

    private void dialogOk(){

        // DIALOG CON CONFIRMACIÓN DE OPERACIÓN REALIZADA CON ÉXITO
        new Handler().postDelayed(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                Dialogs dialogs = new Dialogs(getActivity());
                int layout = R.layout.dialog_ok;
                dialogs.startResultado(layout);

                EditText editComercio = getView().findViewById(R.id.editComercio);
                EditText editDireccion = getView().findViewById(R.id.editDireccion);
                EditText editLocalidad = getView().findViewById(R.id.editLocalidad);
                EditText editCuit = getView().findViewById(R.id.editCuit);
                EditText editCondicion = getView().findViewById(R.id.editCondicion);
                EditText editEmail = getView().findViewById(R.id.editEmail);
                EditText editTelefono = getView().findViewById(R.id.editTelefono);

                editComercio.setFocusableInTouchMode(false);
                editDireccion.setFocusableInTouchMode(false);
                editLocalidad.setFocusableInTouchMode(false);
                editCuit.setFocusableInTouchMode(false);
                editCondicion.setFocusableInTouchMode(false);
                editEmail.setFocusableInTouchMode(false);
                editTelefono.setFocusableInTouchMode(false);

                editComercio.setError(null);
                editDireccion.setError(null);
                editLocalidad.setError(null);
                editCuit.setError(null);
                editCondicion.setError(null);
                editEmail.setError(null);
                editTelefono.setError(null);

                Button buttonModificar = getView().findViewById(R.id.buttonModificar);
                buttonModificar.setVisibility(View.VISIBLE);

                ScrollView scrollView = getView().findViewById(R.id.scroll);
                scrollView.setScrollY(0);
            }
        }, 3000);
    }

    private void consultarUsuario(){

        // RECIBE EL USUARIO Y LO CONSULTA EN LA BASE PARA OBTENER SUS DATOS
        datoUsuario = getActivity().getIntent().getStringExtra("user");

        String URL = "http://malpicas.heliohost.org/malpica/usuario/usuario_consultar_datos.php?parameter=" + datoUsuario;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                UsuarioSetters setters = new UsuarioSetters();

                                setters.setLocal(jsonObject.getString("comercio"));
                                setters.setDireccion(jsonObject.getString("direccion"));
                                setters.setLocalidad(jsonObject.getString("localidad"));
                                setters.setCuit(jsonObject.getString("cuit"));
                                setters.setCondicion(jsonObject.getString("condicion"));
                                setters.setEmail(jsonObject.getString("email"));
                                setters.setTelefono(jsonObject.getString("telefono"));
                                setters.setEstado(jsonObject.getString("estado"));
                                setters.setFechaAlta(jsonObject.getString("fecha_alta"));

                                String comercio = setters.getLocal();
                                String direccion = setters.getDireccion();
                                String localidad = setters.getLocalidad();
                                String cuit = setters.getCuit();
                                String condicion = setters.getCondicion();
                                String email = setters.getEmail();
                                String telefono = setters.getTelefono();
                                String estado = setters.getEstado();
                                String fecha = setters.getFechaAlta();

                                String fechaAlta = fecha.replace("/2","/202");

                                EditText editComercio = getView().findViewById(R.id.editComercio);
                                EditText editDireccion = getView().findViewById(R.id.editDireccion);
                                EditText editLocalidad = getView().findViewById(R.id.editLocalidad);
                                EditText editCuit = getView().findViewById(R.id.editCuit);
                                EditText editCondicion = getView().findViewById(R.id.editCondicion);
                                EditText editEmail = getView().findViewById(R.id.editEmail);
                                EditText editTelefono = getView().findViewById(R.id.editTelefono);
                                TextView textEstado = getView().findViewById(R.id.textEstado1);
                                TextView textFechaAlta = getView().findViewById(R.id.textFechaAlta1);

                                editComercio.setText(comercio);
                                editDireccion.setText(direccion);
                                editLocalidad.setText(localidad);
                                editCuit.setText(cuit);
                                editCondicion.setText(condicion);
                                editEmail.setText(email);
                                editTelefono.setText(telefono);
                                textEstado.setText(estado);
                                textFechaAlta.setText(fechaAlta);

                                consultarStock();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Por favor, revise su conexión!", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void consultarStock(){

        // OBTIENE EL TOTAL HISTÓRICO DEL STOCK REGISTRADO
        String URL = "http://malpicas.heliohost.org/malpica/usuario/usuario_consultar_stock.php?parameter=" + "ARS";
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                UsuarioSetters usuarioSetters = new UsuarioSetters();

                                usuarioSetters.setTotalStock(jsonObject.getString("total_historico"));

                                String total = usuarioSetters.getTotalStock();

                                if(total.equals("null")){

                                    // SI DEVUELVE QUE NO EXISTE, MUESTRA UN CERO COMO RESULTADO
                                    double totalStock = 0;

                                    DecimalFormat decimalFormat = new DecimalFormat("######0.00");
                                    String stock = "$ " + decimalFormat.format(totalStock).replace(".",",");
                                    TextView textStock = getView().findViewById(R.id.textStock1);
                                    textStock.setText(stock);

                                } else {

                                    // SI DEVUELVE QUE EXISTE, MUESTRA EL RESULTADO OBTENIDO
                                    double totalStock = Double.parseDouble(total);

                                    DecimalFormat decimalFormat = new DecimalFormat("######0.00");
                                    String stock = "$ " + decimalFormat.format(totalStock).replace(".",",");
                                    TextView textStock = getView().findViewById(R.id.textStock1);
                                    textStock.setText(stock);
                                }

                                consultarVentas();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Por favor, revise su conexión!", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void consultarVentas(){

        // OBTIENE EL TOTAL DE VENTAS REGISTRADAS EN EL MES ACTUAL
        String URL = "http://malpicas.heliohost.org/malpica/usuario/usuario_consultar_ventas.php?parameter=" + datoMes;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                UsuarioSetters usuarioSetters = new UsuarioSetters();

                                usuarioSetters.setTotalVentas(jsonObject.getString("total_mensual"));

                                String total = usuarioSetters.getTotalVentas();

                                if(total.equals("null")){

                                    // SI DEVUELVE QUE NO EXISTE, MUESTRA UN CERO COMO RESULTADO
                                    totalVentas = 0;

                                    DecimalFormat decimalFormat = new DecimalFormat("######0.00");
                                    String ventas = "$ " + decimalFormat.format(totalVentas).replace(".",",");
                                    TextView textVentas = getView().findViewById(R.id.textVentas1);
                                    textVentas.setText(ventas);

                                } else {

                                    // SI DEVUELVE QUE EXISTE, MUESTRA EL RESULTADO OBTENIDO
                                    totalVentas = Double.parseDouble(total);

                                    DecimalFormat decimalFormat = new DecimalFormat("######0.00");
                                    String ventas = "$ " + decimalFormat.format(totalVentas).replace(".",",");
                                    TextView textVentas = getView().findViewById(R.id.textVentas1);
                                    textVentas.setText(ventas);
                                }

                                consultarCompras();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Por favor, revise su conexión!", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void consultarCompras(){

        // OBTIENE EL TOTAL DE COMPRAS REGISTRADAS EN EL MES ACTUAL
        String URL = "http://malpicas.heliohost.org/malpica/usuario/usuario_consultar_compras.php?parameter=" + datoMes;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                UsuarioSetters usuarioSetters = new UsuarioSetters();

                                usuarioSetters.setTotalCompras(jsonObject.getString("total_mensual"));

                                String total = usuarioSetters.getTotalCompras();

                                if(total.equals("null")){

                                    // SI DEVUELVE QUE NO EXISTE, MUESTRA UN CERO COMO RESULTADO
                                    totalCompras = 0;

                                    DecimalFormat decimalFormat = new DecimalFormat("######0.00");
                                    String compras = "$ " + decimalFormat.format(totalCompras).replace(".",",");
                                    TextView textCompras = getView().findViewById(R.id.textCompras1);
                                    textCompras.setText(compras);

                                } else {

                                    // SI DEVUELVE QUE EXISTE, MUESTRA EL RESULTADO OBTENIDO
                                    totalCompras = Double.parseDouble(total);

                                    DecimalFormat decimalFormat = new DecimalFormat("######0.00");
                                    String compras = "$ " + decimalFormat.format(totalCompras).replace(".",",");
                                    TextView textCompras = getView().findViewById(R.id.textCompras1);
                                    textCompras.setText(compras);
                                }

                                consultarGastos();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Por favor, revise su conexión!", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void consultarGastos(){

        // OBTIENE EL TOTAL DE GASTOS REGISTRADOS EN EL MES ACTUAL
        String URL = "http://malpicas.heliohost.org/malpica/usuario/usuario_consultar_gastos.php?parameter=" + datoMes;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                UsuarioSetters usuarioSetters = new UsuarioSetters();

                                usuarioSetters.setTotalGastos(jsonObject.getString("total_mensual"));

                                String total = usuarioSetters.getTotalGastos();

                                if(total.equals("null")){

                                    // SI DEVUELVE QUE NO EXISTE, MUESTRA UN CERO COMO RESULTADO
                                    totalGastos = 0;

                                    DecimalFormat decimalFormat = new DecimalFormat("######0.00");
                                    String gastos = "$ " + decimalFormat.format(totalGastos).replace(".",",");
                                    TextView textGastos = getView().findViewById(R.id.textGastos1);
                                    textGastos.setText(gastos);

                                } else {

                                    // SI DEVUELVE QUE EXISTE, MUESTRA EL RESULTADO OBTENIDO
                                    totalGastos = Double.parseDouble(total);

                                    DecimalFormat decimalFormat = new DecimalFormat("######0.00");
                                    String gastos = "$ " + decimalFormat.format(totalGastos).replace(".",",");
                                    TextView textGastos = getView().findViewById(R.id.textGastos1);
                                    textGastos.setText(gastos);
                                }

                                // CALCULA EL BALANCE MENSUAL DEL USUARIO
                                totalBalance = totalVentas - totalCompras - totalGastos;

                                DecimalFormat decimalFormat = new DecimalFormat("######0.00");
                                String balance = "$ " + decimalFormat.format(totalBalance).replace(".",",");
                                String balanceFinal = balance.replace("-","");
                                TextView textBalance = getView().findViewById(R.id.textBalance1);
                                textBalance.setText(balanceFinal);

                                if(totalBalance > 0) {
                                    textBalance.setTextColor(Color.GREEN);
                                }

                                if(totalBalance < 0) {
                                    textBalance.setTextColor(Color.RED);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Por favor, revise su conexión!", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void modificarDatos(){

        // MODIFICA LOS DATOS DEL USUARIO
        final EditText editComercio = getView().findViewById(R.id.editComercio);
        final EditText editDireccion = getView().findViewById(R.id.editDireccion);
        final EditText editLocalidad = getView().findViewById(R.id.editLocalidad);
        final EditText editCuit = getView().findViewById(R.id.editCuit);
        final EditText editCondicion = getView().findViewById(R.id.editCondicion);
        final EditText editEmail = getView().findViewById(R.id.editEmail);
        final EditText editTelefono = getView().findViewById(R.id.editTelefono);

        if(!editComercio.getText().toString().isEmpty() && !editDireccion.getText().toString().isEmpty() &&
                !editLocalidad.getText().toString().isEmpty() && editCuit.getText().toString().length() == 13 &&
                editCuit.getText().toString().contains("-") && editCondicion.getText().toString().length() == 2 &&
                !editEmail.getText().toString().isEmpty() && !editTelefono.getText().toString().isEmpty()) {

            // SI SE INGRESAN LOS DATOS CORRECTAMENTE REALIZA LA MODIFICACIÓN
            String URL = "http://malpicas.heliohost.org/malpica/usuario/usuario_actualizar_datos.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dialogOk();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(),error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> parameter = new HashMap<>();

                    parameter.put("usuario",datoUsuario);
                    parameter.put("comercio",editComercio.getText().toString());
                    parameter.put("direccion",editDireccion.getText().toString());
                    parameter.put("localidad",editLocalidad.getText().toString());
                    parameter.put("cuit",editCuit.getText().toString());
                    parameter.put("condicion",editCondicion.getText().toString());
                    parameter.put("email",editEmail.getText().toString());
                    parameter.put("telefono",editTelefono.getText().toString());
                    parameter.put("fecha_modif",datoFechaActual);
                    parameter.put("hora_modif",datoHoraActual);
                    parameter.put("dia_modif",datoDia);
                    parameter.put("mes_modif",datoMes);
                    parameter.put("ano_modif",datoAno);

                    return parameter;
                }
            };
            requestQueue.add(stringRequest);

        } else {

            // SI NO SE INGRESAN LOS DATOS CORRECTAMENTE MUESTRA UN MENSAJE DE ERROR
            dialogError();
        }
    }

    private void actualizarIngreso(){

        // MODIFICA EL DÍA Y LA HORA DE INGRESO EN LA BASE
        String URL = "http://malpicas.heliohost.org/malpica/usuario/usuario_actualizar_ingreso.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameter = new HashMap<>();

                parameter.put("usuario",datoUsuario);
                parameter.put("fecha_ingreso",datoFechaActual);
                parameter.put("hora_ingreso",datoHoraActual);
                parameter.put("dia_ingreso",datoDia);
                parameter.put("mes_ingreso",datoMes);
                parameter.put("ano_ingreso",datoAno);

                return parameter;
            }
        };
        requestQueue.add(stringRequest);
    }
}