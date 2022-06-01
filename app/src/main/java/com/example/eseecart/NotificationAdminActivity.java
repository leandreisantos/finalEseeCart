package com.example.eseecart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NotificationAdminActivity extends AppCompatActivity {

    TextView backholder;
    RecyclerView recyclerView;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference databaseReference;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentid = user.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_admin);

        databaseReference = database.getReference("All Payment");

        backholder = findViewById(R.id.back);

        backholder.setOnClickListener(view -> onBackPressed());

        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<BuyMember> options =
                new FirebaseRecyclerOptions.Builder<BuyMember>()
                        .setQuery(databaseReference,BuyMember.class)
                        .build();

        FirebaseRecyclerAdapter<BuyMember,Furnitureholder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<BuyMember, Furnitureholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull Furnitureholder holder, int position, @NonNull BuyMember model) {
                        holder.setNotificationAdmin(getApplication(),model.getId(),model.getUserid(),model.getFurid(),model.getTotal(), model.getDate(), model.getTime(),model.getStatus());

                        String userid = getItem(position).getUserid();
                        String furid = getItem(position).getFurid();
                        String id = getItem(position).getId();

                        holder.clholder.setOnClickListener(view -> {
                            Intent intent = new Intent(NotificationAdminActivity.this,ShowBuyerInfo.class);
                            intent.putExtra("userid",userid);
                            intent.putExtra("furid",furid);
                            intent.putExtra("id",id);
                            startActivity(intent);
                        });
                    }

                    @NonNull
                    @Override
                    public Furnitureholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.notification_admin_item,parent,false);

                        return new Furnitureholder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}