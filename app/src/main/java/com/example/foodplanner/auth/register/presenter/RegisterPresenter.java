package com.example.foodplanner.auth.register.presenter;

import android.app.Activity;
import android.util.Patterns;

import com.example.foodplanner.auth.IAuthCallback;
import com.example.foodplanner.auth.register.view.IRegisterAuth;
import com.example.foodplanner.models.IRepository;

public class RegisterPresenter implements IAuthCallback {

    private final IRegisterAuth view;
    private final IRepository model;

    public RegisterPresenter(IRegisterAuth view, IRepository model) {
        this.view = view;
        this.model = model;
    }

    public void register(String email, String password) {
        if (validateEmail(email) && validatePassword(password))
            model.registerWithEmailAndPassword(this, email, password);
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

    public void checkPasswordsMatch(String password, String confirmPassword) {
        if (!password.equals(confirmPassword))
            view.showPasswordNotMatching("Passwords don't match.");
        else
            view.showPasswordMatching();
    }
}
