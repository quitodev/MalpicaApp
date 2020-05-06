package com.example.malpicasoft.compras;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.malpicasoft.Dialogs;
import com.example.malpicasoft.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class IngresarCompras extends Fragment {

    int counter, cantidad;
    double impuestos, precioUnit, precioTotal;
    String datoFechaActual, datoFechaCompra, datoNroFactura, datoCodigoProv, datoRazonSocialProv, datoCondicionProv,
            datoCodigoProd, datoDescripcionProd, datoCantidad, datoPrecioUnit, datoImpuestos, datoPrecioTotal,
            datoDia, datoMes, datoAno;
    Button buttonGuardar;

    public IngresarCompras() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_ingresar_compras, container, false);

        dateFragments();

        final EditText editFechaCompra = root.findViewById(R.id.editFechaCompra);
        final EditText editNroFactura = root.findViewById(R.id.editNroFactura);
        final EditText editCodigoProv = root.findViewById(R.id.editCodigoProv);
        final EditText editRazonSocialProv = root.findViewById(R.id.editRazonSocialProv);
        final EditText editCondicionProv = root.findViewById(R.id.editCondicionProv);
        final EditText editCodigoProd = root.findViewById(R.id.editCodigoProd);
        final EditText editDescripcionProd = root.findViewById(R.id.editDescripcionProd);
        final EditText editCantidad = root.findViewById(R.id.editCantidad);
        final EditText editPrecioUnit = root.findViewById(R.id.editPrecioUnit);
        final EditText editImpuestos = root.findViewById(R.id.editImpuestos);
        final EditText editPrecioTotal = root.findViewById(R.id.editPrecioTotal);

        Button buttonGuardar = root.findViewById(R.id.buttonGuardar);

        editFechaCompra.setText(datoFechaActual);
        editFechaCompra.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if (!hasFocus) {

                    datoFechaCompra = editFechaCompra.getText().toString();

                    if (datoFechaCompra.length() == 10 && datoFechaCompra.contains("/")) {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editFechaCompra.setError("Datos correctos!", drawable);

                    } else {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_error);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editFechaCompra.setError("Revise los datos!", drawable);
                    }

                } else {
                    editFechaCompra.setError(null);
                }
            }
        });
        editNroFactura.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if (!hasFocus) {

                    datoNroFactura = editNroFactura.getText().toString();

                    if (datoNroFactura.length() == 15 && datoNroFactura.contains("-")) {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editNroFactura.setError("Datos correctos!", drawable);

                    } else {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_error);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editNroFactura.setError("Revise los datos!", drawable);
                    }

                } else {
                    editNroFactura.setError(null);
                }
            }
        });
        editCodigoProv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    datoCodigoProv = editCodigoProv.getText().toString();

                    if (!datoCodigoProv.isEmpty()) {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editCodigoProv.setError("Datos correctos!", drawable);

                    } else {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_error);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editCodigoProv.setError("Revise los datos!", drawable);
                    }

                } else {
                    editCodigoProv.setError(null);
                }
            }
        });
        editRazonSocialProv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

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
                    editRazonSocialProv.setError(null);
                }
            }
        });
        editCondicionProv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

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

                    datoCodigoProd = editCodigoProd.getText().toString();

                    if (!datoCodigoProd.isEmpty()) {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editCodigoProd.setError("Datos correctos!", drawable);

                    } else {

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_error);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editCodigoProd.setError("Revise los datos!", drawable);
                    }

                } else {
                    editCodigoProd.setError(null);
                }
            }
        });
        editDescripcionProd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

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
                    editDescripcionProd.setError(null);
                }
            }
        });
        editCantidad.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

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
                    editCantidad.setError(null);
                }
            }
        });
        editPrecioUnit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    datoPrecioUnit = editPrecioUnit.getText().toString();

                    if(!datoPrecioUnit.isEmpty()) {

                        precioUnit = Double.parseDouble(datoPrecioUnit);

                        if (precioUnit > 0) {

                            Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                            editPrecioUnit.setError("Datos correctos!", drawable);

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
                    editPrecioUnit.setError(null);
                }
            }
        });
        editImpuestos.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    datoImpuestos = editImpuestos.getText().toString();

                    if (!datoImpuestos.isEmpty()) {

                        impuestos = Double.parseDouble(datoImpuestos);

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_check_green);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        editImpuestos.setError("Datos correctos!", drawable);

                        if(impuestos == 0){
                            editImpuestos.setText("0.00");
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
                    editImpuestos.setError(null);
                }
            }
        });
        editPrecioTotal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    datoPrecioTotal = editPrecioTotal.getText().toString();

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

                    editPrecioTotal.setError(null);
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

        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editFechaCompra.setFocusable(false);
                editNroFactura.setFocusable(false);
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
                        && editNroFactura.getError() == "Datos correctos!"
                        && editCodigoProv.getError() == "Datos correctos!"
                        && editRazonSocialProv.getError() == "Datos correctos!"
                        && editCondicionProv.getError() == "Datos correctos!"
                        && editCodigoProd.getError() == "Datos correctos!"
                        && editDescripcionProd.getError() == "Datos correctos!"
                        && editCantidad.getError() == "Datos correctos!"
                        && editPrecioUnit.getError() == "Datos correctos!"
                        && editImpuestos.getError() == "Datos correctos!"
                        && editPrecioTotal.getError() == "Datos correctos!") {

                    Toast.makeText(getContext(),"Los datos están ok", Toast.LENGTH_LONG).show();

                    // al final, volver a habilitar los campos!!

                } else {
                        dialogProcesando();
                        dialogOk();

                        // empezar a armar el codigo!!
                }
            }
        });
        return root;
    }

    // OBTIENE EL DÍA ACTUAL PARA UTILIZARLO EN LOS FRAGMENTS
    public void dateFragments() {

        Calendar calendar = Calendar.getInstance();
        String fechaActual = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());

        String[] arrayFecha = fechaActual.split("/");
        datoDia = arrayFecha[0];
        datoMes = arrayFecha[1];
        datoAno = "20" + arrayFecha[2];

        if (datoDia.length() == 1) {
            datoDia = "0" + datoDia;
        }

        if (datoMes.length() == 1) {
            datoMes = "0" + datoMes;
        }

        datoFechaActual = datoDia + "/" + datoMes + "/" + datoAno;
    }

    // DIALOG CON PROGRESS BAR MIENTRAS REALIZA LAS CONSULTAS A LAS TABLAS
    public void dialogProcesando(){

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

    // DIALOG CON CONFIRMACIÓN DE OPERACIÓN REALIZADA CON ÉXITO
    public void dialogOk(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Dialogs dialogs = new Dialogs(getActivity());
                dialogs.startOk();
            }
        }, 3000);
    }
}
