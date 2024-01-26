package com.example.usablesecurity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public  class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    ArrayList<Links> links;

    public Adapter(ArrayList<Links> links) {
        this.links = links;
    }

    public ArrayList<Links> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<Links> links) {
        this.links = links;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.listi_tem,null,false);
        ViewHolder holder=new ViewHolder(v);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
          Links links1= links.get(position);
          holder.img.setImageResource(links1.getImg());
          holder.name.setText(links1.getName());
    }

    @Override
    public int getItemCount() {
        return links.size();
    }
    public void addItem(Links newItem) {
        links.add(newItem);
        notifyItemInserted(links.size() - 1);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            img=itemView.findViewById(R.id.imag);
        }
    }
}
