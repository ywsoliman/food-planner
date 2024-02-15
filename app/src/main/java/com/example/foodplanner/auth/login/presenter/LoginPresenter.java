package com.example.foodplanner.auth.login.presenter;

import android.app.Activity;
import android.util.Patterns;

import com.example.foodplanner.auth.IAuthCallback;
import com.example.foodplanner.auth.IAuthenticate;
import com.example.foodplanner.models.IRepository;

public class LoginPresenter implements IAuthCallback {

    private final IAuthenticate view;
    private final IRepository model;

    public LoginPresenter(IAuthenticate view, IRepository model) {
        this.view = view;
        this.model = model;
    }

    public void login(String email, String password) {
        if (validateEmail(email) && validatePassword(password))
            model.loginWithEmailAndPassword(this, email, password);
    }

    public void loginAsGuest() {
        model.loginAsGuest(this);
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

    @Override
    public void onSuccess() {
        view.onSuccess();
    }

    @Override
    public void onFailure(String errorMsg) {
        view.onFailure(errorMsg);
    }

    @Override
    public Activity getActivity() {
        return view.getActivity();
    }
}
