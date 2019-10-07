package com.example.app;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class informationActivity extends FragmentActivity implements OnMapReadyCallback {

    private Geocoder geocoder = null;
    private double lat = 0;
    private double lon = 0;
    private GoogleMap mMapView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);


        TextView text = (TextView) findViewById(R.id.teste);
        TextView end = (TextView) findViewById(R.id.end);
        TextView time = (TextView) findViewById(R.id.tempo);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        geocoder = new Geocoder(this, Locale.getDefault());
        SimpleDateFormat currentTime = new SimpleDateFormat("dd/MM/yyyy '-' HH:mm:ss");
        String timeText = currentTime.format(new Date());

        Info pl = getIntent().getExtras().getParcelable("myPlaca");
        lat = pl.getLat();
        lon = pl.getLon();

        String roubado = isRoubado(pl);
        text.setText("Placa: "+ pl.getPlaca()+ "\n" + "Cor: " + pl.getCor()+  "\n" + "Roubado: " + roubado);
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
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
        mMapView = googleMap;
        mMapView.setMyLocationEnabled(true);
        mMapView.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 15));
    }
/*
    private void addMapaCalor() throws IOException {
        AssetManager assetManager = getAssets();


        List<LatLng> list = null;
        list = (List<LatLng>) assetManager.open("lat_lon.json");
        TileProvider mProvider = new HeatmapTileProvider.Builder()
                .data(list)
                .build();
        // Add a tile overlay to the map, using the heat map tile provider.
        mOverlay = mMapView.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));

    }
    */


    private ArrayList<LatLng> readItems(int resource) throws JSONException {
        ArrayList<LatLng> list = new ArrayList<LatLng>();
        InputStream inputStream = getResources().openRawResource(resource);
        String json = new Scanner(inputStream).useDelimiter("\\A").next();
        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            double lat = object.getDouble("lat");
            double lng = object.getDouble("lng");
            list.add(new LatLng(lat, lng));
        }
        return list;
    }

}



