package com.example.tesisfirebasefinal.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.tesisfirebasefinal.PruebaFamiliar;
import com.example.tesisfirebasefinal.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ReporteFamiliarFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;
    RecyclerView recview;
    myadapterreporteF adapter;
    String idPropio,idPrincipal="22";
    public static int MILISEGUNDOS_ESPERA = 10;
    Button PDFfam;
    private DatabaseReference DbRef;
    private FirebaseAuth baseAutenticacion;

    public ReporteFamiliarFragment() {

    }
    public ReporteFamiliarFragment(String idPropio) {

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
        View view= inflater.inflate(R.layout.fragment_reporte_familiar, container, false);
        recview=(RecyclerView)view.findViewById(R.id.recvwRepFam);
        baseAutenticacion= FirebaseAuth.getInstance();
        final String id=baseAutenticacion.getCurrentUser().getUid();
        DbRef= FirebaseDatabase.getInstance().getReference();
        PDFfam=(Button)view.findViewById(R.id.butReporFam);
        PDFfam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), PruebaFamiliar.class);
                i.putExtra("dato",idPropio);
                startActivity(i);
                //startActivity(new Intent(getActivity(),PruebaFamiliar.class));
            }
        });
        obtener();

        return view;
    }
public void obtener(){
    final String id=baseAutenticacion.getCurrentUser().getUid();
    recview.setLayoutManager(new LinearLayoutManager(getContext()));
    FirebaseRecyclerOptions<modelrepfam> options =
            new FirebaseRecyclerOptions.Builder<modelrepfam>()
                    .setQuery(DbRef.child("TRANSCRIPCIONES").child(idPropio), modelrepfam.class)
                    .build();
    adapter=new myadapterreporteF(options);
    recview.setAdapter(adapter);
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
