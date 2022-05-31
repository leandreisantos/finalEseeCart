package com.example.eseecart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {

    TextView backholder,addchoiceholder;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference databaseReference;

    FurnitureChoiceMember member;
    RecyclerView recyclerView;

    TextView iconivholder;
    ProgressBar pbholder;

    private static final int PICK_IMAGE=1;
    Uri imageUridp;

    ImageView ivholder;
    UploadTask uploadTask;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        databaseReference = database.getReference("All Furniture Choices");
        member = new FurnitureChoiceMember();
        storageReference = FirebaseStorage.getInstance().getReference("Profile images");

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
        EditText desc = view.findViewById(R.id.et_desc);
        ivholder = view.findViewById(R.id.iv);
        CardView add = view.findViewById(R.id.cv_add);
        iconivholder = view.findViewById(R.id.photoicon);
        pbholder = view.findViewById(R.id.pb);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        alertDialog.show();

        ivholder.setOnClickListener(v -> chooseImage());

        add.setOnClickListener(v -> {
            pbholder.setVisibility(View.VISIBLE);
            String tempname = name.getText().toString();
            String tempdesc = desc.getText().toString();

            if(!TextUtils.isEmpty(tempname)&&!TextUtils.isEmpty(tempdesc)&&imageUridp!=null){
                final StorageReference reference = storageReference.child(System.currentTimeMillis()+"."+getFileExt(imageUridp));
                uploadTask = reference.putFile(imageUridp);

                Task<Uri> urlTask = uploadTask.continueWithTask((Task<UploadTask.TaskSnapshot> task) -> {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }).addOnCompleteListener(task -> {
                    Uri downloadUri = task.getResult();

                    String id = databaseReference.push().getKey();
                    member.setName(tempname);
                    member.setId(id);
                    member.setDesc(tempdesc);
                    member.setUrl(downloadUri.toString());
                    databaseReference.child(id).setValue(member);
                    Toast.makeText(AdminActivity.this, "Choices Added", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    pbholder.setVisibility(View.GONE);

                });

            }else{
                Toast.makeText(AdminActivity.this, "Please input name", Toast.LENGTH_SHORT).show();
                pbholder.setVisibility(View.GONE);
            }
        });

        cancel_tv.setOnClickListener(v -> alertDialog.dismiss());

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData()!=null){
                imageUridp = data.getData();
                iconivholder.setVisibility(View.GONE);
                Picasso.get().load(imageUridp).into(ivholder);
            }

        }catch (Exception e){
            Toast.makeText(this, "Error"+e, Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
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
                        holder.setChoices(getApplication(),model.getName(),model.getId(),model.getDesc(),model.getUrl());
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

        deleteholder.setOnClickListener(v -> {
            databaseReference.child(id).removeValue();
            alertDialog.dismiss();

        });


    }
}