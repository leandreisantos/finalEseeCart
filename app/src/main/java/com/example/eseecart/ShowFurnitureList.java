package com.example.eseecart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ShowFurnitureList extends AppCompatActivity {

    TextView titleholder,backholder,addholder;
    String namebundle,idbundle;

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


        titleholder = findViewById(R.id.tv_title);
        backholder = findViewById(R.id.tv_back);
        addholder = findViewById(R.id.tv_add);


        backholder.setOnClickListener(v -> onBackPressed());

        titleholder.setText(namebundle);

        addholder.setOnClickListener(v -> {
            Intent intent = new Intent(ShowFurnitureList.this,AddFurnitureActivity.class);
            intent.putExtra("id",idbundle);
            intent.putExtra("name",namebundle);
            startActivity(intent);
        });

    }
}