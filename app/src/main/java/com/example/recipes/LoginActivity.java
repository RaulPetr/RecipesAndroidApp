package com.example.recipes;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        Button signIn = findViewById(R.id.buttonSignIn);
        Button register = findViewById(R.id.buttonRegister);
        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d(TAG, username.getText().toString() + " " + password.getText().toString());
                String email = username.getText().toString();
                String pass = password.getText().toString();
                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Logged In",
                                            Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = auth.getCurrentUser();
                                    goHome(user);
                                } else {
                                    Toast.makeText(LoginActivity.this, "Autenthication Failed",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void goHome(FirebaseUser user) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("loggedIn", true);
        startActivity(intent);
    }

    public void forgot(View v) {
        startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
    }


}