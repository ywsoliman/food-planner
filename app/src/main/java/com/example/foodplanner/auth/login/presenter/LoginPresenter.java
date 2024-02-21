package com.example.foodplanner.auth.login.presenter;

import android.util.Patterns;

import com.example.foodplanner.auth.IAuthenticate;
import com.example.foodplanner.models.IRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginPresenter {

    private final IAuthenticate view;
    private final IRepository model;

    public LoginPresenter(IAuthenticate view, IRepository model) {
        this.view = view;
        this.model = model;
    }

    public void login(String email, String password) {
        if (validateEmail(email) && validatePassword(password))
            model.loginWithEmailAndPassword(view, email, password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> view.onSuccess(),
                            throwable -> view.onFailure(throwable.getMessage()));
    }

    public void loginAsGuest() {
        model.loginAsGuest(view)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> view.onSuccess(),
                        throwable -> view.onFailure(throwable.getMessage()));
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
