package com.example.malpicasoft.proveedores;

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

public class ConsultarProveedores extends Fragment {

    private RequestQueue requestQueue;
    private String datoBuscarCodigo, datoBuscarRazon, datoBuscarCuit;
    private int counter;

    public ConsultarProveedores() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_consultar_proveedores, container, false);

        // LIBRERÍA VOLLEY PARA CONSULTAS A BASES DE DATOS
        requestQueue = Volley.newRequestQueue(getContext());

        // COMPONENTES DE LA VISTA
        final EditText editBuscarCodigo = root.findViewById(R.id.editBuscarCodigo);
        final EditText editBuscarRazon = root.findViewById(R.id.editBuscarRazon);
        final EditText editBuscarCuit = root.findViewById(R.id.editBuscarCuit);
        final EditText editCodigo = root.findViewById(R.id.editCodigo);
        final EditText editRazonSocial = root.findViewById(R.id.editRazonSocial);
        final EditText editCondicion = root.findViewById(R.id.editCondicion);
        final EditText editDescripcion = root.findViewById(R.id.editDescripcion);
        final EditText editCuit = root.findViewById(R.id.editCuit);
        final EditText editDireccion = root.findViewById(R.id.editDireccion);
        final EditText editLocalidad = root.findViewById(R.id.editLocalidad);
        final EditText editContacto = root.findViewById(R.id.editContacto);
        final EditText editTipo = root.findViewById(R.id.editTipo);

        Button buttonConsultar = root.findViewById(R.id.buttonConsultar);

        // EVENTOS DEL BOTÓN CONSULTAR
        buttonConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datoBuscarCodigo = editBuscarCodigo.getText().toString();
                datoBuscarRazon = editBuscarRazon.getText().toString();
                datoBuscarCuit = editBuscarCuit.getText().toString();

                // SI LOS CAMPOS NO ESTÁN VACÍOS, REALIZA LA CONSULTA CORRESPONDIENTE
                if (!datoBuscarCodigo.isEmpty()) {

                    dialogProcesando();
                    consultarCodigo();
                }

                if (!datoBuscarRazon.isEmpty()) {

                    dialogProcesando();
                    consultarNombre();
                }

                if (!datoBuscarCuit.isEmpty()) {

                    dialogProcesando();
                    consultarCuit();
                }

                if (datoBuscarCodigo.isEmpty() && datoBuscarRazon.isEmpty() && datoBuscarCuit.isEmpty()) {

                    // SI LOS CAMPOS ESTÁN VACÍOS, SE LIMPIAN LOS RESULTADOS ANTERIORES Y MUESTRA UN ERROR
                    editCodigo.setText("");
                    editRazonSocial.setText("");
                    editCondicion.setText("");
                    editDescripcion.setText("");
                    editCuit.setText("");
                    editDireccion.setText("");
                    editLocalidad.setText("");
                    editContacto.setText("");
                    editTipo.setText("");

                    dialogProcesando();
                    dialogError();
                }
            }
        });

        // EVENTOS AL CAMBIAR DE CAMPOS
        editBuscarCodigo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {

                    // SI INGRESA AL CAMPO, ELIMINA LOS DATOS INGRESADOS EN LOS OTROS CAMPOS
                    editBuscarRazon.setText("");
                    editBuscarCuit.setText("");
                }
            }
        });
        editBuscarRazon.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {

                    // SI INGRESA AL CAMPO, ELIMINA LOS DATOS INGRESADOS EN LOS OTROS CAMPOS
                    editBuscarCodigo.setText("");
                    editBuscarCuit.setText("");
                }
            }
        });
        editBuscarCuit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {

                    // SI INGRESA AL CAMPO, ELIMINA LOS DATOS INGRESADOS EN LOS OTROS CAMPOS
                    editBuscarCodigo.setText("");
                    editBuscarRazon.setText("");
                }
            }
        });

        return root;
    }

    private void dialogProcesando() {

        // DIALOG CON PROGRESS BAR MIENTRAS CONSULTA A LAS TABLAS
        final Dialogs dialogs = new Dialogs(getActivity());
        dialogs.startProcesando();

        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                counter++;

                if (counter == 20) {
                    timer.cancel();
                    dialogs.endProcesando();
                    counter = 0;
                }
            }
        };
        timer.schedule(timerTask, 0, 100);
    }

    private void dialogError() {

        // DIALOG CON MENSAJE DE ERROR
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Dialogs dialogs = new Dialogs(getActivity());
                dialogs.startError();

                EditText editCodigo = getView().findViewById(R.id.editCodigo);
                EditText editRazonSocial = getView().findViewById(R.id.editRazonSocial);
                EditText editCondicion = getView().findViewById(R.id.editCondicion);
                EditText editDescripcion = getView().findViewById(R.id.editDescripcion);
                EditText editCuit = getView().findViewById(R.id.editCuit);
                EditText editDireccion = getView().findViewById(R.id.editDireccion);
                EditText editLocalidad = getView().findViewById(R.id.editLocalidad);
                EditText editContacto = getView().findViewById(R.id.editContacto);
                EditText editTipo = getView().findViewById(R.id.editTipo);

                editCodigo.setText("");
                editRazonSocial.setText("");
                editCondicion.setText("");
                editDescripcion.setText("");
                editCuit.setText("");
                editDireccion.setText("");
                editLocalidad.setText("");
                editContacto.setText("");
                editTipo.setText("");
            }
        }, 2000);
    }

    private void consultarCodigo() {

        // CONSULTA POR CÓDIGO SI YA FUE INGRESADO ANTERIORMENTE A LA BASE
        String URL = "http://malpicas.heliohost.org/malpica/proveedores/proveedores_consultar_codigo.php?codigo=" + datoBuscarCodigo;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("datos");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ProveedoresSetters proveedoresSetters = new ProveedoresSetters();

                                proveedoresSetters.setCodigo(jsonObject.getString("codigo_prov"));
                                proveedoresSetters.setRazonSocial(jsonObject.getString("nombre_prov"));
                                proveedoresSetters.setCondicion(jsonObject.getString("condicion_prov"));
                                proveedoresSetters.setDescripcion(jsonObject.getString("descripcion"));
                                proveedoresSetters.setCuit(jsonObject.getString("cuit_cuil"));
                                proveedoresSetters.setDireccion(jsonObject.getString("direccion"));
                                proveedoresSetters.setLocalidad(jsonObject.getString("localidad"));
                                proveedoresSetters.setContacto(jsonObject.getString("contacto"));
                                proveedoresSetters.setTipo(jsonObject.getString("tipo_prov"));

                                String codigo = proveedoresSetters.getCodigo();
                                String nombre = proveedoresSetters.getRazonSocial();
                                String condicion = proveedoresSetters.getCondicion();
                                String descripcion = proveedoresSetters.getDescripcion();
                                String cuit = proveedoresSetters.getCuit();
                                String direccion = proveedoresSetters.getDireccion();
                                String localidad = proveedoresSetters.getLocalidad();
                                String contacto = proveedoresSetters.getContacto();
                                String tipo = proveedoresSetters.getTipo();

                                if (!codigo.equals("No existe")) {

                                    // SI DEVUELVE VALORES LOS MUESTRA EN LOS CAMPOS
                                    EditText editCodigo = getView().findViewById(R.id.editCodigo);
                                    EditText editRazonSocial = getView().findViewById(R.id.editRazonSocial);
                                    EditText editCondicion = getView().findViewById(R.id.editCondicion);
                                    EditText editDescripcion = getView().findViewById(R.id.editDescripcion);
                                    EditText editCuit = getView().findViewById(R.id.editCuit);
                                    EditText editDireccion = getView().findViewById(R.id.editDireccion);
                                    EditText editLocalidad = getView().findViewById(R.id.editLocalidad);
                                    EditText editContacto = getView().findViewById(R.id.editContacto);
                                    EditText editTipo = getView().findViewById(R.id.editTipo);

                                    editCodigo.setText(codigo);
                                    editRazonSocial.setText(nombre);
                                    editCondicion.setText(condicion);
                                    editDescripcion.setText(descripcion);
                                    editCuit.setText(cuit);
                                    editDireccion.setText(direccion);
                                    editLocalidad.setText(localidad);
                                    editContacto.setText(contacto);
                                    editTipo.setText(tipo);

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

    private void consultarNombre() {

        // CONSULTA POR RAZÓN SOCIAL SI YA FUE INGRESADO ANTERIORMENTE A LA BASE
        String razonSocial = datoBuscarRazon.replace(" ", "%20");

        String URL = "http://malpicas.heliohost.org/malpica/proveedores/proveedores_consultar_nombre.php?nombre=" + razonSocial;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("datos");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ProveedoresSetters proveedoresSetters = new ProveedoresSetters();

                                proveedoresSetters.setCodigo(jsonObject.getString("codigo_prov"));
                                proveedoresSetters.setRazonSocial(jsonObject.getString("nombre_prov"));
                                proveedoresSetters.setCondicion(jsonObject.getString("condicion_prov"));
                                proveedoresSetters.setDescripcion(jsonObject.getString("descripcion"));
                                proveedoresSetters.setCuit(jsonObject.getString("cuit_cuil"));
                                proveedoresSetters.setDireccion(jsonObject.getString("direccion"));
                                proveedoresSetters.setLocalidad(jsonObject.getString("localidad"));
                                proveedoresSetters.setContacto(jsonObject.getString("contacto"));
                                proveedoresSetters.setTipo(jsonObject.getString("tipo_prov"));

                                String codigo = proveedoresSetters.getCodigo();
                                String nombre = proveedoresSetters.getRazonSocial();
                                String condicion = proveedoresSetters.getCondicion();
                                String descripcion = proveedoresSetters.getDescripcion();
                                String cuit = proveedoresSetters.getCuit();
                                String direccion = proveedoresSetters.getDireccion();
                                String localidad = proveedoresSetters.getLocalidad();
                                String contacto = proveedoresSetters.getContacto();
                                String tipo = proveedoresSetters.getTipo();

                                if (!nombre.equals("No existe")) {

                                    // SI DEVUELVE VALORES LOS MUESTRA EN LOS CAMPOS
                                    EditText editCodigo = getView().findViewById(R.id.editCodigo);
                                    EditText editRazonSocial = getView().findViewById(R.id.editRazonSocial);
                                    EditText editCondicion = getView().findViewById(R.id.editCondicion);
                                    EditText editDescripcion = getView().findViewById(R.id.editDescripcion);
                                    EditText editCuit = getView().findViewById(R.id.editCuit);
                                    EditText editDireccion = getView().findViewById(R.id.editDireccion);
                                    EditText editLocalidad = getView().findViewById(R.id.editLocalidad);
                                    EditText editContacto = getView().findViewById(R.id.editContacto);
                                    EditText editTipo = getView().findViewById(R.id.editTipo);

                                    editCodigo.setText(codigo);
                                    editRazonSocial.setText(nombre);
                                    editCondicion.setText(condicion);
                                    editDescripcion.setText(descripcion);
                                    editCuit.setText(cuit);
                                    editDireccion.setText(direccion);
                                    editLocalidad.setText(localidad);
                                    editContacto.setText(contacto);
                                    editTipo.setText(tipo);

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

    private void consultarCuit() {

        // CONSULTA POR CUIT SI YA FUE INGRESADO ANTERIORMENTE A LA BASE
        String URL = "http://malpicas.heliohost.org/malpica/proveedores/proveedores_consultar_cuit.php?cuit=" + datoBuscarCuit;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("datos");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ProveedoresSetters proveedoresSetters = new ProveedoresSetters();

                                proveedoresSetters.setCodigo(jsonObject.getString("codigo_prov"));
                                proveedoresSetters.setRazonSocial(jsonObject.getString("nombre_prov"));
                                proveedoresSetters.setCondicion(jsonObject.getString("condicion_prov"));
                                proveedoresSetters.setDescripcion(jsonObject.getString("descripcion"));
                                proveedoresSetters.setCuit(jsonObject.getString("cuit_cuil"));
                                proveedoresSetters.setDireccion(jsonObject.getString("direccion"));
                                proveedoresSetters.setLocalidad(jsonObject.getString("localidad"));
                                proveedoresSetters.setContacto(jsonObject.getString("contacto"));
                                proveedoresSetters.setTipo(jsonObject.getString("tipo_prov"));

                                String codigo = proveedoresSetters.getCodigo();
                                String nombre = proveedoresSetters.getRazonSocial();
                                String condicion = proveedoresSetters.getCondicion();
                                String descripcion = proveedoresSetters.getDescripcion();
                                String cuit = proveedoresSetters.getCuit();
                                String direccion = proveedoresSetters.getDireccion();
                                String localidad = proveedoresSetters.getLocalidad();
                                String contacto = proveedoresSetters.getContacto();
                                String tipo = proveedoresSetters.getTipo();

                                if (!cuit.equals("No existe")) {

                                    // SI DEVUELVE VALORES LOS MUESTRA EN LOS CAMPOS
                                    EditText editCodigo = getView().findViewById(R.id.editCodigo);
                                    EditText editRazonSocial = getView().findViewById(R.id.editRazonSocial);
                                    EditText editCondicion = getView().findViewById(R.id.editCondicion);
                                    EditText editDescripcion = getView().findViewById(R.id.editDescripcion);
                                    EditText editCuit = getView().findViewById(R.id.editCuit);
                                    EditText editDireccion = getView().findViewById(R.id.editDireccion);
                                    EditText editLocalidad = getView().findViewById(R.id.editLocalidad);
                                    EditText editContacto = getView().findViewById(R.id.editContacto);
                                    EditText editTipo = getView().findViewById(R.id.editTipo);

                                    editCodigo.setText(codigo);
                                    editRazonSocial.setText(nombre);
                                    editCondicion.setText(condicion);
                                    editDescripcion.setText(descripcion);
                                    editCuit.setText(cuit);
                                    editDireccion.setText(direccion);
                                    editLocalidad.setText(localidad);
                                    editContacto.setText(contacto);
                                    editTipo.setText(tipo);

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
}