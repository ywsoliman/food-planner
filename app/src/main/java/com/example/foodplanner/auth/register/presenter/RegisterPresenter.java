package com.example.foodplanner.auth.register.presenter;

import android.util.Patterns;

import com.example.foodplanner.auth.register.view.IRegisterAuth;
import com.example.foodplanner.models.IRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RegisterPresenter {

    private final IRegisterAuth view;
    private final IRepository model;

    public RegisterPresenter(IRegisterAuth view, IRepository model) {
        this.view = view;
        this.model = model;
    }

    public void register(String email, String password) {
        if (validateEmail(email) && validatePassword(password))
            model.registerWithEmailAndPassword(view, email, password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> view.onSuccess(),
                            throwable -> view.onFailure(throwable.getMessage()));
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

    public void checkPasswordsMatch(String password, String confirmPassword) {
        if (!password.equals(confirmPassword))
            view.showPasswordNotMatching("Passwords don't match.");
        else
            view.showPasswordMatching();
    }
}
