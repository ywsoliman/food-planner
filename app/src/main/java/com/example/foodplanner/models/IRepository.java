package com.example.foodplanner.models;

import com.example.foodplanner.network.NetworkCallback;

public interface IRepository {
    void getRemoteProducts(NetworkCallback networkCallback);
}
