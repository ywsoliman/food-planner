package com.example.foodplanner.auth.register.view;

import com.example.foodplanner.auth.IAuthenticate;

public interface IRegisterAuth extends IAuthenticate {
    void showPasswordTooShort(String errorMsg);
    void showPasswordValid();

    void showPasswordNotMatching(String errorMsg);

    void showPasswordMatching();
}
