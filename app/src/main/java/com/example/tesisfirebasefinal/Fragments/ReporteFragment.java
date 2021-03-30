package com.example.tesisfirebasefinal.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tesisfirebasefinal.Prueba;
import com.example.tesisfirebasefinal.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ReporteFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;
    RecyclerView recview;
    myadapterreporte adapter;
    String idPropio;
    Button PDF;

    private DatabaseReference DbRef;
    private FirebaseAuth baseAutenticacion;

    public ReporteFragment() {

    }
    public ReporteFragment(String idPropio) {
        this.idPropio=idPropio;
    }

    public static ReporteFragment newInstance(String param1, String param2) {
        ReporteFragment fragment = new ReporteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        baseAutenticacion= FirebaseAuth.getInstance();
        final String id=baseAutenticacion.getCurrentUser().getUid();
        DbRef= FirebaseDatabase.getInstance().getReference().child("TRANSCRIPCIONES");

        View view= inflater.inflate(R.layout.fragment_reporte, container, false);
        recview=(RecyclerView)view.findViewById(R.id.recviewReporte);
        recview.setLayoutManager(new LinearLayoutManager(getContext()));
        PDF=(Button)view.findViewById(R.id.buttonReporte);
        PDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), Prueba.class));
            }
        });
                FirebaseRecyclerOptions<modelReporte> options =
                new FirebaseRecyclerOptions.Builder<modelReporte>()
                        .setQuery(DbRef.child(id), modelReporte.class)
                        .build();
        adapter=new myadapterreporte(options);
        recview.setAdapter(adapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
