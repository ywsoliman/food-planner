package com.example.foodplanner.login.presenter;

import android.app.Activity;
import android.util.Patterns;

import com.example.foodplanner.login.view.ILoginView;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPresenter {

    private final ILoginView view;
    private final FirebaseAuth model;

    public LoginPresenter(ILoginView view) {
        this.view = view;
        model = FirebaseAuth.getInstance();
    }

    public void login(String email, String password) {
        model.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) view, task -> {
                    if (task.isSuccessful()) {
                        view.onLoginSuccess();
                    } else {
                        // If sign in fails, display a message to the user.
                        view.onLoginFailed("Authentication failed");
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

    private boolean validateEmail(String email) {
        return !email.isEmpty() && isEmailValid(email);
    }

    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
