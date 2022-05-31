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

public class RegisterActivity extends AppCompatActivity {

    EditText emailholder,passholder,confpassholder;
    CardView registerholder;
    ProgressBar pbholder;
    TextView back;
    //firebase
    FirebaseAuth mAuth=FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailholder = findViewById(R.id.et_email);
        passholder = findViewById(R.id.et_pass);
        confpassholder = findViewById(R.id.et_pass_conf);
        registerholder = findViewById(R.id.cv_login);
        pbholder = findViewById(R.id.pb);
        back = findViewById(R.id.tv_back);

        registerholder.setOnClickListener(v -> {
           register();
        });
        back.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void register() {
        String emailtemp = emailholder.getText().toString();
        String passtemp = passholder.getText().toString();
        String conftemp = confpassholder.getText().toString();
        pbholder.setVisibility(View.VISIBLE);
        if(!TextUtils.isEmpty(emailtemp)&&!TextUtils.isEmpty(passtemp)&&!TextUtils.isEmpty(conftemp)){
            if(passtemp.equals(conftemp)){
                mAuth.createUserWithEmailAndPassword(emailtemp,passtemp).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(RegisterActivity.this,"Successfully created your account enjoy!", Toast.LENGTH_SHORT).show();
                        pbholder.setVisibility(View.INVISIBLE);
                    }else{
                        String error = Objects.requireNonNull(task.getException()).getMessage();
                        Toast.makeText(RegisterActivity.this, "Error:"+error, Toast.LENGTH_SHORT).show();
                        pbholder.setVisibility(View.INVISIBLE);
                    }
                });
            }else{
                Toast.makeText(RegisterActivity.this, "password and confirm password is not matching", Toast.LENGTH_SHORT).show();
                pbholder.setVisibility(View.INVISIBLE);
            }
        }else{
            Toast.makeText(this, "Please fill up all requirements", Toast.LENGTH_SHORT).show();
            pbholder.setVisibility(View.INVISIBLE);
        }
    }
}