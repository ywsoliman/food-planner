package com.example.foodplanner.auth;

import android.app.Activity;

public interface IAuthCallback {
    void onSuccess();

    void onFailure(String errorMsg);

    Activity getActivity();
}
