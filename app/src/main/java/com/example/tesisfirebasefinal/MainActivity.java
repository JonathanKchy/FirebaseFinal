package com.example.tesisfirebasefinal;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

public class MainActivity extends AppCompatActivity {

    private EditText emailMA,claveMA;
    private String emailMA_S,claveMA_S;
    private Button buttonIngresar;
    private FirebaseAuth authUser;
    private Button buttonRecuperar;
    private DatabaseReference dbUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailMA=(EditText)findViewById(R.id.editTextEmail);
        claveMA=(EditText)findViewById(R.id.editTextClave);
        buttonIngresar=(Button)findViewById(R.id.btnIniciar);
        buttonRecuperar=(Button)findViewById(R.id.btnRecuperar);
        dbUser=FirebaseDatabase.getInstance().getReference().child("USUARIOS");
        authUser=FirebaseAuth.getInstance();


        buttonIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailMA_S=emailMA.getText().toString();
                claveMA_S=claveMA.getText().toString();

                if(!emailMA_S.isEmpty()&&!claveMA_S.isEmpty()){
                    //iremos a ActiviyTranscripcion
                    iniciarLogin();
                }else {
                    Toast.makeText(MainActivity.this, "Complete Datos", Toast.LENGTH_SHORT).show();
                }

            }
        });

        buttonRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RecuperarClaveActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (authUser.getCurrentUser()!=null){
            FirebaseUser UserBD=authUser.getCurrentUser();
            if (UserBD.isEmailVerified()){
                final String id=authUser.getCurrentUser().getUid();
                dbUser.child("ADMINISTRADOR").child(id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            //dbUser.child("USUARIOS").child("ADMINISTRADOR").child(id).child("Clave").setValue(claveMA_S);
                            startActivity(new Intent(MainActivity.this,ActivityAdministrador.class));
                            finish();
                        }else {
                            dbUser.child("PRINCIPAL").child(id).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                    if (dataSnapshot2.exists()){
                                        //dbUser.child("USUARIOS").child("PRINCIPAL").child(id).child("Clave").setValue(claveMA_S);
                                        startActivity(new Intent(MainActivity.this,ActivityTranscripcion.class));
                                        finish();

                                    }else{
                                        dbUser.child("FAMILIAR").child(id).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {
                                                if (dataSnapshot3.exists()){
                                                    // dbUser.child("USUARIOS").child("FAMILIAR").child(id).child("Clave").setValue(claveMA_S);
                                                    startActivity(new Intent(MainActivity.this,ActivityFamiliar.class));
                                                    finish();
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
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }else{
                Toast.makeText(MainActivity.this, "Se envio un mensaje a su correo, confirme por favor", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void btnRegistrarse (View view){

        Intent siguiente=new Intent(this,RegistroActivity.class);
        startActivity(siguiente);
    }

    public void iniciarLogin(){

        authUser.signInWithEmailAndPassword(emailMA_S,claveMA_S).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser UserBD=authUser.getCurrentUser();
                    if (UserBD.isEmailVerified()){
                        final String id=authUser.getCurrentUser().getUid();
                        dbUser.child("ADMINISTRADOR").child(id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    dbUser.child("ADMINISTRADOR").child(id).child("Clave").setValue(claveMA_S);
                                    startActivity(new Intent(MainActivity.this,ActivityAdministrador.class));
                                    finish();
                                }else {
                                    dbUser.child("PRINCIPAL").child(id).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                            if (dataSnapshot2.exists()){
                                                dbUser.child("PRINCIPAL").child(id).child("Clave").setValue(claveMA_S);
                                                startActivity(new Intent(MainActivity.this,ActivityTranscripcion.class));
                                                finish();

                                            }else{
                                                dbUser.child("FAMILIAR").child(id).addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {
                                                        if (dataSnapshot3.exists()){
                                                            dbUser.child("FAMILIAR").child(id).child("Clave").setValue(claveMA_S);
                                                            startActivity(new Intent(MainActivity.this,ActivityFamiliar.class));
                                                            finish();
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
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }else{
                        Toast.makeText(MainActivity.this, "Se envio un mensaje a su correo, confirme por favor", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(MainActivity.this, "No se pudo iniciar sesion, compruebe sus datos por favor", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}