package com.example.recipes;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import org.jetbrains.annotations.NotNull;

public class ForgotPasswordActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        auth = FirebaseAuth.getInstance();
        EditText username = findViewById(R.id.usernameForgot);
        Button button = findViewById(R.id.buttonForgot);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidEmail(username.getText())) {
                    auth.sendPasswordResetEmail(username.getText().toString()).addOnCompleteListener(
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(ForgotPasswordActivity.this, "Reset link " +
                                                        "sent to your email", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(ForgotPasswordActivity.this,
                                                LoginActivity.class));
                                    } else {
                                        Toast.makeText(ForgotPasswordActivity.this, "Unable to " +
                                                        "send reset email", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                } else {
                    username.setError("Email address invalid");
                }
            }
        });
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}