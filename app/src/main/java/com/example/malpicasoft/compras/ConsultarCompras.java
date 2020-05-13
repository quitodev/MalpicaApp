package com.example.malpicasoft.compras;

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

public class ConsultarCompras extends Fragment {

    private RequestQueue requestQueue;
    private String datoBuscarFactura;
    private int counter;

    public ConsultarCompras() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_consultar_compras, container, false);

        // LIBRERÍA VOLLEY PARA CONSULTAS A BASES DE DATOS
        requestQueue = Volley.newRequestQueue(getContext());

        // COMPONENTES DE LA VISTA
        final EditText editBuscarFactura = root.findViewById(R.id.editBuscarFactura);
        final EditText editFechaCompra = root.findViewById(R.id.editFechaCompra);
        final EditText editFechaIngresada = root.findViewById(R.id.editFechaIngresada);
        final EditText editCodigoProv = root.findViewById(R.id.editCodigoProv);
        final EditText editRazonSocialProv = root.findViewById(R.id.editRazonSocialProv);
        final EditText editCondicionProv = root.findViewById(R.id.editCondicionProv);
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
                    editFechaCompra.setText("");
                    editFechaIngresada.setText("");
                    editCodigoProv.setText("");
                    editRazonSocialProv.setText("");
                    editCondicionProv.setText("");
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

                EditText editFechaCompra = getView().findViewById(R.id.editFechaCompra);
                EditText editFechaIngresada = getView().findViewById(R.id.editFechaIngresada);
                EditText editCodigoProv = getView().findViewById(R.id.editCodigoProv);
                EditText editRazonSocialProv = getView().findViewById(R.id.editRazonSocialProv);
                EditText editCondicionProv = getView().findViewById(R.id.editCondicionProv);
                EditText editCodigoProd = getView().findViewById(R.id.editCodigoProd);
                EditText editDescripcionProd = getView().findViewById(R.id.editDescripcionProd);
                EditText editCantidad = getView().findViewById(R.id.editCantidad);
                EditText editPrecioUnit = getView().findViewById(R.id.editPrecioUnit);
                EditText editImpuestos = getView().findViewById(R.id.editImpuestos);
                EditText editPrecioTotal = getView().findViewById(R.id.editPrecioTotal);

                editFechaCompra.setText("");
                editFechaIngresada.setText("");
                editCodigoProv.setText("");
                editRazonSocialProv.setText("");
                editCondicionProv.setText("");
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
        String URL = "http://malpicas.heliohost.org/malpica/compras/compras_buscar_factura.php?factura=" + datoBuscarFactura;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("factura");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ComprasSetters comprasSetters = new ComprasSetters();

                                comprasSetters.setFechaCompra(jsonObject.getString("fecha_compra"));
                                comprasSetters.setFechaIngreso(jsonObject.getString("fecha_ingreso"));
                                comprasSetters.setCodigoProv(jsonObject.getString("codigo_prov"));
                                comprasSetters.setNombreProv(jsonObject.getString("nombre_prov"));
                                comprasSetters.setCondicionProv(jsonObject.getString("condicion_prov"));
                                comprasSetters.setCodigoProd(jsonObject.getString("codigo"));
                                comprasSetters.setDescripcionProd(jsonObject.getString("descripcion"));
                                comprasSetters.setCantidadProd(jsonObject.getString("cantidad"));
                                comprasSetters.setPrecioUnitProd(jsonObject.getString("precio_unit"));
                                comprasSetters.setImpuestos(jsonObject.getString("impuestos"));
                                comprasSetters.setPrecioTotalProd(jsonObject.getString("precio_final"));

                                String fechaCompra = comprasSetters.getFechaCompra();
                                String fechaIngreso = comprasSetters.getFechaIngreso();
                                String codigoProv = comprasSetters.getCodigoProv();
                                String nombreProv = comprasSetters.getNombreProv();
                                String condicionProv = comprasSetters.getCondicionProv();
                                String codigoProd = comprasSetters.getCodigoProd();
                                String descripcionProd = comprasSetters.getDescripcionProd();
                                String cantidadProd = comprasSetters.getCantidadProd();
                                String precioUnitProd = comprasSetters.getPrecioUnitProd();
                                String impuestos = comprasSetters.getImpuestos();
                                String precioTotalProd = comprasSetters.getPrecioTotalProd();

                                if (!fechaCompra.equals("No existe")) {

                                    // SI DEVUELVE UNA FACTURA MUESTRA LOS DATOS EN LOS CAMPOS
                                    EditText editFechaCompra = getView().findViewById(R.id.editFechaCompra);
                                    EditText editFechaIngresada = getView().findViewById(R.id.editFechaIngresada);
                                    EditText editCodigoProv = getView().findViewById(R.id.editCodigoProv);
                                    EditText editRazonSocialProv = getView().findViewById(R.id.editRazonSocialProv);
                                    EditText editCondicionProv = getView().findViewById(R.id.editCondicionProv);
                                    EditText editCodigoProd = getView().findViewById(R.id.editCodigoProd);
                                    EditText editDescripcionProd = getView().findViewById(R.id.editDescripcionProd);
                                    EditText editCantidad = getView().findViewById(R.id.editCantidad);
                                    EditText editPrecioUnit = getView().findViewById(R.id.editPrecioUnit);
                                    EditText editImpuestos = getView().findViewById(R.id.editImpuestos);
                                    EditText editPrecioTotal = getView().findViewById(R.id.editPrecioTotal);

                                    editFechaCompra.setText(fechaCompra);
                                    editFechaIngresada.setText(fechaIngreso);
                                    editCodigoProv.setText(codigoProv);
                                    editRazonSocialProv.setText(nombreProv);
                                    editCondicionProv.setText(condicionProv);
                                    editCodigoProd.setText(codigoProd);
                                    editDescripcionProd.setText(descripcionProd);
                                    editCantidad.setText(cantidadProd);
                                    editPrecioUnit.setText(precioUnitProd);
                                    editImpuestos.setText(impuestos);
                                    editPrecioTotal.setText(precioTotalProd);

                                    if (fechaCompra.contains("/2") && fechaIngreso.contains("/2")) {

                                        // REEMPLAZO DE FORMATO DE FECHA
                                        String fechaCompra1 = fechaCompra.replace("/2","/202");
                                        String fechaIngreso1 = fechaIngreso.replace("/2","/202");

                                        editFechaCompra.setText(fechaCompra1);
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
