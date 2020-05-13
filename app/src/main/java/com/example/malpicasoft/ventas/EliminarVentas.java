package com.example.malpicasoft.ventas;

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

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class EliminarVentas extends Fragment {

    private RequestQueue requestQueue;
    private String datoBuscarFactura, datoCodigo, datoFechaActual, datoDia, datoMes, datoAno, nuevoPrecioTotal;
    private int counter, cantidadFactura, nuevaCantidad;
    
    public EliminarVentas() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_eliminar_ventas, container, false);

        // LIBRERÍA VOLLEY PARA CONSULTAS A BASES DE DATOS
        requestQueue = Volley.newRequestQueue(getContext());

        // COMPONENTES DE LA VISTA
        final EditText editBuscarFactura = root.findViewById(R.id.editBuscarFactura);
        final EditText editFechaVenta = root.findViewById(R.id.editFechaVenta);
        final EditText editFechaIngresada = root.findViewById(R.id.editFechaIngresada);
        final EditText editCodigoCliente = root.findViewById(R.id.editCodigoCliente);
        final EditText editRazonSocialCliente = root.findViewById(R.id.editRazonSocialCliente);
        final EditText editCondicionCliente = root.findViewById(R.id.editCondicionCliente);
        final EditText editCodigoProd = root.findViewById(R.id.editCodigoProd);
        final EditText editDescripcionProd = root.findViewById(R.id.editDescripcionProd);
        final EditText editCantidad = root.findViewById(R.id.editCantidad);
        final EditText editPrecioUnit = root.findViewById(R.id.editPrecioUnit);
        final EditText editImpuestos = root.findViewById(R.id.editImpuestos);
        final EditText editPrecioTotal = root.findViewById(R.id.editPrecioTotal);

        Button buttonConsultar = root.findViewById(R.id.buttonConsultar);
        Button buttonEliminar = root.findViewById(R.id.buttonEliminar);

        dateFragments();

        // EVENTOS DEL BOTÓN CONSULTAR
        buttonConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datoBuscarFactura = editBuscarFactura.getText().toString();
                editBuscarFactura.clearFocus();

                if(!datoBuscarFactura.isEmpty()){

                    // SI SE INGRESÓ UNA FACTURA, CONSULTA A LAS TABLAS
                    dialogProcesando();
                    consultarFactura();

                } else {

                    // SI NO SE INGRESÓ UNA FACTURA, LIMPIA CAMPOS Y MUESTRA UN ERROR
                    editFechaVenta.setText("");
                    editFechaIngresada.setText("");
                    editCodigoCliente.setText("");
                    editRazonSocialCliente.setText("");
                    editCondicionCliente.setText("");
                    editCodigoProd.setText("");
                    editDescripcionProd.setText("");
                    editCantidad.setText("");
                    editPrecioUnit.setText("");
                    editImpuestos.setText("");
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

                datoBuscarFactura = editBuscarFactura.getText().toString();
                editBuscarFactura.clearFocus();

                if(!datoBuscarFactura.isEmpty()){

                    // MUESTRA UN DIALOG DE CONFIRMACIÓN ANTES DE ELIMINAR LA FACTURA
                    dialogEliminar();
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

                EditText editFechaVenta = getView().findViewById(R.id.editFechaVenta);
                EditText editFechaIngresada = getView().findViewById(R.id.editFechaIngresada);
                EditText editCodigoCliente = getView().findViewById(R.id.editCodigoCliente);
                EditText editRazonSocialCliente = getView().findViewById(R.id.editRazonSocialCliente);
                EditText editCondicionCliente = getView().findViewById(R.id.editCondicionCliente);
                EditText editCodigoProd = getView().findViewById(R.id.editCodigoProd);
                EditText editDescripcionProd = getView().findViewById(R.id.editDescripcionProd);
                EditText editCantidad = getView().findViewById(R.id.editCantidad);
                EditText editPrecioUnit = getView().findViewById(R.id.editPrecioUnit);
                EditText editImpuestos = getView().findViewById(R.id.editImpuestos);
                EditText editPrecioTotal = getView().findViewById(R.id.editPrecioTotal);

                editFechaVenta.setText("");
                editFechaIngresada.setText("");
                editCodigoCliente.setText("");
                editRazonSocialCliente.setText("");
                editCondicionCliente.setText("");
                editCodigoProd.setText("");
                editDescripcionProd.setText("");
                editCantidad.setText("");
                editPrecioUnit.setText("");
                editImpuestos.setText("");
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
                consultarProducto();
                eliminarFactura();
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

                EditText editFechaVenta = getView().findViewById(R.id.editFechaVenta);
                EditText editFechaIngresada = getView().findViewById(R.id.editFechaIngresada);
                EditText editCodigoCliente = getView().findViewById(R.id.editCodigoCliente);
                EditText editRazonSocialCliente = getView().findViewById(R.id.editRazonSocialCliente);
                EditText editCondicionCliente = getView().findViewById(R.id.editCondicionCliente);
                EditText editCodigoProd = getView().findViewById(R.id.editCodigoProd);
                EditText editDescripcionProd = getView().findViewById(R.id.editDescripcionProd);
                EditText editCantidad = getView().findViewById(R.id.editCantidad);
                EditText editPrecioUnit = getView().findViewById(R.id.editPrecioUnit);
                EditText editImpuestos = getView().findViewById(R.id.editImpuestos);
                EditText editPrecioTotal = getView().findViewById(R.id.editPrecioTotal);

                editFechaVenta.setText("");
                editFechaIngresada.setText("");
                editCodigoCliente.setText("");
                editRazonSocialCliente.setText("");
                editCondicionCliente.setText("");
                editCodigoProd.setText("");
                editDescripcionProd.setText("");
                editCantidad.setText("");
                editPrecioUnit.setText("");
                editImpuestos.setText("");
                editPrecioTotal.setText("");

                ScrollView scrollView = getView().findViewById(R.id.scroll);
                scrollView.setScrollY(0);
            }
        }, 3000);
    }

    private void consultarFactura() {

        // CONSULTA LA FACTURA INGRESADA EN LA BASE DE DATOS
        String URL = "http://malpicas.heliohost.org/malpica/ventas/ventas_buscar_factura.php?factura=" + datoBuscarFactura;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("factura");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                VentasSetters ventasSetters = new VentasSetters();

                                ventasSetters.setFechaVenta(jsonObject.getString("fecha_venta"));
                                ventasSetters.setFechaIngreso(jsonObject.getString("fecha_ingreso"));
                                ventasSetters.setCodigoCliente(jsonObject.getString("codigo_cliente"));
                                ventasSetters.setNombreCliente(jsonObject.getString("nombre_cliente"));
                                ventasSetters.setCondicionCliente(jsonObject.getString("condicion_cliente"));
                                ventasSetters.setCodigoProd(jsonObject.getString("codigo"));
                                ventasSetters.setDescripcionProd(jsonObject.getString("descripcion"));
                                ventasSetters.setCantidadProd(jsonObject.getString("cantidad"));
                                ventasSetters.setPrecioUnitProd(jsonObject.getString("precio_unit"));
                                ventasSetters.setImpuestos(jsonObject.getString("impuestos"));
                                ventasSetters.setPrecioTotalProd(jsonObject.getString("precio_final"));

                                String fechaVenta = ventasSetters.getFechaVenta();
                                String fechaIngreso = ventasSetters.getFechaIngreso();
                                String codigoCliente = ventasSetters.getCodigoCliente();
                                String nombreCliente = ventasSetters.getNombreCliente();
                                String condicionCliente = ventasSetters.getCondicionCliente();
                                String codigoProd = ventasSetters.getCodigoProd();
                                String descripcionProd = ventasSetters.getDescripcionProd();
                                String cantidadProd = ventasSetters.getCantidadProd();
                                String precioUnitProd = ventasSetters.getPrecioUnitProd();
                                String impuestos = ventasSetters.getImpuestos();
                                String precioTotalProd = ventasSetters.getPrecioTotalProd();

                                if (!fechaVenta.equals("No existe")) {

                                    // SI DEVUELVE UNA FACTURA MUESTRA LOS DATOS EN LOS CAMPOS
                                    EditText editFechaVenta = getView().findViewById(R.id.editFechaVenta);
                                    EditText editFechaIngresada = getView().findViewById(R.id.editFechaIngresada);
                                    EditText editCodigoCliente = getView().findViewById(R.id.editCodigoCliente);
                                    EditText editRazonSocialCliente = getView().findViewById(R.id.editRazonSocialCliente);
                                    EditText editCondicionCliente = getView().findViewById(R.id.editCondicionCliente);
                                    EditText editCodigoProd = getView().findViewById(R.id.editCodigoProd);
                                    EditText editDescripcionProd = getView().findViewById(R.id.editDescripcionProd);
                                    EditText editCantidad = getView().findViewById(R.id.editCantidad);
                                    EditText editPrecioUnit = getView().findViewById(R.id.editPrecioUnit);
                                    EditText editImpuestos = getView().findViewById(R.id.editImpuestos);
                                    EditText editPrecioTotal = getView().findViewById(R.id.editPrecioTotal);

                                    editFechaVenta.setText(fechaVenta);
                                    editFechaIngresada.setText(fechaIngreso);
                                    editCodigoCliente.setText(codigoCliente);
                                    editRazonSocialCliente.setText(nombreCliente);
                                    editCondicionCliente.setText(condicionCliente);
                                    editCodigoProd.setText(codigoProd);
                                    editDescripcionProd.setText(descripcionProd);
                                    editCantidad.setText(cantidadProd);
                                    editPrecioUnit.setText(precioUnitProd);
                                    editImpuestos.setText(impuestos);
                                    editPrecioTotal.setText(precioTotalProd);

                                    datoCodigo = codigoProd + "";
                                    cantidadFactura = Integer.parseInt(cantidadProd);

                                    if (fechaVenta.contains("/2") && fechaIngreso.contains("/2")) {

                                        // REEMPLAZO DE FORMATO DE FECHA
                                        String fechaVenta1 = fechaVenta.replace("/2","/202");
                                        String fechaIngreso1 = fechaIngreso.replace("/2","/202");

                                        editFechaVenta.setText(fechaVenta1);
                                        editFechaIngresada.setText(fechaIngreso1);
                                    }

                                } else {

                                    // SI NO DEVUELVE UNA FACTURA MUESTRA UN MENSAJE DE ERROR
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

    private void consultarProducto() {

        // CONSULTA EL PRODUCTO EN LA BASE DE DATOS
        String URL = "http://malpicas.heliohost.org/malpica/ventas/ventas_consultar_producto.php?codigo=" + datoCodigo;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("stock");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y ACTUALIZA EL STOCK
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                VentasSetters ventasSetters = new VentasSetters();

                                ventasSetters.setCodigoProd(jsonObject.getString("codigo"));
                                ventasSetters.setDescripcionProd(jsonObject.getString("descripcion"));
                                ventasSetters.setCantidadProd(jsonObject.getString("cantidad"));
                                ventasSetters.setMonedaProd(jsonObject.getString("moneda"));
                                ventasSetters.setPrecioUnitProd(jsonObject.getString("precio_unit"));
                                ventasSetters.setPrecioTotalProd(jsonObject.getString("precio_total"));

                                String cantidadProd = ventasSetters.getCantidadProd();
                                String precioUnitProd = ventasSetters.getPrecioUnitProd();

                                int cantidadGetter = Integer.parseInt(cantidadProd);
                                double precioUnit = Double.parseDouble(precioUnitProd);
                                nuevaCantidad = cantidadGetter - cantidadFactura;
                                double preciototal = nuevaCantidad * precioUnit;

                                DecimalFormat decimalFormat = new DecimalFormat("#.00");
                                nuevoPrecioTotal = decimalFormat.format(preciototal).replace(",",".");

                                if (preciototal == 0){
                                    nuevoPrecioTotal = "0.00";
                                }

                                actualizarProducto();
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

    private void actualizarProducto() {

        // ACTUALIZA EL PRODUCTO EN LA TABLA DE STOCK
        final String datoCantidad = nuevaCantidad + "";
        final String datoPrecioTotal = nuevoPrecioTotal + "";

        String URL = "http://malpicas.heliohost.org/malpica/ventas/ventas_actualizar_producto.php";
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
                Map<String, String> datosStock = new HashMap<>();

                datosStock.put("codigo",datoCodigo);
                datosStock.put("cantidad",datoCantidad);
                datosStock.put("precio_total",datoPrecioTotal);
                datosStock.put("fecha_modif",datoFechaActual);
                datosStock.put("dia_modif",datoDia);
                datosStock.put("mes_modif",datoMes);
                datosStock.put("ano_modif",datoAno);

                return datosStock;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void eliminarFactura() {

        // ELIMINA LA FACTURA DE LA BASE DE DATOS
        String URL1 = "http://malpicas.heliohost.org/malpica/ventas/ventas_eliminar_factura.php";
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL1, new Response.Listener<String>() {
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
                Map<String, String> datosFactura = new HashMap<>();

                datosFactura.put("factura",datoBuscarFactura);

                return datosFactura;
            }
        };
        requestQueue.add(stringRequest1);
    }
}
