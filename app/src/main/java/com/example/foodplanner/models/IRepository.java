package com.example.foodplanner.models;

import com.example.foodplanner.IAuthenticate;
import com.example.foodplanner.network.NetworkCallback;

public interface IRepository {
    void getRemoteProducts(NetworkCallback networkCallback);
    void loginWithEmailAndPassword(IAuthenticate view, String email, String pass);
}
