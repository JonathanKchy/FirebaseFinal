package com.example.tesisfirebasefinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RecuperarClaveActivity extends AppCompatActivity {

    private EditText correoRCA;
    private Button buttonConfirmar;
    private String emailS;
    private FirebaseAuth authCambio;
    private ProgressDialog progreso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_clave);
        correoRCA=(EditText)findViewById(R.id.editTextEmailRecuperar);
        buttonConfirmar=(Button)findViewById(R.id.btnEnviar);
        authCambio=FirebaseAuth.getInstance();
        progreso=new ProgressDialog(this);
        buttonConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailS=correoRCA.getText().toString();
                if(!emailS.isEmpty())
                {
                    progreso.setMessage("Enviando mensaje");
                    progreso.setCanceledOnTouchOutside(false);
                    progreso.show();
                    cambiarClave();
                }else {
                    Toast.makeText(RecuperarClaveActivity.this, "Ingrese Correo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void cambiarClave(){
        authCambio.setLanguageCode("es");
        authCambio.sendPasswordResetEmail(emailS).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    Toast.makeText(RecuperarClaveActivity.this, "Se envio un mensaje a su correo", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(RecuperarClaveActivity.this, "Este correo no existe", Toast.LENGTH_SHORT).show();
                }
                progreso.dismiss();
            }
        });
    }
}

