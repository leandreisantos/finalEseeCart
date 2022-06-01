package com.example.eseecart;

import android.app.Application;
import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Furnitureholder extends RecyclerView.ViewHolder {


    TextView more;
    ConstraintLayout cl;
    ConstraintLayout clholder;
    CardView view;
    TextView deleteholder;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference UserJournalRef;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentid = user.getUid();

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
        TextView statusholder = itemView.findViewById(R.id.status);

        clholder = itemView.findViewById(R.id.clf);

        UserJournalRef = database.getReference("All users").child(currentid);

        UserJournalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                String url = snapshot.child("url").getValue(String.class);


                Picasso.get().load(url).into(ivholder);
                messageholder.setText(name +" Buy furniture. Tap to view");
                statusholder.setText(status);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        
    }

    public void setNotificationfrag(FragmentActivity application,String id,String userid,String furid,String total,String date,String time,String status){
        TextView messageholder = itemView.findViewById(R.id.tv_id);
        deleteholder = itemView.findViewById(R.id.tv_delete);
        ConstraintLayout clholder2 = itemView.findViewById(R.id.cl1);

        clholder = itemView.findViewById(R.id.clf);

        if(status.equals("Pending")){
            clholder2.setVisibility(View.GONE);
        }
        messageholder.setText("Your order "+id+" is out for delivery");

    }

    public void setPurchase(FragmentActivity application,String id,String userid,String furid,String total,String date,String time,String status){
        ImageView ivholder = itemView.findViewById(R.id.iv);
        TextView nameholder = itemView.findViewById(R.id.name);
        view = itemView.findViewById(R.id.cv_login);

        UserJournalRef = database.getReference("All Furnitures item").child(furid);

        UserJournalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                String url = snapshot.child("url").getValue(String.class);

                nameholder.setText(name);
                Picasso.get().load(url).into(ivholder);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}
