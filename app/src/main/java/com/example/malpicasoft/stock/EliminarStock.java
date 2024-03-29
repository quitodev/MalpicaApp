package com.example.malpicasoft.stock;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class EliminarStock extends Fragment {

    private RequestQueue requestQueue;
    private String datoBuscarCodigo, datoBuscarDescripcion, datoCodigo;
    private int counter;

    public EliminarStock() { }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_eliminar_stock, container, false);

        // LIBRERÍA VOLLEY PARA CONSULTAS A BASES DE DATOS
        requestQueue = Volley.newRequestQueue(getContext());

        // COMPONENTES DE LA VISTA
        final EditText editBuscarCodigo = root.findViewById(R.id.editBuscarCodigo);
        final EditText editBuscarDescripcion = root.findViewById(R.id.editBuscarDescripcion);
        final EditText editFechaAlta = root.findViewById(R.id.editFechaAlta);
        final EditText editFechaModif = root.findViewById(R.id.editFechaModif);
        final EditText editCodigo = root.findViewById(R.id.editCodigo);
        final EditText editDescripcion = root.findViewById(R.id.editDescripcion);
        final EditText editCantidad = root.findViewById(R.id.editCantidad);
        final EditText editMoneda = root.findViewById(R.id.editMoneda);
        final EditText editPrecioUnit = root.findViewById(R.id.editPrecioUnit);
        final EditText editPrecioTotal = root.findViewById(R.id.editPrecioTotal);

        final TextView textBuscarCodigo = root.findViewById(R.id.textBuscarCodigo);
        final TextView textBuscarDescripcion = root.findViewById(R.id.textBuscarDescripcion);

        final Button buttonConsultar = root.findViewById(R.id.buttonConsultar);
        final Button buttonEliminar = root.findViewById(R.id.buttonEliminar);

        // EVENTOS DEL BOTÓN CONSULTAR
        buttonConsultar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        buttonConsultar.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSelected));
                        buttonConsultar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_yellow, 0, 0, 0);

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
                            editFechaAlta.setText("");
                            editFechaModif.setText("");
                            editCodigo.setText("");
                            editDescripcion.setText("");
                            editCantidad.setText("");
                            editMoneda.setText("");
                            editPrecioUnit.setText("");
                            editPrecioTotal.setText("");

                            dialogProcesando();
                            dialogError();
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        buttonConsultar.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                        buttonConsultar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_orange, 0, 0, 0);
                        break;
                }
                return true;
            }
        });

        // EVENTOS DEL BOTÓN ELIMINAR
        buttonEliminar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        buttonEliminar.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSelected));
                        buttonEliminar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_delete_yellow, 0, 0, 0);

                        String codigo = editCodigo.getText().toString();

                        // SI LOS CAMPOS NO ESTÁN VACÍOS, REALIZA LA CONSULTA CORRESPONDIENTE
                        if (!codigo.isEmpty()) {

                            // MUESTRA UN DIALOG DE CONFIRMACIÓN ANTES DE ELIMINARLO
                            dialogEliminar();
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        buttonEliminar.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                        buttonEliminar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_delete_orange, 0, 0, 0);
                        break;
                }
                return true;
            }
        });

        // EVENTOS AL CAMBIAR DE CAMPOS
        editBuscarCodigo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, RECUPERA COLOR DE TEXTO
                    textBuscarCodigo.setTextColor(ContextCompat.getColor(getContext(), R.color.colorText));

                } else {

                    // SI INGRESA AL CAMPO, CAMBIA COLOR DE TEXTO Y ELIMINA LOS DATOS INGRESADOS EN LOS OTROS CAMPOS
                    textBuscarCodigo.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSelected));
                    editBuscarDescripcion.setText("");
                }
            }
        });
        editBuscarDescripcion.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, RECUPERA COLOR DE TEXTO
                    textBuscarDescripcion.setTextColor(ContextCompat.getColor(getContext(), R.color.colorText));

                } else {

                    // SI INGRESA AL CAMPO, CAMBIA COLOR DE TEXTO Y ELIMINA LOS DATOS INGRESADOS EN LOS OTROS CAMPOS
                    textBuscarDescripcion.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSelected));
                    editBuscarCodigo.setText("");
                }
            }
        });

        return root;
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

                EditText editFechaAlta = getView().findViewById(R.id.editFechaAlta);
                EditText editFechaModif = getView().findViewById(R.id.editFechaModif);
                EditText editCodigo = getView().findViewById(R.id.editCodigo);
                EditText editDescripcion = getView().findViewById(R.id.editDescripcion);
                EditText editCantidad = getView().findViewById(R.id.editCantidad);
                EditText editMoneda = getView().findViewById(R.id.editMoneda);
                EditText editPrecioUnit = getView().findViewById(R.id.editPrecioUnit);
                EditText editPrecioTotal = getView().findViewById(R.id.editPrecioTotal);

                editFechaAlta.setText("");
                editFechaModif.setText("");
                editCodigo.setText("");
                editDescripcion.setText("");
                editCantidad.setText("");
                editMoneda.setText("");
                editPrecioUnit.setText("");
                editPrecioTotal.setText("");

                ScrollView scrollView = getView().findViewById(R.id.scroll);
                scrollView.setScrollY(0);
            }
        }, 3000);
    }

    private void dialogErrorOps(){

        // DIALOG CON MENSAJE DE ERROR POR TENER OPERACIONES VINCULADAS REGISTRADAS
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Dialogs dialogs = new Dialogs(getActivity());
                int layout = R.layout.dialog_error_operaciones;
                dialogs.startResultado(layout);

                EditText editFechaAlta = getView().findViewById(R.id.editFechaAlta);
                EditText editFechaModif = getView().findViewById(R.id.editFechaModif);
                EditText editCodigo = getView().findViewById(R.id.editCodigo);
                EditText editDescripcion = getView().findViewById(R.id.editDescripcion);
                EditText editCantidad = getView().findViewById(R.id.editCantidad);
                EditText editMoneda = getView().findViewById(R.id.editMoneda);
                EditText editPrecioUnit = getView().findViewById(R.id.editPrecioUnit);
                EditText editPrecioTotal = getView().findViewById(R.id.editPrecioTotal);

                editFechaAlta.setText("");
                editFechaModif.setText("");
                editCodigo.setText("");
                editDescripcion.setText("");
                editCantidad.setText("");
                editMoneda.setText("");
                editPrecioUnit.setText("");
                editPrecioTotal.setText("");

                ScrollView scrollView = getView().findViewById(R.id.scroll);
                scrollView.setScrollY(0);
            }
        }, 3000);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void dialogEliminar(){

        // DIALOG CON CONFIRMACIÓN DE ELIMINACIÓN
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_eliminar,null);
        builder.setView(view);
        builder.setCancelable(true);

        final Button buttonContinuar = view.findViewById(R.id.buttonContinuar);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        buttonContinuar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        buttonContinuar.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSelected));

                        alertDialog.dismiss();
                        dialogProcesando();
                        consultarCompras();
                        break;

                    case MotionEvent.ACTION_UP:
                        buttonContinuar.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                        break;
                }
                return true;
            }
        });
    }

    private void dialogOk(){

        // DIALOG CON CONFIRMACIÓN DE OPERACIÓN REALIZADA CON ÉXITO
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Dialogs dialogs = new Dialogs(getActivity());
                int layout = R.layout.dialog_ok;
                dialogs.startResultado(layout);

                EditText editFechaAlta = getView().findViewById(R.id.editFechaAlta);
                EditText editFechaModif = getView().findViewById(R.id.editFechaModif);
                EditText editCodigo = getView().findViewById(R.id.editCodigo);
                EditText editDescripcion = getView().findViewById(R.id.editDescripcion);
                EditText editCantidad = getView().findViewById(R.id.editCantidad);
                EditText editMoneda = getView().findViewById(R.id.editMoneda);
                EditText editPrecioUnit = getView().findViewById(R.id.editPrecioUnit);
                EditText editPrecioTotal = getView().findViewById(R.id.editPrecioTotal);

                editFechaAlta.setText("");
                editFechaModif.setText("");
                editCodigo.setText("");
                editDescripcion.setText("");
                editCantidad.setText("");
                editMoneda.setText("");
                editPrecioUnit.setText("");
                editPrecioTotal.setText("");

                ScrollView scrollView = getView().findViewById(R.id.scroll);
                scrollView.setScrollY(0);
            }
        }, 3000);
    }

    private void consultarCodigo() {

        // CONSULTA POR CÓDIGO SI YA FUE INGRESADO ANTERIORMENTE A LA BASE
        String URL = "http://malpicas.heliohost.org/malpica/stock/stock_consultar_codigo.php?parameter=" + datoBuscarCodigo;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                StockSetters setters = new StockSetters();

                                setters.setFechaAlta(jsonObject.getString("fecha_alta"));
                                setters.setHoraAlta(jsonObject.getString("hora_alta"));
                                setters.setFechaModif(jsonObject.getString("fecha_modif"));
                                setters.setHoraModif(jsonObject.getString("hora_modif"));
                                setters.setCodigo(jsonObject.getString("codigo_stock"));
                                setters.setDescripcion(jsonObject.getString("descripcion_stock"));
                                setters.setCantidad(jsonObject.getString("cantidad"));
                                setters.setMoneda(jsonObject.getString("moneda"));
                                setters.setPrecioUnit(jsonObject.getString("precio_unit"));
                                setters.setPrecioTotal(jsonObject.getString("precio_total"));

                                String fechaAlta = setters.getFechaAlta() + ", " + setters.getHoraAlta() + " hs.";
                                String fechaModif = setters.getFechaModif() + ", " + setters.getHoraModif() + " hs.";
                                String codigo = setters.getCodigo();
                                String descripcion = setters.getDescripcion();
                                String cantidad = setters.getCantidad();
                                String moneda = setters.getMoneda();
                                String precioUnit = setters.getPrecioUnit();
                                String precioTotal = setters.getPrecioTotal();

                                if (!codigo.equals("No existe")) {

                                    // SI DEVUELVE VALORES LOS MUESTRA EN LOS CAMPOS
                                    EditText editFechaAlta = getView().findViewById(R.id.editFechaAlta);
                                    EditText editFechaModif = getView().findViewById(R.id.editFechaModif);
                                    EditText editCodigo = getView().findViewById(R.id.editCodigo);
                                    EditText editDescripcion = getView().findViewById(R.id.editDescripcion);
                                    EditText editCantidad = getView().findViewById(R.id.editCantidad);
                                    EditText editMoneda = getView().findViewById(R.id.editMoneda);
                                    EditText editPrecioUnit = getView().findViewById(R.id.editPrecioUnit);
                                    EditText editPrecioTotal = getView().findViewById(R.id.editPrecioTotal);

                                    editFechaAlta.setText(fechaAlta);
                                    editFechaModif.setText(fechaModif);
                                    editCodigo.setText(codigo);
                                    editDescripcion.setText(descripcion);
                                    editCantidad.setText(cantidad);
                                    editMoneda.setText(moneda);
                                    editPrecioUnit.setText(precioUnit);
                                    editPrecioTotal.setText(precioTotal);

                                    datoCodigo = codigo + "";

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
                Toast.makeText(getContext(), "Por favor, revise su conexión!", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void consultarDescripcion() {

        // CONSULTA POR DESCRIPCIÓN SI YA FUE INGRESADO ANTERIORMENTE A LA BASE
        String descripcion = datoBuscarDescripcion.replace(" ", "%20");

        String URL = "http://malpicas.heliohost.org/malpica/stock/stock_consultar_descripcion.php?parameter=" + descripcion;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                StockSetters setters = new StockSetters();

                                setters.setFechaAlta(jsonObject.getString("fecha_alta"));
                                setters.setHoraAlta(jsonObject.getString("hora_alta"));
                                setters.setFechaModif(jsonObject.getString("fecha_modif"));
                                setters.setHoraModif(jsonObject.getString("hora_modif"));
                                setters.setCodigo(jsonObject.getString("codigo_stock"));
                                setters.setDescripcion(jsonObject.getString("descripcion_stock"));
                                setters.setCantidad(jsonObject.getString("cantidad"));
                                setters.setMoneda(jsonObject.getString("moneda"));
                                setters.setPrecioUnit(jsonObject.getString("precio_unit"));
                                setters.setPrecioTotal(jsonObject.getString("precio_total"));

                                String fechaAlta = setters.getFechaAlta() + ", " + setters.getHoraAlta() + " hs.";
                                String fechaModif = setters.getFechaModif() + ", " + setters.getHoraModif() + " hs.";
                                String codigo = setters.getCodigo();
                                String descripcion = setters.getDescripcion();
                                String cantidad = setters.getCantidad();
                                String moneda = setters.getMoneda();
                                String precioUnit = setters.getPrecioUnit();
                                String precioTotal = setters.getPrecioTotal();

                                if (!codigo.equals("No existe")) {

                                    // SI DEVUELVE VALORES LOS MUESTRA EN LOS CAMPOS
                                    EditText editFechaAlta = getView().findViewById(R.id.editFechaAlta);
                                    EditText editFechaModif = getView().findViewById(R.id.editFechaModif);
                                    EditText editCodigo = getView().findViewById(R.id.editCodigo);
                                    EditText editDescripcion = getView().findViewById(R.id.editDescripcion);
                                    EditText editCantidad = getView().findViewById(R.id.editCantidad);
                                    EditText editMoneda = getView().findViewById(R.id.editMoneda);
                                    EditText editPrecioUnit = getView().findViewById(R.id.editPrecioUnit);
                                    EditText editPrecioTotal = getView().findViewById(R.id.editPrecioTotal);

                                    editFechaAlta.setText(fechaAlta);
                                    editFechaModif.setText(fechaModif);
                                    editCodigo.setText(codigo);
                                    editDescripcion.setText(descripcion);
                                    editCantidad.setText(cantidad);
                                    editMoneda.setText(moneda);
                                    editPrecioUnit.setText(precioUnit);
                                    editPrecioTotal.setText(precioTotal);

                                    datoCodigo = codigo + "";

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
                Toast.makeText(getContext(), "Por favor, revise su conexión!", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void consultarCompras() {

        // CONSULTA EL PRODUCTO EN LA BASE DE COMPRAS POR SU CÓDIGO
        String URL = "http://malpicas.heliohost.org/malpica/stock/stock_consultar_compras.php?parameter=" + datoCodigo;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y DEVUELVE LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                StockSetters setters = new StockSetters();

                                setters.setCodigo(jsonObject.getString("codigo_stock"));

                                String codigo = setters.getCodigo();

                                if (!codigo.equals("No existe")){

                                    // SI DEVUELVE EL CÓDIGO, MUESTRA UN MENSAJE AVISANDO QUE NO SE ELIMINÓ POR TENER OPERACIONES
                                    dialogErrorOps();

                                } else {

                                    // SI NO DEVUELVE EL CÓDIGO, CONSULTA LAS VENTAS
                                    dialogProcesando();
                                    consultarVentas();
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

    private void consultarVentas() {

        // CONSULTA EL PRODUCTO EN LA BASE DE VENTAS POR SU CÓDIGO
        String URL = "http://malpicas.heliohost.org/malpica/stock/stock_consultar_ventas.php?parameter=" + datoCodigo;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y DEVUELVE LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                StockSetters setters = new StockSetters();

                                setters.setCodigo(jsonObject.getString("codigo_stock"));

                                String codigo = setters.getCodigo();

                                if (!codigo.equals("No existe")){

                                    // SI DEVUELVE EL CÓDIGO, MUESTRA UN MENSAJE AVISANDO QUE NO SE ELIMINÓ POR TENER OPERACIONES
                                    dialogErrorOps();

                                } else {

                                    // SI NO DEVUELVE EL CÓDIGO, CONSULTA LOS GASTOS
                                    dialogProcesando();
                                    consultarGastos();
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

    private void consultarGastos() {

        // CONSULTA EL PRODUCTO EN LA BASE DE GASTOS POR SU CÓDIGO
        String URL = "http://malpicas.heliohost.org/malpica/stock/stock_consultar_gastos.php?parameter=" + datoCodigo;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y DEVUELVE LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                StockSetters setters = new StockSetters();

                                setters.setCodigo(jsonObject.getString("codigo_stock"));

                                String codigo = setters.getCodigo();

                                if (!codigo.equals("No existe")){

                                    // SI DEVUELVE EL CÓDIGO, MUESTRA UN MENSAJE AVISANDO QUE NO SE ELIMINÓ POR TENER OPERACIONES
                                    dialogErrorOps();

                                } else {

                                    // SI NO DEVUELVE EL CÓDIGO, ENTONCES SE ELIMINA EL PRODUCTO
                                    dialogProcesando();
                                    eliminarProducto();
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

    private void eliminarProducto() {

        // ELIMINA AL PRODUCTO DE LA BASE DE DATOS
        String URL = "http://malpicas.heliohost.org/malpica/stock/stock_eliminar_producto.php";
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

                parameter.put("parameter",datoCodigo);

                return parameter;
            }
        };
        requestQueue.add(stringRequest);
    }
}
