package com.example.tesisfirebasefinal.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tesisfirebasefinal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class QuitarSincronismoFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private ImageButton btnGuardar, btnEliminar,btnRegresar;
    String idDeSeleccion,getIdDeSeleccionReemplazo;
    private String idSincSeleccion;
    private DatabaseReference DbRef;
    private FirebaseAuth baseAutenticacion;

    public QuitarSincronismoFragment() {
    }
    public QuitarSincronismoFragment(String idDeSeleccion) {
        this.idDeSeleccion=idDeSeleccion;
    }


    public static QuitarSincronismoFragment newInstance(String param1, String param2) {
        QuitarSincronismoFragment fragment = new QuitarSincronismoFragment();
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

        final View view=inflater.inflate(R.layout.fragment_quitar_sincronismo, container, false);

        baseAutenticacion= FirebaseAuth.getInstance();
        final String id=baseAutenticacion.getCurrentUser().getUid();
        DbRef= FirebaseDatabase.getInstance().getReference().child("USUARIOS");

        //btnRegresar=(ImageButton) view.findViewById(R.id.botonRegresarSincronismo);


        DbRef.child("FAMILIAR").child(idDeSeleccion).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    idSincSeleccion=dataSnapshot.child("Sincronismo").getValue().toString();
                    if(idSincSeleccion.equals("0")){

                        sincronizarUsuarios();
                    }else if (idSincSeleccion.equals(id)){
                        Toast.makeText(getActivity(), "Se SINCRONIZÓ correctamente ", Toast.LENGTH_LONG).show();
                        DbRef=null;
                        AppCompatActivity activity=(AppCompatActivity)getContext();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.container,new TranscripcionFragment()).addToBackStack(null).commit();


                    }else {
                        Toast.makeText(getActivity(), "El usuario NO PUEDE ser seleccionado ", Toast.LENGTH_LONG).show();
                        DbRef=null;
                        AppCompatActivity activity=(AppCompatActivity)getContext();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.container,new SincronismoFragment()).addToBackStack(null).commit();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }
    public void sincronizarUsuarios(){
        final String id=baseAutenticacion.getCurrentUser().getUid();

        DbRef.child("FAMILIAR").child(idDeSeleccion).child("Sincronismo").setValue(id);
        DbRef.child("PRINCIPAL").child(id).child("Sincronismo").setValue(idDeSeleccion);
        DbRef=null;
        AppCompatActivity activity=(AppCompatActivity)getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.container,new TranscripcionFragment()).addToBackStack(null).commit();



    }
}
