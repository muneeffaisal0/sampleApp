package com.example.sampleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class login extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText log_email, log_pass;
    TextView signup_text;
    Button signin_button;
    String nm;


    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);


        log_email = findViewById(R.id.log_email);
        log_pass = findViewById(R.id.log_pass);
        signup_text = findViewById(R.id.signup_text);
        signin_button = findViewById(R.id.signin_button);
        mAuth = FirebaseAuth.getInstance();

        showProgress(true);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            showProgress(false);
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
        else {
            showProgress(false);
        }


        signin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(log_email.getText().toString().isEmpty() || log_pass.getText().toString().isEmpty()){
                    Toast.makeText(login.this,"Please enter all fields",Toast.LENGTH_SHORT).show();

                }
                else{
                    showProgress(true);
                    String email=log_email.getText().toString().trim();
                    String pass=log_pass.getText().toString().trim();

                    mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");




                                showProgress(false);
                                Intent in= new Intent(getApplicationContext(),MainActivity.class);
                                in.putExtra("name",nm);
                                startActivity(in);
                            } else {
                                showProgress(false);

                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(login.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();


                            }

                        }
                    });


                }


            }
        });

        signup_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),register.class));

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
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}