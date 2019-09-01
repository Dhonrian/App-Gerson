package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class informationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        TextView text = (TextView) findViewById(R.id.teste);


        PlacaInfo pl = getIntent().getExtras().getParcelable("myPlaca");
        String roubado = isRoubado(pl);

        text.setText("Placa: "+ pl.getPlaca()+ "\n" + "Cor: " + pl.getCor()+  "\n" + "Roubado: " + roubado);

    }

    private String isRoubado(PlacaInfo p){
        if (p.isRoubado()){
            return "Sim";
        } else {
            return "Não";
        }
    }
}
