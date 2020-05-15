package com.example.malpicasoft;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import java.util.Timer;
import java.util.TimerTask;

public class Dialogs {

    private Activity activity;
    private AlertDialog alertDialog;
    private int counter;

    public Dialogs(Activity act){
        activity = act;
    }

    public void startProcesando() {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_procesando, null));
        builder.setCancelable(true);

        alertDialog = builder.create();
        alertDialog.show();
    }

    public void endProcesando() {
        alertDialog.dismiss();
    }

    public void startOk() {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_ok, null));
        builder.setCancelable(true);

        alertDialog = builder.create();
        alertDialog.show();

        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                counter++;

                if(counter == 20) {
                    timer.cancel();
                    alertDialog.dismiss();
                    counter = 0;
                }
            }
        };
        timer.schedule(timerTask,0,100);
    }

    public void endOk() {
        alertDialog.dismiss();
    }

    public void startError() {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_error, null));
        builder.setCancelable(true);

        alertDialog = builder.create();
        alertDialog.show();

        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                counter++;

                if(counter == 20) {
                    timer.cancel();
                    alertDialog.dismiss();
                    counter = 0;
                }
            }
        };
        timer.schedule(timerTask,0,100);
    }

    public void endError() {
        alertDialog.dismiss();
    }

    public void startErrorFactura() {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_error_factura, null));
        builder.setCancelable(true);

        alertDialog = builder.create();
        alertDialog.show();

        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                counter++;

                if(counter == 20) {
                    timer.cancel();
                    alertDialog.dismiss();
                    counter = 0;
                }
            }
        };
        timer.schedule(timerTask,0,100);
    }

    public void endErrorFactura() {
        alertDialog.dismiss();
    }

    public void startErrorCliente() {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_error_cliente, null));
        builder.setCancelable(true);

        alertDialog = builder.create();
        alertDialog.show();

        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                counter++;

                if(counter == 20) {
                    timer.cancel();
                    alertDialog.dismiss();
                    counter = 0;
                }
            }
        };
        timer.schedule(timerTask,0,100);
    }

    public void endErrorCliente() { alertDialog.dismiss(); }

    public void startErrorOps() {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_error_operaciones, null));
        builder.setCancelable(true);

        alertDialog = builder.create();
        alertDialog.show();

        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                counter++;

                if(counter == 20) {
                    timer.cancel();
                    alertDialog.dismiss();
                    counter = 0;
                }
            }
        };
        timer.schedule(timerTask,0,100);
    }

    public void endErrorOps() {
        alertDialog.dismiss();
    }

    public void startErrorProveedor() {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_error_proveedor, null));
        builder.setCancelable(true);

        alertDialog = builder.create();
        alertDialog.show();

        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                counter++;

                if(counter == 20) {
                    timer.cancel();
                    alertDialog.dismiss();
                    counter = 0;
                }
            }
        };
        timer.schedule(timerTask,0,100);
    }

    public void endErrorProveedor() { alertDialog.dismiss(); }

    public void startErrorProducto() {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_error_producto, null));
        builder.setCancelable(true);

        alertDialog = builder.create();
        alertDialog.show();

        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                counter++;

                if(counter == 20) {
                    timer.cancel();
                    alertDialog.dismiss();
                    counter = 0;
                }
            }
        };
        timer.schedule(timerTask,0,100);
    }

    public void endErrorProducto() { alertDialog.dismiss(); }
}
