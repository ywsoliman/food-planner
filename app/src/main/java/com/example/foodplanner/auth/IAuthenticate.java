package com.example.foodplanner.auth;

import android.app.Activity;

public interface IAuthenticate {
    void onSuccess();
    void onFailure(String errorMsg);
    void showEmailValid();
    void showEmailNotValid(String errorMsg);
    Activity getActivity();
}
