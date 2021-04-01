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
import com.example.tesisfirebasefinal.Fragments.ChatFragmentFamiliar;
import com.example.tesisfirebasefinal.Fragments.FamiliarFragment;
import com.example.tesisfirebasefinal.Fragments.ReporteFamiliarFragment;
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

public class ActivityFamiliar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout2;
    private ActionBarDrawerToggle actionBarDrawerToggle2;
    private Toolbar toolbar2;
    private NavigationView navigationView2;
    FragmentManager fragmentManager2;
    FragmentTransaction fragmentTransaction2;
    TextView txtViewHea2;
    //base de datos
    private DatabaseReference DbRef;
    private FirebaseAuth baseAutenticacion;
    String condicionSincronismoPrincipal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_familiar);

        toolbar2=(Toolbar)findViewById(R.id.toolbarFamiliar);
        setSupportActionBar(toolbar2);
        drawerLayout2=(DrawerLayout)findViewById(R.id.drawerFamiliar);
        navigationView2=(NavigationView)findViewById(R.id.navigationViewFamiliar);

        //cargo usuario FBase
        baseAutenticacion= FirebaseAuth.getInstance();
        final String id=baseAutenticacion.getCurrentUser().getUid();
        DbRef= FirebaseDatabase.getInstance().getReference("USUARIOS").child("FAMILIAR");

        DbRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    View headerView = navigationView2.getHeaderView(0);
                    TextView navUsername = (TextView) headerView.findViewById(R.id.textView4);
                    TextView correoFront=(TextView)headerView.findViewById(R.id.textView5);
                    condicionSincronismoPrincipal=dataSnapshot.child("Sincronismo").getValue().toString();
                    navUsername.setText(dataSnapshot.child("Apodo").getValue().toString());
                    correoFront.setText(dataSnapshot.child("Correo").getValue().toString());


                    DbRef=null;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        actionBarDrawerToggle2=new ActionBarDrawerToggle(this,drawerLayout2,toolbar2,R.string.open2,R.string.close2);
        drawerLayout2.addDrawerListener(actionBarDrawerToggle2);
        actionBarDrawerToggle2.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle2.syncState();
        //establecer el evento onclick al navigationview
        navigationView2.setNavigationItemSelectedListener(this);
        //cargar fragment inicial
        fragmentManager2=getSupportFragmentManager();
        fragmentTransaction2=fragmentManager2.beginTransaction();
        fragmentTransaction2.add(R.id.containerFamiliar,new FamiliarFragment());
        fragmentTransaction2.commit();
        txtViewHea2=(TextView) findViewById(R.id.textView4);

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

        drawerLayout2.closeDrawer(GravityCompat.START);
        if (menuItem.getItemId()==R.id.Ubicacion){
            if(condicionSincronismoPrincipal.equals("0")){

                Toast.makeText(this, "Por Favor, pide sincronizar a usuario Principal", Toast.LENGTH_LONG).show();

            }else {
                //ir a chatfragment
                //Toast.makeText(this, condicionSincronismoPrincipal, Toast.LENGTH_LONG).show();
                startActivity(new Intent(ActivityFamiliar.this,MapaActivityFamiliar.class));

            }

        }
        if (menuItem.getItemId()==R.id.Estadistica){
            if(condicionSincronismoPrincipal.equals("0")){

                Toast.makeText(this, "Por Favor, pide sincronizar a usuario Principal", Toast.LENGTH_LONG).show();

            }else {
                fragmentManager2=getSupportFragmentManager();
                fragmentTransaction2=fragmentManager2.beginTransaction();
                fragmentTransaction2.replace(R.id.containerFamiliar,new ReporteFamiliarFragment(condicionSincronismoPrincipal));
                fragmentTransaction2.commit();
            }
        }
        if (menuItem.getItemId()==R.id.Chat){
            if(condicionSincronismoPrincipal.equals("0")){

                Toast.makeText(this, "Por Favor, pide sincronizar a usuario Principal", Toast.LENGTH_LONG).show();

            }else {
                //ir a chatfragment
                //Toast.makeText(this, condicionSincronismoPrincipal, Toast.LENGTH_LONG).show();
                fragmentManager2=getSupportFragmentManager();
                fragmentTransaction2=fragmentManager2.beginTransaction();
                fragmentTransaction2.replace(R.id.containerFamiliar,new ChatFragmentFamiliar());
                fragmentTransaction2.commit();
            }
        }
        if (menuItem.getItemId()==R.id.CerrarSesion){
            DbRef=null;
            baseAutenticacion.signOut();

            startActivity(new Intent(ActivityFamiliar.this,MainActivity.class));
            finish();
        }
        return false;
    }
}

