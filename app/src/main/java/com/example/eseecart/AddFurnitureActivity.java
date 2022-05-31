package com.example.eseecart;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AddFurnitureActivity extends AppCompatActivity {

    ImageView ivholder;
    TextView iviconholder;
    EditText nameholder,priceholder,colorholder;
    CardView addholder;
    ProgressBar pb;

    private static final int PICK_IMAGE=1;
    Uri imageUridp;
    UploadTask uploadTask;
    StorageReference storageReference;

    FurnitureMember member;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference databaseReference,databaseReference2;

    String namebundle,idbundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_furniture);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            namebundle = extras.getString("name");
            idbundle = extras.getString("id");
        }else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }

        member = new FurnitureMember();
        storageReference = FirebaseStorage.getInstance().getReference("Profile images");
        databaseReference = database.getReference("All Furniture").child(idbundle);
        databaseReference2 = database.getReference("All Furnitures item");

        nameholder = findViewById(R.id.name);
        priceholder = findViewById(R.id.price);
        colorholder = findViewById(R.id.color);
        ivholder = findViewById(R.id.iv);
        iviconholder = findViewById(R.id.iconphoto);
        addholder = findViewById(R.id.cv_login);
        pb = findViewById(R.id.pb);

        addholder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addfur();
            }
        });

        ivholder.setOnClickListener(v -> chooseImage());
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
                iviconholder.setVisibility(View.GONE);
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

    private void addfur() {
        pb.setVisibility(View.VISIBLE);
        String tempname = nameholder.getText().toString();
        String tempprice = priceholder.getText().toString();
        String tempcolor = colorholder.getText().toString();

        if(!TextUtils.isEmpty(tempname)&&!TextUtils.isEmpty(tempprice)&&!TextUtils.isEmpty(tempcolor)&&imageUridp!=null){
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
                member.setId(id);
                member.setIdcat(idbundle);
                member.setName(tempname);
                member.setPrice(tempprice);
                member.setColor(tempcolor);
                member.setUrl(downloadUri.toString());
                member.setKey("");

                databaseReference.child(id).setValue(member);
                databaseReference2.child(id).setValue(member);
                Toast.makeText(this, "Furniture Add", Toast.LENGTH_SHORT).show();
                onBackPressed();
                pb.setVisibility(View.GONE);

            });
        }else{
            Toast.makeText(this, "Please input all requirements", Toast.LENGTH_SHORT).show();
            pb.setVisibility(View.GONE);
        }


    }
}