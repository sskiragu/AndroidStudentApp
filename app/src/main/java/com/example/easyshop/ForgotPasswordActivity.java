package com.example.easyshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText emailResetPwdEditText;
    private ProgressBar resetPwdprogressBar;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();

        emailResetPwdEditText = findViewById(R.id.emailResetPwdEditText);
        resetPwdprogressBar = findViewById(R.id.resetPwdprogressBar);
    }

    public void resetPasswordNow(View v){
        String email = emailResetPwdEditText.getText().toString().trim();
        if(email.isEmpty()){
            emailResetPwdEditText.setError("Email is required");
            emailResetPwdEditText.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailResetPwdEditText.setError("Provide valid email!");
            emailResetPwdEditText.requestFocus();
            return;
        }

        resetPwdprogressBar.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPasswordActivity.this, "Check your email to reset your password", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(ForgotPasswordActivity.this, "Try again Later! Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}