package com.example.tesisfirebasefinal.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.tesisfirebasefinal.R;

public class AdminFragment extends Fragment {
    private View vista3;
    //metodo para cargar la vista
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista3=inflater.inflate(R.layout.fragment_admin, container, false);
        return vista3;
    }
}

