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

import com.example.tesisfirebasefinal.Fragments.AdminFragment;
import com.example.tesisfirebasefinal.Fragments.AdminUsuariosFragment;
import com.example.tesisfirebasefinal.Fragments.FamiliarFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ActivityAdministrador extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout3;
    private ActionBarDrawerToggle actionBarDrawerToggle3;
    private Toolbar toolbar3;
    private NavigationView navigationView3;
    FragmentManager fragmentManager3;
    FragmentTransaction fragmentTransaction3;
    TextView txtViewHea3;
    //base de datos
    private DatabaseReference DbRef;
    private FirebaseAuth baseAutenticacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador);

        toolbar3=(Toolbar)findViewById(R.id.toolbarAdmin);
        setSupportActionBar(toolbar3);
        drawerLayout3=(DrawerLayout)findViewById(R.id.drawerAdmin);
        navigationView3=(NavigationView)findViewById(R.id.navigationViewAdmin);


        //cargo usuario FBase
        baseAutenticacion= FirebaseAuth.getInstance();
        final String id=baseAutenticacion.getCurrentUser().getUid();
        DbRef= FirebaseDatabase.getInstance().getReference();

        DbRef.child("USUARIOS").child("ADMINISTRADOR").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    View headerView = navigationView3.getHeaderView(0);
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
        actionBarDrawerToggle3=new ActionBarDrawerToggle(this,drawerLayout3,toolbar3,R.string.open3,R.string.close3);
        drawerLayout3.addDrawerListener(actionBarDrawerToggle3);
        actionBarDrawerToggle3.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle3.syncState();
        //establecer el evento onclick al navigationview
        navigationView3.setNavigationItemSelectedListener(this);
        //cargar fragment inicial
        fragmentManager3=getSupportFragmentManager();
        fragmentTransaction3=fragmentManager3.beginTransaction();
        fragmentTransaction3.add(R.id.containerAdmin,new AdminUsuariosFragment());
        fragmentTransaction3.commit();
        txtViewHea3=(TextView) findViewById(R.id.textView4);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawerLayout3.closeDrawer(GravityCompat.START);
        if (menuItem.getItemId()==R.id.Detalles){
            fragmentManager3=getSupportFragmentManager();
            fragmentTransaction3=fragmentManager3.beginTransaction();
            fragmentTransaction3.replace(R.id.containerAdmin,new AdminUsuariosFragment());
            fragmentTransaction3.commit();
        }

        if (menuItem.getItemId()==R.id.Estadistica){
            Toast.makeText(this, "Estadistica", Toast.LENGTH_LONG).show();
            //startActivity(new Intent(ActivityFamiliar.this,MapaActivity.class));
        }
        if (menuItem.getItemId()==R.id.CrearAdministrador){
            Toast.makeText(this, "CrearAdministrador", Toast.LENGTH_LONG).show();
            //startActivity(new Intent(ActivityFamiliar.this,MapaActivity.class));
        }
        if (menuItem.getItemId()==R.id.CerrarSesion){
            baseAutenticacion.signOut();
            startActivity(new Intent(ActivityAdministrador.this,MainActivity.class));
            finish();
        }
        return false;
    }
}

