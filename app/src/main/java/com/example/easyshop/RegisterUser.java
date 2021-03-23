package com.example.easyshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView title, registerUser;
    private EditText fullNameEditText, ageEditText, emailEditText, passwordEditText;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        fullNameEditText = findViewById(R.id.fullnameEditText);
        ageEditText = findViewById(R.id.ageEditText);
        emailEditText = findViewById(R.id.emailuserEditText);
        passwordEditText = findViewById(R.id.passworduserEditText);

    }
    public void registerUser(View v){
        String fullname = fullNameEditText.getText().toString().trim();
        String age = ageEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if(fullname.isEmpty()){
            fullNameEditText.setError("Name is required");
            fullNameEditText.requestFocus();
            return;
        }

        if(age.isEmpty()){
            ageEditText.setError("Age is required");
            ageEditText.requestFocus();
            return;
        }

        if(email.isEmpty()){
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Provide valid email!");
            emailEditText.requestFocus();
            return;
        }

        if(password.isEmpty()){
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return;
        }

        if(password.length() < 8){
            passwordEditText.setError("Min password length should be 8 characters");
            passwordEditText.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful()){
                           User user = new User(fullname, age, email);
                           FirebaseDatabase.getInstance().getReference("Users")
                                   .child(FirebaseAuth.getInstance().getUid())
                                   .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if(task.isSuccessful()){
                                       Toast.makeText(RegisterUser.this, "User registered successfully", Toast.LENGTH_LONG).show();
                                       //Progress bar

                                       //Redirect to login page
                                   }else{
                                       Toast.makeText(RegisterUser.this,"Failed to register user! Try again Later", Toast.LENGTH_LONG).show();
                                   }
                               }
                           });
                           fullNameEditText.getText().clear();
                           ageEditText.getText().clear();
                           emailEditText.getText().clear();
                           passwordEditText.getText().clear();
                       }else {
                           Toast.makeText(RegisterUser.this,"Failed to register user! Try again Later", Toast.LENGTH_LONG).show();
                       }
                    }
                });
    }
}