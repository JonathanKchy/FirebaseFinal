package com.example.tesisfirebasefinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;

import android.text.Html;
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


public class RegistroActivity extends AppCompatActivity {
    private Button buttonContinuar;
    private EditText clave,claveRep,apodo,email;
    private FirebaseAuth baseAutenticacion;
    private RadioButton rbMAprincipal,rbMAfamiliar;
    String claveS;
    String claveRepS;
    String emailS;
    String sincronismo="0";
    String apodoS,claseUsuario;
    private DatabaseReference baseDatos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        clave=(EditText) findViewById(R.id.editTextClaveRA);
        claveRep=(EditText) findViewById(R.id.editTextClaveRepRA);
        rbMAprincipal=(RadioButton)findViewById(R.id.radioButtonPrincipal);
        rbMAfamiliar=(RadioButton)findViewById(R.id.radioButtonFamiliar);
        apodo=(EditText)findViewById(R.id.editTextApodoRA);
        email=(EditText)findViewById(R.id.editTextEmailRA);
        buttonContinuar=(Button) findViewById(R.id.btnContinuar);
        baseAutenticacion= FirebaseAuth.getInstance();
        baseDatos= FirebaseDatabase.getInstance().getReference();
        buttonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnContinuar();
            }
        });
    }

    public void btnContinuar (){
        claveS=clave.getText().toString();
        claveRepS=claveRep.getText().toString();
        emailS=email.getText().toString();
        apodoS=apodo.getText().toString();
        if(!claveRepS.isEmpty()&&!claveS.isEmpty()&&!apodoS.isEmpty()&&!emailS.isEmpty()){
            if (claveS.equals(claveRepS)){
                if (claveS.length()>5){
                    if(rbMAfamiliar.isChecked()==false&&rbMAprincipal.isChecked()==false){
                        Toast.makeText(this, "Seleccione su Usuario", Toast.LENGTH_SHORT).show();
                    }else {
                        //validacion de principal o familiar
                        if (rbMAprincipal.isChecked() == true) {
                            claseUsuario = "PRINCIPAL";
                        } else if (rbMAfamiliar.isChecked() == true) {
                            claseUsuario = "FAMILIAR";
                        }
                        //
                        final AlertDialog.Builder confirmacionDialog = new AlertDialog.Builder(RegistroActivity.this);
                        confirmacionDialog.setMessage(Html.fromHtml("Confirma que el correo es: " + "<b>" + emailS + "</b>")).setCancelable(false)
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        registrarUsuario();
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

    public void registrarUsuario(){

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

                    baseDatos.child("USUARIOS").child(claseUsuario).child(id).setValue(mapUsuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()){
                                //ir a activity que ya transcribe
                                FirebaseUser UserBD=baseAutenticacion.getCurrentUser();
                                UserBD.sendEmailVerification();
                                Toast.makeText(RegistroActivity.this, "Usuario creado, confirme en su correo por favor", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(RegistroActivity.this, "Por el momento no es posible crear usuarios, disculpas", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


                }else{
                    Toast.makeText(RegistroActivity.this, "El email ya existe", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}

