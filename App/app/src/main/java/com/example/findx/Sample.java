package com.example.findx;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Sample extends AppCompatActivity {

    DatabaseReference reff;
    FirebaseUser user;
    String uid;
    TextView favoritedAssist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample);

        favoritedAssist = findViewById(R.id.favoritedAssists);
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        //Pegando todas assists favoritadas pelo usuario
        reff = FirebaseDatabase.getInstance().getReference().child("usuarios").child(uid).child("favoritos").child("m1");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String assist = dataSnapshot.child("nomeAssist").getValue(String.class);
                    favoritedAssist.setText(assist);
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}