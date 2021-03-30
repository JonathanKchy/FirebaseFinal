package com.example.tesisfirebasefinal;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tesisfirebasefinal.Fragments.ChatFragment;
import com.example.tesisfirebasefinal.Fragments.EliminarSincronismo;
import com.example.tesisfirebasefinal.Fragments.PortafolioFragment;
import com.example.tesisfirebasefinal.Fragments.QuitarSincronismoFragment;
import com.example.tesisfirebasefinal.Fragments.ReporteFragment;
import com.example.tesisfirebasefinal.Fragments.SincronismoFragment;
import com.example.tesisfirebasefinal.Fragments.TranscripcionFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActivityTranscripcion extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    // private Button buttonCerrar;
    //private FirebaseAuth authUserAT;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    //variables para cargar el fragment
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    //text
    TextView txtViewHea;
    //base de datos
    private DatabaseReference DbRef;
    private FirebaseAuth baseAutenticacion;
    String condicionSincronismo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transcripcion);

        //cargar menu
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout=findViewById(R.id.drawer);
        navigationView=findViewById(R.id.navigationView);
        //base de datos
        baseAutenticacion= FirebaseAuth.getInstance();
        final String id=baseAutenticacion.getCurrentUser().getUid();
        DbRef= FirebaseDatabase.getInstance().getReference("USUARIOS").child("PRINCIPAL");

        DbRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    View headerView = navigationView.getHeaderView(0);
                    TextView navUsername = (TextView) headerView.findViewById(R.id.textView4);
                    TextView correoFront=(TextView)headerView.findViewById(R.id.textView5);
                    navUsername.setText(dataSnapshot.child("Apodo").getValue().toString());
                    correoFront.setText(dataSnapshot.child("Correo").getValue().toString());
                    condicionSincronismo=dataSnapshot.child("Sincronismo").getValue().toString();
                    DbRef=null;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //mostrar menu lateral
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        //establecer el evento onclick al navigationview
        navigationView.setNavigationItemSelectedListener(this);


        //cargar fragment principal
        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container,new TranscripcionFragment());
        fragmentTransaction.commit();
        txtViewHea=(TextView) findViewById(R.id.textView4);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == event.KEYCODE_BACK){
            final AlertDialog.Builder confirmacionDialog = new AlertDialog.Builder(this);
            confirmacionDialog.setMessage(Html.fromHtml( "Si sales PERDERÁS los datos que aun no hayas guardado,"+"<b>" +" ¿DESEAS SALIR?  "+"</b>")).setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alerta = confirmacionDialog.create();
            alerta.setTitle("ALERTA");
            alerta.show();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);
        if (menuItem.getItemId()==R.id.SincronizacionFamiliar){
            //Toast.makeText(this, "SincronizacionFamiliar", Toast.LENGTH_LONG).show();


            if(condicionSincronismo.equals("0")){
                Toast.makeText(this, "Por Favor, sincronizar con su Familiar", Toast.LENGTH_LONG).show();
                fragmentManager=getSupportFragmentManager();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container,new SincronismoFragment());
                fragmentTransaction.commit();
            }else {
                Toast.makeText(this, "Ya cuenta con Familiar", Toast.LENGTH_LONG).show();
                /*fragmentManager=getSupportFragmentManager();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container,new QuitarSincronismoFragment());
                fragmentTransaction.commit();*/
            }
        }
        if (menuItem.getItemId()==R.id.EliminarSincronizacion){
            //Toast.makeText(this, "SincronizacionFamiliar", Toast.LENGTH_LONG).show();

            if(condicionSincronismo.equals("0")){
                Toast.makeText(this, "Aun no tienes SINCRONISMO", Toast.LENGTH_LONG).show();

            }else {

                //getFragmentManager().beginTransaction().remove(this).commit();
                fragmentManager=getSupportFragmentManager();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container,new EliminarSincronismo());
                fragmentTransaction.commit();
            }
        }
        if (menuItem.getItemId()==R.id.Transcripcion){
            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container,new TranscripcionFragment());
            fragmentTransaction.commit();

        }
        if (menuItem.getItemId()==R.id.Portafolio){
            // Toast.makeText(this, "Portafolio", Toast.LENGTH_LONG).show();
            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container,new PortafolioFragment());
            fragmentTransaction.commit();
        }
        if (menuItem.getItemId()==R.id.Ubicacion){
            startActivity(new Intent(ActivityTranscripcion.this,MapaActivity.class));
        }
        if (menuItem.getItemId()==R.id.Estadistica){
            //Toast.makeText(this, "Estadistica", Toast.LENGTH_LONG).show();
            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container,new ReporteFragment());
            fragmentTransaction.commit();
           // startActivity(new Intent(ActivityTranscripcion.this,Prueba.class));
        }
        if (menuItem.getItemId()==R.id.Chat){
            if(condicionSincronismo.equals("0")){

                Toast.makeText(this, "Por Favor, sincronizar con su Familiar", Toast.LENGTH_LONG).show();
                fragmentManager=getSupportFragmentManager();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container,new SincronismoFragment());
                fragmentTransaction.commit();
            }else {
                //ir a chatfragment
                //Toast.makeText(this, condicionSincronismo, Toast.LENGTH_LONG).show();
                fragmentManager=getSupportFragmentManager();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container,new ChatFragment());
                fragmentTransaction.commit();
            }

            //Toast.makeText(this, "Chat", Toast.LENGTH_LONG).show();
            //startActivity(new Intent(ActivityFamiliar.this,MapaActivity.class));
        }
        if (menuItem.getItemId()==R.id.CerrarSesion){
            baseAutenticacion.signOut();
            startActivity(new Intent(ActivityTranscripcion.this,MainActivity.class));
            finish();
        }
        return false;
    }
}

