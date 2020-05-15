package com.example.malpicasoft.stock;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

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

public class ModificarStock extends Fragment {

    private RequestQueue requestQueue;
    private JsonObjectRequest jsonObjectRequest;
    private String datoFechaActual, datoBuscarCodigo, datoBuscarDescripcion, datoCodigo, datoDescripcion,
            datoCantidad, datoMoneda, datoPrecioUnit, datoPrecioTotal, datoDia, datoMes, datoAno;
    private int counter, cantidad;
    private double precioUnit, precioTotal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_modificar_stock, container, false);

        dateFragments();

        // LIBRERÍA VOLLEY PARA CONSULTAS A BASES DE DATOS
        requestQueue = Volley.newRequestQueue(getContext());

        // COMPONENTES DE LA VISTA
        final EditText editBuscarCodigo = root.findViewById(R.id.editBuscarCodigo);
        final EditText editBuscarDescripcion = root.findViewById(R.id.editBuscarDescripcion);
        final EditText editCodigo = root.findViewById(R.id.editCodigo);
        final EditText editDescripcion = root.findViewById(R.id.editDescripcion);
        final EditText editCantidad = root.findViewById(R.id.editCantidad);
        final EditText editMoneda = root.findViewById(R.id.editMoneda);
        final EditText editPrecioUnit = root.findViewById(R.id.editPrecioUnit);
        final EditText editPrecioTotal = root.findViewById(R.id.editPrecioTotal);

        Button buttonConsultar = root.findViewById(R.id.buttonConsultar);
        Button buttonModificar = root.findViewById(R.id.buttonModificar);
        Button buttonGuardar = root.findViewById(R.id.buttonGuardar);

        // EVENTOS DEL BOTÓN CONSULTAR
        buttonConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Button buttonModificar = getView().findViewById(R.id.buttonModificar);
                buttonModificar.setVisibility(View.VISIBLE);

                editCodigo.setFocusable(false);
                editDescripcion.setFocusable(false);
                editCantidad.setFocusable(false);
                editMoneda.setFocusable(false);
                editPrecioUnit.setFocusable(false);
                editPrecioTotal.setFocusable(false);

                editCodigo.setError(null);
                editDescripcion.setError(null);
                editCantidad.setError(null);
                editMoneda.setError(null);
                editPrecioUnit.setError(null);
                editPrecioTotal.setError(null);

                datoBuscarCodigo = editBuscarCodigo.getText().toString();
                datoBuscarDescripcion = editBuscarDescripcion.getText().toString();

                // SI LOS CAMPOS NO ESTÁN VACÍOS, REALIZA LA CONSULTA CORRESPONDIENTE
                if (!datoBuscarCodigo.isEmpty()) {

                    dialogProcesando();
                    consultarCodigo();
                }

                if (!datoBuscarDescripcion.isEmpty()) {

                    dialogProcesando();
                    consultarDescripcion();
                }

                if (datoBuscarCodigo.isEmpty() && datoBuscarDescripcion.isEmpty()) {

                    // SI LOS CAMPOS ESTÁN VACÍOS, SE LIMPIAN LOS RESULTADOS ANTERIORES Y MUESTRA UN ERROR
                    editCodigo.setText("");
                    editDescripcion.setText("");
                    editCantidad.setText("");
                    editMoneda.setText("");
                    editPrecioUnit.setText("");
                    editPrecioTotal.setText("");

                    dialogProcesando();
                    dialogError();
                }
            }
        });

        // EVENTOS DEL BOTÓN MODIFICAR
        buttonModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datoCodigo = editCodigo.getText().toString();

                if(!datoCodigo.isEmpty()){

                    // SI DEVUELVE UN CÓDIGO, HABILITA LOS CAMPOS PARA HACER MODIFICACIONES
                    Button buttonModificar = getView().findViewById(R.id.buttonModificar);
                    buttonModificar.setVisibility(View.INVISIBLE);

                    Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

                    editDescripcion.setError("Datos correctos!", drawable);
                    editCantidad.setError("Datos correctos!", drawable);
                    editMoneda.setError("Datos correctos!", drawable);
                    editPrecioUnit.setError("Datos correctos!", drawable);
                    editPrecioTotal.setError("Datos correctos!", drawable);

                    editCodigo.setFocusableInTouchMode(true);
                    editDescripcion.setFocusableInTouchMode(true);
                    editCantidad.setFocusableInTouchMode(true);
                    editMoneda.setFocusableInTouchMode(true);
                    editPrecioUnit.setFocusableInTouchMode(true);
                    editPrecioTotal.setFocusableInTouchMode(true);

                    editCodigo.requestFocusFromTouch();

                    ScrollView scrollView = getView().findViewById(R.id.scroll);
                    scrollView.setScrollY(0);
                }
            }
        });

        // EVENTOS DEL BOTÓN GUARDAR
        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datoBuscarCodigo = editBuscarCodigo.getText().toString();
                datoBuscarDescripcion = editBuscarDescripcion.getText().toString();
                datoCodigo = editCodigo.getText().toString();
                datoDescripcion = editDescripcion.getText().toString();
                datoCantidad = editCantidad.getText().toString();
                datoMoneda = editMoneda.getText().toString();
                datoPrecioUnit = editPrecioUnit.getText().toString();
                datoPrecioTotal = editPrecioTotal.getText().toString();

                editCodigo.setFocusable(false);
                editDescripcion.setFocusable(false);
                editCantidad.setFocusable(false);
                editMoneda.setFocusable(false);
                editPrecioUnit.setFocusable(false);
                editPrecioTotal.setFocusable(false);

                if (editCodigo.getError() == "Datos correctos!"
                        && editDescripcion.getError() == "Datos correctos!"
                        && editCantidad.getError() == "Datos correctos!"
                        && editMoneda.getError() == "Datos correctos!"
                        && editPrecioUnit.getError() == "Datos correctos!"
                        && editPrecioTotal.getError() == "Datos correctos!") {

                    // SI TODOS LOS DATOS ESTÁN OK, PASA A REGISTRARLO
                    editPrecioTotal.requestFocusFromTouch();
                    editPrecioTotal.setError(null);

                    dialogProcesando();
                    modificarProducto();

                } else {

                    // SI ALGÚN DATO ES INCORRECTO, MUESTRA UN MENSAJE DE ERROR
                    editPrecioTotal.requestFocusFromTouch();
                    editPrecioTotal.setError(null);

                    dialogProcesando();
                    dialogError();

                    editCodigo.setFocusableInTouchMode(true);
                    editDescripcion.setFocusableInTouchMode(true);
                    editCantidad.setFocusableInTouchMode(true);
                    editMoneda.setFocusableInTouchMode(true);
                    editPrecioUnit.setFocusableInTouchMode(true);
                    editPrecioTotal.setFocusableInTouchMode(true);
                }
            }
        });

        // EVENTOS AL CAMBIAR DE CAMPOS
        editCodigo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, GUARDA Y CORROBORA LOS DATOS INGRESADOS
                    datoCodigo = editCodigo.getText().toString().replace(" ","");

                    if (!datoCodigo.isEmpty()) {

                        dialogProcesando();
                        consultarCodigoBis();

                    } else {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_error);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editCodigo.setError("Revise los datos!", drawable);
                    }

                } else {

                    // SI INGRESA AL CAMPO, OCULTA EL ÍCONO DE VALIDACIÓN
                    editCodigo.setError(null);
                }
            }
        });
        editDescripcion.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, GUARDA Y CORROBORA LOS DATOS INGRESADOS
                    datoDescripcion = editDescripcion.getText().toString();

                    if (!datoDescripcion.isEmpty()) {

                        dialogProcesando();
                        consultarDescripcionBis();

                    } else {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_error);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editDescripcion.setError("Revise los datos!", drawable);
                    }

                } else {

                    // SI INGRESA AL CAMPO, OCULTA EL ÍCONO DE VALIDACIÓN
                    editDescripcion.setError(null);
                }
            }
        });
        editCantidad.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, GUARDA Y CORROBORA LOS DATOS INGRESADOS
                    datoCantidad = editCantidad.getText().toString();

                    if(!datoCantidad.isEmpty()) {

                        cantidad = Integer.parseInt(datoCantidad);

                        if (cantidad > 0) {

                            Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                            editCantidad.setError("Datos correctos!", drawable);

                        } else {

                            Drawable drawable = getResources().getDrawable(R.drawable.ic_error);
                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                            editCantidad.setError("Revise los datos!", drawable);

                            editCantidad.setText("0");
                        }

                    } else {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_error);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editCantidad.setError("Revise los datos!", drawable);

                        editCantidad.setText("0");
                    }

                    datoCantidad = editCantidad.getText().toString();
                    cantidad = Integer.parseInt(datoCantidad);

                } else {

                    // SI INGRESA AL CAMPO, OCULTA EL ÍCONO DE VALIDACIÓN Y CHEQUEA LOS DATOS
                    editCantidad.setError(null);
                    datoCantidad = editCantidad.getText().toString();
                    cantidad = Integer.parseInt(datoCantidad);

                    if (cantidad == 0) {
                        editCantidad.setText("");
                    }
                }
            }
        });
        editMoneda.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, GUARDA Y VALIDA LOS DATOS INGRESADOS
                    datoMoneda = editMoneda.getText().toString();

                    if (!datoMoneda.isEmpty()) {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editMoneda.setError("Datos correctos!", drawable);

                    } else {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editMoneda.setError("Datos correctos!", drawable);

                        editMoneda.setText("ARS");
                    }

                } else {

                    // SI INGRESA AL CAMPO, OCULTA EL ÍCONO DE VALIDACIÓN
                    editMoneda.setError(null);
                }
            }
        });
        editPrecioUnit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, GUARDA Y CORROBORA LOS DATOS INGRESADOS
                    datoPrecioUnit = editPrecioUnit.getText().toString();

                    if(!datoPrecioUnit.isEmpty()) {

                        precioUnit = Double.parseDouble(datoPrecioUnit);

                        if (precioUnit > 0) {

                            Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                            editPrecioUnit.setError("Datos correctos!", drawable);

                            DecimalFormat decimalFormat = new DecimalFormat("#.00");
                            datoPrecioUnit = decimalFormat.format(precioUnit).replace(",",".");
                            editPrecioUnit.setText(datoPrecioUnit);

                        } else {

                            Drawable drawable = getResources().getDrawable(R.drawable.ic_error);
                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                            editPrecioUnit.setError("Revise los datos!", drawable);

                            editPrecioUnit.setText("0.00");
                        }

                    } else {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_error);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editPrecioUnit.setError("Revise los datos!", drawable);

                        editPrecioUnit.setText("0.00");
                    }

                    datoPrecioUnit = editPrecioUnit.getText().toString();
                    precioUnit = Double.parseDouble(datoPrecioUnit);

                } else {

                    // SI INGRESA AL CAMPO, OCULTA EL ÍCONO DE VALIDACIÓN Y CHEQUEA LOS DATOS
                    editPrecioUnit.setError(null);
                    datoPrecioUnit = editPrecioUnit.getText().toString();
                    precioUnit = Double.parseDouble(datoPrecioUnit);

                    if (precioUnit == 0) {
                        editPrecioUnit.setText("");
                    }
                }
            }
        });
        editPrecioTotal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, GUARDA Y CORROBORA LOS DATOS INGRESADOS
                    datoPrecioTotal = editPrecioTotal.getText().toString();
                    datoCantidad = editCantidad.getText().toString();
                    datoPrecioUnit = editPrecioUnit.getText().toString();
                    cantidad = Integer.parseInt(datoCantidad);
                    precioUnit = Double.parseDouble(datoPrecioUnit);

                    if(!datoPrecioTotal.isEmpty()) {

                        precioTotal = Double.parseDouble(datoPrecioTotal);

                        if (precioTotal > 0) {

                            Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                            editPrecioTotal.setError("Datos correctos!", drawable);

                        } else {

                            Drawable drawable = getResources().getDrawable(R.drawable.ic_error);
                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                            editPrecioTotal.setError("Revise los datos!", drawable);

                            editPrecioTotal.setText("0.00");
                        }

                    } else {

                        precioTotal = cantidad * precioUnit;

                        if(precioTotal == 0) {

                            Drawable drawable = getResources().getDrawable(R.drawable.ic_error);
                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                            editPrecioTotal.setError("Revise los datos!", drawable);

                            editPrecioTotal.setText("0.00");

                        } else {

                            Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                            editPrecioTotal.setError("Datos correctos!", drawable);

                            DecimalFormat decimalFormat = new DecimalFormat("#.00");
                            datoPrecioTotal = decimalFormat.format(precioTotal).replace(",",".");
                            editPrecioTotal.setText(datoPrecioTotal);
                        }
                    }

                    datoPrecioTotal = editPrecioTotal.getText().toString();
                    precioTotal = Double.parseDouble(datoPrecioTotal);

                } else {

                    // SI INGRESA AL CAMPO, OCULTA EL ÍCONO DE VALIDACIÓN Y CALCULA EL PRECIO TOTAL
                    editPrecioTotal.setError(null);

                    datoCantidad = editCantidad.getText().toString();
                    datoPrecioUnit = editPrecioUnit.getText().toString();
                    cantidad = Integer.parseInt(datoCantidad);
                    precioUnit = Double.parseDouble(datoPrecioUnit);

                    precioTotal = cantidad * precioUnit;

                    if(precioTotal == 0) {

                        editPrecioTotal.setText("0.00");

                    } else {

                        DecimalFormat decimalFormat = new DecimalFormat("#.00");
                        datoPrecioTotal = decimalFormat.format(precioTotal).replace(",",".");
                        editPrecioTotal.setText(datoPrecioTotal);
                    }

                    datoPrecioTotal = editPrecioTotal.getText().toString();
                    precioTotal = Double.parseDouble(datoPrecioTotal);
                }
            }
        });

        return root;
    }

    private void dateFragments() {

        // MÉTODO PARA OBTENER EL DÍA ACTUAL
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

                if(counter == 20) {
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
                dialogs.startError();
            }
        }, 2000);
    }

    private void dialogErrorProducto(){

        // DIALOG QUE INFORMA QUE EL PRODUCTO YA SE INGRESÓ ANTERIORMENTE
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Dialogs dialogs = new Dialogs(getActivity());
                dialogs.startErrorProducto();
            }
        }, 2000);
    }

    private void dialogOk(){

        // DIALOG CON CONFIRMACIÓN DE OPERACIÓN REALIZADA CON ÉXITO
        new Handler().postDelayed(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                Dialogs dialogs = new Dialogs(getActivity());
                dialogs.startOk();

                EditText editBuscarCodigo = getView().findViewById(R.id.editBuscarCodigo);
                EditText editCodigo = getView().findViewById(R.id.editCodigo);
                EditText editDescripcion = getView().findViewById(R.id.editDescripcion);
                EditText editCantidad = getView().findViewById(R.id.editCantidad);
                EditText editMoneda = getView().findViewById(R.id.editMoneda);
                EditText editPrecioUnit = getView().findViewById(R.id.editPrecioUnit);
                EditText editPrecioTotal = getView().findViewById(R.id.editPrecioTotal);

                editCodigo.setFocusableInTouchMode(true);
                editDescripcion.setFocusableInTouchMode(true);
                editCantidad.setFocusableInTouchMode(true);
                editMoneda.setFocusableInTouchMode(true);
                editPrecioUnit.setFocusableInTouchMode(true);
                editPrecioTotal.setFocusableInTouchMode(true);

                editCodigo.setText("");
                editDescripcion.setText("");
                editCantidad.setText("");
                editMoneda.setText("");
                editPrecioUnit.setText("");
                editPrecioTotal.setText("");

                editCodigo.setError(null);
                editDescripcion.setError(null);
                editCantidad.setError(null);
                editMoneda.setError(null);
                editPrecioUnit.setError(null);
                editPrecioTotal.setError(null);

                ScrollView scrollView = getView().findViewById(R.id.scroll);
                scrollView.setScrollY(0);
                editBuscarCodigo.requestFocusFromTouch();
            }
        }, 2000);
    }

    private void consultarCodigo() {

        // CONSULTA POR CÓDIGO SI YA FUE INGRESADO ANTERIORMENTE A LA BASE
        String URL = "http://malpicas.heliohost.org/malpica/stock/stock_consultar_codigo.php?codigo=" + datoBuscarCodigo;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("datos");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                StockSetters stockSetters = new StockSetters();

                                stockSetters.setCodigo(jsonObject.getString("codigo"));
                                stockSetters.setDescripcion(jsonObject.getString("descripcion"));
                                stockSetters.setCantidad(jsonObject.getString("cantidad"));
                                stockSetters.setMoneda(jsonObject.getString("moneda"));
                                stockSetters.setPrecioUnit(jsonObject.getString("precio_unit"));
                                stockSetters.setPrecioTotal(jsonObject.getString("precio_total"));

                                String codigo = stockSetters.getCodigo();
                                String descripcion = stockSetters.getDescripcion();
                                String cantidad = stockSetters.getCantidad();
                                String moneda = stockSetters.getMoneda();
                                String precioUnit = stockSetters.getPrecioUnit();
                                String precioTotal = stockSetters.getPrecioTotal();

                                if (!codigo.equals("No existe")) {

                                    // SI DEVUELVE VALORES LOS MUESTRA EN LOS CAMPOS
                                    EditText editCodigo = getView().findViewById(R.id.editCodigo);
                                    EditText editDescripcion = getView().findViewById(R.id.editDescripcion);
                                    EditText editCantidad = getView().findViewById(R.id.editCantidad);
                                    EditText editMoneda = getView().findViewById(R.id.editMoneda);
                                    EditText editPrecioUnit = getView().findViewById(R.id.editPrecioUnit);
                                    EditText editPrecioTotal = getView().findViewById(R.id.editPrecioTotal);

                                    editCodigo.setText(codigo);
                                    editDescripcion.setText(descripcion);
                                    editCantidad.setText(cantidad);
                                    editMoneda.setText(moneda);
                                    editPrecioUnit.setText(precioUnit);
                                    editPrecioTotal.setText(precioTotal);

                                } else {

                                    // SI DEVUELVE QUE NO EXISTE MUESTRA UN MENSAJE DE ERROR
                                    dialogError();
                                }
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

    private void consultarDescripcion() {

        // CONSULTA POR DESCRIPCIÓN SI YA FUE INGRESADO ANTERIORMENTE A LA BASE
        String descripcion = datoBuscarDescripcion.replace(" ", "%20");

        String URL = "http://malpicas.heliohost.org/malpica/stock/stock_consultar_descripcion.php?descripcion=" + descripcion;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("datos");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                StockSetters stockSetters = new StockSetters();

                                stockSetters.setCodigo(jsonObject.getString("codigo"));
                                stockSetters.setDescripcion(jsonObject.getString("descripcion"));
                                stockSetters.setCantidad(jsonObject.getString("cantidad"));
                                stockSetters.setMoneda(jsonObject.getString("moneda"));
                                stockSetters.setPrecioUnit(jsonObject.getString("precio_unit"));
                                stockSetters.setPrecioTotal(jsonObject.getString("precio_total"));

                                String codigo = stockSetters.getCodigo();
                                String descripcion = stockSetters.getDescripcion();
                                String cantidad = stockSetters.getCantidad();
                                String moneda = stockSetters.getMoneda();
                                String precioUnit = stockSetters.getPrecioUnit();
                                String precioTotal = stockSetters.getPrecioTotal();

                                if (!descripcion.equals("No existe")) {

                                    // SI DEVUELVE VALORES LOS MUESTRA EN LOS CAMPOS
                                    EditText editCodigo = getView().findViewById(R.id.editCodigo);
                                    EditText editDescripcion = getView().findViewById(R.id.editDescripcion);
                                    EditText editCantidad = getView().findViewById(R.id.editCantidad);
                                    EditText editMoneda = getView().findViewById(R.id.editMoneda);
                                    EditText editPrecioUnit = getView().findViewById(R.id.editPrecioUnit);
                                    EditText editPrecioTotal = getView().findViewById(R.id.editPrecioTotal);

                                    editCodigo.setText(codigo);
                                    editDescripcion.setText(descripcion);
                                    editCantidad.setText(cantidad);
                                    editMoneda.setText(moneda);
                                    editPrecioUnit.setText(precioUnit);
                                    editPrecioTotal.setText(precioTotal);

                                } else {

                                    // SI DEVUELVE QUE NO EXISTE MUESTRA UN MENSAJE DE ERROR
                                    dialogError();
                                }
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

    private void consultarCodigoBis() {

        // CONSULTA POR CÓDIGO SI NO EXISTE OTRO CON EL MISMO CÓDIGO
        String URL = "http://malpicas.heliohost.org/malpica/stock/stock_consultar_codigo.php?codigo=" + datoCodigo;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("datos");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                StockSetters stockSetters = new StockSetters();

                                stockSetters.setCodigo(jsonObject.getString("codigo"));

                                String codigo = stockSetters.getCodigo();

                                if(!codigo.equals("No existe")){

                                    // SI YA SE ENCUENTRA REGISTRADO MUESTRA UN MENSAJE DE ERROR PERO VALIDA EL CAMPO
                                    dialogErrorProducto();

                                    Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                                    EditText editCodigo = getView().findViewById(R.id.editCodigo);
                                    editCodigo.setError("Datos correctos!", drawable);

                                } else {

                                    // SI NO SE ENCUENTRA REGISTRADO VALIDA EL CAMPO
                                    Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                                    EditText editCodigo = getView().findViewById(R.id.editCodigo);
                                    editCodigo.setError("Datos correctos!", drawable);
                                }
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

    private void consultarDescripcionBis() {

        // CONSULTA POR DESCRIPCIÓN SI YA FUE INGRESADO ANTERIORMENTE A LA BASE
        String descripcion = datoDescripcion.replace(" ","%20");

        String URL = "http://malpicas.heliohost.org/malpica/stock/stock_consultar_descripcion.php?descripcion=" + descripcion;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("datos");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                StockSetters stockSetters = new StockSetters();

                                stockSetters.setDescripcion(jsonObject.getString("descripcion"));

                                String descripcion = stockSetters.getDescripcion();

                                if(!descripcion.equals("No existe")){

                                    // SI YA SE ENCUENTRA REGISTRADO MUESTRA UN MENSAJE DE ERROR PERO VALIDA EL CAMPO
                                    dialogErrorProducto();

                                    Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                                    EditText editDescripcion = getView().findViewById(R.id.editDescripcion);
                                    editDescripcion.setError("Datos correctos!", drawable);

                                } else {

                                    // SI NO SE ENCUENTRA REGISTRADO VALIDA EL CAMPO
                                    Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                                    EditText editDescripcion = getView().findViewById(R.id.editDescripcion);
                                    editDescripcion.setError("Datos correctos!", drawable);
                                }
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

    private void modificarProducto() {

        // FINALMENTE MODIFICA EL PRODUCTO EN LA BASE
        String URL = "http://malpicas.heliohost.org/malpica/stock/stock_actualizar_producto.php";
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
                Map<String, String> parametros = new HashMap<>();

                parametros.put("codigo_viejo",datoBuscarCodigo);
                parametros.put("codigo_nuevo",datoCodigo);
                parametros.put("descripcion",datoDescripcion);
                parametros.put("cantidad",datoCantidad);
                parametros.put("moneda",datoMoneda);
                parametros.put("precio_unit",datoPrecioUnit);
                parametros.put("precio_total",datoPrecioTotal);
                parametros.put("fecha_modif",datoFechaActual);
                parametros.put("dia_modif",datoDia);
                parametros.put("mes_modif",datoMes);
                parametros.put("ano_modif",datoAno);

                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }
}
