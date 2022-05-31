package com.example.eseecart;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    TextView logoutholder,mypurchse;
    FirebaseAuth mAuth;
    CardView editholder;
    TextView nameholder,mobileholder,addholder;
    ImageView ivholder;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference UserJournalRef;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentid = user.getUid();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        UserJournalRef = database.getReference("All users").child(currentid);

        logoutholder = getActivity().findViewById(R.id.tv_logout);
        editholder = getActivity().findViewById(R.id.tv_editprofile);
        nameholder = getActivity().findViewById(R.id.et_name);
        mobileholder = getActivity().findViewById(R.id.et_number);
        addholder = getActivity().findViewById(R.id.et_add);
        ivholder = getActivity().findViewById(R.id.iv);
        mypurchse = getActivity().findViewById(R.id.tv_mpurchase);


        logoutholder.setOnClickListener(v -> showlogout());

        editholder.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),EditProfileActivity.class);
            startActivity(intent);
        });
        mypurchse.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),MyPurchaseActivity.class);
            startActivity(intent);
        });


    }

    private void showlogout() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.logout_layout,null);
        TextView logout_tv = view.findViewById(R.id.logout_tv_ll);
        TextView cancel_tv = view.findViewById(R.id.cancel_tv_ll);

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
        alertDialog.show();
        logout_tv.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(getActivity(),WaitingActivity.class));
        });
        cancel_tv.setOnClickListener(v -> alertDialog.dismiss());
    }

    @Override
    public void onStart() {
        super.onStart();

        UserJournalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                String mobile = snapshot.child("mobile").getValue(String.class);
                String url = snapshot.child("url").getValue(String.class);
                String add = snapshot.child("address").getValue(String.class);


                Picasso.get().load(url).into(ivholder);
                nameholder.setText(name);
                mobileholder.setText(mobile);
                addholder.setText(add);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
