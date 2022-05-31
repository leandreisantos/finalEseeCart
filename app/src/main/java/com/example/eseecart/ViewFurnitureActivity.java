package com.example.eseecart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class ViewFurnitureActivity extends AppCompatActivity {

    String namebundle,idbundle,pricebundle,colorbundle,urlbundle;

    TextView backholder,namholder,priceholder,colorholder;

    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_furniture);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            namebundle = extras.getString("name");
            idbundle = extras.getString("id");
            pricebundle = extras.getString("price");
            colorbundle = extras.getString("color");
            urlbundle = extras.getString("url");
        }else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }

        backholder = findViewById(R.id.tv_back);
        namholder = findViewById(R.id.tv_name);
        priceholder = findViewById(R.id.tv_price);
        colorholder = findViewById(R.id.tv_color);
        iv = findViewById(R.id.iv);

        backholder.setOnClickListener(v -> onBackPressed());
    }

    @Override
    protected void onStart() {
        super.onStart();

        namholder.setText(namebundle);
        priceholder.setText("₱"+pricebundle);
        colorholder.setText("Color:"+colorbundle);
        Picasso.get().load(urlbundle).into(iv);
    }
}