package com.example.eseecart;

import android.app.Application;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class Furnitureholder extends RecyclerView.ViewHolder {


    TextView more;
    ConstraintLayout cl;
    ConstraintLayout clholder;
    CardView view;
    public Furnitureholder(@NonNull View itemView) {
        super(itemView);
    }

    public void setChoices(Application application,String name,String id,String desc,String url){
        TextView nameholder = itemView.findViewById(R.id.tv_name);
        TextView descholder = itemView.findViewById(R.id.tv_desc);
        TextView count = itemView.findViewById(R.id.item_count);
        ImageView iv = itemView.findViewById(R.id.iv);
        more = itemView.findViewById(R.id.more);

        Picasso.get().load(url).into(iv);

        nameholder.setText(name);
        descholder.setText(desc);

    }

    public void setCat(FragmentActivity fragmentActivity,String name,String id,String desc,String url){
        cl = itemView.findViewById(R.id.cl_f);
        TextView nameholder = itemView.findViewById(R.id.tv_name);
        TextView descholder = itemView.findViewById(R.id.tv_desc);
        TextView count = itemView.findViewById(R.id.item_count);
        ImageView iv = itemView.findViewById(R.id.iv);
        more = itemView.findViewById(R.id.more);

        more.setVisibility(View.GONE);
        nameholder.setText(name);
        Picasso.get().load(url).into(iv);
        descholder.setText(desc);
    }

    public void setFurniture(Application application,String id,String idcat,String name,String price,String color,String url,String key){
        TextView nameholder = itemView.findViewById(R.id.tv_name);
        TextView priceholder = itemView.findViewById(R.id.tv_price);
        TextView colorholder = itemView.findViewById(R.id.tv_color);
        clholder = itemView.findViewById(R.id.cl);
        ImageView ivholder = itemView.findViewById(R.id.iv);

        nameholder.setText(name);
        priceholder.setText("₱:"+price);
        colorholder.setText("Color:"+color);

        Picasso.get().load(url).into(ivholder);
    }

    public void setFurnituredash(FragmentActivity application,String id,String idcat,String name,String price,String color,String url,String key){
        TextView nameholder = itemView.findViewById(R.id.tv_name);
        TextView priceholder = itemView.findViewById(R.id.price);
        TextView colorholder = itemView.findViewById(R.id.color);
        ImageView ivholder = itemView.findViewById(R.id.iv);
        view = itemView.findViewById(R.id.cv_login);

        nameholder.setText(name);
        priceholder.setText("₱:"+price);
        colorholder.setText("Color:"+color);

        Picasso.get().load(url).into(ivholder);
    }

    public void setNotificationAdmin(Application application,String id,String userid,String furid,String total,String date,String time,String status){
        ImageView ivholder = itemView.findViewById(R.id.iv);
        TextView messageholder = itemView.findViewById(R.id.message);
        clholder = itemView.findViewById(R.id.clf);
        
    }


}
