package com.example.foodplanner.register.presenter;

import android.app.Activity;
import android.util.Patterns;

import androidx.annotation.NonNull;

import com.example.foodplanner.register.view.IRegisterAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterPresenter {

    private final IRegisterAuth view;
    private final FirebaseAuth model;

    public RegisterPresenter(IRegisterAuth view) {
        this.view = view;
        model = FirebaseAuth.getInstance();
    }

    public void register(String email, String password) {
        model.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) view, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            FirebaseUser user = model.getCurrentUser();
//                            updateUI(user);
                            view.onSuccess();
                        } else {
                            // If sign in fails, display a message to the user.
                            view.onFailure("Authentication failed");
                        }
                    }
                });
    }

    public void tryRegister(String email, String password) {
        if (validateEmail(email) && validatePassword(password))
            register(email, password);
    }

    public boolean validatePassword(String password) {
        if (!password.isEmpty() && password.length() >= 6) {
            view.showPasswordValid();
            return true;
        }
        view.showPasswordTooShort("Password too short! Minimum 6 characters");
        return false;
    }

    public boolean validateEmail(String email) {
        if (!email.isEmpty() && isEmailValid(email)) {
            view.showEmailValid();
            return true;
        }
        view.showEmailNotValid("Invalid email address!");
        return false;
    }

    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
