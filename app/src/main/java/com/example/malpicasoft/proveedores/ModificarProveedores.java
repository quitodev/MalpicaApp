package com.example.malpicasoft.proveedores;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ModificarProveedores extends Fragment {

    private RequestQueue requestQueue;
    private JsonObjectRequest jsonObjectRequest;
    private String datoFechaActual, datoHoraActual, datoBuscarCodigo, datoBuscarRazon, datoBuscarCuit, datoCodigo, datoRazonSocial,
            datoCondicion, datoDescripcion, datoCuit, datoDireccion, datoLocalidad, datoContacto, datoTipo,
            datoDia, datoMes, datoAno;
    private int counter;
    
    public ModificarProveedores() { }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_modificar_proveedores, container, false);

        dateFragments();

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

        final TextView textBuscarCodigo = root.findViewById(R.id.textBuscarCodigo);
        final TextView textBuscarRazon = root.findViewById(R.id.textBuscarRazon);
        final TextView textBuscarCuit = root.findViewById(R.id.textBuscarCuit);

        final Button buttonConsultar = root.findViewById(R.id.buttonConsultar);
        final Button buttonModificar = root.findViewById(R.id.buttonModificar);
        final Button buttonGuardar = root.findViewById(R.id.buttonGuardar);

        // EVENTOS DEL BOTÓN CONSULTAR
        buttonConsultar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        buttonConsultar.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSelected));
                        buttonConsultar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_yellow, 0, 0, 0);

                        buttonModificar.setVisibility(View.VISIBLE);

                        editCodigo.setFocusable(false);
                        editRazonSocial.setFocusable(false);
                        editCondicion.setFocusable(false);
                        editDescripcion.setFocusable(false);
                        editCuit.setFocusable(false);
                        editDireccion.setFocusable(false);
                        editLocalidad.setFocusable(false);
                        editContacto.setFocusable(false);
                        editTipo.setFocusable(false);

                        editCodigo.setError(null);
                        editRazonSocial.setError(null);
                        editCondicion.setError(null);
                        editDescripcion.setError(null);
                        editCuit.setError(null);
                        editDireccion.setError(null);
                        editLocalidad.setError(null);
                        editContacto.setError(null);
                        editTipo.setError(null);

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
                        break;

                    case MotionEvent.ACTION_UP:
                        buttonConsultar.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                        buttonConsultar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_orange, 0, 0, 0);
                        break;
                }
                return true;
            }
        });

        // EVENTOS DEL BOTÓN MODIFICAR
        buttonModificar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        buttonModificar.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSelected));
                        buttonModificar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_yellow, 0, 0, 0);

                        datoCodigo = editCodigo.getText().toString();

                        if(!datoCodigo.isEmpty()){

                            // SI DEVUELVE UNA FACTURA, HABILITA LOS CAMPOS PARA HACER MODIFICACIONES
                            buttonModificar.setVisibility(View.INVISIBLE);

                            Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

                            editCodigo.setError("Datos correctos!", drawable);
                            editRazonSocial.setError("Datos correctos!", drawable);
                            editCondicion.setError("Datos correctos!", drawable);
                            editDescripcion.setError("Datos correctos!", drawable);
                            editCuit.setError("Datos correctos!", drawable);
                            editDireccion.setError("Datos correctos!", drawable);
                            editLocalidad.setError("Datos correctos!", drawable);
                            editContacto.setError("Datos correctos!", drawable);
                            editTipo.setError("Datos correctos!", drawable);

                            editCodigo.setFocusableInTouchMode(true);
                            editRazonSocial.setFocusableInTouchMode(true);
                            editCondicion.setFocusableInTouchMode(true);
                            editDescripcion.setFocusableInTouchMode(true);
                            editCuit.setFocusableInTouchMode(true);
                            editDireccion.setFocusableInTouchMode(true);
                            editLocalidad.setFocusableInTouchMode(true);
                            editContacto.setFocusableInTouchMode(true);
                            editTipo.setFocusableInTouchMode(true);

                            editTipo.requestFocusFromTouch();

                            ScrollView scrollView = getView().findViewById(R.id.scroll);
                            scrollView.setScrollY(10000);
                        }
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
                        buttonGuardar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_save_yellow, 0, 0, 0);

                        datoBuscarCodigo = editBuscarCodigo.getText().toString();
                        datoBuscarRazon = editBuscarRazon.getText().toString();
                        datoBuscarCuit = editBuscarCuit.getText().toString();
                        datoCodigo = editCodigo.getText().toString();
                        datoRazonSocial = editRazonSocial.getText().toString();
                        datoCondicion = editCondicion.getText().toString();
                        datoDescripcion = editDescripcion.getText().toString();
                        datoCuit = editCuit.getText().toString();
                        datoDireccion = editDireccion.getText().toString();
                        datoLocalidad = editLocalidad.getText().toString();
                        datoContacto = editContacto.getText().toString();
                        datoTipo = editTipo.getText().toString();

                        editCodigo.setFocusable(false);
                        editRazonSocial.setFocusable(false);
                        editCondicion.setFocusable(false);
                        editDescripcion.setFocusable(false);
                        editCuit.setFocusable(false);
                        editDireccion.setFocusable(false);
                        editLocalidad.setFocusable(false);
                        editContacto.setFocusable(false);
                        editTipo.setFocusable(false);

                        if (editCodigo.getError() == "Datos correctos!"
                                && editRazonSocial.getError() == "Datos correctos!"
                                && editCondicion.getError() == "Datos correctos!"
                                && editDescripcion.getError() == "Datos correctos!"
                                && editCuit.getError() == "Datos correctos!"
                                && editDireccion.getError() == "Datos correctos!"
                                && editLocalidad.getError() == "Datos correctos!"
                                && editContacto.getError() == "Datos correctos!"
                                && editTipo.getError() == "Datos correctos!") {

                            // SI TODOS LOS DATOS ESTÁN OK, PASA A REGISTRARLO
                            editTipo.requestFocusFromTouch();
                            editTipo.setError(null);

                            dialogProcesando();
                            modificarProveedor();

                        } else {

                            // SI ALGÚN DATO ES INCORRECTO, MUESTRA UN MENSAJE DE ERROR
                            editTipo.requestFocusFromTouch();
                            editTipo.setError(null);

                            dialogProcesando();
                            dialogError();

                            editCodigo.setFocusableInTouchMode(true);
                            editRazonSocial.setFocusableInTouchMode(true);
                            editCondicion.setFocusableInTouchMode(true);
                            editDescripcion.setFocusableInTouchMode(true);
                            editCuit.setFocusableInTouchMode(true);
                            editDireccion.setFocusableInTouchMode(true);
                            editLocalidad.setFocusableInTouchMode(true);
                            editContacto.setFocusableInTouchMode(true);
                            editTipo.setFocusableInTouchMode(true);

                            editTipo.requestFocusFromTouch();
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        buttonGuardar.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                        buttonGuardar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_save_orange, 0, 0, 0);
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
                    editBuscarRazon.setText("");
                    editBuscarCuit.setText("");
                }
            }
        });
        editBuscarRazon.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, RECUPERA COLOR DE TEXTO
                    textBuscarRazon.setTextColor(ContextCompat.getColor(getContext(), R.color.colorText));

                } else {

                    // SI INGRESA AL CAMPO, CAMBIA COLOR DE TEXTO Y ELIMINA LOS DATOS INGRESADOS EN LOS OTROS CAMPOS
                    textBuscarRazon.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSelected));
                    editBuscarCodigo.setText("");
                    editBuscarCuit.setText("");
                }
            }
        });
        editBuscarCuit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, RECUPERA COLOR DE TEXTO
                    textBuscarCuit.setTextColor(ContextCompat.getColor(getContext(), R.color.colorText));

                } else {

                    // SI INGRESA AL CAMPO, CAMBIA COLOR DE TEXTO Y ELIMINA LOS DATOS INGRESADOS EN LOS OTROS CAMPOS
                    textBuscarCuit.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSelected));
                    editBuscarCodigo.setText("");
                    editBuscarRazon.setText("");
                }
            }
        });
        editCodigo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, GUARDA Y CORROBORA LOS DATOS INGRESADOS
                    datoCodigo = editCodigo.getText().toString().replace(" ","");

                    if (!datoCodigo.isEmpty()) {

                        if(!datoCodigo.equals(datoBuscarCodigo)) {

                            // SI HUBO CAMBIOS, CONSULTA PARA VER SI YA EXISTE EN LA BASE
                            dialogProcesando();
                            consultarCodigoBis();

                        } else {

                            // SI NO HUBO CAMBIOS, VALIDA EL CAMPO
                            Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                            editCodigo.setError("Datos correctos!", drawable);
                        }

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
        editRazonSocial.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, GUARDA Y CORROBORA LOS DATOS INGRESADOS
                    datoRazonSocial = editRazonSocial.getText().toString();

                    if (!datoRazonSocial.isEmpty()) {

                        if(!datoRazonSocial.equals(datoBuscarRazon)) {

                            // SI HUBO CAMBIOS, CONSULTA PARA VER SI YA EXISTE EN LA BASE
                            dialogProcesando();
                            consultarNombreBis();

                        } else {

                            // SI NO HUBO CAMBIOS, VALIDA EL CAMPO
                            Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                            editRazonSocial.setError("Datos correctos!", drawable);
                        }

                    } else {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_error);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editRazonSocial.setError("Revise los datos!", drawable);
                    }

                } else {

                    // SI INGRESA AL CAMPO, OCULTA EL ÍCONO DE VALIDACIÓN
                    editRazonSocial.setError(null);
                }
            }
        });
        editCondicion.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, GUARDA Y VALIDA LOS DATOS INGRESADOS
                    datoCondicion = editCondicion.getText().toString();

                    if (datoCondicion.length() == 2) {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editCondicion.setError("Datos correctos!", drawable);

                    } else {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_error);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editCondicion.setError("Revise los datos!", drawable);
                    }

                } else {

                    // SI INGRESA AL CAMPO, MUESTRA UN DIALOG CON INFORMACIÓN RELEVANTE
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_info);
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                    editCondicion.setError("RI (Resp. Inscripto)\nMT (Resp. Mono.)\nCF (Cons. Final)", drawable);
                }
            }
        });
        editDescripcion.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, GUARDA Y VALIDA LOS DATOS INGRESADOS
                    datoDescripcion = editDescripcion.getText().toString();

                    if (!datoDescripcion.isEmpty()) {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editDescripcion.setError("Datos correctos!", drawable);

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
        editCuit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, GUARDA Y CORROBORA LOS DATOS INGRESADOS
                    datoCuit = editCuit.getText().toString().replace(" ","");

                    if (datoCuit.length() == 13 && datoCuit.contains("-")) {

                        if(!datoCuit.equals(datoBuscarCuit)) {

                            // SI HUBO CAMBIOS, CONSULTA PARA VER SI YA EXISTE EN LA BASE
                            dialogProcesando();
                            consultarCuitBis();

                        } else {

                            // SI NO HUBO CAMBIOS, VALIDA EL CAMPO
                            Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                            editCuit.setError("Datos correctos!", drawable);
                        }

                    } else {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_error);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editCuit.setError("Revise los datos!", drawable);
                    }

                } else {

                    // SI INGRESA AL CAMPO, OCULTA EL ÍCONO DE VALIDACIÓN
                    editCuit.setError(null);
                }
            }
        });
        editDireccion.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, GUARDA Y VALIDA LOS DATOS INGRESADOS
                    datoDireccion = editDireccion.getText().toString();

                    if (!datoDireccion.isEmpty()) {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editDireccion.setError("Datos correctos!", drawable);

                    } else {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_error);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editDireccion.setError("Revise los datos!", drawable);
                    }

                } else {

                    // SI INGRESA AL CAMPO, OCULTA EL ÍCONO DE VALIDACIÓN
                    editDireccion.setError(null);
                }
            }
        });
        editLocalidad.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, GUARDA Y VALIDA LOS DATOS INGRESADOS
                    datoLocalidad = editLocalidad.getText().toString();

                    if (!datoLocalidad.isEmpty()) {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editLocalidad.setError("Datos correctos!", drawable);

                    } else {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_error);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editLocalidad.setError("Revise los datos!", drawable);
                    }

                } else {

                    // SI INGRESA AL CAMPO, OCULTA EL ÍCONO DE VALIDACIÓN
                    editLocalidad.setError(null);
                }
            }
        });
        editContacto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, GUARDA Y VALIDA LOS DATOS INGRESADOS
                    datoContacto = editContacto.getText().toString();

                    if (!datoContacto.isEmpty()) {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editContacto.setError("Datos correctos!", drawable);

                    } else {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_error);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editContacto.setError("Revise los datos!", drawable);
                    }

                } else {

                    // SI INGRESA AL CAMPO, OCULTA EL ÍCONO DE VALIDACIÓN
                    editContacto.setError(null);
                }
            }
        });
        editTipo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, GUARDA Y VALIDA LOS DATOS INGRESADOS
                    datoTipo = editTipo.getText().toString();

                    if (!datoTipo.isEmpty()) {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editTipo.setError("Datos correctos!", drawable);

                    } else {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_error);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editTipo.setError("Revise los datos!", drawable);
                    }

                } else {

                    // SI INGRESA AL CAMPO, MUESTRA UN DIALOG CON INFORMACIÓN RELEVANTE
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_info);
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                    editTipo.setError("FRECUENTE O ESPORÁDICO", drawable);
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

    private void dialogErrorProveedor(){

        // DIALOG QUE INFORMA QUE EL PROVEEDOR YA SE INGRESÓ ANTERIORMENTE
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Dialogs dialogs = new Dialogs(getActivity());
                int layout = R.layout.dialog_error_proveedor;
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

                EditText editBuscarCodigo = getView().findViewById(R.id.editBuscarCodigo);
                EditText editCodigo = getView().findViewById(R.id.editCodigo);
                EditText editRazonSocial = getView().findViewById(R.id.editRazonSocial);
                EditText editCondicion = getView().findViewById(R.id.editCondicion);
                EditText editDescripcion = getView().findViewById(R.id.editDescripcion);
                EditText editCuit = getView().findViewById(R.id.editCuit);
                EditText editDireccion = getView().findViewById(R.id.editDireccion);
                EditText editLocalidad = getView().findViewById(R.id.editLocalidad);
                EditText editContacto = getView().findViewById(R.id.editContacto);
                EditText editTipo = getView().findViewById(R.id.editTipo);

                editCodigo.setFocusableInTouchMode(false);
                editRazonSocial.setFocusableInTouchMode(false);
                editCondicion.setFocusableInTouchMode(false);
                editDescripcion.setFocusableInTouchMode(false);
                editCuit.setFocusableInTouchMode(false);
                editDireccion.setFocusableInTouchMode(false);
                editLocalidad.setFocusableInTouchMode(false);
                editContacto.setFocusableInTouchMode(false);
                editTipo.setFocusableInTouchMode(false);

                editCodigo.setText("");
                editRazonSocial.setText("");
                editCondicion.setText("");
                editDescripcion.setText("");
                editCuit.setText("");
                editDireccion.setText("");
                editLocalidad.setText("");
                editContacto.setText("");
                editTipo.setText("");

                editCodigo.setError(null);
                editRazonSocial.setError(null);
                editCondicion.setError(null);
                editDescripcion.setError(null);
                editCuit.setError(null);
                editDireccion.setError(null);
                editLocalidad.setError(null);
                editContacto.setError(null);
                editTipo.setError(null);

                ScrollView scrollView = getView().findViewById(R.id.scroll);
                scrollView.setScrollY(0);

                Button buttonModificar = getView().findViewById(R.id.buttonModificar);
                buttonModificar.setVisibility(View.VISIBLE);

                editBuscarCodigo.requestFocusFromTouch();
            }
        }, 3000);
    }

    private void consultarCodigo() {

        // CONSULTA POR CÓDIGO SI YA FUE INGRESADO ANTERIORMENTE A LA BASE
        String URL = "http://malpicas.heliohost.org/malpica/proveedores/proveedores_consultar_codigo.php?parameter=" + datoBuscarCodigo;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ProveedoresSetters setters = new ProveedoresSetters();

                                setters.setCodigo(jsonObject.getString("codigo"));
                                setters.setRazonSocial(jsonObject.getString("razon_social"));
                                setters.setCondicion(jsonObject.getString("condicion"));
                                setters.setDescripcion(jsonObject.getString("descripcion"));
                                setters.setCuit(jsonObject.getString("cuit"));
                                setters.setDireccion(jsonObject.getString("direccion"));
                                setters.setLocalidad(jsonObject.getString("localidad"));
                                setters.setContacto(jsonObject.getString("contacto"));
                                setters.setTipo(jsonObject.getString("tipo"));

                                String codigo = setters.getCodigo();
                                String razonSocial = setters.getRazonSocial();
                                String condicion = setters.getCondicion();
                                String descripcion = setters.getDescripcion();
                                String cuit = setters.getCuit();
                                String direccion = setters.getDireccion();
                                String localidad = setters.getLocalidad();
                                String contacto = setters.getContacto();
                                String tipo = setters.getTipo();

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
                                    editRazonSocial.setText(razonSocial);
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
                Toast.makeText(getContext(), "Por favor, revise su conexión!", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void consultarNombre() {

        // CONSULTA POR RAZÓN SOCIAL SI YA FUE INGRESADO ANTERIORMENTE A LA BASE
        String razonSocial = datoBuscarRazon.replace(" ", "%20");

        String URL = "http://malpicas.heliohost.org/malpica/proveedores/proveedores_consultar_nombre.php?parameter=" + razonSocial;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ProveedoresSetters setters = new ProveedoresSetters();

                                setters.setCodigo(jsonObject.getString("codigo"));
                                setters.setRazonSocial(jsonObject.getString("razon_social"));
                                setters.setCondicion(jsonObject.getString("condicion"));
                                setters.setDescripcion(jsonObject.getString("descripcion"));
                                setters.setCuit(jsonObject.getString("cuit"));
                                setters.setDireccion(jsonObject.getString("direccion"));
                                setters.setLocalidad(jsonObject.getString("localidad"));
                                setters.setContacto(jsonObject.getString("contacto"));
                                setters.setTipo(jsonObject.getString("tipo"));

                                String codigo = setters.getCodigo();
                                String razonSocial = setters.getRazonSocial();
                                String condicion = setters.getCondicion();
                                String descripcion = setters.getDescripcion();
                                String cuit = setters.getCuit();
                                String direccion = setters.getDireccion();
                                String localidad = setters.getLocalidad();
                                String contacto = setters.getContacto();
                                String tipo = setters.getTipo();

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
                                    editRazonSocial.setText(razonSocial);
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
                Toast.makeText(getContext(), "Por favor, revise su conexión!", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void consultarCuit() {

        // CONSULTA POR CUIT SI YA FUE INGRESADO ANTERIORMENTE A LA BASE
        String URL = "http://malpicas.heliohost.org/malpica/proveedores/proveedores_consultar_cuit.php?parameter=" + datoBuscarCuit;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ProveedoresSetters setters = new ProveedoresSetters();

                                setters.setCodigo(jsonObject.getString("codigo"));
                                setters.setRazonSocial(jsonObject.getString("razon_social"));
                                setters.setCondicion(jsonObject.getString("condicion"));
                                setters.setDescripcion(jsonObject.getString("descripcion"));
                                setters.setCuit(jsonObject.getString("cuit"));
                                setters.setDireccion(jsonObject.getString("direccion"));
                                setters.setLocalidad(jsonObject.getString("localidad"));
                                setters.setContacto(jsonObject.getString("contacto"));
                                setters.setTipo(jsonObject.getString("tipo"));

                                String codigo = setters.getCodigo();
                                String razonSocial = setters.getRazonSocial();
                                String condicion = setters.getCondicion();
                                String descripcion = setters.getDescripcion();
                                String cuit = setters.getCuit();
                                String direccion = setters.getDireccion();
                                String localidad = setters.getLocalidad();
                                String contacto = setters.getContacto();
                                String tipo = setters.getTipo();

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
                                    editRazonSocial.setText(razonSocial);
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
                Toast.makeText(getContext(), "Por favor, revise su conexión!", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void consultarCodigoBis() {

        // CONSULTA POR CÓDIGO SI NO EXISTE OTRO CON EL MISMO CÓDIGO
        String URL = "http://malpicas.heliohost.org/malpica/proveedores/proveedores_consultar_codigo.php?parameter=" + datoCodigo;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ProveedoresSetters setters = new ProveedoresSetters();

                                setters.setCodigo(jsonObject.getString("codigo"));

                                String codigo = setters.getCodigo();

                                if(!codigo.equals("No existe")){

                                    // SI YA SE ENCUENTRA REGISTRADO MUESTRA UN MENSAJE DE ERROR
                                    dialogErrorProveedor();

                                    Drawable drawable = getResources().getDrawable(R.drawable.ic_error);
                                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                                    EditText editCodigo = getView().findViewById(R.id.editCodigo);
                                    editCodigo.setError("Revise los datos!", drawable);

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
                Toast.makeText(getContext(), "Por favor, revise su conexión!", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void consultarNombreBis() {

        // CONSULTA POR RAZÓN SOCIAL SI YA FUE INGRESADO ANTERIORMENTE A LA BASE
        String razonSocial = datoRazonSocial.replace(" ","%20");

        String URL = "http://malpicas.heliohost.org/malpica/proveedores/proveedores_consultar_nombre.php?parameter=" + razonSocial;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ProveedoresSetters setters = new ProveedoresSetters();

                                setters.setRazonSocial(jsonObject.getString("razon_social"));

                                String razonSocial = setters.getRazonSocial();

                                if(!razonSocial.equals("No existe")){

                                    // SI YA SE ENCUENTRA REGISTRADO MUESTRA UN MENSAJE DE ERROR
                                    dialogErrorProveedor();

                                    Drawable drawable = getResources().getDrawable(R.drawable.ic_error);
                                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                                    EditText editRazonSocial = getView().findViewById(R.id.editRazonSocial);
                                    editRazonSocial.setError("Revise los datos!", drawable);

                                } else {

                                    // SI NO SE ENCUENTRA REGISTRADO VALIDA EL CAMPO
                                    Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                                    EditText editRazonSocial = getView().findViewById(R.id.editRazonSocial);
                                    editRazonSocial.setError("Datos correctos!", drawable);
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

    private void consultarCuitBis() {

        // CONSULTA POR CUIT SI YA FUE INGRESADO ANTERIORMENTE A LA BASE
        String URL = "http://malpicas.heliohost.org/malpica/proveedores/proveedores_consultar_cuit.php?parameter=" + datoCuit;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ProveedoresSetters setters = new ProveedoresSetters();

                                setters.setCuit(jsonObject.getString("cuit"));

                                String cuit = setters.getCuit();

                                if(!cuit.equals("No existe")){

                                    // SI YA SE ENCUENTRA REGISTRADO MUESTRA UN MENSAJE DE ERROR
                                    dialogErrorProveedor();

                                    Drawable drawable = getResources().getDrawable(R.drawable.ic_error);
                                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                                    EditText editCuit = getView().findViewById(R.id.editCuit);
                                    editCuit.setError("Revise los datos!", drawable);

                                } else {

                                    // SI NO SE ENCUENTRA REGISTRADO VALIDA EL CAMPO
                                    Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                                    EditText editCuit = getView().findViewById(R.id.editCuit);
                                    editCuit.setError("Datos correctos!", drawable);
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

    private void modificarProveedor() {

        // FINALMENTE MODIFICA EL PROVEEDOR EN LA BASE
        String URL = "http://malpicas.heliohost.org/malpica/proveedores/proveedores_actualizar_proveedor.php";
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

                parameter.put("codigo_viejo",datoBuscarCodigo);
                parameter.put("codigo_nuevo",datoCodigo);
                parameter.put("razon_social",datoRazonSocial);
                parameter.put("condicion",datoCondicion);
                parameter.put("descripcion",datoDescripcion);
                parameter.put("cuit",datoCuit);
                parameter.put("direccion",datoDireccion);
                parameter.put("localidad",datoLocalidad);
                parameter.put("contacto",datoContacto);
                parameter.put("tipo",datoTipo);
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
}
