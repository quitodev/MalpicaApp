package com.example.malpicasoft.gastos;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
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

public class ModificarGastos extends Fragment {

    private RequestQueue requestQueue;
    private JsonObjectRequest jsonObjectRequest;
    private String datoFechaActual, datoFechaCompra, datoFechaIngresada, datoCodigoProv, datoRazonSocialProv,
            datoCondicionProv, datoCodigoProd, datoDescripcionProd, datoCantidad, datoPrecioUnit, datoImpuestos,
            datoPrecioTotal, datoBuscarFactura, datoDia, datoMes, datoAno, nuevoPrecioTotal, codigoFactura;
    private int counter, cantidad, nuevaCantidad, cantidadFactura;
    private double impuestos, precioUnit, precioTotal;
    
    public ModificarGastos() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_modificar_gastos, container, false);

        dateFragments();

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
        Button buttonModificar = root.findViewById(R.id.buttonModificar);
        Button buttonGuardar = root.findViewById(R.id.buttonGuardar);

        // EVENTOS DEL BOTÓN CONSULTAR
        buttonConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Button buttonModificar = getView().findViewById(R.id.buttonModificar);
                buttonModificar.setVisibility(View.VISIBLE);

                editFechaCompra.setFocusable(false);
                editFechaIngresada.setFocusable(false);
                editCodigoProv.setFocusable(false);
                editRazonSocialProv.setFocusable(false);
                editCondicionProv.setFocusable(false);
                editCodigoProd.setFocusable(false);
                editDescripcionProd.setFocusable(false);
                editCantidad.setFocusable(false);
                editPrecioUnit.setFocusable(false);
                editImpuestos.setFocusable(false);
                editPrecioTotal.setFocusable(false);

                editFechaCompra.setError(null);
                editFechaIngresada.setError(null);
                editCodigoProv.setError(null);
                editRazonSocialProv.setError(null);
                editCondicionProv.setError(null);
                editCodigoProd.setError(null);
                editDescripcionProd.setError(null);
                editCantidad.setError(null);
                editPrecioUnit.setError(null);
                editImpuestos.setError(null);
                editPrecioTotal.setError(null);

                datoBuscarFactura = editBuscarFactura.getText().toString();
                editBuscarFactura.clearFocus();

                if(!datoBuscarFactura.isEmpty()){

                    // SI EL CAMPO NO ESTÁ VACÍO, CONSULTA A LA BASE
                    dialogProcesando();
                    consultarFactura();

                } else {

                    // SI EL CAMPO ESTÁ VACÍO, MUESTRA UN MENSAJE DE ERROR
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

        // EVENTOS DEL BOTÓN MODIFICAR
        buttonModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datoFechaCompra = editFechaCompra.getText().toString();

                if(!datoFechaCompra.isEmpty()){

                    // SI DEVUELVE UNA FACTURA, HABILITA LOS CAMPOS PARA HACER MODIFICACIONES
                    Button buttonModificar = getView().findViewById(R.id.buttonModificar);
                    buttonModificar.setVisibility(View.INVISIBLE);

                    Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

                    editFechaIngresada.setError("Datos correctos!", drawable);
                    editCodigoProv.setError("Datos correctos!", drawable);
                    editRazonSocialProv.setError("Datos correctos!", drawable);
                    editCondicionProv.setError("Datos correctos!", drawable);
                    editCodigoProd.setError("Datos correctos!", drawable);
                    editDescripcionProd.setError("Datos correctos!", drawable);
                    editCantidad.setError("Datos correctos!", drawable);
                    editPrecioUnit.setError("Datos correctos!", drawable);
                    editImpuestos.setError("Datos correctos!", drawable);
                    editPrecioTotal.setError("Datos correctos!", drawable);

                    editFechaCompra.setFocusableInTouchMode(true);
                    editFechaIngresada.setFocusableInTouchMode(true);
                    editCodigoProv.setFocusableInTouchMode(true);
                    editRazonSocialProv.setFocusableInTouchMode(true);
                    editCondicionProv.setFocusableInTouchMode(true);
                    editCodigoProd.setFocusableInTouchMode(true);
                    editDescripcionProd.setFocusableInTouchMode(true);
                    editCantidad.setFocusableInTouchMode(true);
                    editPrecioUnit.setFocusableInTouchMode(true);
                    editImpuestos.setFocusableInTouchMode(true);
                    editPrecioTotal.setFocusableInTouchMode(true);

                    editFechaCompra.requestFocusFromTouch();

                    ScrollView scrollView = getView().findViewById(R.id.scroll);
                    scrollView.setScrollY(0);
                }
            }
        });

        // EVENTOS DEL BOTÓN GUARDAR
        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editBuscarFactura = getView().findViewById(R.id.editBuscarFactura);
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

                datoBuscarFactura = editBuscarFactura.getText().toString();
                datoFechaCompra = editFechaCompra.getText().toString();
                datoFechaIngresada = editFechaIngresada.getText().toString();
                datoCodigoProv = editCodigoProv.getText().toString();
                datoRazonSocialProv = editRazonSocialProv.getText().toString();
                datoCondicionProv = editCondicionProv.getText().toString();
                datoCodigoProd = editCodigoProd.getText().toString();
                datoDescripcionProd = editDescripcionProd.getText().toString();
                datoCantidad = editCantidad.getText().toString();
                datoPrecioUnit = editPrecioUnit.getText().toString();
                datoImpuestos = editImpuestos.getText().toString();
                datoPrecioTotal = editPrecioTotal.getText().toString();

                editFechaCompra.setFocusable(false);
                editFechaIngresada.setFocusable(false);
                editCodigoProv.setFocusable(false);
                editRazonSocialProv.setFocusable(false);
                editCondicionProv.setFocusable(false);
                editCodigoProd.setFocusable(false);
                editDescripcionProd.setFocusable(false);
                editCantidad.setFocusable(false);
                editPrecioUnit.setFocusable(false);
                editImpuestos.setFocusable(false);
                editPrecioTotal.setFocusable(false);

                if (editFechaCompra.getError() == "Datos correctos!"
                        && editFechaIngresada.getError() == "Datos correctos!"
                        && editCodigoProv.getError() == "Datos correctos!"
                        && editRazonSocialProv.getError() == "Datos correctos!"
                        && editCondicionProv.getError() == "Datos correctos!"
                        && editCodigoProd.getError() == "Datos correctos!"
                        && editDescripcionProd.getError() == "Datos correctos!"
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

                    editFechaCompra.setFocusableInTouchMode(true);
                    editFechaIngresada.setFocusableInTouchMode(true);
                    editCodigoProv.setFocusableInTouchMode(true);
                    editRazonSocialProv.setFocusableInTouchMode(true);
                    editCondicionProv.setFocusableInTouchMode(true);
                    editCodigoProd.setFocusableInTouchMode(true);
                    editDescripcionProd.setFocusableInTouchMode(true);
                    editCantidad.setFocusableInTouchMode(true);
                    editPrecioUnit.setFocusableInTouchMode(true);
                    editImpuestos.setFocusableInTouchMode(true);
                    editPrecioTotal.setFocusableInTouchMode(true);

                    editPrecioTotal.requestFocusFromTouch();
                    editPrecioTotal.setError(null);
                }
            }
        });

        // EVENTOS AL CAMBIAR DE CAMPOS
        editFechaCompra.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, GUARDA Y VALIDA LOS DATOS INGRESADOS
                    String datoFechaLarga = editFechaCompra.getText().toString();
                    datoFechaCompra = datoFechaLarga.replace("/20", "/");

                    if (datoFechaLarga.length() == 10 && datoFechaLarga.contains("/")) {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editFechaCompra.setError("Datos correctos!", drawable);

                    } else {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_error);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editFechaCompra.setError("Revise los datos!", drawable);
                    }

                } else {

                    // SI INGRESA AL CAMPO, OCULTA EL ÍCONO DE VALIDACIÓN
                    editFechaCompra.setError(null);
                }
            }
        });
        editFechaIngresada.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, GUARDA Y VALIDA LOS DATOS INGRESADOS
                    String datoFechaLarga = editFechaIngresada.getText().toString();
                    datoFechaIngresada = datoFechaLarga.replace("/20", "/");

                    if (datoFechaLarga.length() == 10 && datoFechaLarga.contains("/")) {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editFechaIngresada.setError("Datos correctos!", drawable);

                    } else {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_error);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editFechaIngresada.setError("Revise los datos!", drawable);
                    }

                } else {

                    // SI INGRESA AL CAMPO, OCULTA EL ÍCONO DE VALIDACIÓN
                    editFechaIngresada.setError(null);
                }
            }
        });
        editCodigoProv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, GUARDA Y VALIDA LOS DATOS INGRESADOS
                    datoCodigoProv = editCodigoProv.getText().toString();

                    if (!datoCodigoProv.isEmpty()) {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editCodigoProv.setError("Datos correctos!", drawable);

                        // CONSULTA LOS DATOS INGRESADOS EN LA BASE PARA AGILIZAR LA CARGA
                        dialogProcesando();
                        consultarProveedor();

                    } else {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_error);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editCodigoProv.setError("Revise los datos!", drawable);
                    }

                } else {

                    // SI INGRESA AL CAMPO, OCULTA EL ÍCONO DE VALIDACIÓN
                    editCodigoProv.setError(null);
                }
            }
        });
        editRazonSocialProv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, GUARDA Y VALIDA LOS DATOS INGRESADOS
                    datoRazonSocialProv = editRazonSocialProv.getText().toString();

                    if (!datoRazonSocialProv.isEmpty()) {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editRazonSocialProv.setError("Datos correctos!", drawable);

                    } else {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_error);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editRazonSocialProv.setError("Revise los datos!", drawable);
                    }

                } else {

                    // SI INGRESA AL CAMPO, OCULTA EL ÍCONO DE VALIDACIÓN
                    editRazonSocialProv.setError(null);
                }
            }
        });
        editCondicionProv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, GUARDA Y VALIDA LOS DATOS INGRESADOS
                    datoCondicionProv = editCondicionProv.getText().toString();

                    if (datoCondicionProv.length() == 2) {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editCondicionProv.setError("Datos correctos!", drawable);

                    } else {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_error);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editCondicionProv.setError("Revise los datos!", drawable);
                    }

                } else {

                    // SI INGRESA AL CAMPO, MUESTRA UN DIALOG CON INFORMACIÓN RELEVANTE
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_help);
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                    editCondicionProv.setError("RI (Resp. Inscripto)\nMT (Resp. Mono.)\nCF (Cons. Final)", drawable);
                }
            }
        });
        editCodigoProd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, GUARDA Y VALIDA LOS DATOS INGRESADOS
                    datoCodigoProd = editCodigoProd.getText().toString();

                    if (!datoCodigoProd.isEmpty()) {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editCodigoProd.setError("Datos correctos!", drawable);

                        // CONSULTA LOS DATOS INGRESADOS EN LA BASE PARA AGILIZAR LA CARGA
                        dialogProcesando();
                        consultarProducto();

                    } else {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_error);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editCodigoProd.setError("Revise los datos!", drawable);
                    }

                } else {

                    // SI INGRESA AL CAMPO, OCULTA EL ÍCONO DE VALIDACIÓN
                    editCodigoProd.setError(null);
                }
            }
        });
        editDescripcionProd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, GUARDA Y VALIDA LOS DATOS INGRESADOS
                    datoDescripcionProd = editDescripcionProd.getText().toString();

                    if (!datoDescripcionProd.isEmpty()) {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editDescripcionProd.setError("Datos correctos!", drawable);

                    } else {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_error);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editDescripcionProd.setError("Revise los datos!", drawable);
                    }

                } else {

                    // SI INGRESA AL CAMPO, OCULTA EL ÍCONO DE VALIDACIÓN
                    editDescripcionProd.setError(null);
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
            }
        }, 3000);
    }

    private void dialogOk(){

        // DIALOG CON CONFIRMACIÓN DE OPERACIÓN REALIZADA CON ÉXITO
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Dialogs dialogs = new Dialogs(getActivity());
                dialogs.startOk();

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

                editFechaCompra.setError(null);
                editFechaIngresada.setError(null);
                editCodigoProv.setError(null);
                editRazonSocialProv.setError(null);
                editCondicionProv.setError(null);
                editCodigoProd.setError(null);
                editDescripcionProd.setError(null);
                editCantidad.setError(null);
                editPrecioUnit.setError(null);
                editImpuestos.setError(null);
                editPrecioTotal.setError(null);

                ScrollView scrollView = getView().findViewById(R.id.scroll);
                scrollView.setScrollY(0);

                Button buttonModificar = getView().findViewById(R.id.buttonModificar);
                buttonModificar.setVisibility(View.VISIBLE);
            }
        }, 4000);
    }

    private void consultarFactura() {

        // CONSULTA LA FACTURA INGRESADA EN LA BASE DE DATOS
        String URL = "http://malpicas.heliohost.org/malpica/gastos/gastos_buscar_factura.php?factura=" + datoBuscarFactura;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("factura");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                GastosSetters gastosSetters = new GastosSetters();

                                gastosSetters.setFechaCompra(jsonObject.getString("fecha_compra"));
                                gastosSetters.setFechaIngreso(jsonObject.getString("fecha_ingreso"));
                                gastosSetters.setCodigoProv(jsonObject.getString("codigo_prov"));
                                gastosSetters.setNombreProv(jsonObject.getString("nombre_prov"));
                                gastosSetters.setCondicionProv(jsonObject.getString("condicion_prov"));
                                gastosSetters.setCodigoProd(jsonObject.getString("codigo"));
                                gastosSetters.setDescripcionProd(jsonObject.getString("descripcion"));
                                gastosSetters.setCantidadProd(jsonObject.getString("cantidad"));
                                gastosSetters.setPrecioUnitProd(jsonObject.getString("precio_unit"));
                                gastosSetters.setImpuestos(jsonObject.getString("impuestos"));
                                gastosSetters.setPrecioTotalProd(jsonObject.getString("precio_final"));

                                String fechaCompra = gastosSetters.getFechaCompra();
                                String fechaIngreso = gastosSetters.getFechaIngreso();
                                String codigoProv = gastosSetters.getCodigoProv();
                                String nombreProv = gastosSetters.getNombreProv();
                                String condicionProv = gastosSetters.getCondicionProv();
                                String codigoProd = gastosSetters.getCodigoProd();
                                String descripcionProd = gastosSetters.getDescripcionProd();
                                String cantidadProd = gastosSetters.getCantidadProd();
                                String precioUnitProd = gastosSetters.getPrecioUnitProd();
                                String impuestos = gastosSetters.getImpuestos();
                                String precioTotalProd = gastosSetters.getPrecioTotalProd();

                                if(!fechaCompra.equals("No existe")){

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

                                    codigoFactura = codigoProd + "";
                                    cantidadFactura = Integer.parseInt(cantidadProd);

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

                                    // SI NO DEVUELVE UNA FACTURA MUESTRA UN MENSAJE DE ERROR Y LIMPIA LOS CAMPOS
                                    dialogError();

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

    private void consultarProveedor() {

        // CONSULTA POR CÓDIGO DE PROVEEDOR SI YA FUE INGRESADO PARA OBTENER EL RESTO DE LOS DATOS
        String URL = "http://malpicas.heliohost.org/malpica/gastos/gastos_consultar_proveedor.php?codigo_prov=" + datoCodigoProv;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("proveedores");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                GastosSetters gastosSetters = new GastosSetters();

                                gastosSetters.setCodigoProv(jsonObject.getString("codigo_prov"));
                                gastosSetters.setNombreProv(jsonObject.getString("nombre_prov"));
                                gastosSetters.setCondicionProv(jsonObject.getString("condicion_prov"));

                                String nombreProv = gastosSetters.getNombreProv();
                                String condicionProv = gastosSetters.getCondicionProv();

                                if(!nombreProv.equals("No existe")){

                                    // SI DEVUELVE EL PROVEEDOR MUESTRA LOS DATOS EN LOS CAMPOS, SINO DEJA EN BLANCO
                                    EditText editRazonSocialProv = getView().findViewById(R.id.editRazonSocialProv);
                                    EditText editCondicionProv = getView().findViewById(R.id.editCondicionProv);
                                    editRazonSocialProv.setText(nombreProv);
                                    editCondicionProv.setText(condicionProv);
                                    datoRazonSocialProv = nombreProv + "";
                                    datoCondicionProv = condicionProv + "";

                                    Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                                    editRazonSocialProv.setError("Datos correctos!", drawable);
                                    editCondicionProv.setError("Datos correctos!", drawable);

                                    EditText editCodigoProd = getView().findViewById(R.id.editCodigoProd);
                                    editCodigoProd.requestFocus();
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

        // CONSULTA POR CÓDIGO DE PRODUCTO SI YA FUE INGRESADO PARA OBTENER EL RESTO DE LOS DATOS
        String URL = "http://malpicas.heliohost.org/malpica/gastos/gastos_consultar_producto.php?codigo=" + datoCodigoProd;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("stock");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                GastosSetters gastosSetters = new GastosSetters();

                                gastosSetters.setCodigoProd(jsonObject.getString("codigo"));
                                gastosSetters.setDescripcionProd(jsonObject.getString("descripcion"));
                                gastosSetters.setCantidadProd(jsonObject.getString("cantidad"));
                                gastosSetters.setMonedaProd(jsonObject.getString("moneda"));
                                gastosSetters.setPrecioUnitProd(jsonObject.getString("precio_unit"));
                                gastosSetters.setPrecioTotalProd(jsonObject.getString("precio_total"));

                                String descripcionProd = gastosSetters.getDescripcionProd();
                                String precioUnitProd = gastosSetters.getPrecioUnitProd();

                                if(!descripcionProd.equals("No existe")){

                                    // SI DEVUELVE EL PRODUCTO MUESTRA LOS DATOS EN LOS CAMPOS, SINO DEJA EN BLANCO
                                    EditText editDescripcionProd = getView().findViewById(R.id.editDescripcionProd);
                                    EditText editPrecioUnitProd = getView().findViewById(R.id.editPrecioUnit);
                                    editDescripcionProd.setText(descripcionProd);
                                    editPrecioUnitProd.setText(precioUnitProd);
                                    datoDescripcionProd = descripcionProd + "";
                                    datoPrecioUnit = precioUnitProd + "";

                                    Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                                    editDescripcionProd.setError("Datos correctos!", drawable);
                                    editPrecioUnitProd.setError("Datos correctos!", drawable);

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
                Toast.makeText(getContext(), "Por favor, revise su conexión!", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void consultarProveedorBis() {

        // CONSULTA EL PROVEEDOR DE VUELTA PARA VER SI SE ENCUENTRA O NO EN LA TABLA DE PROVEEDORES
        String URL = "http://malpicas.heliohost.org/malpica/gastos/gastos_consultar_proveedor.php?codigo_prov=" + datoCodigoProv;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("proveedores");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                GastosSetters gastosSetters = new GastosSetters();

                                gastosSetters.setCodigoProv(jsonObject.getString("codigo_prov"));
                                gastosSetters.setNombreProv(jsonObject.getString("nombre_prov"));
                                gastosSetters.setCondicionProv(jsonObject.getString("condicion_prov"));

                                String nombreProv = gastosSetters.getNombreProv();

                                if(nombreProv.equals("No existe")){

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
                Toast.makeText(getContext(), "Por favor, revise su conexión!", Toast.LENGTH_LONG).show();
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
                Map<String, String> datosProveedores = new HashMap<>();

                datosProveedores.put("codigo_prov",datoCodigoProv);
                datosProveedores.put("nombre_prov",datoRazonSocialProv);
                datosProveedores.put("condicion_prov",datoCondicionProv);
                datosProveedores.put("fecha_alta",datoFechaActual);
                datosProveedores.put("fecha_modif",datoFechaActual);
                datosProveedores.put("dia_alta",datoDia);
                datosProveedores.put("mes_alta",datoMes);
                datosProveedores.put("ano_alta",datoAno);
                datosProveedores.put("dia_modif",datoDia);
                datosProveedores.put("mes_modif",datoMes);
                datosProveedores.put("ano_modif",datoAno);

                return datosProveedores;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void consultarProductoBis() {

        if(!datoCodigoProd.equals(codigoFactura)){

            // SI EL PRODUCTO CAMBIÓ, TIENE QUE CONSULTAR EN LA BASE SI ESTÁ CREADO O HAY QUE CREARLO
            String URL = "http://malpicas.heliohost.org/malpica/gastos/gastos_consultar_producto.php?codigo=" + datoCodigoProd;
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                JSONArray jsonArray = response.getJSONArray("stock");

                                // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    GastosSetters gastosSetters = new GastosSetters();

                                    gastosSetters.setCodigoProd(jsonObject.getString("codigo"));
                                    gastosSetters.setDescripcionProd(jsonObject.getString("descripcion"));
                                    gastosSetters.setCantidadProd(jsonObject.getString("cantidad"));
                                    gastosSetters.setMonedaProd(jsonObject.getString("moneda"));
                                    gastosSetters.setPrecioUnitProd(jsonObject.getString("precio_unit"));
                                    gastosSetters.setPrecioTotalProd(jsonObject.getString("precio_total"));

                                    String descripcionProd = gastosSetters.getDescripcionProd();
                                    String cantidadProd = gastosSetters.getCantidadProd();
                                    String precioUnitProd = gastosSetters.getPrecioUnitProd();

                                    if(descripcionProd.equals("No existe")){

                                        // SI NO DEVUELVE EL PRODUCTO, REGISTRA LO BÁSICO EN LA TABLA DE STOCK
                                        dialogProcesando();
                                        registrarProducto();

                                    } else {

                                        // SI DEVUELVE EL PRODUCTO, ACTUALIZA LOS VALORES DEL PRODUCTO NUEVO
                                        int cantidadGetter = Integer.parseInt(cantidadProd);
                                        nuevaCantidad = cantidadGetter + cantidad;
                                        double precioUnitGetter = Double.parseDouble(precioUnitProd);
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
                    Toast.makeText(getContext(), "Por favor, revise su conexión!", Toast.LENGTH_LONG).show();
                }
            });
            requestQueue.add(jsonObjectRequest);

        } else {

            // SI EL PRODUCTO NO CAMBIÓ, TIENE QUE CONSULTAR EN LA BASE LA CANTIDAD Y EL PRECIO UNITARIO PARA ACTUALIZAR STOCK
            String URL = "http://malpicas.heliohost.org/malpica/gastos/gastos_consultar_producto.php?codigo=" + datoCodigoProd;
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                JSONArray jsonArray = response.getJSONArray("stock");

                                // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    GastosSetters gastosSetters = new GastosSetters();

                                    gastosSetters.setCodigoProd(jsonObject.getString("codigo"));
                                    gastosSetters.setDescripcionProd(jsonObject.getString("descripcion"));
                                    gastosSetters.setCantidadProd(jsonObject.getString("cantidad"));
                                    gastosSetters.setMonedaProd(jsonObject.getString("moneda"));
                                    gastosSetters.setPrecioUnitProd(jsonObject.getString("precio_unit"));
                                    gastosSetters.setPrecioTotalProd(jsonObject.getString("precio_total"));

                                    String cantidadProd = gastosSetters.getCantidadProd();
                                    String precioUnitProd = gastosSetters.getPrecioUnitProd();

                                    // ACTUALIZA LOS VALORES DEL MISMO PRODUCTO
                                    int cantidadGetter = Integer.parseInt(cantidadProd);
                                    nuevaCantidad = cantidadGetter - cantidadFactura + cantidad;
                                    double precioUnitGetter = Double.parseDouble(precioUnitProd);
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
                    Toast.makeText(getContext(), "Por favor, revise su conexión!", Toast.LENGTH_LONG).show();
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
                Map<String, String> datosProducto = new HashMap<>();

                datosProducto.put("codigo",datoCodigoProd);
                datosProducto.put("descripcion",datoDescripcionProd);
                datosProducto.put("cantidad",datoCantidad);
                datosProducto.put("moneda",datoMoneda);
                datosProducto.put("precio_unit",datoPrecioUnit);
                datosProducto.put("precio_total",datoPrecioTotal);
                datosProducto.put("fecha_alta",datoFechaActual);
                datosProducto.put("fecha_modif",datoFechaActual);
                datosProducto.put("dia_alta",datoDia);
                datosProducto.put("mes_alta",datoMes);
                datosProducto.put("ano_alta",datoAno);
                datosProducto.put("dia_modif",datoDia);
                datosProducto.put("mes_modif",datoMes);
                datosProducto.put("ano_modif",datoAno);

                return datosProducto;
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
                Map<String, String> datosProducto = new HashMap<>();

                datosProducto.put("codigo",datoCodigoProd);
                datosProducto.put("cantidad",datoCantidadNueva);
                datosProducto.put("precio_total",datoPrecioTotaNuevo);
                datosProducto.put("fecha_modif",datoFechaActual);
                datosProducto.put("dia_modif",datoDia);
                datosProducto.put("mes_modif",datoMes);
                datosProducto.put("ano_modif",datoAno);

                return datosProducto;
            }
        };
        requestQueue.add(stringRequest);

        // CONTINÚA CON LA MODIFICACIÓN Y MUESTRA MENSAJE DE OPERACIÓN REGISTRADA CON ÉXITO
        dialogOk();
        actualizarCompra();
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
                Map<String, String> datosProducto = new HashMap<>();

                datosProducto.put("codigo",datoCodigoProd);
                datosProducto.put("cantidad",datoCantidadNueva);
                datosProducto.put("precio_total",datoPrecioTotaNuevo);
                datosProducto.put("fecha_modif",datoFechaActual);
                datosProducto.put("dia_modif",datoDia);
                datosProducto.put("mes_modif",datoMes);
                datosProducto.put("ano_modif",datoAno);

                return datosProducto;
            }
        };
        requestQueue.add(stringRequest);

        // CONTINÚA CON LA ACTUALIZACIÓN DEL PRODUCTO ANTERIOR
        dialogProcesando();
        consultarProductoAnterior();
    }

    private void consultarProductoAnterior() {

        // CONSULTA POR CÓDIGO DE PRODUCTO SI YA FUE INGRESADO PARA OBTENER EL RESTO DE LOS DATOS
        String URL = "http://malpicas.heliohost.org/malpica/gastos/gastos_consultar_producto.php?codigo=" + codigoFactura;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("stock");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                GastosSetters gastosSetters = new GastosSetters();

                                gastosSetters.setCodigoProd(jsonObject.getString("codigo"));
                                gastosSetters.setDescripcionProd(jsonObject.getString("descripcion"));
                                gastosSetters.setCantidadProd(jsonObject.getString("cantidad"));
                                gastosSetters.setMonedaProd(jsonObject.getString("moneda"));
                                gastosSetters.setPrecioUnitProd(jsonObject.getString("precio_unit"));
                                gastosSetters.setPrecioTotalProd(jsonObject.getString("precio_total"));

                                String cantidadProd = gastosSetters.getCantidadProd();
                                String precioUnitProd = gastosSetters.getPrecioUnitProd();

                                // ACTUALIZA LOS VALORES DEL PRODUCTO ANTERIOR
                                int cantidadGetter = Integer.parseInt(cantidadProd);
                                nuevaCantidad = cantidadGetter - cantidadFactura;
                                double precioUnitGetter = Double.parseDouble(precioUnitProd);
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
                Toast.makeText(getContext(), "Por favor, revise su conexión!", Toast.LENGTH_LONG).show();
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
                Map<String, String> datosProducto = new HashMap<>();

                datosProducto.put("codigo",codigoFactura);
                datosProducto.put("cantidad",datoCantidadNueva);
                datosProducto.put("precio_total",datoPrecioTotaNuevo);
                datosProducto.put("fecha_modif",datoFechaActual);
                datosProducto.put("dia_modif",datoDia);
                datosProducto.put("mes_modif",datoMes);
                datosProducto.put("ano_modif",datoAno);

                return datosProducto;
            }
        };
        requestQueue.add(stringRequest);

        // CONTINÚA CON LA MODIFICACIÓN Y MUESTRA MENSAJE DE OPERACIÓN REGISTRADA CON ÉXITO
        dialogOk();
        actualizarCompra();
    }

    private void actualizarCompra() {

        // ANTES DE TERMINAR DE ACTUALIZAR, CON UN ARRAY DIVIDE LAS FECHAS INGRESADAS
        String[] arrayFechaCompra = datoFechaCompra.split("/");
        final String datoDiaCompra = arrayFechaCompra[0];
        final String datoMesCompra = arrayFechaCompra[1];
        final String datoAnoCompra = arrayFechaCompra[2];

        String[] arrayFechaIngresada = datoFechaIngresada.split("/");
        final String datoDiaIngresada = arrayFechaIngresada[0];
        final String datoMesIngresada = arrayFechaIngresada[1];
        final String datoAnoIngresada = arrayFechaIngresada[2];

        // FINALMENTE MODIFICA LA COMPRA CON LOS DATOS INGRESADOS
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
                Map<String, String> datosFactura = new HashMap<>();

                datosFactura.put("fecha_compra",datoFechaCompra);
                datosFactura.put("fecha_ingreso",datoFechaIngresada);
                datosFactura.put("nro_factura",datoBuscarFactura);
                datosFactura.put("codigo_prov",datoCodigoProv);
                datosFactura.put("nombre_prov",datoRazonSocialProv);
                datosFactura.put("condicion_prov",datoCondicionProv);
                datosFactura.put("codigo",datoCodigoProd);
                datosFactura.put("descripcion",datoDescripcionProd);
                datosFactura.put("cantidad",datoCantidad);
                datosFactura.put("precio_unit",datoPrecioUnit);
                datosFactura.put("impuestos",datoImpuestos);
                datosFactura.put("precio_final",datoPrecioTotal);
                datosFactura.put("dia_ingreso",datoDiaIngresada);
                datosFactura.put("mes_ingreso",datoMesIngresada);
                datosFactura.put("ano_ingreso",datoAnoIngresada);
                datosFactura.put("dia_compra",datoDiaCompra);
                datosFactura.put("mes_compra",datoMesCompra);
                datosFactura.put("ano_compra",datoAnoCompra);

                return datosFactura;
            }
        };
        requestQueue.add(stringRequest1);
    }
}
