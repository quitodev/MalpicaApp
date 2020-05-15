package com.example.malpicasoft.stock;

import android.app.AlertDialog;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class EliminarStock extends Fragment {

    private RequestQueue requestQueue;
    private String datoBuscarCodigo, datoBuscarDescripcion, datoCodigo;
    private int counter;

    public EliminarStock() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_eliminar_stock, container, false);

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
        Button buttonEliminar = root.findViewById(R.id.buttonEliminar);

        // EVENTOS DEL BOTÓN CONSULTAR
        buttonConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

        // EVENTOS DEL BOTÓN ELIMINAR
        buttonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // SI LOS CAMPOS NO ESTÁN VACÍOS, REALIZA LA CONSULTA CORRESPONDIENTE
                if (!datoBuscarCodigo.isEmpty()) {

                    // MUESTRA UN DIALOG DE CONFIRMACIÓN ANTES DE ELIMINARLO
                    dialogEliminar();
                }

                if (!datoBuscarDescripcion.isEmpty()) {

                    // MUESTRA UN DIALOG DE CONFIRMACIÓN ANTES DE ELIMINARLO
                    dialogEliminar();
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
                dialogs.startError();

                EditText editCodigo = getView().findViewById(R.id.editCodigo);
                EditText editDescripcion = getView().findViewById(R.id.editDescripcion);
                EditText editCantidad = getView().findViewById(R.id.editCantidad);
                EditText editMoneda = getView().findViewById(R.id.editMoneda);
                EditText editPrecioUnit = getView().findViewById(R.id.editPrecioUnit);
                EditText editPrecioTotal = getView().findViewById(R.id.editPrecioTotal);

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

        // DIALOG CON MENSAJE DE ERROR
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Dialogs dialogs = new Dialogs(getActivity());
                dialogs.startErrorOps();

                EditText editCodigo = getView().findViewById(R.id.editCodigo);
                EditText editDescripcion = getView().findViewById(R.id.editDescripcion);
                EditText editCantidad = getView().findViewById(R.id.editCantidad);
                EditText editMoneda = getView().findViewById(R.id.editMoneda);
                EditText editPrecioUnit = getView().findViewById(R.id.editPrecioUnit);
                EditText editPrecioTotal = getView().findViewById(R.id.editPrecioTotal);

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

    private void dialogEliminar(){

        // DIALOG CON CONFIRMACIÓN DE ELIMINACIÓN
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_eliminar,null);
        builder.setView(view);
        builder.setCancelable(true);

        Button buttonContinuar = view.findViewById(R.id.buttonContinuar);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        buttonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                dialogProcesando();
                consultarCompras();
            }
        });
    }

    private void dialogOk(){

        // DIALOG CON CONFIRMACIÓN DE OPERACIÓN REALIZADA CON ÉXITO
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Dialogs dialogs = new Dialogs(getActivity());
                dialogs.startOk();

                EditText editCodigo = getView().findViewById(R.id.editCodigo);
                EditText editDescripcion = getView().findViewById(R.id.editDescripcion);
                EditText editCantidad = getView().findViewById(R.id.editCantidad);
                EditText editMoneda = getView().findViewById(R.id.editMoneda);
                EditText editPrecioUnit = getView().findViewById(R.id.editPrecioUnit);
                EditText editPrecioTotal = getView().findViewById(R.id.editPrecioTotal);

                editCodigo.setText("");
                editDescripcion.setText("");
                editCantidad.setText("");
                editMoneda.setText("");
                editPrecioUnit.setText("");
                editPrecioTotal.setText("");

                ScrollView scrollView = getView().findViewById(R.id.scroll);
                scrollView.setScrollY(0);
            }
        }, 1000);
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
                Toast.makeText(getContext(), "Por favor, revise su conexión!", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void consultarCompras() {

        // CONSULTA EL PRODUCTO EN LA BASE DE COMPRAS POR SU CÓDIGO
        String URL = "http://malpicas.heliohost.org/malpica/stock/stock_consultar_compras.php?codigo=" + datoCodigo;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("datos");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y DEVUELVE LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                StockSetters stockSetters = new StockSetters();

                                stockSetters.setCodigo(jsonObject.getString("codigo"));

                                String codigo = stockSetters.getCodigo();

                                if (!codigo.equals("No existe")){

                                    // SI DEVUELVE EL CÓDIGO, MUESTRA UN MENSAJE AVISANDO QUE NO SE ELIMINÓ POR TENER OPERACIONES
                                    dialogErrorOps();

                                } else {

                                    // SI NO DEVUELVE EL CÓDIGO, CONSULTA LAS VENTAS
                                    consultarVentas();
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

    private void consultarVentas() {

        // CONSULTA EL PRODUCTO EN LA BASE DE VENTAS POR SU CÓDIGO
        String URL = "http://malpicas.heliohost.org/malpica/stock/stock_consultar_ventas.php?codigo=" + datoCodigo;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("datos");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y DEVUELVE LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                StockSetters stockSetters = new StockSetters();

                                stockSetters.setCodigo(jsonObject.getString("codigo"));

                                String codigo = stockSetters.getCodigo();

                                if (!codigo.equals("No existe")){

                                    // SI DEVUELVE EL CÓDIGO, MUESTRA UN MENSAJE AVISANDO QUE NO SE ELIMINÓ POR TENER OPERACIONES
                                    dialogErrorOps();

                                } else {

                                    // SI NO DEVUELVE EL CÓDIGO, ENTONCES SE ELIMINA EL PRODUCTO
                                    eliminarProducto();
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
                dialogError();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();

                parametros.put("codigo",datoCodigo);

                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }
}
