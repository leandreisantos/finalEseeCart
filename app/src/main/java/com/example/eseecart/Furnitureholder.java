package com.example.eseecart;

import android.app.Application;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Furnitureholder extends RecyclerView.ViewHolder {


    TextView more;
    public Furnitureholder(@NonNull View itemView) {
        super(itemView);
    }

    public void setChoices(Application application,String name,String id){
        TextView nameholder = itemView.findViewById(R.id.tv_name);
        TextView count = itemView.findViewById(R.id.item_count);
        more = itemView.findViewById(R.id.more);

        nameholder.setText(name);
    }
}
