package com.example.eseecart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentuid = user.getUid();

    BottomNavigationView bottomNavigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNav);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new DashBoardFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNav = item -> {
        Fragment selected = null;
        switch(item.getItemId()){
            case R.id.dashboard_bottom:
                selected = new DashBoardFragment();
                break;
            case R.id.cart_bottom:
                selected = new CartFragment();
                break;
            case R.id.notification_bottom:
                selected = new NotificationFragment();
                break;
            case R.id.profile_bottom:
                selected = new ProfileFragment();
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,selected).commit();
        return true;
    };

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