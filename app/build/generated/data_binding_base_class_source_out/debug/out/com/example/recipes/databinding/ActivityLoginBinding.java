// Generated by view binder compiler. Do not edit!
package com.example.recipes.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import com.example.recipes.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityLoginBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button buttonRegister;

  @NonNull
  public final Button buttonSignIn;

  @NonNull
  public final TextView forgotPass;

  @NonNull
  public final EditText password;

  @NonNull
  public final EditText username;

  private ActivityLoginBinding(@NonNull ConstraintLayout rootView, @NonNull Button buttonRegister,
      @NonNull Button buttonSignIn, @NonNull TextView forgotPass, @NonNull EditText password,
      @NonNull EditText username) {
    this.rootView = rootView;
    this.buttonRegister = buttonRegister;
    this.buttonSignIn = buttonSignIn;
    this.forgotPass = forgotPass;
    this.password = password;
    this.username = username;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityLoginBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityLoginBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_login, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityLoginBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.buttonRegister;
      Button buttonRegister = rootView.findViewById(id);
      if (buttonRegister == null) {
        break missingId;
      }

      id = R.id.buttonSignIn;
      Button buttonSignIn = rootView.findViewById(id);
      if (buttonSignIn == null) {
        break missingId;
      }

      id = R.id.forgotPass;
      TextView forgotPass = rootView.findViewById(id);
      if (forgotPass == null) {
        break missingId;
      }

      id = R.id.password;
      EditText password = rootView.findViewById(id);
      if (password == null) {
        break missingId;
      }

      id = R.id.username;
      EditText username = rootView.findViewById(id);
      if (username == null) {
        break missingId;
      }

      return new ActivityLoginBinding((ConstraintLayout) rootView, buttonRegister, buttonSignIn,
          forgotPass, password, username);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}