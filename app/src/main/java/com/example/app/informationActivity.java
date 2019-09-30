package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class informationActivity extends AppCompatActivity {
    private Geocoder geocoder = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        TextView text = (TextView) findViewById(R.id.teste);
        TextView end = (TextView) findViewById(R.id.end);

        geocoder = new Geocoder(this, Locale.getDefault());


        Info pl = getIntent().getExtras().getParcelable("myPlaca");
        String roubado = isRoubado(pl);
        System.out.println("AAAAAAAAAAAA" + pl.getPlaca());
        System.out.println("aaaaaaaa" + pl.getLat()+ " " + pl.getLon());
        text.setText("Placa: "+ pl.getPlaca()+ "\n" + "Cor: " + pl.getCor()+  "\n" + "Roubado: " + roubado);
        //end.setText(pl.getLocal());
        List<Address> addresses = null;


        try {
            addresses = geocoder.getFromLocation(pl.getLat(), pl.getLon(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String endereco = addresses.get(0).getAddressLine(0);
        System.out.println(endereco);

    }

    private String isRoubado(Info p){
        if (p.isRoubado()){
            return "Sim";
        } else {
            return "Não";
        }
    }
}
