package com.example.add_contactapp;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Mainadapter extends RecyclerView.Adapter<Mainadapter.ViewHolder>{

    Activity activity;
    ArrayList<caontactmodel> arrayList;
    ArrayList<caontactmodel>arraylistcpy;


    public Mainadapter(Activity activity, ArrayList<caontactmodel> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;
        this.arraylistcpy=new ArrayList<>(arrayList);
        arraylistcpy.addAll(arrayList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        caontactmodel model= arrayList.get(position);

        holder.tvName.setText(model.getName());
        holder.tvNumber.setText(model.getNumber());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void filter(CharSequence charSequence){
        ArrayList<caontactmodel> temparraylist= new ArrayList<>();
        if (!TextUtils.isEmpty(charSequence)){
            for (caontactmodel data:arrayList) {
                if (data.getName().toLowerCase().contains(charSequence)){
                    temparraylist.add(data);
                }

            }
        }else {
            temparraylist.addAll(arraylistcpy);
        }
        arrayList.clear();
        arrayList.addAll(temparraylist);
        notifyDataSetChanged();
        temparraylist.clear();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName,tvNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tv_name);
            tvNumber=itemView.findViewById(R.id.tv_number);
        }
    }
}
