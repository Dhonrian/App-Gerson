package com.example.app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateOptions;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateResult;

import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private Geocoder geocoder;
    private Location mLastLocation;
    public LocationManager mLocationManager;


    int LOCATION_REFRESH_TIME = 1000;
    int LOCATION_REFRESH_DISTANCE = 5;




    final StitchAppClient client =
            Stitch.initializeDefaultAppClient("placas-android-fnqxq");

    final RemoteMongoClient mongoClient =
            client.getServiceClient(RemoteMongoClient.factory, "MongoDB-Placas-Service");

    final RemoteMongoCollection<Document> coll =
            mongoClient.getDatabase("Placas").getCollection("PlacasCarro");


    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        geocoder = new Geocoder(this, Locale.getDefault());


        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE, mLocationListener);


        changeScreen();
        System.out.println("aaaaaaaa: " + mLocationManager.isLocationEnabled());

    }


    public final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //code
            System.out.println("onLocationChanged");

            mLastLocation = location;
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            
        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }





    };

    private void findDocument(String placa){

        Document filterDoc = new Document()
                .append("placa", placa);

        final Task <Document> findTask = coll.find(filterDoc).limit(1).first();
        findTask.addOnCompleteListener(new OnCompleteListener <Document> () {
            @Override
            public void onComplete(@NonNull Task <Document> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() == null) {
                        Log.d("app", "Could not find any matching documents");

                    } else {
                        Log.d("app", String.format("successfully found document: %s",
                                task.getResult().toString()));
                        System.out.println("Resultado: " + task.getResult().toJson());
                        String placa = task.getResult().get("placa").toString();
                        String cor = task.getResult().get("veiculo.0.cor.descricao").toString();
                        boolean roubado = (boolean) task.getResult().get("veiculo.0.indicadorRouboFurto");
                        PlacaInfo pl = new PlacaInfo(placa, cor, roubado);
                        startActivity(new Intent(MainActivity.this, informationActivity.class).
                                putExtra("myPlaca", pl));
                    }
                } else {
                    Log.e("app", "failed to find document with: ", task.getException());
                }
            }
        });
    }


    private void changeScreen() {

        Stitch.getDefaultAppClient().getAuth().loginWithCredential(new AnonymousCredential()).addOnCompleteListener(new OnCompleteListener<StitchUser>() {
            @Override
            public void onComplete(@NonNull final Task<StitchUser> task) {
                if (task.isSuccessful()) {
                    Log.d("stitch", "logged in anonymously");
                } else {
                    Log.e("stitch", "failed to log in anonymously", task.getException());
                }
            }
        });


        EditText consultaPlaca = (EditText) findViewById(R.id.inserirPlaca);

        Button btn = (Button) findViewById(R.id.search);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Address> addresses = null;


                try {
                     addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String endereco = addresses.get(0).getAddressLine(0);

                System.out.println("AAAAAA: " + endereco);



                if(isConnected()) {
                    if (TextUtils.isEmpty(consultaPlaca.getText())) {
                        consultaPlaca.setError("Campo Vazio!");
                    } else {
                        findDocument(consultaPlaca.getText().toString());
                    }
                } else {
                    Context context = getApplicationContext();
                    CharSequence text = "É necessário estar conectado a internet";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });
    }

    private boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        }
        else
            return false;
}


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
