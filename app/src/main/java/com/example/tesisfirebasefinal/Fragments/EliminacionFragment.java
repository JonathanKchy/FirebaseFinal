package com.example.tesisfirebasefinal.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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


public class EliminacionFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    String titulo,captura;
    TextView tituloTV;
    EditText transcripcionET;
    private DatabaseReference DbRef;
    private FirebaseAuth baseAutenticacion;
    private ImageButton btnGuardar, btnEliminar,btnRegresar;

    public EliminacionFragment() {
    }

    public EliminacionFragment(String titulo) {
        this.titulo=titulo;
    }

    public static EliminacionFragment newInstance(String param1, String param2) {
        EliminacionFragment fragment = new EliminacionFragment();
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

        final View view=inflater.inflate(R.layout.fragment_eliminacion, container, false);

        baseAutenticacion= FirebaseAuth.getInstance();
        final String id=baseAutenticacion.getCurrentUser().getUid();
        DbRef= FirebaseDatabase.getInstance().getReference("TRANSCRIPCIONES");
        btnGuardar=(ImageButton) view.findViewById(R.id.botonGuardar);
        btnEliminar=(ImageButton) view.findViewById(R.id.botonEliminar);
        btnRegresar=(ImageButton) view.findViewById(R.id.botonRegresar);
        tituloTV=(TextView)view.findViewById(R.id.tituloTextoEliminacion);
        transcripcionET=(EditText)view.findViewById((R.id.textoEntradaEliminacion));
        tituloTV.setText(titulo);
        //addListenerForSingleValueEvent
        DbRef.child(id).child(titulo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    transcripcionET.setText(dataSnapshot.child("transcripcion").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CARGAR DIALOGO DE CONFIRMACIÓN
                final AlertDialog.Builder confirmacionDialog = new AlertDialog.Builder(getActivity());
                confirmacionDialog.setMessage(Html.fromHtml("Confirma sobreescribir: " + "<b>" + titulo + "</b>")).setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                captura=(String)transcripcionET.getText().toString();
                                guardarTranscripcionEditada();
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

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder confirmacionDialog = new AlertDialog.Builder(getActivity());
                confirmacionDialog.setMessage(Html.fromHtml("¿Confirma Eliminar su Transcripción?")).setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                eliminarTranscripcion();
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

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity=(AppCompatActivity)getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container,new PortafolioFragment()).addToBackStack(null).commit();

            }
        });
        return view;
    }

    public void eliminarTranscripcion(){
        final String id=baseAutenticacion.getCurrentUser().getUid();
        //DbRef.child(id).child(titulo).setValue(null);
        DbRef.child(id).child(titulo).removeValue();
        DbRef=null;
        Toast.makeText(getActivity(), "TRANSCRIPCION ELIMINADA", Toast.LENGTH_SHORT).show();
        //getFragmentManager().beginTransaction().remove(this).commit();
        AppCompatActivity activity=(AppCompatActivity)getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.container,new PortafolioFragment()).addToBackStack(null).commit();

    }
    //guardar
    public void guardarTranscripcionEditada(){
        final String id=baseAutenticacion.getCurrentUser().getUid();
        DbRef.child(id).child(titulo).child("transcripcion").setValue(captura);
        DbRef=null;
        Toast.makeText(getActivity(), "SU TRANSCRIPCIÓN SE GUARDO CORRECTAMENTE", Toast.LENGTH_LONG).show();
        //getFragmentManager().beginTransaction().remove(this).commit();
        AppCompatActivity activity2=(AppCompatActivity)getContext();
        activity2.getSupportFragmentManager().beginTransaction().replace(R.id.container,new PortafolioFragment()).addToBackStack(null).commit();

    }
    //ir para atras
    public void onBackPressed()
    {
        AppCompatActivity activity3=(AppCompatActivity)getContext();
        activity3.getSupportFragmentManager().beginTransaction().replace(R.id.container,new PortafolioFragment()).addToBackStack(null).commit();

    }
}
