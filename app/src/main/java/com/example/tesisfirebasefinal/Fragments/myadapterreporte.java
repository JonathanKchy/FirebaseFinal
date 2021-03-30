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

public class myadapterreporte  extends FirebaseRecyclerAdapter<modelReporte,myadapterreporte.myviewholder> {


    public myadapterreporte(@NonNull FirebaseRecyclerOptions<modelReporte> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull modelReporte model) {
        holder.titulotextReporte.setText(model.getTitulo());
        holder.fechatextReporte.setText(model.getFecha());
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowreporte,parent,false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder{
        TextView titulotextReporte,fechatextReporte;
        public myviewholder(@NonNull View itemView) {
            super(itemView);

            titulotextReporte=itemView.findViewById(R.id.titReporte);
            fechatextReporte=itemView.findViewById(R.id.fechaReporte);
        }
    }
}