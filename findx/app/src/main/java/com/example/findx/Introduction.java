package com.example.findx;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ViewFlipper;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class Introduction extends AppCompatActivity {
    ViewFlipper flipper;
    ViewFlipper flipper2;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.introduction);
        auth = FirebaseAuth.getInstance();

        //Indo para tela principal caso esteja ja logado
        if(auth.getCurrentUser() != null){
            Intent principal = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(principal);
        }

            flipper = findViewById(R.id.flipper);
            flipper2 = findViewById(R.id.flipper2);

            int[] images = {R.drawable.ponto, R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d};
            int[] transicoes = {R.drawable.transicao, R.drawable.transicaodois, R.drawable.transicaotres, R.drawable.transicaoquatro, R.drawable.transicaocinco};

            //for loop
            for (int i = 0; i < images.length; i++) {
                flipperimages(images[i]);
            }

            for (int i = 0; i < transicoes.length; i++) {
                flippertransicoes(transicoes[i]);
            }
        }

        public void flipperimages (int image){
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(image);


            flipper.addView(imageView);
            flipper.setFlipInterval(3000);
            flipper.setAutoStart(true);

            //animation
            flipper.setInAnimation(this, android.R.anim.slide_in_left);
            flipper.setOutAnimation(this, android.R.anim.slide_out_right);
        }
        public void flippertransicoes (int image) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(image);

            flipper2.addView(imageView);
            flipper2.setFlipInterval(3000);
            flipper2.setAutoStart(true);

            //animation
            flipper2.setInAnimation(this, android.R.anim.slide_in_left);
            flipper2.setOutAnimation(this, android.R.anim.slide_out_right);

        }
        public void irparalogin(View view) {
            Intent login = new Intent(getApplicationContext(), Login.class);
            startActivity(login);
        }

        public void irparacadastro(View view) {
            Intent cadastro = new Intent(getApplicationContext(), SignIn.class);
            startActivity(cadastro);
        }
    }
