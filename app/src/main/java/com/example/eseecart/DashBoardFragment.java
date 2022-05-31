package com.example.eseecart;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DashBoardFragment extends Fragment {

    CardView adminholder;
    RecyclerView recyclerView,recyclerView2;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference databaseReference,databaseReference2;



    FurnitureChoiceMember member;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dashboard_fragment,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        databaseReference = database.getReference("All Furniture Choices");
        databaseReference2 = database.getReference("All Furnitures item");

        adminholder = getActivity().findViewById(R.id.admin);

        recyclerView = getActivity().findViewById(R.id.rv_cat);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView2 = getActivity().findViewById(R.id.df_event_rv);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));


        adminholder.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),AdminActivity.class);
            startActivity(intent);
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<FurnitureMember> options2 =
                new FirebaseRecyclerOptions.Builder<FurnitureMember>()
                        .setQuery(databaseReference2,FurnitureMember.class)
                        .build();

        FirebaseRecyclerAdapter<FurnitureMember,Furnitureholder> firebaseRecyclerAdapter2 =
                new FirebaseRecyclerAdapter<FurnitureMember, Furnitureholder>(options2) {
                    @Override
                    protected void onBindViewHolder(@NonNull Furnitureholder holder, int position, @NonNull FurnitureMember model) {
                        holder.setFurnituredash(getActivity(),model.getId(),model.getIdcat(),model.getName(),model.getPrice(),model.getColor(),model.getUrl(),model.getKey());

                        String id = getItem(position).getId();
                        String name = getItem(position).getName();
                        String price = getItem(position).getPrice();
                        String color = getItem(position).getColor();
                        String url = getItem(position).getUrl();

                        holder.view.setOnClickListener(v -> {
                            Intent intent = new Intent(getActivity(),ViewFurnitureActivity.class);
                            intent.putExtra("id",id);
                            intent.putExtra("name",name);
                            intent.putExtra("price",price);
                            intent.putExtra("color",color);
                            intent.putExtra("url",url);
                            startActivity(intent);

                        });
                    }

                    @NonNull
                    @Override
                    public Furnitureholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.furniture_item_horizontal,parent,false);

                        return new Furnitureholder(view);
                    }
                };

        firebaseRecyclerAdapter2.startListening();

        recyclerView2.setAdapter(firebaseRecyclerAdapter2);

        FirebaseRecyclerOptions<FurnitureChoiceMember> options =
                new FirebaseRecyclerOptions.Builder<FurnitureChoiceMember>()
                        .setQuery(databaseReference,FurnitureChoiceMember.class)
                        .build();

        FirebaseRecyclerAdapter<FurnitureChoiceMember,Furnitureholder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<FurnitureChoiceMember, Furnitureholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull Furnitureholder holder, int position, @NonNull FurnitureChoiceMember model) {
                        holder.setCat(getActivity(),model.getName(),model.getId(),model.getDesc(),model.getUrl());
                        String id = getItem(position).getId();
                        String name = getItem(position).getName();

                        holder.cl.setOnClickListener(v -> {
                            Intent intent = new Intent(getActivity(),ShowFurnitureList.class);
                            intent.putExtra("name",name);
                            intent.putExtra("id",id);
                            startActivity(intent);
                        });

                    }

                    @NonNull
                    @Override
                    public Furnitureholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.choicefurniture_item,parent,false);

                        return new Furnitureholder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();

        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }
}
