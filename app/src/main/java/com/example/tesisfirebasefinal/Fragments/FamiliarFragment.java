package com.example.tesisfirebasefinal.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.tesisfirebasefinal.R;

import java.util.Locale;

public class FamiliarFragment extends Fragment {
    private View vista2;
    //metodo para cargar la vista
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista2=inflater.inflate(R.layout.fragment_familiar, container, false);
        return vista2;
    }
}

