package com.example.eseecart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class WaitingActivity extends AppCompatActivity {

    TextView loginholder,createholder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);


        loginholder = findViewById(R.id.btn_signin);
        createholder = findViewById(R.id.btn_signup);

        loginholder.setOnClickListener(v -> {
            Intent intent = new Intent(WaitingActivity.this,LoginActivity.class);
            startActivity(intent);
        });
        createholder.setOnClickListener(v -> {
            Intent intent = new Intent(WaitingActivity.this,RegisterActivity.class);
            startActivity(intent);
        });

    }


}