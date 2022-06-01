package com.example.eseecart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ShowBuyerInfo extends AppCompatActivity {

    TextView nameholder,numberholder,addholder;

    ImageView ivholder;
    TextView nameitemholder,priceholder,colorholder,totalholder,statusholder;
    CardView cvholder;

    String useridbundle,furidbundle,idbundle;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference UserJournalRef,databaseReference,databaseReference2,databaseReference3,databaseReference4;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentid = user.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_buyer_info);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            useridbundle = extras.getString("userid");
            furidbundle = extras.getString("furid");
            idbundle = extras.getString("id");
        }else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }

        UserJournalRef = database.getReference("All users").child(useridbundle);
        databaseReference = database.getReference("All Furnitures item").child(furidbundle);
        databaseReference2 = database.getReference("All Payment").child(idbundle);
        databaseReference3 = database.getReference("All Payment user").child(currentid).child(idbundle);
        databaseReference4 = database.getReference("All Notification").child(useridbundle);

        nameholder = findViewById(R.id.tv_name);
        numberholder = findViewById(R.id.tv_number);
        addholder = findViewById(R.id.tv_add);

        nameitemholder = findViewById(R.id.tv_name_item);
        priceholder = findViewById(R.id.tv_price);
        colorholder = findViewById(R.id.tv_color);
        totalholder = findViewById(R.id.tv_total);
        cvholder = findViewById(R.id.cv_login);
        statusholder = findViewById(R.id.status_text);
        ivholder = findViewById(R.id.iv);
    }

    @Override
    protected void onStart() {
        super.onStart();

        UserJournalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                String mobile = snapshot.child("mobile").getValue(String.class);
                String url = snapshot.child("url").getValue(String.class);
                String add = snapshot.child("address").getValue(String.class);

                nameholder .setText(name);
                numberholder.setText(mobile);
                addholder.setText(add);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                String price = snapshot.child("price").getValue(String.class);
                String url = snapshot.child("url").getValue(String.class);
                String color = snapshot.child("color").getValue(String.class);

                nameitemholder .setText(name);
                priceholder.setText(price);
                colorholder.setText(color);

                Picasso.get().load(url).into(ivholder);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String status = snapshot.child("status").getValue(String.class);
                String total = snapshot.child("total").getValue(String.class);
                String id = snapshot.child("id").getValue(String.class);

                statusholder.setText(status);
                totalholder.setText("₱"+total);

                cvholder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialog(id);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void showDialog(String id) {

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.status_dialog,null);
        TextView pending = view.findViewById(R.id.pending);
        TextView otw = view.findViewById(R.id.otw);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        alertDialog.show();

        pending.setOnClickListener(view1 -> {
            databaseReference2.child("status").setValue("Pending");
            databaseReference3.child("status").setValue("Pending");
            Toast.makeText(this, "Change to Pending", Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
        });
        otw.setOnClickListener(view1 -> {
            databaseReference2.child("status").setValue("Out for delivery");
            databaseReference3.child("status").setValue("Out for delivery");
            Toast.makeText(this, "Change to Pending", Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
        });

    }
}