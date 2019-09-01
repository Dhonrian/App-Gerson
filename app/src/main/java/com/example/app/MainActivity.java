package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    private StitchAppClient stitchClient;
    private RemoteMongoCollection itemsCollection;


    final StitchAppClient client =
            Stitch.initializeDefaultAppClient("placas-android-fnqxq");

    final RemoteMongoClient mongoClient =
            client.getServiceClient(RemoteMongoClient.factory, "MongoDB-Placas-Service");

    final RemoteMongoCollection<Document> coll =
            mongoClient.getDatabase("Placas").getCollection("PlacasCarros");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




/*
        client.getAuth().loginWithCredential(new AnonymousCredential()).continueWithTask(
                new Continuation<StitchUser, Task<RemoteUpdateResult>>() {

                    @Override
                    public Task<RemoteUpdateResult> then(@NonNull Task<StitchUser> task) throws Exception {
                        if (!task.isSuccessful()) {
                            Log.e("STITCH", "Login failed!");
                            throw task.getException();
                        }

                        final Document updateDoc = new Document(
                                "owner_id",
                                task.getResult().getId()
                        );

                        updateDoc.put("number", 42);
                        return coll.updateOne(
                                null, updateDoc, new RemoteUpdateOptions().upsert(true)
                        );
                    }
                }
        ).continueWithTask(new Continuation<RemoteUpdateResult, Task<List<Document>>>() {
            @Override
            public Task<List<Document>> then(@NonNull Task<RemoteUpdateResult> task) throws Exception {
                if (!task.isSuccessful()) {
                    Log.e("STITCH", "Update failed!");
                    throw task.getException();
                }
                List<Document> docs = new ArrayList<>();
                return coll
                        .find(new Document("owner_id", client.getAuth().getUser().getId()))
                        .limit(100)
                        .into(docs);
            }
        }).addOnCompleteListener(new OnCompleteListener<List<Document>>() {
            @Override
            public void onComplete(@NonNull Task<List<Document>> task) {
                if (task.isSuccessful()) {
                    Log.d("STITCH", "Found docs: " + task.getResult().toString());
                    return;
                }
                Log.e("STITCH", "Error: " + task.getException().toString());
                task.getException().printStackTrace();
            }
        });


 */
        changeScreen();
    }


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
                        String cor = task.getResult().get("cor").toString();
                        boolean roubado = (boolean) task.getResult().get("roubado");
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

        EditText consultaPlaca = (EditText) findViewById(R.id.inserirPlaca);


        Button btn = (Button) findViewById(R.id.search);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("AAAAAAAAAAAAAAAAAAAAAA: " + consultaPlaca.getText().toString());

                findDocument(consultaPlaca.getText().toString());
            }
        });


    }
}
