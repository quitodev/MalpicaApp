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

    public void startResultado(int layout) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(layout, null));
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

    public void endResultado() {
        alertDialog.dismiss();
    }
}
