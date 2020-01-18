package com.techyourchance.testdoublesfundamentals.exercise4;

import com.techyourchance.testdoublesfundamentals.example4.networking.LoginHttpEndpointSync;
import com.techyourchance.testdoublesfundamentals.example4.networking.NetworkErrorException;
import com.techyourchance.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpointSync;
import com.techyourchance.testdoublesfundamentals.exercise4.users.User;
import com.techyourchance.testdoublesfundamentals.exercise4.users.UsersCache;

import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import javax.jws.soap.SOAPBinding;

import static com.techyourchance.testdoublesfundamentals.exercise4.FetchUserProfileUseCaseSync.UseCaseResult.NETWORK_ERROR;

public class FetchUserProfileUseCaseSyncTest {
    private static final String USER_ID = "userId";
    public static final String FULL_NAME = "fullName";
    public static final String IMAGE_URL = "imageUrl";
    public static final User user = new User(USER_ID, FULL_NAME, IMAGE_URL);
    FetchUserProfileUseCaseSync SUT;
    UserProfileHttpEndPointSyncTD mUserProfileHttpEndPointSyncTD;
    UsersCacheTD mUsersCacheTD;


    @Before
    public void setUp() throws Exception {
        mUserProfileHttpEndPointSyncTD = new UserProfileHttpEndPointSyncTD();
        mUsersCacheTD = new UsersCacheTD();
        SUT = new FetchUserProfileUseCaseSync(mUserProfileHttpEndPointSyncTD, mUsersCacheTD);
    }

    //if success, UserID is passed to EndPoint.
    @Test
    public void fetchUserProfileSync_success_userIdIsPassedToEndPoint() {
        SUT.fetchUserProfileSync(USER_ID);
        Assert.assertEquals(USER_ID, mUserProfileHttpEndPointSyncTD.getUserId());
    }

    //if success, USER is cached.
    @Test
    public void fetchUserProfileSync_success_userIsCached() {
        SUT.fetchUserProfileSync(USER_ID);
        User user = mUsersCacheTD.getUser(USER_ID);
        Assert.assertEquals(USER_ID,user.getUserId());
        Assert.assertEquals(FULL_NAME,user.getFullName());
        Assert.assertEquals(IMAGE_URL,user.getImageUrl());
    }


    //if failure, User is Not cached.
    @Test
    public void fetchUserProfileSync_generalError_UserIsNotCached() {
        mUserProfileHttpEndPointSyncTD.mIsGeneralError = true;
        SUT.fetchUserProfileSync(USER_ID);
        Assert.assertNull(mUsersCacheTD.getUser(USER_ID));
    }
    @Test
    public void fetchUserProfileSync_serviceError_UserIsNotCached() {
        mUserProfileHttpEndPointSyncTD.mIsServerError = true;
        SUT.fetchUserProfileSync(USER_ID);
        Assert.assertNull(mUsersCacheTD.getUser(USER_ID));
    }
    @Test
    public void fetchUserProfileSync_authError_UserIsNotCached() {
        mUserProfileHttpEndPointSyncTD.mIsAuthError = true;
        SUT.fetchUserProfileSync(USER_ID);
        Assert.assertNull(mUsersCacheTD.getUser(USER_ID));
    }


    //if success, Success login is returned.
    @Test
    public void fetchUserProfileSync_success_succesResultIsReturned() {
        FetchUserProfileUseCaseSync.UseCaseResult useCaseResult = SUT.fetchUserProfileSync(USER_ID);
        Assert.assertEquals(FetchUserProfileUseCaseSync.UseCaseResult.SUCCESS,useCaseResult);
    }

    //if failure, failure login is returned.
    @Test
    public void fetchUserProfileSync_generalError_failureResultIsReturned() {
        mUserProfileHttpEndPointSyncTD.mIsGeneralError = true;
        FetchUserProfileUseCaseSync.UseCaseResult useCaseResult = SUT.fetchUserProfileSync(USER_ID);
        Assert.assertEquals(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE,useCaseResult);
    }
    @Test
    public void fetchUserProfileSync_authError_failureResultIsReturned() {
        mUserProfileHttpEndPointSyncTD.mIsAuthError = true;
        FetchUserProfileUseCaseSync.UseCaseResult useCaseResult = SUT.fetchUserProfileSync(USER_ID);
        Assert.assertEquals(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE,useCaseResult);
    }
    @Test
    public void fetchUserProfileSync_serviceError_failureResultIsReturned() {
        mUserProfileHttpEndPointSyncTD.mIsServerError = true;
        FetchUserProfileUseCaseSync.UseCaseResult useCaseResult = SUT.fetchUserProfileSync(USER_ID);
        Assert.assertEquals(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE,useCaseResult);
    }

    //if networkError,throw NetworkException
    @Test
    public void fetchUserProfileSync_networkError_networkErrorReturned() {
        mUserProfileHttpEndPointSyncTD.mIsNetworkError = true;
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        Assert.assertEquals(NETWORK_ERROR,result);
    }

    //------------------------------------------------------------------------------------------------------------------
    //Helper Class
    class UserProfileHttpEndPointSyncTD implements UserProfileHttpEndpointSync {
        private String mUserId = "";
        boolean mIsAuthError;
        boolean mIsServerError;
        boolean mIsGeneralError;
        boolean mIsNetworkError;

        @Override
        public EndpointResult getUserProfile(String userId) throws NetworkErrorException {
            mUserId = userId;
            if (mIsAuthError) {
                return new EndpointResult(EndpointResultStatus.AUTH_ERROR, "", "", "");
            } else if (mIsServerError) {
                return new EndpointResult(EndpointResultStatus.SERVER_ERROR, "", "", "");
            } else if (mIsGeneralError) {
                return new EndpointResult(EndpointResultStatus.GENERAL_ERROR, "", "", "");
            } else if (mIsNetworkError) {
                throw new NetworkErrorException();
            } else {
                return new EndpointResult(EndpointResultStatus.SUCCESS, user.getUserId(), user.getFullName(),user.getImageUrl());
            }
        }

        String getUserId() {
            return mUserId;
        }
    }

    class UsersCacheTD implements UsersCache {
        private List<User> mUsers = new ArrayList<>(1);

        @Override
        public void cacheUser(User user) {
            User existingUser = getUser(user.getUserId());
            if (existingUser==null) {
                mUsers.add(user);
            }
        }

        @Nullable
        @Override
        public User getUser(String userId) {
           for (User user : mUsers){
               if (user.getUserId().equals(userId)){
                   return user;
               }
           }
            return  null;
        }
    }
}