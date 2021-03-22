package com.example.tesisfirebasefinal.Fragments;


import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tesisfirebasefinal.ActivityTranscripcion;
import com.example.tesisfirebasefinal.RegistroActivity;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import com.example.tesisfirebasefinal.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class TranscripcionFragment extends Fragment  {

    private TextView txtNombre,txtTranscripcion;
    private static final int REQ_CODE_SPEECH_INPUT=100;
    private String captura="",tituloString;
    private EditText tituloET;
    private ImageButton btnHablar, btnStop,btnMAs, btnMenos;
    private View vista;
    private DatabaseReference DbRef;
    private FirebaseAuth baseAutenticacion;
    private Intent intent;
    private int letraSize=14;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //base de datos
        baseAutenticacion= FirebaseAuth.getInstance();
        final String id=baseAutenticacion.getCurrentUser().getUid();
        DbRef= FirebaseDatabase.getInstance().getReference().child("TRANSCRIPCIONES");

        // Inflate the layout for this fragment
        vista=inflater.inflate(R.layout.fragment_transcripcion, container, false);
        //intent para inicializar escuchar; ACTION_RECOGNIZE_SPEECH: Inicia una actividad que solicitará al usuario la voz y la enviará a través de un reconocedor de voz.
        intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);//ACTION_VOICE_SEARCH_HANDS_FREE
        //El reconocedor utiliza esta información para ajustar los resultados
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //Etiqueta de idioma IETF opcional
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        //Mensaje de texto opcional para mostrar al usuario cuando le pide que hable
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"...");
        //
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 0);


        txtTranscripcion=(TextView)vista.findViewById(R.id.textoEntrada);
        btnHablar=(ImageButton) vista.findViewById(R.id.botonHablar);
        btnStop=(ImageButton) vista.findViewById(R.id.botonStop);
        btnMAs=(ImageButton)vista.findViewById(R.id.botonMas);
        btnMenos=(ImageButton)vista.findViewById(R.id.botonMenos);
        tituloET=(EditText)vista.findViewById(R.id.editTextTitulo);



        btnHablar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarEntradaVoz();
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtTranscripcion.setTextSize(14);
                tituloString=tituloET.getText().toString();

                if (!tituloString.isEmpty()){
                    //CARGAR DIALOGO DE CONFIRMACIÓN
                    final AlertDialog.Builder confirmacionDialog = new AlertDialog.Builder(getActivity());
                    confirmacionDialog.setMessage(Html.fromHtml("El titulo de su trascripción es: " + "<b>" + tituloString + "</b>")).setCancelable(false)
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    guardarTranscripcion();
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

                }else {
                    Toast.makeText(getActivity(), "Complete el campo *TITULO*", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnMAs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                letraSize=letraSize+2;
                txtTranscripcion.setTextSize(letraSize);
            }
        });
        btnMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                letraSize=letraSize-2;
                txtTranscripcion.setTextSize(letraSize);
            }
        });


        return vista;

    }


    private void guardarTranscripcion(){
        baseAutenticacion= FirebaseAuth.getInstance();

        DbRef= FirebaseDatabase.getInstance().getReference().child("TRANSCRIPCIONES");
        DateFormat df = new SimpleDateFormat("EEEE, d MMMM yyyy, HH:mm:ss");
        String date = df.format(Calendar.getInstance().getTime());
        final Map<String,Object> mapUsuario=new HashMap<>();
        mapUsuario.put("titulo",tituloString);
        mapUsuario.put("fecha",date);
        mapUsuario.put("transcripcion",captura);
        final String id2=baseAutenticacion.getCurrentUser().getUid();
        DbRef.child(id2).child(tituloString).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Toast.makeText(getActivity(), "El titulo ya existe, CÁMBIALO, por favor", Toast.LENGTH_LONG).show();

                }else{
                    DbRef.child(id2).child(tituloString).setValue(mapUsuario);
                    Toast.makeText(getActivity(), "SU TRANSCRIPCIÓN SE GUARDO CORRECTAMENTE", Toast.LENGTH_LONG).show();
                    captura="";
                    txtTranscripcion.setText(captura);
                    tituloString="";
                    tituloET.setText("");
                    DbRef=null;

                    /*
                    DbRef.child("TRANSCRIPCIONES").child(id2).child(tituloString).setValue(mapUsuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()){
                                //ir a activity que ya transcribe
                                Toast.makeText(getActivity(), "SU TRANSCRIPCIÓN SE GUARDO "+"<b>"+" CORRECTAMENTE"+"</b>", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity(), "Por el momento no es posible guardar su transcripción, disculpas", Toast.LENGTH_SHORT).show();
                             }

                        }
                    });*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void iniciarEntradaVoz(){

        try{
            startActivityForResult(intent,REQ_CODE_SPEECH_INPUT);
        }catch (ActivityNotFoundException e){}
        //Toast.makeText(getActivity(), "problema", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case  REQ_CODE_SPEECH_INPUT:{
                if(resultCode==RESULT_OK && null!=data){
                    ArrayList<String> result =data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if(!captura.isEmpty()){
                        captura=captura+ ", "+result.get(0);
                        txtTranscripcion.setText(captura);

                    }else {
                        captura=captura+result.get(0);
                        txtTranscripcion.setText(captura);
                    }

                    iniciarEntradaVoz();
                }
                break;
            }
        }
    }



}

