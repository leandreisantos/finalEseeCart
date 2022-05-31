package com.example.eseecart;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class CreateAccountActivity extends AppCompatActivity {

    ImageView ivholder;
    TextView iconivholder;
    EditText nameholder,mobileholder,addressholder;
    CardView submit;
    ProgressBar pbholder;
    UploadTask uploadTask;
    StorageReference storageReference;

    private static final int PICK_IMAGE=1;
    Uri imageUridp;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId = user.getUid();

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference databaseReference;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;


    AllUserMember member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        member= new AllUserMember();

        documentReference = db.collection("user").document(currentUserId);
        storageReference = FirebaseStorage.getInstance().getReference("Profile images");
        databaseReference = database.getReference("All users");

        ivholder = findViewById(R.id.iv);
        iconivholder = findViewById(R.id.logo_iv);
        nameholder = findViewById(R.id.et_name);
        mobileholder = findViewById(R.id.et_number);
        addressholder = findViewById(R.id.et_add);
        submit = findViewById(R.id.cv_login);
        pbholder = findViewById(R.id.pb);

        submit.setOnClickListener(v -> submitProfile());

        ivholder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
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

    private void submitProfile() {
        String nametemp = nameholder.getText().toString();
        String mobiletemp = mobileholder.getText().toString();
        String addtemp = addressholder.getText().toString();

        if(!TextUtils.isEmpty(nametemp)&&!TextUtils.isEmpty(mobiletemp)&&!TextUtils.isEmpty(addtemp)&&imageUridp!=null){
            pbholder.setVisibility(View.VISIBLE);
            final StorageReference reference = storageReference.child(System.currentTimeMillis()+"."+getFileExt(imageUridp));
            uploadTask = reference.putFile(imageUridp);


            Task<Uri> urlTask = uploadTask.continueWithTask((Task<UploadTask.TaskSnapshot> task) -> {
                if(!task.isSuccessful()){
                    throw task.getException();
                }
                return reference.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                Uri downloadUri = task.getResult();

                Map<String, String> profile = new HashMap<>();
                profile.put("name",nametemp);
                profile.put("url",downloadUri.toString());
                profile.put("mobile",mobiletemp);
                profile.put("address",addtemp);
                profile.put("uid",currentUserId);


                member.setName(nametemp);
                member.setMobile(mobiletemp);
                member.setId(currentUserId);
                member.setUrl(downloadUri.toString());
                member.setAddress(addtemp);

                databaseReference.child(currentUserId).setValue(member);

                documentReference.set(profile)
                        .addOnSuccessListener(aVoid -> {
                            pbholder.setVisibility(View.INVISIBLE);
                            Toast.makeText(CreateAccountActivity.this, "Profile Created", Toast.LENGTH_SHORT).show();


                            Handler handler = new Handler();
                            handler.postDelayed(() -> {
                                Intent intent = new Intent(CreateAccountActivity.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            },2000);
                        });
            });

        }else{
            Toast.makeText(this, "Please fill up all requirements", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onBackPressed() {
    }
}