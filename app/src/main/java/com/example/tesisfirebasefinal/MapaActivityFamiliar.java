package com.example.tesisfirebasefinal;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MapaActivityFamiliar extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseReference DbRef;
    private FirebaseAuth baseAutenticacion;
    private ArrayList<Marker> tmpRealTimeMarkers=new ArrayList<>();
    private ArrayList<Marker> realTimeMarkers=new ArrayList<>();
    double latitudDouble,longitudDouble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_familiar);

        baseAutenticacion= FirebaseAuth.getInstance();
        DbRef= FirebaseDatabase.getInstance().getReference();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        final String id=baseAutenticacion.getCurrentUser().getUid();
        DbRef.child("USUARIOS").child("FAMILIAR").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){}
                final String idPrincipal=dataSnapshot.child("Sincronismo").getValue().toString();

                DbRef.child("UBICACION").child(idPrincipal).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (Marker marker:realTimeMarkers){
                            marker.remove();
                        }
                       for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                            PuntosMapa pm = snapshot.getValue(PuntosMapa.class);
                            String Fecha=pm.getFecha();
                            String latitud=pm.getLatitud();
                            String longitud=pm.getLongitud();
                            latitudDouble=Double.parseDouble(latitud);
                            longitudDouble=Double.parseDouble(longitud);
                            MarkerOptions markerOptions=new MarkerOptions();
                            markerOptions.title(Fecha);
                            markerOptions.position(new LatLng(latitudDouble,longitudDouble));
                            tmpRealTimeMarkers.add(mMap.addMarker(markerOptions));
                        }
                        realTimeMarkers.clear();
                       realTimeMarkers.addAll(tmpRealTimeMarkers);
                        LatLng miUbicacion = new LatLng(latitudDouble, longitudDouble);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(miUbicacion));
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(miUbicacion)
                                .zoom(18)
                                .bearing(90)
                                .tilt(45)
                                .build();
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
