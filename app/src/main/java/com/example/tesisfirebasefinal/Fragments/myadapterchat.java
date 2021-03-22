package com.example.tesisfirebasefinal.Fragments;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesisfirebasefinal.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class myadapterchat extends FirebaseRecyclerAdapter<modelChat,myadapterchat.myviewholder> {


    public myadapterchat(@NonNull FirebaseRecyclerOptions<modelChat> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull modelChat model) {
        holder.estadotext.setText(model.getEstado());
        holder.textotext.setText(model.getTexto());
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowdesingchat,parent,false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder{
        TextView estadotext,textotext;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            estadotext=itemView.findViewById(R.id.estadotext);
            textotext=itemView.findViewById(R.id.textotext);
        }
    }
}
