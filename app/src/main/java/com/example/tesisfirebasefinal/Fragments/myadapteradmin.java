package com.example.tesisfirebasefinal.Fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesisfirebasefinal.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class myadapteradmin  extends FirebaseRecyclerAdapter<modelAdmin,myadapteradmin.myviewholder> {


    public myadapteradmin(@NonNull FirebaseRecyclerOptions<modelAdmin> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull modelAdmin model) {
        holder.nombre.setText(model.getApodo());
        holder.correo.setText(model.getCorreo());
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowadmin,parent,false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder{
        TextView nombre,correo;
        public myviewholder(@NonNull View itemView) {
            super(itemView);

            nombre=itemView.findViewById(R.id.nombreAdmin);
            correo=itemView.findViewById(R.id.correoAdmin);
        }
    }
}