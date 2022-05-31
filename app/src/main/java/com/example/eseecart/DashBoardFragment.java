package com.example.eseecart;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    RecyclerView recyclerView;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference databaseReference;



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

        adminholder = getActivity().findViewById(R.id.admin);

        recyclerView = getActivity().findViewById(R.id.rv_cat);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        adminholder.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),AdminActivity.class);
            startActivity(intent);
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<FurnitureChoiceMember> options =
                new FirebaseRecyclerOptions.Builder<FurnitureChoiceMember>()
                        .setQuery(databaseReference,FurnitureChoiceMember.class)
                        .build();

        FirebaseRecyclerAdapter<FurnitureChoiceMember,Furnitureholder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<FurnitureChoiceMember, Furnitureholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull Furnitureholder holder, int position, @NonNull FurnitureChoiceMember model) {
                        holder.setCat(getActivity(),model.getName(),model.getId());
                        String id = getItem(position).getId();
                        String name = getItem(position).getName();

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
