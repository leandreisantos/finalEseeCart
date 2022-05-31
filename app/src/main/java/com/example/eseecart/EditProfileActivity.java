package com.example.eseecart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
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

public class EditProfileActivity extends AppCompatActivity {

    EditText nameholder,mobileholder,addholder;
    CardView saveholder;
    ImageView ivholder;
    TextView backholder;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference UserJournalRef;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentid = user.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        UserJournalRef = database.getReference("All users").child(currentid);

        nameholder = findViewById(R.id.et_name);
        mobileholder = findViewById(R.id.et_number);
        addholder = findViewById(R.id.et_add);
        saveholder = findViewById(R.id.tv_editprofile);
        ivholder = findViewById(R.id.iv);
        backholder = findViewById(R.id.back);

        backholder.setOnClickListener(v -> onBackPressed());

        saveholder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveprofile();
            }
        });
    }

    private void saveprofile() {

        String nametemp = nameholder.getText().toString();
        String mobiletemp = mobileholder.getText().toString();
        String addtemp = addholder.getText().toString();

        if(!TextUtils.isEmpty(nametemp)&&!TextUtils.isEmpty(mobiletemp)&&!TextUtils.isEmpty(addtemp)){
            UserJournalRef.child("name").setValue(nametemp);
            UserJournalRef.child("mobile").setValue(mobiletemp);
            UserJournalRef.child("address").setValue(addtemp);

            Toast.makeText(this, "Saved changes", Toast.LENGTH_SHORT).show();
            onBackPressed();

        }else{
            Toast.makeText(this, "Please add information!", Toast.LENGTH_SHORT).show();
        }

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


                Picasso.get().load(url).into(ivholder);
                nameholder.setText(name);
                mobileholder.setText(mobile);
                addholder.setText(add);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}