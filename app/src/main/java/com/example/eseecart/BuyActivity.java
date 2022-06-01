package com.example.eseecart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BuyActivity extends AppCompatActivity {
    TextView nameholder,numberholder,addholder;

    ImageView ivholder;
    TextView nameitemholder,priceholder,colorholder,totalholder;
    CardView cvholder;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference UserJournalRef,databaseReference,databaseReference2,databaseReference3;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentid = user.getUid();

    String idbundle;
    BuyMember member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        member = new BuyMember();

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            idbundle = extras.getString("id");
        }else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }

        UserJournalRef = database.getReference("All users").child(currentid);
        databaseReference = database.getReference("All Furnitures item").child(idbundle);
        databaseReference2 = database.getReference("All Payment");
        databaseReference3 = database.getReference("All Payment user").child(currentid);

        nameholder = findViewById(R.id.tv_name);
        numberholder = findViewById(R.id.tv_number);
        addholder = findViewById(R.id.tv_add);

        nameitemholder = findViewById(R.id.tv_name_item);
        priceholder = findViewById(R.id.tv_price);
        colorholder = findViewById(R.id.tv_color);
        totalholder = findViewById(R.id.tv_total);
        cvholder = findViewById(R.id.cv_login);
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

                nameholder.setText(name);
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
                String color = snapshot.child("color").getValue(String.class);
                String price = snapshot.child("price").getValue(String.class);
                String url = snapshot.child("url").getValue(String.class);

                nameitemholder.setText(name);
                priceholder.setText("₱"+price);
                colorholder.setText("Color:"+color);
                Picasso.get().load(url).into(ivholder);

                int tempprice = Integer.parseInt(price)+99;
                totalholder.setText("₱"+tempprice);

                cvholder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Calendar cdate = Calendar.getInstance();
                        SimpleDateFormat currentdate = new SimpleDateFormat("dd-MMMM-yyy");
                        final String savedate = currentdate.format(cdate.getTime());

                        Calendar ctime = Calendar.getInstance();
                        SimpleDateFormat currenttime =new SimpleDateFormat("HH-mm");
                        final String savetime = currenttime.format(ctime.getTime());

                        String id = databaseReference2.push().getKey();
                        member.setId(id);
                        member.setUserid(currentid);
                        member.setFurid(idbundle);
                        member.setTotal(String.valueOf(tempprice));
                        member.setDate(savedate);
                        member.setTime(savetime);
                        member.setStatus("Pending");

                        databaseReference2.child(id).setValue(member);
                        databaseReference3.child(id).setValue(member);
                        Toast.makeText(BuyActivity.this, "Successful payment!", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}