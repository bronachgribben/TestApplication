package com.example.testapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.testapplication.R;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "EmailPassword";

    private TextView statusTextView;
    private TextView detailTextView;
    private EditText emailField;
    private EditText passwordField;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        //Views
        statusTextView = findViewById(R.id.statusText);
        detailTextView = findViewById(R.id.detailText);
        emailField = findViewById(R.id.emailText);
        passwordField = findViewById(R.id.passwordText);

        //Buttons
        findViewById(R.id.signinBtn).setOnClickListener(this);
        findViewById(R.id.createBtn).setOnClickListener(this);
        findViewById(R.id.signoutBtn).setOnClickListener(this);
        findViewById(R.id.verifyBtn).setOnClickListener(this);

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
                            statusTextView.setText(R.string.auth_failed);
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
        findViewById(R.id.verifyBtn).setEnabled(false);

        //Send email
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Re-enable button
                        findViewById(R.id.verifyBtn).setEnabled(true);

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
            statusTextView.setText(getString(R.string.emailpassword_status_fmt,
                    user.getEmail(), user.isEmailVerified()));
            detailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            findViewById(R.id.signinBtn).setVisibility(View.GONE);
            findViewById(R.id.createBtn).setVisibility(View.GONE);

            findViewById(R.id.emailText).setVisibility(View.GONE);
            findViewById(R.id.passwordText).setVisibility(View.GONE);

            findViewById(R.id.signoutBtn).setVisibility(View.VISIBLE);
            findViewById(R.id.verifyBtn).setVisibility(View.VISIBLE);

            findViewById(R.id.verifyBtn).setEnabled(!user.isEmailVerified());

        } else {

            statusTextView.setText(R.string.signed_out);

            detailTextView.setText(null);

            findViewById(R.id.signinBtn).setVisibility(View.VISIBLE);
            findViewById(R.id.createBtn).setVisibility(View.VISIBLE);

            findViewById(R.id.emailText).setVisibility(View.VISIBLE);
            findViewById(R.id.passwordText).setVisibility(View.VISIBLE);

            findViewById(R.id.signoutBtn).setVisibility(View.GONE);
            findViewById(R.id.verifyBtn).setVisibility(View.GONE);

        }

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.createBtn){
            createAccount(emailField.getText().toString(), passwordField.getText().toString());
        } else if (i == R.id.signinBtn){
            signIn(emailField.getText().toString(), passwordField.getText().toString());
        } else if (i == R.id.signoutBtn){
            signOut();
        } else if (i == R.id.verifyBtn){
            sendEmailVerification();
        }

    }
}
