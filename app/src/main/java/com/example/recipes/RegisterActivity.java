package com.example.recipes;

import android.content.Intent;
import android.text.TextUtils;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import org.jetbrains.annotations.NotNull;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private Boolean valid = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        EditText username = findViewById(R.id.usernameReg);
        EditText password = findViewById(R.id.passwordReg);
        EditText passwordVal = findViewById(R.id.passwordReg2);
        EditText firstName = findViewById(R.id.firstNameReg);
        EditText lastName = findViewById(R.id.lastNameReg);
        Button registerButton = findViewById(R.id.buttonReg);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(username, password, passwordVal);
                if(!valid) {
                    Toast.makeText(RegisterActivity.this, "Check requirements",
                            Toast.LENGTH_SHORT).show();
                } else {
                    auth.createUserWithEmailAndPassword(username.getText().toString(),
                            password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Registered Succesfully",
                                                Toast.LENGTH_SHORT).show();
                                        FirebaseUser user = auth.getCurrentUser();
                                        updateName(user, firstName.getText().toString(),
                                                lastName.getText().toString());
                                        goHome(user);
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Registration Failed",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        auth = FirebaseAuth.getInstance();

    }

    private void updateName(FirebaseUser user, String firstName, String lastName) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(firstName + " " + lastName)
                .build();
        user.updateProfile(profileUpdates);
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }



    private void validate(EditText username, EditText password, EditText passwordVal) {
        valid = true;
        if(!isValidEmail(username.getText())) {
            valid = false;
            username.setError("Email address invalid");
        }
        if(!match(password, passwordVal)) {
            valid = false;
            password.setError("Passwords do not match");
        }
        if(!goodPassword(password)) {
            valid = false;
            password.setError("Password must be over 8 characters");
        }
    }

    private boolean goodPassword(EditText password) {
        if(password.getText().toString().length() < 8)
            return false;
        return true;
    }

    private boolean match(EditText password, EditText passwordVal) {
        if(password.getText().toString().equals(passwordVal.getText().toString()))
            return true;
        return false;
    }

    private void goHome(FirebaseUser user) {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.putExtra("loggedIn", true);
        startActivity(intent);
    }
}