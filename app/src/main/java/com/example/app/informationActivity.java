package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class informationActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Geocoder geocoder = null;

    private MapView mMapView;
    public static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);


        TextView text = (TextView) findViewById(R.id.teste);
        TextView end = (TextView) findViewById(R.id.end);
        TextView time = (TextView) findViewById(R.id.tempo);
        MapView mMapView = (MapView) findViewById(R.id.mapView);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);

        geocoder = new Geocoder(this, Locale.getDefault());
        SimpleDateFormat currentTime = new SimpleDateFormat("dd/MM/yyyy '-' HH:mm:ss");
        String timeText = currentTime.format(new Date());

        Info pl = getIntent().getExtras().getParcelable("myPlaca");
        String roubado = isRoubado(pl);
        text.setText("Placa: "+ pl.getPlaca()+ "\n" + "Cor: " + pl.getCor()+  "\n" + "Roubado: " + roubado);
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(pl.getLat(), pl.getLon(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String endereco = addresses.get(0).getAddressLine(0);
        end.setText(endereco);
        time.setText(timeText);

    }

    private String isRoubado(Info p){
        if (p.isRoubado()){
            return "Sim";
        } else {
            return "Não";
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}



