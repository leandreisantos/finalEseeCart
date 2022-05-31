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
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShowFurnitureList extends AppCompatActivity {

    TextView titleholder,backholder,addholder;
    String namebundle,idbundle;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference databaseReference;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_furniture_list);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            namebundle = extras.getString("name");
            idbundle = extras.getString("id");
        }else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }

        databaseReference = database.getReference("All Furniture").child(idbundle);


        titleholder = findViewById(R.id.tv_title);
        backholder = findViewById(R.id.tv_back);
        addholder = findViewById(R.id.tv_add);
        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        backholder.setOnClickListener(v -> onBackPressed());

        titleholder.setText(namebundle);

        addholder.setOnClickListener(v -> {
            Intent intent = new Intent(ShowFurnitureList.this,AddFurnitureActivity.class);
            intent.putExtra("id",idbundle);
            intent.putExtra("name",namebundle);
            startActivity(intent);
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<FurnitureMember> options =
                new FirebaseRecyclerOptions.Builder<FurnitureMember>()
                        .setQuery(databaseReference,FurnitureMember.class)
                        .build();

        FirebaseRecyclerAdapter<FurnitureMember,Furnitureholder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<FurnitureMember, Furnitureholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull Furnitureholder holder, int position, @NonNull FurnitureMember model) {
                        holder.setFurniture(getApplication(),model.getId(),model.getIdcat(),model.getName(),model.getPrice(),model.getColor(),model.getUrl(),model.getKey());

                        String id = getItem(position).getId();
                        String name = getItem(position).getName();
                        String price = getItem(position).getPrice();
                        String color = getItem(position).getColor();
                        String url = getItem(position).getUrl();

                        holder.clholder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ShowFurnitureList.this,ViewFurnitureActivity.class);
                                intent.putExtra("id",id);
                                intent.putExtra("name",name);
                                intent.putExtra("price",price);
                                intent.putExtra("color",color);
                                intent.putExtra("url",url);
                                startActivity(intent);

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public Furnitureholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.furniture_item,parent,false);

                        return new Furnitureholder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();

        recyclerView.setAdapter(firebaseRecyclerAdapter);


    }
}