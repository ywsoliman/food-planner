package com.example.foodplanner.register.view;

import com.example.foodplanner.IAuthenticate;

public interface IRegisterAuth extends IAuthenticate {
    void showPasswordTooShort(String errorMsg);
    void showPasswordValid();
}
