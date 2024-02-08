package com.example.foodplanner.login.presenter;

import android.app.Activity;
import android.util.Patterns;

import com.example.foodplanner.IAuthenticate;
import com.example.foodplanner.models.IRepository;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPresenter {

    private final IAuthenticate view;
    private final IRepository model;

    public LoginPresenter(IAuthenticate view, IRepository model) {
        this.view = view;
        this.model = model;
    }

    public void login(String email, String password) {
        model.loginWithEmailAndPassword(view, email, password);
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
