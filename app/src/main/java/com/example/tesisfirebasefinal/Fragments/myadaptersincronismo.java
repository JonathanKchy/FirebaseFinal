package com.example.tesisfirebasefinal.Fragments;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesisfirebasefinal.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class myadaptersincronismo extends FirebaseRecyclerAdapter<modelSincronismo,myadaptersincronismo.myviewholder> {

    public myadaptersincronismo(@NonNull FirebaseRecyclerOptions<modelSincronismo> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final myviewholder holder, int position, @NonNull final modelSincronismo model) {
        holder.nombretext.setText(model.getApodo());
        holder.correotext.setText(model.getCorreo());
        holder.idPropioText.setText(model.getIdPropio());
        holder.nombretext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity=(AppCompatActivity)v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container,new QuitarSincronismoFragment(model.getIdPropio())).addToBackStack(null).commit();


            }
        });
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowdesingsincronismo,parent,false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder
    {
        TextView nombretext,correotext,idPropioText;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            nombretext=itemView.findViewById(R.id.nombretext);
            correotext=itemView.findViewById(R.id.correotext);
            idPropioText=itemView.findViewById(R.id.sincronismotext);
        }
    }
}
