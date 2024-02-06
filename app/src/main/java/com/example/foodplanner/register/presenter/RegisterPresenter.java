package com.example.foodplanner.register.presenter;

import android.app.Activity;
import android.util.Patterns;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterPresenter {

    private final IRegisterView view;
    private final FirebaseAuth model;

    public RegisterPresenter(IRegisterView view) {
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
                            view.onRegisterSuccess();
                        } else {
                            // If sign in fails, display a message to the user.
                            view.onRegisterFailed("Authentication failed");
                        }
                    }
                });
    }

    public void tryRegister(String email, String password) {
        if (validateEmail(email) && validatePassword(password))
            register(email, password);
    }

    private boolean validatePassword(String password) {
        return !password.isEmpty();
    }

    private boolean validateEmail(String email) {
        return !email.isEmpty() && isEmailValid(email);
    }

    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
