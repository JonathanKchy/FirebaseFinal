package com.example.tesisfirebasefinal;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesisfirebasefinal.Fragments.myadapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import harmony.java.awt.Color;

public class PruebaFamiliar extends AppCompatActivity {
    int contador;
    private DatabaseReference DbRef;
    private FirebaseAuth baseAutenticacion;
    private Font fNegrita=new Font(Font.TIMES_ROMAN,30,Font.BOLD);
    private Font fAzul=new Font(Font.TIMES_ROMAN,20,Font.BOLD, Color.BLUE);
    String NOMBRE_DIRECTORIO = "MisPdfsFam";
    String NOMBRE_DOCUMENTO = "Transcripcion.pdf";
    String nombreUsuario;
    Button btnGenerar;
    Document documento;
    PdfPTable tabla;
    File file;
    FileOutputStream ficheroPDF;
    PdfWriter writer;
    String dato;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba_familiar);
        dato=getIntent().getStringExtra("dato");

        baseAutenticacion= FirebaseAuth.getInstance();
        DbRef= FirebaseDatabase.getInstance().getReference();
        final String idPropio=baseAutenticacion.getCurrentUser().getUid();

        DbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //
                    contador=(int)dataSnapshot.child("TRANSCRIPCIONES").child(dato).getChildrenCount();

                    // Toast.makeText(getApplicationContext(), "hhhh", Toast.LENGTH_LONG).show();

                    nombreUsuario=dataSnapshot.child("USUARIOS").child("PRINCIPAL").child(dato).child("Apodo").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });

        //tabla
       // final String idPropio2=baseAutenticacion.getCurrentUser().getUid();
        DbRef.child("TRANSCRIPCIONES").child(dato).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                tabla = new PdfPTable(1);

                    /*for(int i = 0 ; i < contador ; i++) {
                        tabla.addCell("CELDA "+i);
                    }
                    Toast.makeText(Prueba.this, "Entro", Toast.LENGTH_LONG).show();*/

                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {

                    tabla.addCell("\t\t\t\t\t\t\t\t"+snapshot.child("titulo").getValue().toString() + ":\n\t\t\t\t\t\t\t\t"+snapshot.child("fecha").getValue().toString());
                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //boton
        btnGenerar = findViewById(R.id.btnGenerarFa);
        // Permisos
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,},
                    1000);
        }

        btnGenerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearPDF2();
                Toast.makeText(PruebaFamiliar.this, "SE CREO EL PDF", Toast.LENGTH_LONG).show();
                finish();
            }
        });


    }
    public void crearPDF2(){
        documento= new Document();
        try {
            file=crearFichero2(NOMBRE_DOCUMENTO);
            ficheroPDF=new FileOutputStream(file.getAbsolutePath());

            writer=PdfWriter.getInstance(documento,ficheroPDF);
            documento.open();
            Paragraph paragraph=new Paragraph("Hola la tabla es de: "+nombreUsuario,fNegrita);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            documento.add(paragraph);
            Paragraph paragraph2=new Paragraph("Tiene un total de: "+contador+" transcripciones\n\n",fAzul);
            paragraph2.setAlignment(Element.ALIGN_CENTER);
            documento.add(paragraph2);

            //insertamos tabla
           /* tabla=new PdfPTable(5);
            for (int i=0;i<15;i++){
                tabla.addCell("celda"+i);
            }*/
            documento.add(tabla);
        }catch (DocumentException e){

        }catch (IOException e){

        }finally {
            documento.close();
        }
    }
    public File crearFichero2(String nombreFichero){
        File ruta=getRuta();
        File fichero=null;
        if (ruta!=null){
            fichero=new File(ruta,nombreFichero);
        }
        return fichero;
    }
    public File getRuta(){
        File ruta=null;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            ruta=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),NOMBRE_DIRECTORIO);
            if(ruta!=null){
                if (!ruta.mkdir()){
                    if (!ruta.exists()){
                        return null;
                    }
                }
            }
        }
        return ruta;
    }
}
