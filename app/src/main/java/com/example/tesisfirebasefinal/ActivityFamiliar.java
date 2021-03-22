package com.example.tesisfirebasefinal;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tesisfirebasefinal.Fragments.FamiliarFragment;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_familiar);

        toolbar2=(Toolbar)findViewById(R.id.toolbarFamiliar);
        setSupportActionBar(toolbar2);
        drawerLayout2=(DrawerLayout)findViewById(R.id.drawerFamiliar);
        navigationView2=(NavigationView)findViewById(R.id.navigationViewFamiliar);
        actionBarDrawerToggle2=new ActionBarDrawerToggle(this,drawerLayout2,toolbar2,R.string.open2,R.string.close2);
        drawerLayout2.addDrawerListener(actionBarDrawerToggle2);
        actionBarDrawerToggle2.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle2.syncState();
        //establecer el evento onclick al navigationview
        navigationView2.setNavigationItemSelectedListener(this);

        //cargo usuario FBase
        baseAutenticacion= FirebaseAuth.getInstance();
        final String id=baseAutenticacion.getCurrentUser().getUid();
        DbRef= FirebaseDatabase.getInstance().getReference();

        DbRef.child("USUARIOS").child("FAMILIAR").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    View headerView = navigationView2.getHeaderView(0);
                    TextView navUsername = (TextView) headerView.findViewById(R.id.textView4);
                    TextView correoFront=(TextView)headerView.findViewById(R.id.textView5);
                    navUsername.setText(dataSnapshot.child("Apodo").getValue().toString());
                    correoFront.setText(dataSnapshot.child("Correo").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //cargar fragment inicial
        fragmentManager2=getSupportFragmentManager();
        fragmentTransaction2=fragmentManager2.beginTransaction();
        fragmentTransaction2.add(R.id.containerFamiliar,new FamiliarFragment());
        fragmentTransaction2.commit();
        txtViewHea2=(TextView) findViewById(R.id.textView4);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        drawerLayout2.closeDrawer(GravityCompat.START);
        if (menuItem.getItemId()==R.id.SincronizacionPrincipal){
            Toast.makeText(this, "SincronizacionPrincipal", Toast.LENGTH_LONG).show();
        }
        if (menuItem.getItemId()==R.id.Ubicacion){
            Toast.makeText(this, "Ver Ubicacion", Toast.LENGTH_LONG).show();
            /*fragmentManager2=getSupportFragmentManager();
            fragmentTransaction2=fragmentManager2.beginTransaction();
            fragmentTransaction2.replace(R.id.container,new FamiliarFragment());
            fragmentTransaction2.commit();*/

        }
        if (menuItem.getItemId()==R.id.Estadistica){
            Toast.makeText(this, "Estadistica", Toast.LENGTH_LONG).show();
            //startActivity(new Intent(ActivityFamiliar.this,MapaActivity.class));
        }
        if (menuItem.getItemId()==R.id.Chat){
            Toast.makeText(this, "Chat", Toast.LENGTH_LONG).show();
            //startActivity(new Intent(ActivityFamiliar.this,MapaActivity.class));
        }
        if (menuItem.getItemId()==R.id.CerrarSesion){
            baseAutenticacion.signOut();
            startActivity(new Intent(ActivityFamiliar.this,MainActivity.class));
            finish();
        }
        return false;
    }
}

