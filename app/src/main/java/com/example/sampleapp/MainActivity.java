package com.example.sampleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    Button  logout;
    TextView text;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logout=findViewById(R.id.logout);
        text=findViewById(R.id.text);
        mAuth = FirebaseAuth.getInstance();


        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseFirestore data= FirebaseFirestore.getInstance();
        data.collection("users").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    text.setText("Welcome , "+documentSnapshot.getString("name"));

                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(MainActivity.this,"Couldnot fetch data",Toast.LENGTH_SHORT).show();

                    }
                });





        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent intent=new Intent(MainActivity.this,login.class);
                startActivity(intent);

            }
        });



    }
}