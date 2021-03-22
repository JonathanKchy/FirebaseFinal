package com.example.tesisfirebasefinal.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tesisfirebasefinal.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SincronismoFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;
    RecyclerView recview;
    myadaptersincronismo adapter;
    private DatabaseReference DbRef;
    private FirebaseAuth baseAutenticacion;

    public SincronismoFragment() {

    }


    public static SincronismoFragment newInstance(String param1, String param2) {
        SincronismoFragment fragment = new SincronismoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_sincronismo, container, false);
        recview=(RecyclerView)view.findViewById(R.id.recviewSincronismo);
        recview.setLayoutManager(new LinearLayoutManager(getContext()));

        baseAutenticacion= FirebaseAuth.getInstance();
        final String id=baseAutenticacion.getCurrentUser().getUid();
        DbRef= FirebaseDatabase.getInstance().getReference().child("USUARIOS");

        FirebaseRecyclerOptions<modelSincronismo> options =
                new FirebaseRecyclerOptions.Builder<modelSincronismo>()
                        .setQuery(DbRef.child("FAMILIAR"), modelSincronismo.class)
                        .build();

        adapter=new myadaptersincronismo(options);
        recview.setAdapter(adapter);



        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
