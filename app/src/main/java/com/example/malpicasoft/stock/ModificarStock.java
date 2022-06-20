package com.example.malpicasoft.stock;

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

public class ModificarStock extends Fragment {

    private RequestQueue requestQueue;
    private JsonObjectRequest jsonObjectRequest;
    private String datoFechaActual, datoHoraActual, datoBuscarCodigo, datoBuscarDescripcion, datoCodigo, datoDescripcion,
            datoCantidad, datoMoneda, datoPrecioUnit, datoPrecioTotal, datoDia, datoMes, datoAno;
    private int counter, cantidad;
    private double precioUnit, precioTotal;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_modificar_stock, container, false);

        dateFragments();

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

        final TextView textBuscarCodigo = root.findViewById(R.id.textBuscarCodigo);
        final TextView textBuscarDescripcion = root.findViewById(R.id.textBuscarDescripcion);

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
                        editDescripcion.setFocusable(false);
                        editCantidad.setFocusable(false);
                        editMoneda.setFocusable(false);
                        editPrecioUnit.setFocusable(false);
                        editPrecioTotal.setFocusable(false);

                        editCodigo.setError(null);
                        editDescripcion.setError(null);
                        editCantidad.setError(null);
                        editMoneda.setError(null);
                        editPrecioUnit.setError(null);
                        editPrecioTotal.setError(null);

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

                            // SI DEVUELVE UN CÓDIGO, HABILITA LOS CAMPOS PARA HACER MODIFICACIONES
                            buttonModificar.setVisibility(View.INVISIBLE);

                            Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

                            editCodigo.setError("Datos correctos!", drawable);
                            editDescripcion.setError("Datos correctos!", drawable);
                            editCantidad.setError("Datos correctos!", drawable);
                            editMoneda.setError("Datos correctos!", drawable);
                            editPrecioUnit.setError("Datos correctos!", drawable);
                            editPrecioTotal.setError("Datos correctos!", drawable);

                            editCodigo.setFocusableInTouchMode(true);
                            editDescripcion.setFocusableInTouchMode(true);
                            editCantidad.setFocusableInTouchMode(true);
                            editMoneda.setFocusableInTouchMode(true);
                            editPrecioUnit.setFocusableInTouchMode(true);
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

                        datoBuscarCodigo = editBuscarCodigo.getText().toString();
                        datoBuscarDescripcion = editBuscarDescripcion.getText().toString();
                        datoCodigo = editCodigo.getText().toString();
                        datoDescripcion = editDescripcion.getText().toString();
                        datoCantidad = editCantidad.getText().toString();
                        datoMoneda = editMoneda.getText().toString();
                        datoPrecioUnit = editPrecioUnit.getText().toString();
                        datoPrecioTotal = editPrecioTotal.getText().toString();

                        editCodigo.setFocusable(false);
                        editDescripcion.setFocusable(false);
                        editCantidad.setFocusable(false);
                        editMoneda.setFocusable(false);
                        editPrecioUnit.setFocusable(false);
                        editPrecioTotal.setFocusable(false);

                        if (editCodigo.getError() == "Datos correctos!"
                                && editDescripcion.getError() == "Datos correctos!"
                                && editCantidad.getError() == "Datos correctos!"
                                && editMoneda.getError() == "Datos correctos!"
                                && editPrecioUnit.getError() == "Datos correctos!"
                                && editPrecioTotal.getError() == "Datos correctos!") {

                            // SI TODOS LOS DATOS ESTÁN OK, PASA A REGISTRARLO
                            editPrecioTotal.requestFocusFromTouch();
                            editPrecioTotal.setError(null);

                            dialogProcesando();
                            modificarProducto();

                        } else {

                            // SI ALGÚN DATO ES INCORRECTO, MUESTRA UN MENSAJE DE ERROR
                            editPrecioTotal.requestFocusFromTouch();
                            editPrecioTotal.setError(null);

                            dialogProcesando();
                            dialogError();

                            editCodigo.setFocusableInTouchMode(true);
                            editDescripcion.setFocusableInTouchMode(true);
                            editCantidad.setFocusableInTouchMode(true);
                            editMoneda.setFocusableInTouchMode(true);
                            editPrecioUnit.setFocusableInTouchMode(true);
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
        editBuscarCodigo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, RECUPERA COLOR DE TEXTO
                    textBuscarCodigo.setTextColor(ContextCompat.getColor(getContext(), R.color.colorText));

                } else {

                    // SI INGRESA AL CAMPO, CAMBIA COLOR DE TEXTO Y ELIMINA LOS DATOS INGRESADOS EN LOS OTROS CAMPOS
                    textBuscarCodigo.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSelected));
                    editBuscarDescripcion.setText("");
                }
            }
        });
        editBuscarDescripcion.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, RECUPERA COLOR DE TEXTO
                    textBuscarDescripcion.setTextColor(ContextCompat.getColor(getContext(), R.color.colorText));

                } else {

                    // SI INGRESA AL CAMPO, CAMBIA COLOR DE TEXTO Y ELIMINA LOS DATOS INGRESADOS EN LOS OTROS CAMPOS
                    textBuscarDescripcion.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSelected));
                    editBuscarCodigo.setText("");
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
        editDescripcion.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, GUARDA Y CORROBORA LOS DATOS INGRESADOS
                    datoDescripcion = editDescripcion.getText().toString();

                    if (!datoDescripcion.isEmpty()) {

                        if(!datoDescripcion.equals(datoBuscarDescripcion)) {

                            // SI HUBO CAMBIOS, CONSULTA PARA VER SI YA EXISTE EN LA BASE
                            dialogProcesando();
                            consultarDescripcionBis();

                        } else {

                            // SI NO HUBO CAMBIOS, VALIDA EL CAMPO
                            Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                            editDescripcion.setError("Datos correctos!", drawable);
                        }

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
        editCantidad.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, GUARDA Y CORROBORA LOS DATOS INGRESADOS
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
        editMoneda.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, GUARDA Y VALIDA LOS DATOS INGRESADOS
                    datoMoneda = editMoneda.getText().toString();

                    if (!datoMoneda.isEmpty()) {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editMoneda.setError("Datos correctos!", drawable);

                    } else {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editMoneda.setError("Datos correctos!", drawable);

                        editMoneda.setText("ARS");
                        datoMoneda = editMoneda.getText().toString();
                    }

                } else {

                    // SI INGRESA AL CAMPO, OCULTA EL ÍCONO DE VALIDACIÓN
                    editMoneda.setError(null);
                }
            }
        });
        editPrecioUnit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, GUARDA Y CORROBORA LOS DATOS INGRESADOS
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
        editPrecioTotal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    // SI SALE DEL CAMPO, GUARDA Y CORROBORA LOS DATOS INGRESADOS
                    datoPrecioTotal = editPrecioTotal.getText().toString();
                    datoCantidad = editCantidad.getText().toString();
                    datoPrecioUnit = editPrecioUnit.getText().toString();
                    cantidad = Integer.parseInt(datoCantidad);
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

                        precioTotal = cantidad * precioUnit;

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
                    datoPrecioUnit = editPrecioUnit.getText().toString();
                    cantidad = Integer.parseInt(datoCantidad);
                    precioUnit = Double.parseDouble(datoPrecioUnit);

                    precioTotal = cantidad * precioUnit;

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

    private void dialogErrorProducto(){

        // DIALOG QUE INFORMA QUE EL PRODUCTO YA SE INGRESÓ ANTERIORMENTE
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Dialogs dialogs = new Dialogs(getActivity());
                int layout = R.layout.dialog_error_producto;
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
                EditText editDescripcion = getView().findViewById(R.id.editDescripcion);
                EditText editCantidad = getView().findViewById(R.id.editCantidad);
                EditText editMoneda = getView().findViewById(R.id.editMoneda);
                EditText editPrecioUnit = getView().findViewById(R.id.editPrecioUnit);
                EditText editPrecioTotal = getView().findViewById(R.id.editPrecioTotal);

                editCodigo.setFocusableInTouchMode(false);
                editDescripcion.setFocusableInTouchMode(false);
                editCantidad.setFocusableInTouchMode(false);
                editMoneda.setFocusableInTouchMode(false);
                editPrecioUnit.setFocusableInTouchMode(false);
                editPrecioTotal.setFocusableInTouchMode(false);

                editCodigo.setText("");
                editDescripcion.setText("");
                editCantidad.setText("");
                editMoneda.setText("");
                editPrecioUnit.setText("");
                editPrecioTotal.setText("");

                editCodigo.setError(null);
                editDescripcion.setError(null);
                editCantidad.setError(null);
                editMoneda.setError(null);
                editPrecioUnit.setError(null);
                editPrecioTotal.setError(null);

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
        String URL = "http://malpicas.heliohost.org/malpica/stock/stock_consultar_codigo.php?parameter=" + datoBuscarCodigo;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                StockSetters setters = new StockSetters();

                                setters.setCodigo(jsonObject.getString("codigo_stock"));
                                setters.setDescripcion(jsonObject.getString("descripcion_stock"));
                                setters.setCantidad(jsonObject.getString("cantidad"));
                                setters.setMoneda(jsonObject.getString("moneda"));
                                setters.setPrecioUnit(jsonObject.getString("precio_unit"));
                                setters.setPrecioTotal(jsonObject.getString("precio_total"));

                                String codigo = setters.getCodigo();
                                String descripcion = setters.getDescripcion();
                                String cantidad = setters.getCantidad();
                                String moneda = setters.getMoneda();
                                String precioUnit = setters.getPrecioUnit();
                                String precioTotal = setters.getPrecioTotal();

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

    private void consultarDescripcion() {

        // CONSULTA POR DESCRIPCIÓN SI YA FUE INGRESADO ANTERIORMENTE A LA BASE
        String descripcion = datoBuscarDescripcion.replace(" ", "%20");

        String URL = "http://malpicas.heliohost.org/malpica/stock/stock_consultar_descripcion.php?parameter=" + descripcion;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                StockSetters setters = new StockSetters();

                                setters.setCodigo(jsonObject.getString("codigo_stock"));
                                setters.setDescripcion(jsonObject.getString("descripcion_stock"));
                                setters.setCantidad(jsonObject.getString("cantidad"));
                                setters.setMoneda(jsonObject.getString("moneda"));
                                setters.setPrecioUnit(jsonObject.getString("precio_unit"));
                                setters.setPrecioTotal(jsonObject.getString("precio_total"));

                                String codigo = setters.getCodigo();
                                String descripcion = setters.getDescripcion();
                                String cantidad = setters.getCantidad();
                                String moneda = setters.getMoneda();
                                String precioUnit = setters.getPrecioUnit();
                                String precioTotal = setters.getPrecioTotal();

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
        String URL = "http://malpicas.heliohost.org/malpica/stock/stock_consultar_codigo.php?parameter=" + datoCodigo;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                StockSetters setters = new StockSetters();

                                setters.setCodigo(jsonObject.getString("codigo_stock"));

                                String codigo = setters.getCodigo();

                                if(!codigo.equals("No existe")){

                                    // SI YA SE ENCUENTRA REGISTRADO MUESTRA UN MENSAJE DE ERROR
                                    dialogErrorProducto();

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

    private void consultarDescripcionBis() {

        // CONSULTA POR DESCRIPCIÓN SI YA FUE INGRESADO ANTERIORMENTE A LA BASE
        String descripcion = datoDescripcion.replace(" ","%20");

        String URL = "http://malpicas.heliohost.org/malpica/stock/stock_consultar_descripcion.php?parameter=" + descripcion;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            // RECORRE EL ARRAY DE JSON CON LA CONSULTA Y CON UN SETTER & GETTER MUESTRA LOS RESULTADOS
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                StockSetters setters = new StockSetters();

                                setters.setDescripcion(jsonObject.getString("descripcion_stock"));

                                String descripcion = setters.getDescripcion();

                                if(!descripcion.equals("No existe")){

                                    // SI YA SE ENCUENTRA REGISTRADO MUESTRA UN MENSAJE DE ERROR
                                    dialogErrorProducto();

                                    Drawable drawable = getResources().getDrawable(R.drawable.ic_error);
                                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                                    EditText editDescripcion = getView().findViewById(R.id.editDescripcion);
                                    editDescripcion.setError("Revise los datos!", drawable);

                                } else {

                                    // SI NO SE ENCUENTRA REGISTRADO VALIDA EL CAMPO
                                    Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                                    EditText editDescripcion = getView().findViewById(R.id.editDescripcion);
                                    editDescripcion.setError("Datos correctos!", drawable);
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

    private void modificarProducto() {

        // FINALMENTE MODIFICA EL PRODUCTO EN LA BASE
        String URL = "http://malpicas.heliohost.org/malpica/stock/stock_actualizar_producto.php";
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
                parameter.put("descripcion_stock",datoDescripcion);
                parameter.put("cantidad",datoCantidad);
                parameter.put("moneda",datoMoneda);
                parameter.put("precio_unit",datoPrecioUnit);
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
}
