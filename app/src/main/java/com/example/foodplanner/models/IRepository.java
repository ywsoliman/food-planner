package com.example.foodplanner.models;

import com.example.foodplanner.auth.IAuthenticate;
import com.example.foodplanner.network.NetworkCallback;

public interface IRepository {
    void loginWithEmailAndPassword(IAuthenticate view, String email, String pass);

    void getRemoteProducts(NetworkCallback networkCallback);

    void getRemoteCategories(NetworkCallback networkCallback);
}
