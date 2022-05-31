package com.example.eseecart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminActivity extends AppCompatActivity {

    TextView backholder,addchoiceholder;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference databaseReference;

    FurnitureChoiceMember member;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        databaseReference = database.getReference("All Furniture Choices");
        member = new FurnitureChoiceMember();

        backholder = findViewById(R.id.tv_back);
        addchoiceholder = findViewById(R.id.tv_addchoices);

        backholder.setOnClickListener(v -> onBackPressed());
        addchoiceholder.setOnClickListener(v -> showdia());
        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void showdia() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.add_choices_furniture_dialog,null);
        TextView cancel_tv = view.findViewById(R.id.tv_back);
        EditText name = view.findViewById(R.id.et_name);
        CardView add = view.findViewById(R.id.cv_add);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        alertDialog.show();

        add.setOnClickListener(v -> {
            String tempname = name.getText().toString();
            if(!TextUtils.isEmpty(tempname)){
                String id = databaseReference.push().getKey();
                member.setName(tempname);
                member.setId(id);
                databaseReference.child(id).setValue(member);
                Toast.makeText(AdminActivity.this, "Choices Added", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }else{
                Toast.makeText(AdminActivity.this, "Please input name", Toast.LENGTH_SHORT).show();
            }
        });

        cancel_tv.setOnClickListener(v -> alertDialog.dismiss());

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<FurnitureChoiceMember> options =
                new FirebaseRecyclerOptions.Builder<FurnitureChoiceMember>()
                        .setQuery(databaseReference,FurnitureChoiceMember.class)
                        .build();

        FirebaseRecyclerAdapter<FurnitureChoiceMember,Furnitureholder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<FurnitureChoiceMember, Furnitureholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull Furnitureholder holder, int position, @NonNull FurnitureChoiceMember model) {
                        holder.setChoices(getApplication(),model.getName(),model.getId());
                        String id = getItem(position).getId();
                        String name = getItem(position).getName();

                        holder.more.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showdiaoptions(id,name);
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public Furnitureholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.choicefurniture_item,parent,false);

                        return new Furnitureholder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void showdiaoptions(String id,String name) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.option_choices_furniture_dialog,null);

        TextView viewholder = view.findViewById(R.id.view);
        TextView editholder = view.findViewById(R.id.edit);
        TextView deleteholder = view.findViewById(R.id.delete);

        viewholder.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this,ShowFurnitureList.class);
            intent.putExtra("name",name);
            intent.putExtra("id",id);
            startActivity(intent);
        });




        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        alertDialog.show();


    }
}