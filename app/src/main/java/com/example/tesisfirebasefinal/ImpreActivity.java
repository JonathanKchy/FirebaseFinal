package com.example.tesisfirebasefinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImpreActivity extends AppCompatActivity {
    String NOMBRE_DIRECTORIO = "MisPdfs";
    String NOMBRE_DOCUMENTO = "MiPdf.pdf";
    Button btnGenerar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impre);
        btnGenerar = findViewById(R.id.btnGenerarImpre);
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
            Toast.makeText(ImpreActivity.this, "SE CREO EL PDF", Toast.LENGTH_LONG).show();
        }
        });
    }
    public void crearPDF2(){
        Document documento= new Document();
        try {
            File file=crearFichero2(NOMBRE_DOCUMENTO);
            FileOutputStream ficheroPDF=new FileOutputStream(file.getAbsolutePath());

            PdfWriter writer=PdfWriter.getInstance(documento,ficheroPDF);
            documento.open();
            documento.add(new Paragraph("Tabla \n\n"));
            documento.add(new Paragraph("menso \n\n"));
            //insertamos tabla
            PdfPTable tabla=new PdfPTable(5);
            for (int i=0;i<15;i++){
                tabla.addCell("celda"+i);
            }
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
