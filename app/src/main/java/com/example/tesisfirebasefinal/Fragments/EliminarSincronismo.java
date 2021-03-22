package com.example.tesisfirebasefinal.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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


public class EliminarSincronismo extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    String idFamiliar,nomUsuario;
    TextView nombreUsuario,nombreFamiliar,correoFamiliar;
    private DatabaseReference DbRef;
    private FirebaseAuth baseAutenticacion;
    private ImageButton btnEliminarSincronismo,btnRegresarSincronismo;
    public EliminarSincronismo() {
    }


    public static EliminarSincronismo newInstance(String param1, String param2) {
        EliminarSincronismo fragment = new EliminarSincronismo();
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
        final View view=inflater.inflate(R.layout.fragment_eliminar_sincronismo, container, false);
        baseAutenticacion= FirebaseAuth.getInstance();
        final String id=baseAutenticacion.getCurrentUser().getUid();
        DbRef= FirebaseDatabase.getInstance().getReference().child("USUARIOS");

        btnEliminarSincronismo=(ImageButton) view.findViewById(R.id.botonEliminarSincronismo);
        btnRegresarSincronismo=(ImageButton) view.findViewById(R.id.botonRegresarSincronismo);

        nombreUsuario=(TextView)view.findViewById((R.id.textnombreSincronismo));
        nombreFamiliar=(TextView)view.findViewById((R.id.textnombredeFamiliar));
        correoFamiliar=(TextView)view.findViewById((R.id.textcorreodeFamiliar));


        DbRef.child("PRINCIPAL").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    nomUsuario=dataSnapshot.child("Apodo").getValue().toString();
                    nombreUsuario.setText("HOLA!  "+nomUsuario);
                    idFamiliar=dataSnapshot.child("Sincronismo").getValue().toString();

                    DbRef.child("FAMILIAR").child(idFamiliar).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                correoFamiliar.setText(dataSnapshot.child("Correo").getValue().toString());
                                nombreFamiliar.setText(dataSnapshot.child("Apodo").getValue().toString());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        btnEliminarSincronismo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CARGAR DIALOGO DE CONFIRMACIÓN
                final AlertDialog.Builder confirmacionDialog = new AlertDialog.Builder(getActivity());
                confirmacionDialog.setMessage(Html.fromHtml( "<b>" + "Confirma Eliminar su sincronismo? " + "</b>")).setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                eliminarSincronismo();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alerta = confirmacionDialog.create();
                alerta.setTitle("Cofirmación");
                alerta.show();
            }
        });
        btnRegresarSincronismo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity=(AppCompatActivity)getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container,new TranscripcionFragment()).addToBackStack(null).commit();

            }
        });
        return view;
    }
    public void eliminarSincronismo(){
        final String id=baseAutenticacion.getCurrentUser().getUid();
        DbRef.child("PRINCIPAL").child(id).child("Sincronismo").setValue("0");
        DbRef.child("FAMILIAR").child(idFamiliar).child("Sincronismo").setValue("0");
        DbRef=null;
        AppCompatActivity activity=(AppCompatActivity)getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.container,new TranscripcionFragment()).addToBackStack(null).commit();

    }
}
