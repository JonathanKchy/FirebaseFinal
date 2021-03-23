package com.example.tesisfirebasefinal.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tesisfirebasefinal.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragmentFamiliar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragmentFamiliar extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView recview;
    myadapterchat adapter;
    String idPrincipal,oracionText;
    TextView oracion;

    private DatabaseReference DbRef;
    private FirebaseAuth baseAutenticacion;
    private ImageButton btnEnviarFamiliar;

    public ChatFragmentFamiliar() {

    }


    public static ChatFragmentFamiliar newInstance(String param1, String param2) {
        ChatFragmentFamiliar fragment = new ChatFragmentFamiliar();
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
        DbRef= FirebaseDatabase.getInstance().getReference();

        View view= inflater.inflate(R.layout.fragment_chat_familiar, container, false);
        recview=(RecyclerView)view.findViewById(R.id.recviewChatFamiliar);
        recview.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<modelChat> options =
                new FirebaseRecyclerOptions.Builder<modelChat>()
                        .setQuery(DbRef.child("MENSAJES").child(id), modelChat.class)
                        .build();
        adapter=new myadapterchat(options);
        recview.setAdapter(adapter);
        oracion=(TextView)view.findViewById(R.id.editTextOracionFamiliar);
        btnEnviarFamiliar=(ImageButton)view.findViewById(R.id.botonEnviarFamiliar);

        btnEnviarFamiliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oracionText=oracion.getText().toString();
                if (!oracionText.isEmpty()) {
                    enviarMensaje();
                    //Toast.makeText(getActivity(), "si hola", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(getActivity(), "Escribe tu mensaje", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
    public void enviarMensaje(){
        final String id=baseAutenticacion.getCurrentUser().getUid();
        DbRef.child("USUARIOS").child("FAMILIAR").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    idPrincipal=dataSnapshot.child("Sincronismo").getValue().toString();
                    final Map<String,Object> mapUsuario=new HashMap<>();
                    mapUsuario.put("estado","recibido");
                    mapUsuario.put("texto",oracionText);
                    DbRef.child("MENSAJES").child(idPrincipal).push().setValue(mapUsuario);
                    final Map<String,Object> mapUsuario2=new HashMap<>();
                    mapUsuario2.put("estado","enviado");
                    mapUsuario2.put("texto",oracionText);
                    DbRef.child("MENSAJES").child(id).push().setValue(mapUsuario2);
                    oracion.setText("");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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

