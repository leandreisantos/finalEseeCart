package com.example.eseecart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentuid = user.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(user != null){
            DocumentReference reference1;
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();

            reference1 = firestore.collection("user").document(currentuid);
            reference1.get()
                    .addOnCompleteListener(task -> {
                        if(!task.getResult().exists()){
                            Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
                            startActivity(intent);
                        }
                    });
        }else{
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
        }

    }
}