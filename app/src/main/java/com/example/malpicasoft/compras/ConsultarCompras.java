package com.example.malpicasoft.compras;

import android.annotation.SuppressLint;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.malpicasoft.Dialogs;
import com.example.malpicasoft.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class ConsultarCompras extends Fragment {

    private RequestQueue requestQueue;
    private String datoBuscarFactura;
    private int counter;

    public ConsultarCompras() { }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_consultar_compras, container, false);

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
            }
        }, 3000);
    }

    private void consultarFactura() {

        // CONSULTA LA FACTURA INGRESADA EN LA BASE DE DATOS
        String URL = "http://malpicas.heliohost.org/malpica/compras/compras_consultar_factura.php?parameter=" + datoBuscarFactura;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ComprasSetters setters = new ComprasSetters();

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
}
