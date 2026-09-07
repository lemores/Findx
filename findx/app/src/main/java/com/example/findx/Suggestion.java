package com.example.findx;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Suggestion extends AppCompatActivity {

    private Button favB;
    private ImageView imageView;
    TextView a,b,c,d,e,f;
    DatabaseReference reff;
    FirebaseUser user;
    String uid;
    int i = 0;
    int countAssist = 0;
    String assistParent;
    private int current_image;
    ImageView favImage;
    int[] images = {R.drawable.salvar,R.drawable.salvo};
    String extraId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.suggestion);
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

       //Toast.makeText(Suggestion.this,"Marker position: "+getIntent().getStringExtra("Marker position"),Toast.LENGTH_SHORT).show();

        favImage = findViewById(R.id.favImage);
        favB = findViewById(R.id.favB);
        imageView = findViewById(R.id.image);
        a = findViewById(R.id.nome_textview);
        b = findViewById(R.id.categoria_textview);
        c = findViewById(R.id.localizacao_textview);
        d = findViewById(R.id.telefone_textview);
        e = findViewById(R.id.horario_textview);
        f = findViewById(R.id.site_textview);

        // TODO deixar icone de favoritado quando salvo na banco
        // TODO tirar assist do banco quando desfavoritado


        //Acessando informações de quantidade de assists cadastradas
        reff = FirebaseDatabase.getInstance().getReference().child("assistencias");
        reff.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange (@NonNull DataSnapshot dataSnapshot)
            {
                countAssist = (int) dataSnapshot.getChildrenCount();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {}});


        //Comparando Id recebido da assist clicada com Id´s do BD para descobrir child de qual assist é,
        //e puxar suas informações
        extraId = getIntent().getStringExtra("Marker Id");

        //Puxando todos valores das assists
        reff = FirebaseDatabase.getInstance().getReference().child("assistencias").child("1"); //TODO achar jeito de percorrer todas childs(1-10) sem ficar nulo
        reff.addValueEventListener(new ValueEventListener() {

                //Pegando os valores de Id´s do BD
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String Id = (String) dataSnapshot.child("id").getValue();
                    Toast.makeText(Suggestion.this,"Id:"+extraId+Id,Toast.LENGTH_SHORT).show();

                    //Comparando com o da assist clicada
                    if(extraId.equals(Id)){
                    assistParent =  String.valueOf(dataSnapshot.getRef().getKey());
                    //Toast.makeText(Suggestion.this,""+assistParent,Toast.LENGTH_SHORT).show();


                    //Acessando informações da assistência clicada
                    reff = FirebaseDatabase.getInstance().getReference().child("assistencias").child(assistParent);
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
                }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        //Puxando todos valores das assists
        reff = FirebaseDatabase.getInstance().getReference().child("assistencias").child("2"); //TODO achar jeito de percorrer todas childs(1-10) sem ficar nulo
        reff.addValueEventListener(new ValueEventListener() {

            //Pegando os valores de Id´s do BD
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Id = (String) dataSnapshot.child("id").getValue();
                Toast.makeText(Suggestion.this,"Id:"+extraId+Id,Toast.LENGTH_SHORT).show();

                //Comparando com o da assist clicada
                if(extraId.equals(Id)){
                    assistParent =  String.valueOf(dataSnapshot.getRef().getKey());
                    //Toast.makeText(Suggestion.this,""+assistParent,Toast.LENGTH_SHORT).show();


                    //Acessando informações da assistência clicada
                    reff = FirebaseDatabase.getInstance().getReference().child("assistencias").child(assistParent);
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
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        //Puxando todos valores das assists
        reff = FirebaseDatabase.getInstance().getReference().child("assistencias").child("3"); //TODO achar jeito de percorrer todas childs(1-10) sem ficar nulo
        reff.addValueEventListener(new ValueEventListener() {

            //Pegando os valores de Id´s do BD
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Id = (String) dataSnapshot.child("id").getValue();
                Toast.makeText(Suggestion.this,"Id:"+extraId+Id,Toast.LENGTH_SHORT).show();

                //Comparando com o da assist clicada
                if(extraId.equals(Id)){
                    assistParent =  String.valueOf(dataSnapshot.getRef().getKey());
                    //Toast.makeText(Suggestion.this,""+assistParent,Toast.LENGTH_SHORT).show();


                    //Acessando informações da assistência clicada
                    reff = FirebaseDatabase.getInstance().getReference().child("assistencias").child(assistParent);
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
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //Puxando todos valores das assists
        reff = FirebaseDatabase.getInstance().getReference().child("assistencias").child("4"); //TODO achar jeito de percorrer todas childs(1-10) sem ficar nulo
        reff.addValueEventListener(new ValueEventListener() {

            //Pegando os valores de Id´s do BD
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Id = (String) dataSnapshot.child("id").getValue();
                Toast.makeText(Suggestion.this,"Id:"+extraId+Id,Toast.LENGTH_SHORT).show();

                //Comparando com o da assist clicada
                if(extraId.equals(Id)){
                    assistParent =  String.valueOf(dataSnapshot.getRef().getKey());
                    //Toast.makeText(Suggestion.this,""+assistParent,Toast.LENGTH_SHORT).show();


                    //Acessando informações da assistência clicada
                    reff = FirebaseDatabase.getInstance().getReference().child("assistencias").child(assistParent);
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
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //Puxando todos valores das assists
        reff = FirebaseDatabase.getInstance().getReference().child("assistencias").child("5"); //TODO achar jeito de percorrer todas childs(1-10) sem ficar nulo
        reff.addValueEventListener(new ValueEventListener() {

            //Pegando os valores de Id´s do BD
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Id = (String) dataSnapshot.child("id").getValue();
                Toast.makeText(Suggestion.this,"Id:"+extraId+Id,Toast.LENGTH_SHORT).show();

                //Comparando com o da assist clicada
                if(extraId.equals(Id)){
                    assistParent =  String.valueOf(dataSnapshot.getRef().getKey());
                    //Toast.makeText(Suggestion.this,""+assistParent,Toast.LENGTH_SHORT).show();


                    //Acessando informações da assistência clicada
                    reff = FirebaseDatabase.getInstance().getReference().child("assistencias").child(assistParent);
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
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //Puxando todos valores das assists
        reff = FirebaseDatabase.getInstance().getReference().child("assistencias").child("6"); //TODO achar jeito de percorrer todas childs(1-10) sem ficar nulo
        reff.addValueEventListener(new ValueEventListener() {

            //Pegando os valores de Id´s do BD
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Id = (String) dataSnapshot.child("id").getValue();
                Toast.makeText(Suggestion.this,"Id:"+extraId+Id,Toast.LENGTH_SHORT).show();

                //Comparando com o da assist clicada
                if(extraId.equals(Id)){
                    assistParent =  String.valueOf(dataSnapshot.getRef().getKey());
                    //Toast.makeText(Suggestion.this,""+assistParent,Toast.LENGTH_SHORT).show();


                    //Acessando informações da assistência clicada
                    reff = FirebaseDatabase.getInstance().getReference().child("assistencias").child(assistParent);
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
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //Puxando todos valores das assists
        reff = FirebaseDatabase.getInstance().getReference().child("assistencias").child("7"); //TODO achar jeito de percorrer todas childs(1-10) sem ficar nulo
        reff.addValueEventListener(new ValueEventListener() {

            //Pegando os valores de Id´s do BD
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Id = (String) dataSnapshot.child("id").getValue();
                Toast.makeText(Suggestion.this,"Id:"+extraId+Id,Toast.LENGTH_SHORT).show();

                //Comparando com o da assist clicada
                if(extraId.equals(Id)){
                    assistParent =  String.valueOf(dataSnapshot.getRef().getKey());
                    //Toast.makeText(Suggestion.this,""+assistParent,Toast.LENGTH_SHORT).show();


                    //Acessando informações da assistência clicada
                    reff = FirebaseDatabase.getInstance().getReference().child("assistencias").child(assistParent);
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
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //Puxando todos valores das assists
        reff = FirebaseDatabase.getInstance().getReference().child("assistencias").child("8"); //TODO achar jeito de percorrer todas childs(1-10) sem ficar nulo
        reff.addValueEventListener(new ValueEventListener() {

            //Pegando os valores de Id´s do BD
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Id = (String) dataSnapshot.child("id").getValue();
                Toast.makeText(Suggestion.this,"Id:"+extraId+Id,Toast.LENGTH_SHORT).show();

                //Comparando com o da assist clicada
                if(extraId.equals(Id)){
                    assistParent =  String.valueOf(dataSnapshot.getRef().getKey());
                    //Toast.makeText(Suggestion.this,""+assistParent,Toast.LENGTH_SHORT).show();


                    //Acessando informações da assistência clicada
                    reff = FirebaseDatabase.getInstance().getReference().child("assistencias").child(assistParent);
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
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //Puxando todos valores das assists
        reff = FirebaseDatabase.getInstance().getReference().child("assistencias").child("9"); //TODO achar jeito de percorrer todas childs(1-10) sem ficar nulo
        reff.addValueEventListener(new ValueEventListener() {

            //Pegando os valores de Id´s do BD
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Id = (String) dataSnapshot.child("id").getValue();
                Toast.makeText(Suggestion.this,"Id:"+extraId+Id,Toast.LENGTH_SHORT).show();

                //Comparando com o da assist clicada
                if(extraId.equals(Id)){
                    assistParent =  String.valueOf(dataSnapshot.getRef().getKey());
                    //Toast.makeText(Suggestion.this,""+assistParent,Toast.LENGTH_SHORT).show();


                    //Acessando informações da assistência clicada
                    reff = FirebaseDatabase.getInstance().getReference().child("assistencias").child(assistParent);
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
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //Puxando todos valores das assists
        reff = FirebaseDatabase.getInstance().getReference().child("assistencias").child("10"); //TODO achar jeito de percorrer todas childs(1-10) sem ficar nulo
        reff.addValueEventListener(new ValueEventListener() {



            //Pegando os valores de Id´s do BD
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Id = (String) dataSnapshot.child("id").getValue();
                Toast.makeText(Suggestion.this,"Id:"+extraId+Id,Toast.LENGTH_SHORT).show();

                //Comparando com o da assist clicada
                if(extraId.equals(Id)){
                    assistParent =  String.valueOf(dataSnapshot.getRef().getKey());
                    //Toast.makeText(Suggestion.this,""+assistParent,Toast.LENGTH_SHORT).show();


                    //Acessando informações da assistência clicada
                    reff = FirebaseDatabase.getInstance().getReference().child("assistencias").child(assistParent);
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
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //Favoritando assistências
        favB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                favAssist();
            }
        });

        //Firebase Storage image Link
        String url = "https://firebasestorage.googleapis.com/v0/b/findx-x6969.appspot.com/o/IMG-20180731-WA0000.jpg?alt=media&token=3b27f9f2-a0a5-4892-8276-69382e6ea1ce";
        Glide.with(getApplicationContext()).load(url).into(imageView);

    }

    private void favAssist() {
        //Configurando o tempo para salvar
        String saveCurrentTime;
        String saveCurrentDate;
        i++;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM, YYYY");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        //Salvando informações da assist favoritada na child certa
        final DatabaseReference favRef = FirebaseDatabase.getInstance().getReference();

        final HashMap<String, Object> favMap = new HashMap<>();
        favMap.put("e-mail", user.getEmail());
        favMap.put("nomeAssist", a.getText().toString());
        favMap.put("data", saveCurrentDate);
        favMap.put("horário", saveCurrentTime);

            favRef.child("usuarios").child(uid).child("favoritos").child(String.valueOf(i))
                    .updateChildren(favMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(Suggestion.this,"adicionado a lista de favoritos",Toast.LENGTH_SHORT).show();
                        // mudando imagem icone de salvar assist
                        current_image++;
                        current_image= current_image % images.length;
                        favImage.setImageResource(images[current_image]);

                    }

                }
            });
        }

    public void voltar(View view) {
            Intent voltar = new Intent(getApplication(), MapsActivity.class);
            startActivity(voltar);
        }
}