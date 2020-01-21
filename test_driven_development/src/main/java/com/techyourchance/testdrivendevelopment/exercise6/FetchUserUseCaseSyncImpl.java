package com.techyourchance.testdrivendevelopment.exercise6;

import com.techyourchance.testdrivendevelopment.example9.AddToCartUseCaseSync;
import com.techyourchance.testdrivendevelopment.example9.networking.AddToCartHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise6.networking.NetworkErrorException;
import com.techyourchance.testdrivendevelopment.exercise6.users.User;
import com.techyourchance.testdrivendevelopment.exercise6.users.UsersCache;

import javax.jws.soap.SOAPBinding;

public class FetchUserUseCaseSyncImpl implements FetchUserUseCaseSync {
    private FetchUserHttpEndpointSync.EndpointResult result;
    private FetchUserHttpEndpointSync mFetchUserHttpEndpointSync;
    private UsersCache mUsersCache;

    public FetchUserUseCaseSyncImpl(FetchUserHttpEndpointSync fetchUserHttpEndpointSync, UsersCache usersCache) {
        mFetchUserHttpEndpointSync = fetchUserHttpEndpointSync;
        mUsersCache = usersCache;
    }

    @Override
    public UseCaseResult fetchUserSync(String userId) {
        if (mUsersCache.getUser(userId) != null) {
            return new UseCaseResult(Status.SUCCESS, mUsersCache.getUser(userId));
        } else {
            try {
                result = mFetchUserHttpEndpointSync.fetchUserSync(userId);
            } catch (NetworkErrorException e) {
                return new UseCaseResult(Status.NETWORK_ERROR, null);
            }
            if (result.getStatus() == FetchUserHttpEndpointSync.EndpointStatus.SUCCESS) {
                User user = new User(result.getUserId(), result.getUsername());
                mUsersCache.cacheUser(user);
                return new UseCaseResult(Status.SUCCESS, user);

            } else {
                return new UseCaseResult(Status.FAILURE, null);
            }
        }
    }
}
