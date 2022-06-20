package com.example.malpicasoft.gastos;

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
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ModificarGastos extends Fragment {

    private RequestQueue requestQueue;
    private JsonObjectRequest jsonObjectRequest;
    private String datoFechaActual, datoHoraActual, datoBuscarFactura, datoFechaFactura, datoFechaIngreso, datoNroFactura, datoCodigo,
            datoRazonSocial, datoCondicion, datoCodigoStock, datoDescripcionStock, datoCantidad, datoPrecioUnit, datoImpuestos,
            datoPrecioTotal, datoDia, datoMes, datoAno, nuevoPrecioTotal, codigoFactura;
    private int counter, cantidad, nuevaCantidad, cantidadFactura;
    private double impuestos, precioUnit, precioTotal;
    
    public ModificarGastos() { }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_modificar_gastos, container, false);

        dateFragments();

        // LIBRERÍA VOLLEY PARA CONSULTAS A BASES DE DATOS
        requestQueue = Volley.newRequestQueue(getContext());

        // COMPONENTES DE LA VISTA
        final EditText editBuscarFactura = root.findViewById(R.id.editBuscarFactura);
        final EditText editFechaFactura = root.findViewById(R.id.editFechaFactura);
        final EditText editFechaIngreso = root.findViewById(R.id.editFechaIngreso);
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

                        editFechaFactura.setFocusable(false);
                        editFechaIngreso.setFocusable(false);
                        editNroFactura.setFocusable(false);
                        editCodigo.setFocusable(false);
                        editRazonSocial.setFocusable(false);
                        editCondicion.setFocusable(false);
                        editCodigoStock.setFocusable(false);
                        editDescripcionStock.setFocusable(false);
                        editCantidad.setFocusable(false);
                        editPrecioUnit.setFocusable(false);
                        editImpuestos.setFocusable(false);
                        editPrecioTotal.setFocusable(false);

                        editFechaFactura.setError(null);
                        editFechaIngreso.setError(null);
                        editNroFactura.setError(null);
                        editCodigo.setError(null);
                        editRazonSocial.setError(null);
                        editCondicion.setError(null);
                        editCodigoStock.setError(null);
                        editDescripcionStock.setError(null);
                        editCantidad.setError(null);
                        editPrecioUnit.setError(null);
                        editImpuestos.setError(null);
                        editPrecioTotal.setError(null);

                        datoBuscarFactura = editBuscarFactura.getText().toString();

                        if(!datoBuscarFactura.isEmpty()){

                            // SI EL CAMPO NO ESTÁ VACÍO, CONSULTA A LA BASE
                            dialogProcesando();
                            consultarFactura();

                        } else {

                            // SI EL CAMPO ESTÁ VACÍO, MUESTRA UN MENSAJE DE ERROR
                            editFechaFactura.setText("");
                            editFechaIngreso.setText("");
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

        // EVENTOS DEL BOTÓN MODIFICAR
        buttonModificar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        buttonModificar.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSelected));
                        buttonModificar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_yellow, 0, 0, 0);

                        datoFechaFactura = editFechaFactura.getText().toString();

                        if(!datoFechaFactura.isEmpty()){

                            // SI DEVUELVE UNA FACTURA, HABILITA LOS CAMPOS PARA HACER MODIFICACIONES
                            buttonModificar.setVisibility(View.INVISIBLE);

                            Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

                            editFechaIngreso.setError("Datos correctos!", drawable);
                            editNroFactura.setError("Datos correctos!", drawable);
                            editCodigo.setError("Datos correctos!", drawable);
                            editRazonSocial.setError("Datos correctos!", drawable);
                            editCondicion.setError("Datos correctos!", drawable);
                            editCodigoStock.setError("Datos correctos!", drawable);
                            editDescripcionStock.setError("Datos correctos!", drawable);
                            editCantidad.setError("Datos correctos!", drawable);
                            editPrecioUnit.setError("Datos correctos!", drawable);
                            editImpuestos.setError("Datos correctos!", drawable);
                            editPrecioTotal.setError("Datos correctos!", drawable);

                            editFechaFactura.setFocusableInTouchMode(true);
                            editFechaIngreso.setFocusableInTouchMode(true);
                            editNroFactura.setFocusableInTouchMode(true);
                            editCodigo.setFocusableInTouchMode(true);
                            editRazonSocial.setFocusableInTouchMode(true);
                            editCondicion.setFocusableInTouchMode(true);
                            editCodigoStock.setFocusableInTouchMode(true);
                            editDescripcionStock.setFocusableInTouchMode(true);
                            editCantidad.setFocusableInTouchMode(true);
                            editPrecioUnit.setFocusableInTouchMode(true);
                            editImpuestos.setFocusableInTouchMode(true);
                            editPrecioTotal.setFocusableInTouchMode(true);

                            editPrecioTotal.requestFocusFromTouch();

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

                        datoBuscarFactura = editBuscarFactura.getText().toString();
                        datoFechaFactura = editFechaFactura.getText().toString();
                        datoFechaIngreso = editFechaIngreso.getText().toString();
                        datoNroFactura = editNroFactura.getText().toString();
                        datoCodigo = editCodigo.getText().toString();
                        datoRazonSocial = editRazonSocial.getText().toString();
                        datoCondicion = editCondicion.getText().toString();
                        datoCodigoStock = editCodigoStock.getText().toString();
                        datoDescripcionStock = editDescripcionStock.getText().toString();
                        datoCantidad = editCantidad.getText().toString();
                        datoPrecioUnit = editPrecioUnit.getText().toString();
                        datoImpuestos = editImpuestos.getText().toString();
                        datoPrecioTotal = editPrecioTotal.getText().toString();

                        editFechaFactura.setFocusable(false);
                        editFechaIngreso.setFocusable(false);
                        editNroFactura.setFocusable(false);
                        editCodigo.setFocusable(false);
                        editRazonSocial.setFocusable(false);
                        editCondicion.setFocusable(false);
                        editCodigoStock.setFocusable(false);
                        editDescripcionStock.setFocusable(false);
                        editCantidad.setFocusable(false);
                        editPrecioUnit.setFocusable(false);
                        editImpuestos.setFocusable(false);
                        editPrecioTotal.setFocusable(false);

                        if (editFechaFactura.getError() == "Datos correctos!"
                                && editFechaIngreso.getError() == "Datos correctos!"
                                && editNroFactura.getError() == "Datos correctos!"
                                && editCodigo.getError() == "Datos correctos!"
                                && editRazonSocial.getError() == "Datos correctos!"
                                && editCondicion.getError() == "Datos correctos!"
                                && editCodigoStock.getError() == "Datos correctos!"
                                && editDescripcionStock.getError() == "Datos correctos!"
                                && editCantidad.getError() == "Datos correctos!"
                                && editPrecioUnit.getError() == "Datos correctos!"
                                && editImpuestos.getError() == "Datos correctos!"
                                && editPrecioTotal.getError() == "Datos correctos!") {

                            // SI TODOS LOS DATOS ESTÁN CORRECTOS, CONTINÚA CON LA MODIFICACIÓN
                            editPrecioTotal.requestFocusFromTouch();
                            editPrecioTotal.setError(null);

                            dialogProcesando();
                            consultarProveedorBis();

                        } else {

                            // SI ALGÚN DATO ES INCORRECTO, MUESTRA UN MENSAJE DE ERROR
                            editPrecioTotal.requestFocusFromTouch();
                            editPrecioTotal.setError(null);

                            dialogProcesando();
                            dialogError();

                            editFechaFactura.setFocusableInTouchMode(true);
                            editFechaIngreso.setFocusableInTouchMode(true);
                            editNroFactura.setFocusableInTouchMode(true);
                            editCodigo.setFocusableInTouchMode(true);
                            editRazonSocial.setFocusableInTouchMode(true);
                            editCondicion.setFocusableInTouchMode(true);
                            editCodigoStock.setFocusableInTouchMode(true);
                            editDescripcionStock.setFocusableInTouchMode(true);
                            editCantidad.setFocusableInTouchMode(true);
                            editPrecioUnit.setFocusableInTouchMode(true);
                            editImpuestos.setFocusableInTouchMode(true);
                            editPrecioTotal.setFocusableInTouchMode(true);

                            editPrecioTotal.requestFocusFromTouch();
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
        editFechaFactura.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, GUARDA Y VALIDA LOS DATOS INGRESADOS
                    String datoFechaLarga = editFechaFactura.getText().toString();
                    datoFechaFactura = datoFechaLarga.replace("/20", "/");

                    if (datoFechaLarga.length() == 10 && datoFechaLarga.contains("/")) {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editFechaFactura.setError("Datos correctos!", drawable);

                    } else {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_error);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editFechaFactura.setError("Revise los datos!", drawable);
                    }

                } else {

                    // SI INGRESA AL CAMPO, OCULTA EL ÍCONO DE VALIDACIÓN
                    editFechaFactura.setError(null);
                }
            }
        });
        editFechaIngreso.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, GUARDA Y VALIDA LOS DATOS INGRESADOS
                    String datoFechaLarga = editFechaIngreso.getText().toString();
                    datoFechaIngreso = datoFechaLarga.replace("/20", "/");

                    if (datoFechaLarga.length() == 10 && datoFechaLarga.contains("/")) {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editFechaIngreso.setError("Datos correctos!", drawable);

                    } else {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_error);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editFechaIngreso.setError("Revise los datos!", drawable);
                    }

                } else {

                    // SI INGRESA AL CAMPO, OCULTA EL ÍCONO DE VALIDACIÓN
                    editFechaIngreso.setError(null);
                }
            }
        });
        editNroFactura.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, GUARDA Y VALIDA LOS DATOS INGRESADOS
                    datoNroFactura = editNroFactura.getText().toString();

                    if (datoNroFactura.length() == 15 && datoNroFactura.contains("-")) {

                        if(!datoNroFactura.equals(datoBuscarFactura)) {

                            // SI HUBO CAMBIOS, CONSULTA PARA VER SI YA EXISTE EN LA BASE
                            dialogProcesando();
                            consultarFacturaBis();

                        } else {

                            // SI NO HUBO CAMBIOS, VALIDA EL CAMPO
                            Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                            editNroFactura.setError("Datos correctos!", drawable);
                        }

                    } else {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_error);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editNroFactura.setError("Revise los datos!", drawable);
                    }

                } else {

                    // SI INGRESA AL CAMPO, OCULTA EL ÍCONO DE VALIDACIÓN
                    editNroFactura.setError(null);
                }
            }
        });
        editCodigo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, GUARDA Y VALIDA LOS DATOS INGRESADOS
                    datoCodigo = editCodigo.getText().toString();

                    if (!datoCodigo.isEmpty()) {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editCodigo.setError("Datos correctos!", drawable);

                        // CONSULTA LOS DATOS INGRESADOS EN LA BASE PARA AGILIZAR LA CARGA
                        dialogProcesando();
                        consultarProveedor();

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

                    // SI SALE DEL CAMPO, GUARDA Y VALIDA LOS DATOS INGRESADOS
                    datoRazonSocial = editRazonSocial.getText().toString();

                    if (!datoRazonSocial.isEmpty()) {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editRazonSocial.setError("Datos correctos!", drawable);

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
        editCodigoStock.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, GUARDA Y VALIDA LOS DATOS INGRESADOS
                    datoCodigoStock = editCodigoStock.getText().toString();

                    if (!datoCodigoStock.isEmpty()) {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editCodigoStock.setError("Datos correctos!", drawable);

                        // CONSULTA LOS DATOS INGRESADOS EN LA BASE PARA AGILIZAR LA CARGA
                        dialogProcesando();
                        consultarProducto();

                    } else {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_error);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editCodigoStock.setError("Revise los datos!", drawable);
                    }

                } else {

                    // SI INGRESA AL CAMPO, OCULTA EL ÍCONO DE VALIDACIÓN
                    editCodigoStock.setError(null);
                }
            }
        });
        editDescripcionStock.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, GUARDA Y VALIDA LOS DATOS INGRESADOS
                    datoDescripcionStock = editDescripcionStock.getText().toString();

                    if (!datoDescripcionStock.isEmpty()) {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editDescripcionStock.setError("Datos correctos!", drawable);

                    } else {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_error);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editDescripcionStock.setError("Revise los datos!", drawable);
                    }

                } else {

                    // SI INGRESA AL CAMPO, OCULTA EL ÍCONO DE VALIDACIÓN
                    editDescripcionStock.setError(null);
                }
            }
        });
        editCantidad.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, GUARDA Y VALIDA LOS DATOS INGRESADOS
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
        editPrecioUnit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, GUARDA Y VALIDA LOS DATOS INGRESADOS
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
        editImpuestos.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, GUARDA Y VALIDA LOS DATOS INGRESADOS
                    datoImpuestos = editImpuestos.getText().toString();

                    if (!datoImpuestos.isEmpty()) {

                        impuestos = Double.parseDouble(datoImpuestos);

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editImpuestos.setError("Datos correctos!", drawable);

                        if(impuestos == 0){

                            editImpuestos.setText("0.00");

                        } else {

                            DecimalFormat decimalFormat = new DecimalFormat("#.00");
                            datoImpuestos = decimalFormat.format(impuestos).replace(",",".");
                            editImpuestos.setText(datoImpuestos);
                        }

                    } else {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editImpuestos.setError("Datos correctos!", drawable);

                        editImpuestos.setText("0.00");
                    }

                    datoImpuestos = editImpuestos.getText().toString();
                    impuestos = Double.parseDouble(datoImpuestos);

                } else {

                    // SI INGRESA AL CAMPO, OCULTA EL ÍCONO DE VALIDACIÓN Y CHEQUEA LOS DATOS
                    editImpuestos.setError(null);
                    datoImpuestos = editImpuestos.getText().toString();
                    impuestos = Double.parseDouble(datoImpuestos);

                    if (impuestos == 0) {
                        editImpuestos.setText("");
                    }
                }
            }
        });
        editPrecioTotal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, GUARDA Y VALIDA LOS DATOS INGRESADOS
                    datoPrecioTotal = editPrecioTotal.getText().toString();
                    datoCantidad = editCantidad.getText().toString();
                    datoImpuestos = editImpuestos.getText().toString();
                    datoPrecioUnit = editPrecioUnit.getText().toString();
                    cantidad = Integer.parseInt(datoCantidad);
                    impuestos = Double.parseDouble(datoImpuestos);
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

                        precioTotal = (cantidad * precioUnit) + impuestos;

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
                    datoImpuestos = editImpuestos.getText().toString();
                    datoPrecioUnit = editPrecioUnit.getText().toString();
                    cantidad = Integer.parseInt(datoCantidad);
                    impuestos = Double.parseDouble(datoImpuestos);
                    precioUnit = Double.parseDouble(datoPrecioUnit);

                    precioTotal = (cantidad * precioUnit) + impuestos;

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

    private void dialogErrorFactura(){

        // DIALOG CON MENSAJE DE ERROR
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Dialogs dialogs = new Dialogs(getActivity());
                int layout = R.layout.dialog_error_factura;
                dialogs.startResultado(layout);
            }
        }, 3000);
    }

    private void dialogOk(){

        // DIALOG CON CONFIRMACIÓN DE OPERACIÓN REALIZADA CON ÉXITO
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Dialogs dialogs = new Dialogs(getActivity());
                int layout = R.layout.dialog_ok;
                dialogs.startResultado(layout);

                EditText editBuscarFactura = getView().findViewById(R.id.editBuscarFactura);
                EditText editFechaFactura = getView().findViewById(R.id.editFechaFactura);
                EditText editFechaIngreso = getView().findViewById(R.id.editFechaIngreso);
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

                editFechaFactura.setFocusableInTouchMode(false);
                editFechaIngreso.setFocusableInTouchMode(false);
                editNroFactura.setFocusableInTouchMode(false);
                editCodigo.setFocusableInTouchMode(false);
                editRazonSocial.setFocusableInTouchMode(false);
                editCondicion.setFocusableInTouchMode(false);
                editCodigoStock.setFocusableInTouchMode(false);
                editDescripcionStock.setFocusableInTouchMode(false);
                editCantidad.setFocusableInTouchMode(false);
                editPrecioUnit.setFocusableInTouchMode(false);
                editImpuestos.setFocusableInTouchMode(false);
                editPrecioTotal.setFocusableInTouchMode(false);

                editFechaFactura.setText("");
                editFechaIngreso.setText("");
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

                editFechaFactura.setError(null);
                editFechaIngreso.setError(null);
                editNroFactura.setError(null);
                editCodigo.setError(null);
                editRazonSocial.setError(null);
                editCondicion.setError(null);
                editCodigoStock.setError(null);
                editDescripcionStock.setError(null);
                editCantidad.setError(null);
                editPrecioUnit.setError(null);
                editImpuestos.setError(null);
                editPrecioTotal.setError(null);

                ScrollView scrollView = getView().findViewById(R.id.scroll);
                scrollView.setScrollY(0);

                Button buttonModificar = getView().findViewById(R.id.buttonModificar);
                buttonModificar.setVisibility(View.VISIBLE);

                editBuscarFactura.requestFocusFromTouch();
            }
        }, 3000);
    }

    private void consultarFactura() {

        // CONSULTA LA FACTURA INGRESADA EN LA BASE DE DATOS
        String URL = "http://malpicas.heliohost.org/malpica/gastos/gastos_consultar_factura.php?parameter=" + datoBuscarFactura;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                GastosSetters setters = new GastosSetters();

                                setters.setFechaFactura(jsonObject.getString("fecha_factura"));
                                setters.setFechaIngreso(jsonObject.getString("fecha_ingreso"));
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

                                if(!fechaFactura.equals("No existe")){

                                    // SI DEVUELVE UNA FACTURA MUESTRA LOS DATOS EN LOS CAMPOS
                                    EditText editFechaFactura = getView().findViewById(R.id.editFechaFactura);
                                    EditText editFechaIngreso = getView().findViewById(R.id.editFechaIngreso);
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

                                    codigoFactura = codigoStock + "";
                                    cantidadFactura = Integer.parseInt(cantidad);

                                    editFechaFactura.setText(fechaFactura);
                                    editFechaIngreso.setText(fechaIngreso);
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

                                    if (fechaFactura.contains("/2") && fechaIngreso.contains("/2")) {

                                        // REEMPLAZO DE FORMATO DE FECHA
                                        String fechaFactura1 = fechaFactura.replace("/2","/202");
                                        String fechaIngreso1 = fechaIngreso.replace("/2","/202");

                                        editFechaFactura.setText(fechaFactura1);
                                        editFechaIngreso.setText(fechaIngreso1);
                                    }

                                } else {

                                    // SI NO DEVUELVE UNA FACTURA MUESTRA UN MENSAJE DE ERROR Y LIMPIA LOS CAMPOS
                                    dialogError();

                                    EditText editFechaFactura = getView().findViewById(R.id.editFechaFactura);
                                    EditText editFechaIngreso = getView().findViewById(R.id.editFechaIngreso);
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

    private void consultarFacturaBis() {

        // CONSULTA POR FACTURA SI YA FUE INGRESADA ANTERIORMENTE A LA BASE
        String URL = "http://malpicas.heliohost.org/malpica/gastos/gastos_consultar_factura.php?parameter=" + datoNroFactura;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                GastosSetters setters = new GastosSetters();

                                setters.setNroFactura(jsonObject.getString("nro_factura"));

                                String nroFactura = setters.getNroFactura();

                                if(!nroFactura.equals("No existe")){

                                    // SI DEVUELVE UNA FACTURA MUESTRA UN MENSAJE DE ERROR
                                    dialogErrorFactura();

                                    Drawable drawable = getResources().getDrawable(R.drawable.ic_error);
                                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                                    EditText editNroFactura = getView().findViewById(R.id.editNroFactura);
                                    editNroFactura.setError("Revise los datos!", drawable);

                                } else {

                                    // SI NO DEVUELVE UNA FACTURA VALIDA EL CAMPO
                                    Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                                    EditText editNroFactura = getView().findViewById(R.id.editNroFactura);
                                    editNroFactura.setError("Datos correctos!", drawable);
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

    private void consultarProveedor() {

        // CONSULTA POR CÓDIGO DE PROVEEDOR SI YA FUE INGRESADO PARA OBTENER EL RESTO DE LOS DATOS
        String URL = "http://malpicas.heliohost.org/malpica/gastos/gastos_consultar_proveedor.php?parameter=" + datoCodigo;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                GastosSetters setters = new GastosSetters();

                                setters.setCodigo(jsonObject.getString("codigo"));
                                setters.setRazonSocial(jsonObject.getString("razon_social"));
                                setters.setCondicion(jsonObject.getString("condicion"));

                                String razonSocial = setters.getRazonSocial();
                                String condicion = setters.getCondicion();

                                if(!razonSocial.equals("No existe")){

                                    // SI DEVUELVE EL PROVEEDOR MUESTRA LOS DATOS EN LOS CAMPOS, SINO DEJA EN BLANCO
                                    EditText editRazonSocial = getView().findViewById(R.id.editRazonSocial);
                                    EditText editCondicion = getView().findViewById(R.id.editCondicion);
                                    editRazonSocial.setText(razonSocial);
                                    editCondicion.setText(condicion);
                                    datoRazonSocial = razonSocial + "";
                                    datoCondicion = condicion + "";

                                    Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                                    editRazonSocial.setError("Datos correctos!", drawable);
                                    editCondicion.setError("Datos correctos!", drawable);

                                    EditText editCodigoStock = getView().findViewById(R.id.editCodigoStock);
                                    editCodigoStock.requestFocus();
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

        // CONSULTA POR CÓDIGO DE PRODUCTO SI YA FUE INGRESADO PARA OBTENER EL RESTO DE LOS DATOS
        String URL = "http://malpicas.heliohost.org/malpica/gastos/gastos_consultar_producto.php?parameter=" + datoCodigoStock;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                GastosSetters setters = new GastosSetters();

                                setters.setCodigoStock(jsonObject.getString("codigo_stock"));
                                setters.setDescripcionStock(jsonObject.getString("descripcion_stock"));
                                setters.setCantidad(jsonObject.getString("cantidad"));
                                setters.setMoneda(jsonObject.getString("moneda"));
                                setters.setPrecioUnit(jsonObject.getString("precio_unit"));
                                setters.setPrecioTotal(jsonObject.getString("precio_total"));

                                String descripcionStock = setters.getDescripcionStock();
                                String precioUnitStock = setters.getPrecioUnit();

                                if(!descripcionStock.equals("No existe")){

                                    // SI DEVUELVE EL PRODUCTO MUESTRA LOS DATOS EN LOS CAMPOS, SINO DEJA EN BLANCO
                                    EditText editDescripcionStock = getView().findViewById(R.id.editDescripcionStock);
                                    EditText editPrecioUnit = getView().findViewById(R.id.editPrecioUnit);
                                    editDescripcionStock.setText(descripcionStock);
                                    editPrecioUnit.setText(precioUnitStock);
                                    datoDescripcionStock = descripcionStock + "";
                                    datoPrecioUnit = precioUnitStock + "";

                                    Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                                    editDescripcionStock.setError("Datos correctos!", drawable);
                                    editPrecioUnit.setError("Datos correctos!", drawable);

                                    EditText editCantidad = getView().findViewById(R.id.editCantidad);
                                    editCantidad.requestFocus();
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

    private void consultarProveedorBis() {

        // CONSULTA EL PROVEEDOR DE VUELTA PARA VER SI SE ENCUENTRA O NO EN LA TABLA DE PROVEEDORES
        String URL = "http://malpicas.heliohost.org/malpica/gastos/gastos_consultar_proveedor.php?parameter=" + datoCodigo;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                GastosSetters setters = new GastosSetters();

                                setters.setCodigo(jsonObject.getString("codigo"));
                                setters.setRazonSocial(jsonObject.getString("razon_social"));
                                setters.setCondicion(jsonObject.getString("condicion"));

                                String razonSocial = setters.getRazonSocial();

                                if(razonSocial.equals("No existe")){

                                    // SI NO DEVUELVE EL PROVEEDOR, REGISTRA LO BÁSICO EN LA TABLA DE PROVEEDORES
                                    dialogProcesando();
                                    registrarProveedor();
                                }

                                dialogProcesando();
                                consultarProductoBis();
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

    private void registrarProveedor() {

        // REGISTRA EL PROVEEDOR EN CASO DE NO HABER SIDO DADO DE ALTA ANTERIORMENTE CON LOS DATOS BÁSICOS
        String URL = "http://malpicas.heliohost.org/malpica/gastos/gastos_ingresar_proveedor.php";
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

                parameter.put("codigo",datoCodigo);
                parameter.put("razon_social",datoRazonSocial);
                parameter.put("condicion",datoCondicion);
                parameter.put("fecha_alta",datoFechaActual);
                parameter.put("hora_alta",datoHoraActual);
                parameter.put("fecha_modif",datoFechaActual);
                parameter.put("hora_modif",datoHoraActual);
                parameter.put("dia_alta",datoDia);
                parameter.put("mes_alta",datoMes);
                parameter.put("ano_alta",datoAno);
                parameter.put("dia_modif",datoDia);
                parameter.put("mes_modif",datoMes);
                parameter.put("ano_modif",datoAno);

                return parameter;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void consultarProductoBis() {

        if(!datoCodigoStock.equals(codigoFactura)){

            // SI EL PRODUCTO CAMBIÓ, TIENE QUE CONSULTAR EN LA BASE SI ESTÁ CREADO O HAY QUE CREARLO
            String URL = "http://malpicas.heliohost.org/malpica/gastos/gastos_consultar_producto.php?parameter=" + datoCodigoStock;
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                JSONArray jsonArray = response.getJSONArray("data");

                                // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    GastosSetters setters = new GastosSetters();

                                    setters.setCodigoStock(jsonObject.getString("codigo_stock"));
                                    setters.setDescripcionStock(jsonObject.getString("descripcion_stock"));
                                    setters.setCantidad(jsonObject.getString("cantidad"));
                                    setters.setMoneda(jsonObject.getString("moneda"));
                                    setters.setPrecioUnit(jsonObject.getString("precio_unit"));
                                    setters.setPrecioTotal(jsonObject.getString("precio_total"));

                                    String descripcionStock = setters.getDescripcionStock();
                                    String cantidadStock = setters.getCantidad();
                                    String precioUnitStock = setters.getPrecioUnit();

                                    if(descripcionStock.equals("No existe")){

                                        // SI NO DEVUELVE EL PRODUCTO, REGISTRA LO BÁSICO EN LA TABLA DE STOCK
                                        dialogProcesando();
                                        registrarProducto();

                                    } else {

                                        // SI DEVUELVE EL PRODUCTO, ACTUALIZA LOS VALORES DEL PRODUCTO NUEVO
                                        int cantidadGetter = Integer.parseInt(cantidadStock);
                                        nuevaCantidad = cantidadGetter + cantidad;
                                        double precioUnitGetter = Double.parseDouble(precioUnitStock);
                                        double preciototal = nuevaCantidad * precioUnitGetter;

                                        DecimalFormat decimalFormat = new DecimalFormat("#.00");
                                        nuevoPrecioTotal = decimalFormat.format(preciototal).replace(",",".");

                                        if(preciototal == 0){
                                            nuevoPrecioTotal = "0.00";
                                        }

                                        dialogProcesando();
                                        actualizarProductoNuevo();
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

        } else {

            // SI EL PRODUCTO NO CAMBIÓ, TIENE QUE CONSULTAR EN LA BASE LA CANTIDAD Y EL PRECIO UNITARIO PARA ACTUALIZAR STOCK
            String URL = "http://malpicas.heliohost.org/malpica/gastos/gastos_consultar_producto.php?parameter=" + datoCodigoStock;
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                JSONArray jsonArray = response.getJSONArray("data");

                                // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    GastosSetters setters = new GastosSetters();

                                    setters.setCodigoStock(jsonObject.getString("codigo_stock"));
                                    setters.setDescripcionStock(jsonObject.getString("descripcion_stock"));
                                    setters.setCantidad(jsonObject.getString("cantidad"));
                                    setters.setMoneda(jsonObject.getString("moneda"));
                                    setters.setPrecioUnit(jsonObject.getString("precio_unit"));
                                    setters.setPrecioTotal(jsonObject.getString("precio_total"));

                                    String cantidadStock = setters.getCantidad();
                                    String precioUnitStock = setters.getPrecioUnit();

                                    // ACTUALIZA LOS VALORES DEL MISMO PRODUCTO
                                    int cantidadGetter = Integer.parseInt(cantidadStock);
                                    nuevaCantidad = cantidadGetter - cantidadFactura + cantidad;
                                    double precioUnitGetter = Double.parseDouble(precioUnitStock);
                                    double preciototal = nuevaCantidad * precioUnitGetter;

                                    DecimalFormat decimalFormat = new DecimalFormat("#.00");
                                    nuevoPrecioTotal = decimalFormat.format(preciototal).replace(",",".");

                                    if(preciototal == 0){
                                        nuevoPrecioTotal = "0.00";
                                    }

                                    actualizarProductoExistente();
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

    private void registrarProducto() {

        // REGISTRA EL PRODUCTO EN CASO DE NO HABER SIDO DADO DE ALTA ANTERIORMENTE
        final String datoMoneda = "ARS";

        String URL = "http://malpicas.heliohost.org/malpica/gastos/gastos_ingresar_producto.php";
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
                parameter.put("descripcion_stock",datoDescripcionStock);
                parameter.put("cantidad",datoCantidad);
                parameter.put("moneda",datoMoneda);
                parameter.put("precio_unit",datoPrecioUnit);
                parameter.put("precio_total",datoPrecioTotal);
                parameter.put("fecha_alta",datoFechaActual);
                parameter.put("hora_alta",datoHoraActual);
                parameter.put("fecha_modif",datoFechaActual);
                parameter.put("hora_modif",datoHoraActual);
                parameter.put("dia_alta",datoDia);
                parameter.put("mes_alta",datoMes);
                parameter.put("ano_alta",datoAno);
                parameter.put("dia_modif",datoDia);
                parameter.put("mes_modif",datoMes);
                parameter.put("ano_modif",datoAno);

                return parameter;
            }
        };
        requestQueue.add(stringRequest);

        // CONTINÚA CON LA ACTUALIZACIÓN DEL PRODUCTO ANTERIOR
        dialogProcesando();
        consultarProductoAnterior();
    }

    private void actualizarProductoExistente() {

        // ACTUALIZA EL PRODUCTO EN LA TABLA DE STOCK CON LA NUEVA CANTIDAD Y EL PRECIO TOTAL DEL MISMO PRODUCTO
        final String datoCantidadNueva = nuevaCantidad + "";
        final String datoPrecioTotaNuevo = nuevoPrecioTotal + "";

        String URL = "http://malpicas.heliohost.org/malpica/gastos/gastos_actualizar_producto.php";
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
                parameter.put("cantidad",datoCantidadNueva);
                parameter.put("precio_total",datoPrecioTotaNuevo);
                parameter.put("fecha_modif",datoFechaActual);
                parameter.put("hora_modif",datoHoraActual);
                parameter.put("dia_modif",datoDia);
                parameter.put("mes_modif",datoMes);
                parameter.put("ano_modif",datoAno);

                return parameter;
            }
        };
        requestQueue.add(stringRequest);

        // CONTINÚA CON LA MODIFICACIÓN Y MUESTRA MENSAJE DE OPERACIÓN REGISTRADA CON ÉXITO
        dialogOk();
        actualizarGasto();
    }

    private void actualizarProductoNuevo() {

        // ACTUALIZA EL PRODUCTO EN LA TABLA DE STOCK CON LA NUEVA CANTIDAD Y EL PRECIO TOTAL DEL PRODUCTO NUEVO
        final String datoCantidadNueva = nuevaCantidad + "";
        final String datoPrecioTotaNuevo = nuevoPrecioTotal + "";

        String URL = "http://malpicas.heliohost.org/malpica/gastos/gastos_actualizar_producto.php";
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
                parameter.put("cantidad",datoCantidadNueva);
                parameter.put("precio_total",datoPrecioTotaNuevo);
                parameter.put("fecha_modif",datoFechaActual);
                parameter.put("hora_modif",datoHoraActual);
                parameter.put("dia_modif",datoDia);
                parameter.put("mes_modif",datoMes);
                parameter.put("ano_modif",datoAno);

                return parameter;
            }
        };
        requestQueue.add(stringRequest);

        // CONTINÚA CON LA ACTUALIZACIÓN DEL PRODUCTO ANTERIOR
        dialogProcesando();
        consultarProductoAnterior();
    }

    private void consultarProductoAnterior() {

        // CONSULTA POR CÓDIGO DE PRODUCTO SI YA FUE INGRESADO PARA OBTENER EL RESTO DE LOS DATOS
        String URL = "http://malpicas.heliohost.org/malpica/gastos/gastos_consultar_producto.php?parameter=" + codigoFactura;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                GastosSetters setters = new GastosSetters();

                                setters.setCodigoStock(jsonObject.getString("codigo_stock"));
                                setters.setDescripcionStock(jsonObject.getString("descripcion_stock"));
                                setters.setCantidad(jsonObject.getString("cantidad"));
                                setters.setMoneda(jsonObject.getString("moneda"));
                                setters.setPrecioUnit(jsonObject.getString("precio_unit"));
                                setters.setPrecioTotal(jsonObject.getString("precio_total"));

                                String cantidadStock = setters.getCantidad();
                                String precioUnitStock = setters.getPrecioUnit();

                                // ACTUALIZA LOS VALORES DEL PRODUCTO ANTERIOR
                                int cantidadGetter = Integer.parseInt(cantidadStock);
                                nuevaCantidad = cantidadGetter - cantidadFactura;
                                double precioUnitGetter = Double.parseDouble(precioUnitStock);
                                double preciototal = nuevaCantidad * precioUnitGetter;

                                DecimalFormat decimalFormat = new DecimalFormat("#.00");
                                nuevoPrecioTotal = decimalFormat.format(preciototal).replace(",",".");

                                if (preciototal == 0){
                                    nuevoPrecioTotal = "0.00";
                                }

                                dialogProcesando();
                                actualizarProductoAnterior();
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

    private void actualizarProductoAnterior() {

        // ACTUALIZA EL PRODUCTO EN LA TABLA DE STOCK CON LA NUEVA CANTIDAD Y EL PRECIO TOTAL DEL PRODUCTO ANTERIOR
        final String datoCantidadNueva = nuevaCantidad + "";
        final String datoPrecioTotaNuevo = nuevoPrecioTotal + "";

        String URL = "http://malpicas.heliohost.org/malpica/gastos/gastos_actualizar_producto.php";
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

                parameter.put("codigo_stock",codigoFactura);
                parameter.put("cantidad",datoCantidadNueva);
                parameter.put("precio_total",datoPrecioTotaNuevo);
                parameter.put("fecha_modif",datoFechaActual);
                parameter.put("hora_modif",datoHoraActual);
                parameter.put("dia_modif",datoDia);
                parameter.put("mes_modif",datoMes);
                parameter.put("ano_modif",datoAno);

                return parameter;
            }
        };
        requestQueue.add(stringRequest);

        // CONTINÚA CON LA MODIFICACIÓN Y MUESTRA MENSAJE DE OPERACIÓN REGISTRADA CON ÉXITO
        dialogOk();
        actualizarGasto();
    }

    private void actualizarGasto() {

        // ANTES DE TERMINAR DE ACTUALIZAR, CON UN ARRAY DIVIDE LAS FECHAS INGRESADAS
        String[] arrayFechaFactura = datoFechaFactura.split("/");
        final String datoDiaFactura = arrayFechaFactura[0];
        final String datoMesFactura = arrayFechaFactura[1];
        final String datoAnoFactura = arrayFechaFactura[2];

        String[] arrayFechaIngreso = datoFechaIngreso.split("/");
        final String datoDiaIngreso = arrayFechaIngreso[0];
        final String datoMesIngreso = arrayFechaIngreso[1];
        final String datoAnoIngreso = arrayFechaIngreso[2];

        // FINALMENTE MODIFICA LA FACTURA CON LOS DATOS INGRESADOS
        String URL1 = "http://malpicas.heliohost.org/malpica/gastos/gastos_actualizar_factura.php";
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL1, new Response.Listener<String>() {
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

                parameter.put("factura_vieja",datoBuscarFactura);
                parameter.put("factura_nueva",datoNroFactura);
                parameter.put("fecha_factura",datoFechaFactura);
                parameter.put("fecha_ingreso",datoFechaIngreso);
                parameter.put("fecha_modif",datoFechaActual);
                parameter.put("hora_modif",datoHoraActual);
                parameter.put("codigo",datoCodigo);
                parameter.put("razon_social",datoRazonSocial);
                parameter.put("condicion",datoCondicion);
                parameter.put("codigo_stock",datoCodigoStock);
                parameter.put("descripcion_stock",datoDescripcionStock);
                parameter.put("cantidad",datoCantidad);
                parameter.put("precio_unit",datoPrecioUnit);
                parameter.put("impuestos",datoImpuestos);
                parameter.put("precio_total",datoPrecioTotal);
                parameter.put("dia_factura",datoDiaFactura);
                parameter.put("mes_factura",datoMesFactura);
                parameter.put("ano_factura",datoAnoFactura);
                parameter.put("dia_ingreso",datoDiaIngreso);
                parameter.put("mes_ingreso",datoMesIngreso);
                parameter.put("ano_ingreso",datoAnoIngreso);
                parameter.put("dia_modif",datoDia);
                parameter.put("mes_modif",datoMes);
                parameter.put("ano_modif",datoAno);

                return parameter;
            }
        };
        requestQueue.add(stringRequest1);
    }
}
