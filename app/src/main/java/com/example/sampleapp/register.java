package com.example.sampleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText signup_name,signup_email,signup_pass,signup_phone;
    Button signup_button;

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        signup_name=findViewById(R.id.signup_name);
        signup_email=findViewById(R.id.signup_email);
        signup_pass=findViewById(R.id.signup_pass);
        signup_phone=findViewById(R.id.signup_phone);
        signup_button=findViewById(R.id.signup_button);
        mAuth = FirebaseAuth.getInstance();


        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(signup_email.getText().toString().isEmpty() || signup_name.getText().toString().isEmpty() ||signup_pass.getText().toString().isEmpty()||signup_phone.getText().toString().isEmpty()){

                    Toast.makeText(register.this,"Please enter all fields,",Toast.LENGTH_SHORT).show();
                }
                else{
                    showProgress(true);
                    String name=signup_name.getText().toString().trim();
                    String email=signup_email.getText().toString().trim();
                    String pass=signup_pass.getText().toString().trim();
                    String phone=signup_phone.getText().toString().trim();




                    mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                Toast.makeText(register.this,"User creation sucess",Toast.LENGTH_SHORT).show();
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                Map<String,Object> details =new HashMap<>();
                                FirebaseUser user= mAuth.getCurrentUser();
                                details.put("name",name);
                                details.put("phone",phone);
                                db.collection("users").document(user.getUid()).set(details).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG,"Details added");
                                    }
                                });

                                showProgress(false);

                                Intent intent =new Intent(getApplicationContext(),MainActivity.class);
                                intent.putExtra("name",name);
                                startActivity(intent);


                            }
                            else{
                                showProgress(false);
                                Toast.makeText(register.this,"user creation failure"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });

    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
