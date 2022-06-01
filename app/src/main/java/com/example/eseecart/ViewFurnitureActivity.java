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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ViewFurnitureActivity extends AppCompatActivity {

    String namebundle,idbundle,pricebundle,colorbundle,urlbundle;

    TextView backholder,namholder,priceholder,colorholder;

    TextView buyholder;

    ImageView iv;

    RecyclerView recyclerView;
    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference databaseReference,databaseReference2,databaseReference3;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentid = user.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_furniture);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            namebundle = extras.getString("name");
            idbundle = extras.getString("id");
            pricebundle = extras.getString("price");
            colorbundle = extras.getString("color");
            urlbundle = extras.getString("url");
        }else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }

        databaseReference2 = database.getReference("All feedback").child(idbundle);

        backholder = findViewById(R.id.tv_back);
        namholder = findViewById(R.id.tv_name);
        priceholder = findViewById(R.id.tv_price);
        colorholder = findViewById(R.id.tv_color);
        iv = findViewById(R.id.iv);
        buyholder = findViewById(R.id.tv_buy);
        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        backholder.setOnClickListener(v -> onBackPressed());
        buyholder.setOnClickListener(v -> {
            Intent intent = new Intent(ViewFurnitureActivity.this,BuyActivity.class);
            intent.putExtra("id",idbundle);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        namholder.setText(namebundle);
        priceholder.setText("₱"+pricebundle);
        colorholder.setText("Color:"+colorbundle);
        Picasso.get().load(urlbundle).into(iv);


        FirebaseRecyclerOptions<feedbackMember> options =
                new FirebaseRecyclerOptions.Builder<feedbackMember>()
                        .setQuery(databaseReference2,feedbackMember.class)
                        .build();

        FirebaseRecyclerAdapter<feedbackMember,Furnitureholder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<feedbackMember, Furnitureholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull Furnitureholder holder, int position, @NonNull feedbackMember model) {
                        holder.setFeedback(getApplication(),model.getId(),model.getUserid(),model.getMessage(),model.getFurid());

                    }

                    @NonNull
                    @Override
                    public Furnitureholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.cutomer_feedback_item,parent,false);

                        return new Furnitureholder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}