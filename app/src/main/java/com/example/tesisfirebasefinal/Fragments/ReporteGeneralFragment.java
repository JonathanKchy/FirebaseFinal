package com.example.tesisfirebasefinal.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.tesisfirebasefinal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReporteGeneralFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReporteGeneralFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String sPrincipal,sFamiliar,sAdmin,sTranscripcion,sMensajes;
    TextView tAdmin,tFamiliar,tPrincipal,tTranscripciones,tMensajes;
    int numAdmin,numFamiliar,numPrincipal,numTranscripciones,contadorTranscripciones=0,numMensajes,contadorMensajes=0;
    private ImageButton btnRegresarAdmin;
    private DatabaseReference DbRef;
    private FirebaseAuth baseAutenticacion;

    public ReporteGeneralFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReporteGeneralFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReporteGeneralFragment newInstance(String param1, String param2) {
        ReporteGeneralFragment fragment = new ReporteGeneralFragment();
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
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_reporte_general, container, false);
        baseAutenticacion= FirebaseAuth.getInstance();
        final String id=baseAutenticacion.getCurrentUser().getUid();
        DbRef= FirebaseDatabase.getInstance().getReference();
        tAdmin=(TextView)view.findViewById(R.id.totalAdmin);
        tFamiliar=(TextView)view.findViewById(R.id.totalFamiliar);
        tPrincipal=(TextView)view.findViewById(R.id.totalPrincipal);
        tTranscripciones=(TextView)view.findViewById(R.id.totalTranscripciones);
        tMensajes=(TextView)view.findViewById(R.id.totalMensajes);

        DbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                contadorTranscripciones=0;
                if(dataSnapshot.exists()){

                    numAdmin=(int)dataSnapshot.child("USUARIOS").child("ADMINISTRADOR").getChildrenCount();
                    sAdmin=Integer.toString(numAdmin);
                    numPrincipal=(int)dataSnapshot.child("USUARIOS").child("PRINCIPAL").getChildrenCount();
                    sPrincipal=Integer.toString(numPrincipal);
                    numFamiliar=(int)dataSnapshot.child("USUARIOS").child("FAMILIAR").getChildrenCount();
                    sFamiliar=Integer.toString(numFamiliar);
                    tAdmin.setText("Usuarios Administradores: "+sAdmin);
                    tPrincipal.setText("Usuarios Principales: "+sPrincipal);
                    tFamiliar.setText("Usuarios Familiares: "+sFamiliar);
                    contadorTranscripciones=0;
                    contadorMensajes=0;
                    for (DataSnapshot snapshot:dataSnapshot.child("TRANSCRIPCIONES").getChildren()){
                    numTranscripciones=(int)snapshot.getChildrenCount();
                    contadorTranscripciones=contadorTranscripciones+numTranscripciones;
                    }
                    sTranscripcion=Integer.toString(contadorTranscripciones);
                    tTranscripciones.setText("Total de Transcripciones: "+sTranscripcion);
                    for (DataSnapshot snapshot2:dataSnapshot.child("MENSAJES").getChildren()){
                        numMensajes=(int)snapshot2.getChildrenCount();
                        contadorMensajes=contadorMensajes+numMensajes;
                    }
                    sMensajes=Integer.toString(contadorMensajes);
                    tMensajes.setText("Total de Mensajes: "+sMensajes);

                }else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }
}
