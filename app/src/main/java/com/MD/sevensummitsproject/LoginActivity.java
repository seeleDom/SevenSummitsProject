package com.MD.sevensummitsproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.MD.sevensummitsproject.ui.gps.MainActivity2;
import com.MD.sevensummitsproject.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {

    EditText lEmail;
    EditText lPasswort;
    TextView lLink;
    Button lButton;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        lEmail = findViewById(R.id.LoginEmail);
        lPasswort = findViewById(R.id.LoginPasswort);
        lLink = findViewById(R.id.LinkRegister);
        lButton = findViewById(R.id.LoginButton);

        mAuth = FirebaseAuth.getInstance();

        lButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        lLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }

    private void loginUser(){
        String email = lEmail.getText().toString();
        String passwort = lPasswort.getText().toString();

        if(TextUtils.isEmpty(email)){
            lEmail.setError("Bitte eine Email eintragen");
            lEmail.requestFocus();
        } else if(TextUtils.isEmpty(passwort)){
            lPasswort.setError("Bitte ein Passwort eingeben");
            lPasswort.requestFocus();
        } else {
            mAuth.signInWithEmailAndPassword(email,passwort).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "Erfolgreich angemeldet", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(LoginActivity.this, "Leider ist ein Fehler aufgetreten: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}