package com.example.eseecart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText emailholder,passholder;
    CardView loginholder;
    ProgressBar pbholder;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailholder = findViewById(R.id.et_email);
        passholder = findViewById(R.id.et_pass);
        loginholder = findViewById(R.id.cv_login);
        pbholder = findViewById(R.id.pb);
        back = findViewById(R.id.tv_back);


        loginholder.setOnClickListener(v -> login());
        back.setOnClickListener(v -> onBackPressed());
    }

    private void login() {
        String emailsamp = emailholder.getText().toString();
        String passsamp = passholder.getText().toString();

        if(!TextUtils.isEmpty(emailsamp)&&!TextUtils.isEmpty(passsamp)){
            pbholder.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(emailsamp,passsamp).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    pbholder.setVisibility(View.GONE);
                    String error = Objects.requireNonNull(task.getException()).getMessage();
                    Toast.makeText(LoginActivity.this, "Error:"+error, Toast.LENGTH_SHORT).show();
                }
            });

        }else{
            Toast.makeText(LoginActivity.this, "Please Fill up all requirements", Toast.LENGTH_SHORT).show();
        }
    }
}