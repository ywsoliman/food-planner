package com.example.foodplanner;

public interface IAuthenticate {
    void onSuccess();
    void onFailure(String errorMsg);
    void showEmailValid();
    void showEmailNotValid(String errorMsg);
}
