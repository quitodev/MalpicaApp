package com.example.malpicasoft.ventas;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.malpicasoft.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModificarVentas extends Fragment {

    public ModificarVentas() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_modificar_ventas, container, false);
    }
}
