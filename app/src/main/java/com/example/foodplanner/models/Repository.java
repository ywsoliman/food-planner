package com.example.foodplanner.models;

import com.example.foodplanner.network.IMealsRemoteDataSource;
import com.example.foodplanner.network.NetworkCallback;

public class Repository implements IRepository {

    private static Repository repository = null;
    private final IMealsRemoteDataSource remoteDataSource;

    private Repository(IMealsRemoteDataSource remoteDataSource) {

        this.remoteDataSource = remoteDataSource;
    }

    public static synchronized Repository getInstance(IMealsRemoteDataSource remoteDataSource) {
        if (repository == null)
            repository = new Repository(remoteDataSource);
        return repository;
    }

    @Override
    public void getRemoteProducts(NetworkCallback networkCallback) {
        remoteDataSource.requestSingleRandomMeal(networkCallback);
    }

}
