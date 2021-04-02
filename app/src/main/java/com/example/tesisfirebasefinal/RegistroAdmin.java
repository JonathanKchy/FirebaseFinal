package com.example.tesisfirebasefinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegistroAdmin extends AppCompatActivity {
    private Button buttonContinuarAdmin;
    private EditText clave,claveRep,apodo,email;
    private FirebaseAuth baseAutenticacion;
    String claveS;
    String claveRepS;
    String emailS;
    String sincronismo="0";
    String apodoS,claseUsuario;
    private DatabaseReference baseDatos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_admin);
        clave=(EditText) findViewById(R.id.editTextClaveAdmin);
        claveRep=(EditText) findViewById(R.id.editTextClaveRepAdmin);
        apodo=(EditText)findViewById(R.id.editTextApodoAdmin);
        email=(EditText)findViewById(R.id.editTextEmailAdmin);
        buttonContinuarAdmin=(Button) findViewById(R.id.btnContinuarAdmin);
        baseAutenticacion= FirebaseAuth.getInstance();
        baseDatos= FirebaseDatabase.getInstance().getReference();
        buttonContinuarAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnContinuarAdmin();
            }
        });
    }
    public void btnContinuarAdmin (){
        claveS=clave.getText().toString();
        claveRepS=claveRep.getText().toString();
        emailS=email.getText().toString();
        apodoS=apodo.getText().toString();
        if(!claveRepS.isEmpty()&&!claveS.isEmpty()&&!apodoS.isEmpty()&&!emailS.isEmpty()){
            if (claveS.equals(claveRepS)){
                if (claveS.length()>5){

                        //
                        final AlertDialog.Builder confirmacionDialog = new AlertDialog.Builder(RegistroAdmin.this);
                        confirmacionDialog.setMessage(Html.fromHtml("Confirma que el correo es: " + "<b>" + emailS + "</b>")).setCancelable(false)
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        registrarUsuarioAdmin();
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
                else {
                    Toast.makeText(this, "Su clave debe ser al menos de 6 caracteres", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, "Sus Claves no son iguales", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Llene los campos", Toast.LENGTH_SHORT).show();
        }


    }

    public void registrarUsuarioAdmin(){

        baseAutenticacion.createUserWithEmailAndPassword(emailS,claveS).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //baseAutenticacion.setLanguageCode("es");
                    String id=baseAutenticacion.getCurrentUser().getUid();
                    Map<String,Object> mapUsuario=new HashMap<>();
                    mapUsuario.put("idPropio",id);
                    mapUsuario.put("Apodo",apodoS);
                    mapUsuario.put("Correo",emailS);
                    mapUsuario.put("Clave",claveS);
                    mapUsuario.put("Sincronismo",sincronismo);

                    baseDatos.child("USUARIOS").child("ADMINISTRADOR").child(id).setValue(mapUsuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()){
                                //ir a activity que ya transcribe
                                FirebaseUser UserBD=baseAutenticacion.getCurrentUser();
                                UserBD.sendEmailVerification();
                                Toast.makeText(RegistroAdmin.this, "Usuario creado, confirme en su correo por favor", Toast.LENGTH_SHORT).show();
                                baseDatos=null;
                                finish();
                            }else{
                                Toast.makeText(RegistroAdmin.this, "Por el momento no es posible crear usuarios, disculpas", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


                }else{
                    Toast.makeText(RegistroAdmin.this, "El email ya existe", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == event.KEYCODE_BACK){
            baseDatos=null;
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
