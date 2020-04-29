package com.example.malpicasoft.gastos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.malpicasoft.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EliminarGastos extends Fragment {

    public EliminarGastos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_eliminar_gastos, container, false);
    }
}
