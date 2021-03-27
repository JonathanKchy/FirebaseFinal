package com.example.tesisfirebasefinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import harmony.java.awt.Color;

public class Prueba extends AppCompatActivity {
    String NOMBRE_DIRECTORIO = "MisPDFs",nombreUsuario;
    String NOMBRE_DOCUMENTO = "MiPDF.pdf";
    private DatabaseReference DbRef;
    private FirebaseAuth baseAutenticacion;
    EditText etTexto;
    Button btnGenerar;
    Document documento;
    PdfPTable tabla;
    File file;
    FileOutputStream ficheroPDF;
    PdfWriter writer;
    int contador;
    public static int MILISEGUNDOS_ESPERA = 2000;
    private Font fNegrita=new Font(Font.TIMES_ROMAN,30,Font.BOLD);
    private Font fAzul=new Font(Font.TIMES_ROMAN,20,Font.BOLD, Color.BLUE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);
        etTexto = findViewById(R.id.etTexto);
        btnGenerar = findViewById(R.id.btnGenerar);
        baseAutenticacion= FirebaseAuth.getInstance();
        DbRef= FirebaseDatabase.getInstance().getReference();
        final String id=baseAutenticacion.getCurrentUser().getUid();

        DbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //
                    contador=(int)dataSnapshot.child("TRANSCRIPCIONES").child(id).getChildrenCount();
                    nombreUsuario=dataSnapshot.child("USUARIOS").child("PRINCIPAL").child(id).child("Apodo").getValue().toString();
                    Toast.makeText(Prueba.this, "eL NUMERO DE TRANSCRICIONES SON: "+contador, Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(Prueba.this, "eL NUMERO DE TRANSCRICIONES SON: 0", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
        // Permisos
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,},
                    1000);
        }

        // Genera el documento
        btnGenerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                documento = new Document();
                base();
               esperarYCerrar(MILISEGUNDOS_ESPERA);

            }
        });
    }
    public void crearPDF() {


        try {
            file = crearFichero(NOMBRE_DOCUMENTO);
            ficheroPDF = new FileOutputStream(file.getAbsolutePath());

            writer = PdfWriter.getInstance(documento, ficheroPDF);

            documento.open();
            Paragraph paragraph=new Paragraph("Hola "+nombreUsuario,fNegrita);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            documento.add(paragraph);
            Paragraph paragraph2=new Paragraph("Tienes un total de: "+contador+" transcripciones",fAzul);
            paragraph2.setAlignment(Element.ALIGN_CENTER);
            documento.add(paragraph2);
            documento.add(new Paragraph( etTexto.getText().toString()+"\n\n"));


            // Insertamos una tabla
            /*tabla = new PdfPTable(5);
            for(int i = 0 ; i < 15 ; i++) {
                tabla.addCell("CELDA "+i);
            }*/

           documento.add(tabla);

        } catch(DocumentException e) {
        } catch(IOException e) {
        } finally {
            documento.close();
        }
    }

    public File crearFichero(String nombreFichero) {
        File ruta = getRuta();

        File fichero = null;
        if(ruta != null) {
            fichero = new File(ruta, nombreFichero);
        }

        return fichero;
    }

    public File getRuta() {
        File ruta = null;

        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            ruta = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), NOMBRE_DIRECTORIO);

            if(ruta != null) {
                if(!ruta.mkdirs()) {
                    if(!ruta.exists()) {
                        return null;
                    }
                }
            }

        }
        return ruta;
    }
    public void base(){
        final String id=baseAutenticacion.getCurrentUser().getUid();
        DbRef.child("TRANSCRIPCIONES").child(id).addValueEventListener(new ValueEventListener() {

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
    }
    public void esperarYCerrar(int milisegundos) {
    Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
        // acciones que se ejecutan tras los milisegundos
            crearPDF();
                Toast.makeText(Prueba.this, "SE CREO EL PDF", Toast.LENGTH_LONG).show();

            }
}, milisegundos);
}

}
