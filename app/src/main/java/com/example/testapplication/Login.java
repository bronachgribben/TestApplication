package com.example.testapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity implements View.OnClickListener {



    private static final String TAG = "EmailPassword";

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;


    EditText emailField;
    EditText passwordField;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("User");



        //Views

        emailField = findViewById(R.id.emailText);
        passwordField = findViewById(R.id.passwordText);

        //Buttons
        findViewById(R.id.signinBtn).setOnClickListener(this);
        findViewById(R.id.createBtn).setOnClickListener(this);


        //Initialise Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart(){
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    //Create User Account
    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        //Start creating user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //Sign in success
                            Log.d(TAG, "createUserWithEmail: success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            saveAccount();
                            Toast.makeText(Login.this, "Account Saved", Toast.LENGTH_LONG).show();
                        } else {
                            //Sign in fail
                            Log.w(TAG,"createUserWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    //Sign in user
    private void signIn(String email, String password){
        Log.d(TAG, "signIn:" + email);
        //if(!validateForm()){
        //  return;
        //}

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //Sign in success
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            //Sign in fails
                            Log.w(TAG, "signInWithEmail: failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        if (!task.isSuccessful()){
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);

    }

    private void sendEmailVerification(){
        //Disable button


        //Send email
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Re-enable button


                        if (task.isSuccessful()){
                            Toast.makeText(Login.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(Login.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = emailField.getText().toString();
        if(TextUtils.isEmpty(email)){
            emailField.setError("Required.");
            valid = false;
        } else{
            emailField.setError(null);
        }

        String password = passwordField.getText().toString();
        if(TextUtils.isEmpty(password)){
            passwordField.setError("Required.");
            valid = false;
        } else{
            passwordField.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {


            findViewById(R.id.signinBtn).setVisibility(View.VISIBLE);
            findViewById(R.id.createBtn).setVisibility(View.VISIBLE);

            findViewById(R.id.emailText).setVisibility(View.VISIBLE);
            findViewById(R.id.passwordText).setVisibility(View.VISIBLE);



        }

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.createBtn){
            createAccount(emailField.getText().toString(), passwordField.getText().toString());
        } else if (i == R.id.signinBtn){
            signIn(emailField.getText().toString(), passwordField.getText().toString());
        }

    }

    private void saveAccount(){
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        LoginDetails details = new LoginDetails(email, password);
        mDatabaseReference.push().setValue(details);
    }

    private void clean(){
        emailField.setText("");
        passwordField.setText("");
    }
}
