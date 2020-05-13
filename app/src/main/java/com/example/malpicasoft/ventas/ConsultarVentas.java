package com.example.malpicasoft.ventas;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class ConsultarVentas extends Fragment {

    private RequestQueue requestQueue;
    private String datoBuscarFactura;
    private int counter;

    public ConsultarVentas() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_consultar_ventas, container, false);

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
            }
        }, 2000);
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
}
