package com.example.foodplanner.login.presenter;

import android.app.Activity;
import android.util.Patterns;

import com.example.foodplanner.IAuthenticate;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPresenter {

    private final IAuthenticate view;
    private final FirebaseAuth model;

    public LoginPresenter(IAuthenticate view) {
        this.view = view;
        model = FirebaseAuth.getInstance();
    }

    public void login(String email, String password) {
        model.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) view, task -> {
                    if (task.isSuccessful()) {
                        view.onSuccess();
                    } else {
                        // If sign in fails, display a message to the user.
                        view.onFailure("Invalid username or password");
                    }
                });
    }

    public void tryLogin(String email, String password) {
        if (validateEmail(email) && validatePassword(password))
            login(email, password);
    }

    private boolean validatePassword(String password) {
        return !password.isEmpty();
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
