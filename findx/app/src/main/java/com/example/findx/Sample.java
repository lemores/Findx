package com.example.findx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Sample extends AppCompatActivity {

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample);

        final Spinner spinner = findViewById(R.id.spinner);
        TextView recommendedAssist = findViewById(R.id.recommendedAssists);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String yourCategory = spinner.getSelectedItem().toString();
                if(yourCategory.equals("eletrônicos")){
                    Toast.makeText(Sample.this,"Legal men", Toast.LENGTH_SHORT).show();
                }
                else if(yourCategory.equals("smarthphones")){
                    Toast.makeText(Sample.this,"Legal men", Toast.LENGTH_SHORT).show();

                }
                else if(yourCategory.equals("video games")){
                    Toast.makeText(Sample.this,"Legal men", Toast.LENGTH_SHORT).show();

                }
                else if(yourCategory.equals("eletrodomésticos")){

                }
                else{

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
}
