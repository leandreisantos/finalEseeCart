package com.example.eseecart;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CartFragment extends Fragment {

    RecyclerView recyclerView;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference databaseReference,databaseReference2,databaseReference3;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentid = user.getUid();

    feedbackMember member;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cart_fragment,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        member = new feedbackMember();

        databaseReference = database.getReference("All Payment user").child(currentid);
        databaseReference3 = database.getReference("All Payment");
        databaseReference2 = database.getReference("All feedback");

        recyclerView = getActivity().findViewById(R.id.rv);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<BuyMember> options =
                new FirebaseRecyclerOptions.Builder<BuyMember>()
                        .setQuery(databaseReference,BuyMember.class)
                        .build();

        FirebaseRecyclerAdapter<BuyMember,Furnitureholder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<BuyMember, Furnitureholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull Furnitureholder holder, int position, @NonNull BuyMember model) {
                        holder.setPurchase(getActivity(),model.getId(),model.getUserid(),model.getFurid(),model.getTotal(), model.getDate(), model.getTime(),model.getStatus());

                        String id = getItem(position).getId();
                        String furid = getItem(position).getFurid();
                        holder.view.setOnClickListener(view -> showDialog(id,furid));
                    }

                    @NonNull
                    @Override
                    public Furnitureholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.purchase_item,parent,false);

                        return new Furnitureholder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void showDialog(String id,String furid) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.comment_dialog,null);
        EditText messege = view.findViewById(R.id.et_comment);
        CardView submit = view.findViewById(R.id.cv_login);

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
        alertDialog.show();

        submit.setOnClickListener(view1 -> {
            String tempmessage = messege.getText().toString();

            if(!TextUtils.isEmpty(tempmessage)){
                String idd = databaseReference2.push().getKey();
                member.setId(idd);
                member.setUserid(currentid);
                member.setMessage(tempmessage);
                member.setFurid(furid);

                databaseReference2.child(furid).child(idd).setValue(member);
                databaseReference.child(id).removeValue();
                databaseReference3.child(id).removeValue();
                Toast.makeText(getActivity(), "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }else{
                Toast.makeText(getActivity(), "Input some feedback", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
