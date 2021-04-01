package com.example.tesisfirebasefinal.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
 * Use the {@link InformeUsuariosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InformeUsuariosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String nombre,correo,idDeUsuario,idDeSincronismo;
    TextView nombreUser,correoUser,transcripciones,idSincronismoUser;
    int numeroTranscripciones;
    private ImageButton btnRegresarAdmin;
    private DatabaseReference DbRef;
    private FirebaseAuth baseAutenticacion;
    public InformeUsuariosFragment() {
        // Required empty public constructor
    }
    public InformeUsuariosFragment(String nombre,String correo,String idDeUsuario,String idDeSincronismo) {
        this.nombre=nombre;
        this.correo=correo;
        this.idDeUsuario=idDeUsuario;
        this.idDeSincronismo=idDeSincronismo;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InformeUsuariosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InformeUsuariosFragment newInstance(String param1, String param2) {
        InformeUsuariosFragment fragment = new InformeUsuariosFragment();
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
        final View view= inflater.inflate(R.layout.fragment_informe_usuarios, container, false);
        baseAutenticacion= FirebaseAuth.getInstance();
        final String id=baseAutenticacion.getCurrentUser().getUid();
        DbRef= FirebaseDatabase.getInstance().getReference().child("TRANSCRIPCIONES");
        btnRegresarAdmin= (ImageButton) view.findViewById(R.id.botonRegresarAdmin);
        nombreUser=(TextView)view.findViewById(R.id.textnombredePrincipal);
        correoUser=(TextView)view.findViewById(R.id.textcorreodePrincipal);
        idSincronismoUser=(TextView)view.findViewById(R.id.textSincronismoOpcion);
        transcripciones=(TextView)view.findViewById(R.id.textTotalTrans);

        DbRef.child(idDeUsuario).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if (idDeSincronismo.equals("0")){
                        idSincronismoUser.setText("Aun no tiene Sincronismo");
                    }else {
                        idSincronismoUser.setText("Ya tiene Sincronismo");

                    }
                    numeroTranscripciones=(int)dataSnapshot.getChildrenCount();
                    String trans=Integer.toString(numeroTranscripciones);
                    transcripciones.setText("Tiene un total de "+trans+" transcripciones");
                    nombreUser.setText(nombre);
                    correoUser.setText(correo);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btnRegresarAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity=(AppCompatActivity)getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.containerAdmin,new AdminUsuariosFragment()).addToBackStack(null).commit();

            }
        });
        return view;
    }
}
