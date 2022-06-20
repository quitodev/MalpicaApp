package com.example.malpicasoft.ventas;

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

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class EliminarVentas extends Fragment {

    private RequestQueue requestQueue;
    private String datoFechaActual, datoHoraActual, datoBuscarFactura, datoCodigoStock, datoDia, datoMes, datoAno, nuevoPrecioTotal;
    private int counter, cantidadFactura, nuevaCantidad;
    
    public EliminarVentas() { }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_eliminar_ventas, container, false);

        // LIBRERÍA VOLLEY PARA CONSULTAS A BASES DE DATOS
        requestQueue = Volley.newRequestQueue(getContext());

        // COMPONENTES DE LA VISTA
        final EditText editBuscarFactura = root.findViewById(R.id.editBuscarFactura);
        final EditText editFechaFactura = root.findViewById(R.id.editFechaFactura);
        final EditText editFechaIngreso = root.findViewById(R.id.editFechaIngreso);
        final EditText editFechaModif = root.findViewById(R.id.editFechaModif);
        final EditText editNroFactura = root.findViewById(R.id.editNroFactura);
        final EditText editCodigo = root.findViewById(R.id.editCodigo);
        final EditText editRazonSocial = root.findViewById(R.id.editRazonSocial);
        final EditText editCondicion = root.findViewById(R.id.editCondicion);
        final EditText editCodigoStock = root.findViewById(R.id.editCodigoStock);
        final EditText editDescripcionStock = root.findViewById(R.id.editDescripcionStock);
        final EditText editCantidad = root.findViewById(R.id.editCantidad);
        final EditText editPrecioUnit = root.findViewById(R.id.editPrecioUnit);
        final EditText editImpuestos = root.findViewById(R.id.editImpuestos);
        final EditText editPrecioTotal = root.findViewById(R.id.editPrecioTotal);

        final TextView textBuscarFactura = root.findViewById(R.id.textBuscarFactura);

        final Button buttonConsultar = root.findViewById(R.id.buttonConsultar);
        final Button buttonEliminar = root.findViewById(R.id.buttonEliminar);

        dateFragments();

        // EVENTOS DEL BOTÓN CONSULTAR
        buttonConsultar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        buttonConsultar.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSelected));
                        buttonConsultar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_yellow, 0, 0, 0);

                        datoBuscarFactura = editBuscarFactura.getText().toString();

                        if(!datoBuscarFactura.isEmpty()){

                            // SI SE INGRESÓ UNA FACTURA, CONSULTA A LAS TABLAS
                            dialogProcesando();
                            consultarFactura();

                        } else {

                            // SI NO SE INGRESÓ UNA FACTURA, LIMPIA CAMPOS Y MUESTRA UN ERROR
                            editFechaFactura.setText("");
                            editFechaIngreso.setText("");
                            editFechaModif.setText("");
                            editNroFactura.setText("");
                            editCodigo.setText("");
                            editRazonSocial.setText("");
                            editCondicion.setText("");
                            editCodigoStock.setText("");
                            editDescripcionStock.setText("");
                            editCantidad.setText("");
                            editPrecioUnit.setText("");
                            editImpuestos.setText("");
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

                        if(!codigo.isEmpty()){

                            // MUESTRA UN DIALOG DE CONFIRMACIÓN ANTES DE ELIMINAR LA FACTURA
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
        editBuscarFactura.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, RECUPERA COLOR DE TEXTO
                    textBuscarFactura.setTextColor(ContextCompat.getColor(getContext(), R.color.colorText));

                } else {

                    // SI INGRESA AL CAMPO, CAMBIA COLOR DE TEXTO
                    textBuscarFactura.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSelected));
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

                EditText editFechaFactura = getView().findViewById(R.id.editFechaFactura);
                EditText editFechaIngreso = getView().findViewById(R.id.editFechaIngreso);
                EditText editFechaModif = getView().findViewById(R.id.editFechaModif);
                EditText editNroFactura = getView().findViewById(R.id.editNroFactura);
                EditText editCodigo = getView().findViewById(R.id.editCodigo);
                EditText editRazonSocial = getView().findViewById(R.id.editRazonSocial);
                EditText editCondicion = getView().findViewById(R.id.editCondicion);
                EditText editCodigoStock = getView().findViewById(R.id.editCodigoStock);
                EditText editDescripcionStock = getView().findViewById(R.id.editDescripcionStock);
                EditText editCantidad = getView().findViewById(R.id.editCantidad);
                EditText editPrecioUnit = getView().findViewById(R.id.editPrecioUnit);
                EditText editImpuestos = getView().findViewById(R.id.editImpuestos);
                EditText editPrecioTotal = getView().findViewById(R.id.editPrecioTotal);

                editFechaFactura.setText("");
                editFechaIngreso.setText("");
                editFechaModif.setText("");
                editNroFactura.setText("");
                editCodigo.setText("");
                editRazonSocial.setText("");
                editCondicion.setText("");
                editCodigoStock.setText("");
                editDescripcionStock.setText("");
                editCantidad.setText("");
                editPrecioUnit.setText("");
                editImpuestos.setText("");
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
                        consultarProducto();
                        eliminarFactura();
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

                EditText editFechaFactura = getView().findViewById(R.id.editFechaFactura);
                EditText editFechaIngreso = getView().findViewById(R.id.editFechaIngreso);
                EditText editFechaModif = getView().findViewById(R.id.editFechaModif);
                EditText editNroFactura = getView().findViewById(R.id.editNroFactura);
                EditText editCodigo = getView().findViewById(R.id.editCodigo);
                EditText editRazonSocial = getView().findViewById(R.id.editRazonSocial);
                EditText editCondicion = getView().findViewById(R.id.editCondicion);
                EditText editCodigoStock = getView().findViewById(R.id.editCodigoStock);
                EditText editDescripcionStock = getView().findViewById(R.id.editDescripcionStock);
                EditText editCantidad = getView().findViewById(R.id.editCantidad);
                EditText editPrecioUnit = getView().findViewById(R.id.editPrecioUnit);
                EditText editImpuestos = getView().findViewById(R.id.editImpuestos);
                EditText editPrecioTotal = getView().findViewById(R.id.editPrecioTotal);

                editFechaFactura.setText("");
                editFechaIngreso.setText("");
                editFechaModif.setText("");
                editNroFactura.setText("");
                editCodigo.setText("");
                editRazonSocial.setText("");
                editCondicion.setText("");
                editCodigoStock.setText("");
                editDescripcionStock.setText("");
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
        String URL = "http://malpicas.heliohost.org/malpica/ventas/ventas_consultar_factura.php?parameter=" + datoBuscarFactura;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                VentasSetters setters = new VentasSetters();

                                setters.setFechaFactura(jsonObject.getString("fecha_factura"));
                                setters.setFechaIngreso(jsonObject.getString("fecha_ingreso"));
                                setters.setFechaModif(jsonObject.getString("fecha_modif"));
                                setters.setHoraModif(jsonObject.getString("hora_modif"));
                                setters.setNroFactura(jsonObject.getString("nro_factura"));
                                setters.setCodigo(jsonObject.getString("codigo"));
                                setters.setRazonSocial(jsonObject.getString("razon_social"));
                                setters.setCondicion(jsonObject.getString("condicion"));
                                setters.setCodigoStock(jsonObject.getString("codigo_stock"));
                                setters.setDescripcionStock(jsonObject.getString("descripcion_stock"));
                                setters.setCantidad(jsonObject.getString("cantidad"));
                                setters.setPrecioUnit(jsonObject.getString("precio_unit"));
                                setters.setImpuestos(jsonObject.getString("impuestos"));
                                setters.setPrecioTotal(jsonObject.getString("precio_total"));

                                String fechaFactura = setters.getFechaFactura();
                                String fechaIngreso = setters.getFechaIngreso();
                                String fechaModif = setters.getFechaModif() + ", " + setters.getHoraModif() + " hs.";
                                String nroFactura = setters.getNroFactura();
                                String codigo = setters.getCodigo();
                                String razonSocial = setters.getRazonSocial();
                                String condicion = setters.getCondicion();
                                String codigoStock = setters.getCodigoStock();
                                String descripcionStock = setters.getDescripcionStock();
                                String cantidad = setters.getCantidad();
                                String precioUnit = setters.getPrecioUnit();
                                String impuestos = setters.getImpuestos();
                                String precioTotal = setters.getPrecioTotal();

                                if (!fechaFactura.equals("No existe")) {

                                    // SI DEVUELVE UNA FACTURA MUESTRA LOS DATOS EN LOS CAMPOS
                                    EditText editFechaFactura = getView().findViewById(R.id.editFechaFactura);
                                    EditText editFechaIngreso = getView().findViewById(R.id.editFechaIngreso);
                                    EditText editFechaModif = getView().findViewById(R.id.editFechaModif);
                                    EditText editNroFactura = getView().findViewById(R.id.editNroFactura);
                                    EditText editCodigo = getView().findViewById(R.id.editCodigo);
                                    EditText editRazonSocial = getView().findViewById(R.id.editRazonSocial);
                                    EditText editCondicion = getView().findViewById(R.id.editCondicion);
                                    EditText editCodigoStock = getView().findViewById(R.id.editCodigoStock);
                                    EditText editDescripcionStock = getView().findViewById(R.id.editDescripcionStock);
                                    EditText editCantidad = getView().findViewById(R.id.editCantidad);
                                    EditText editPrecioUnit = getView().findViewById(R.id.editPrecioUnit);
                                    EditText editImpuestos = getView().findViewById(R.id.editImpuestos);
                                    EditText editPrecioTotal = getView().findViewById(R.id.editPrecioTotal);

                                    editFechaFactura.setText(fechaFactura);
                                    editFechaIngreso.setText(fechaIngreso);
                                    editFechaModif.setText(fechaModif);
                                    editNroFactura.setText(nroFactura);
                                    editCodigo.setText(codigo);
                                    editRazonSocial.setText(razonSocial);
                                    editCondicion.setText(condicion);
                                    editCodigoStock.setText(codigoStock);
                                    editDescripcionStock.setText(descripcionStock);
                                    editCantidad.setText(cantidad);
                                    editPrecioUnit.setText(precioUnit);
                                    editImpuestos.setText(impuestos);
                                    editPrecioTotal.setText(precioTotal);

                                    datoCodigoStock = codigo + "";
                                    cantidadFactura = Integer.parseInt(cantidad);

                                    if (fechaFactura.contains("/2") && fechaIngreso.contains("/2") && fechaModif.contains("/2")) {

                                        // REEMPLAZO DE FORMATO DE FECHA
                                        String fechaFactura1 = fechaFactura.replace("/2","/202");
                                        String fechaIngreso1 = fechaIngreso.replace("/2","/202");
                                        String fechaModif1 = fechaModif.replace("/2","/202");

                                        editFechaFactura.setText(fechaFactura1);
                                        editFechaIngreso.setText(fechaIngreso1);
                                        editFechaModif.setText(fechaModif1);
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
                Toast.makeText(getContext(), "Por favor, revise su conexión!", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void consultarProducto() {

        // CONSULTA EL PRODUCTO EN LA BASE DE DATOS
        String URL = "http://malpicas.heliohost.org/malpica/ventas/ventas_consultar_producto.php?parameter=" + datoCodigoStock;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y ACTUALIZA EL STOCK
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                VentasSetters setters = new VentasSetters();

                                setters.setCodigoStock(jsonObject.getString("codigo_stock"));
                                setters.setDescripcionStock(jsonObject.getString("descripcion_stock"));
                                setters.setCantidad(jsonObject.getString("cantidad"));
                                setters.setMoneda(jsonObject.getString("moneda"));
                                setters.setPrecioUnit(jsonObject.getString("precio_unit"));
                                setters.setPrecioTotal(jsonObject.getString("precio_total"));

                                String cantidadStock = setters.getCantidad();
                                String precioUnitStock = setters.getPrecioUnit();

                                int cantidadGetter = Integer.parseInt(cantidadStock);
                                double precioUnit = Double.parseDouble(precioUnitStock);
                                nuevaCantidad = cantidadGetter + cantidadFactura;
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
                Toast.makeText(getContext(), "Por favor, revise su conexión!", Toast.LENGTH_SHORT).show();
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
                Map<String, String> parameter = new HashMap<>();

                parameter.put("codigo_stock",datoCodigoStock);
                parameter.put("cantidad",datoCantidad);
                parameter.put("precio_total",datoPrecioTotal);
                parameter.put("fecha_modif",datoFechaActual);
                parameter.put("hora_modif",datoHoraActual);
                parameter.put("dia_modif",datoDia);
                parameter.put("mes_modif",datoMes);
                parameter.put("ano_modif",datoAno);

                return parameter;
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
                Toast.makeText(getContext(),error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameter = new HashMap<>();

                parameter.put("parameter",datoBuscarFactura);

                return parameter;
            }
        };
        requestQueue.add(stringRequest1);
    }
}
