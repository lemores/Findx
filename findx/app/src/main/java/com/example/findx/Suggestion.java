package com.example.findx;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Suggestion extends AppCompatActivity {

    private ImageView imageView;
    TextView a,b,c,d,e,f;
    DatabaseReference reff;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.suggestion);


        imageView = findViewById(R.id.image);
        a = findViewById(R.id.nome_textview);
        b = findViewById(R.id.categoria_textview);
        c = findViewById(R.id.localizacao_textview);
        d = findViewById(R.id.telefone_textview);
        e = findViewById(R.id.horario_textview);
        f = findViewById(R.id.site_textview);

        reff = FirebaseDatabase.getInstance().getReference().child("assistencias").child("1");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nome = dataSnapshot.child("nome").getValue().toString();
                String categoria = dataSnapshot.child("categoria").getValue().toString();
                String endereco = dataSnapshot.child("endereco").getValue().toString();
                String telefone = dataSnapshot.child("telefone").getValue().toString();
                String horario = dataSnapshot.child("horario").getValue().toString();
                String site = dataSnapshot.child("site").getValue().toString();

                a.setText(nome);
                b.setText(categoria);
                c.setText(endereco);
                d.setText(telefone);
                e.setText(horario);
                f.setText(site);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reff = FirebaseDatabase.getInstance().getReference().child("assistencias").child("1");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nome = dataSnapshot.child("nome").getValue().toString();
                String categoria = dataSnapshot.child("categoria").getValue().toString();
                String endereco = dataSnapshot.child("endereco").getValue().toString();
                String telefone = dataSnapshot.child("telefone").getValue().toString();
                String horario = dataSnapshot.child("horario").getValue().toString();
                String site = dataSnapshot.child("site").getValue().toString();

                a.setText(nome);
                b.setText(categoria);
                c.setText(endereco);
                d.setText(telefone);
                e.setText(horario);
                f.setText(site);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Firebase Storage image Link
        String url = "https://firebasestorage.googleapis.com/v0/b/findx-x6969.appspot.com/o/IMG-20180731-WA0000.jpg?alt=media&token=3b27f9f2-a0a5-4892-8276-69382e6ea1ce";
        Glide.with(getApplicationContext()).load(url).into(imageView);

    }

        public void voltar(View view) {
            Intent Login = new Intent(getApplication(), MapsActivity.class);
            startActivity(Login);
        }

}