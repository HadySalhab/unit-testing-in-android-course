package com.techyourchance.testdrivendevelopment.exercise6;

import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise6.networking.NetworkErrorException;
import com.techyourchance.testdrivendevelopment.exercise6.users.User;
import com.techyourchance.testdrivendevelopment.exercise6.users.UsersCache;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FetchUserUseCaseSyncImplTest {
    //region constants
    public static final String USER_ID = "userId";
    private static final String USER_NAME = "username";
    public static final User USER = new User(USER_ID, USER_NAME);

    //endregion constants

    //region helper fields
    @Mock
    FetchUserHttpEndpointSync mFetchUserHttpEndpointSyncMock;
    @Mock
    UsersCache mUsersCacheMock;
    FetchUserUseCaseSyncImpl SUT;
    //endregion helper fields


    @Before
    public void setUp() throws Exception {
        SUT = new FetchUserUseCaseSyncImpl(mFetchUserHttpEndpointSyncMock, mUsersCacheMock);
        success();
        userNotInCache();
    }

    //TestCases------------------------------------------------------------------------------------/


    @Test
    public void fetchUserSync_notInCache_correctUserPassedToEndPoint() throws NetworkErrorException {
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        SUT.fetchUserSync(USER_ID);
        verify(mFetchUserHttpEndpointSyncMock).fetchUserSync(ac.capture());
        Assert.assertThat(ac.getValue(), is(USER_ID));
    }

    @Test
    public void fetchUserSync_notInCacheEndpointSuccess_successStatus() throws Exception {
        FetchUserUseCaseSync.UseCaseResult result = SUT.fetchUserSync(USER_ID);
        Assert.assertEquals(FetchUserUseCaseSync.Status.SUCCESS, result.getStatus());
    }

    @Test
    //unitOfWork_stateUnderTest_expectedBehavior
    public void fetchUserSync_notInCacheEndpointSuccess_correctUserReturned() {
        FetchUserUseCaseSync.UseCaseResult result = SUT.fetchUserSync(USER_ID);
        Assert.assertEquals(USER, result.getUser());
    }

    @Test
    public void fetchUserSync_notInCacheEndpointSuccess_userCached() throws Exception {
        ArgumentCaptor<User> ac = ArgumentCaptor.forClass(User.class);
        SUT.fetchUserSync(USER_ID);
        verify(mUsersCacheMock).cacheUser(ac.capture());
        Assert.assertEquals(USER, ac.getValue());
    }

    @Test
    public void fetchUserSync_notInCacheEndpointAuthError_failureStatus() throws Exception {
        authError();
        FetchUserUseCaseSync.UseCaseResult result = SUT.fetchUserSync(USER_ID);
        Assert.assertEquals(FetchUserUseCaseSync.Status.FAILURE, result.getStatus());
    }

    @Test
    public void fetchUserSync_notInCacheEndpointAuthError_nullUserReturned() throws Exception {
        authError();
        FetchUserUseCaseSync.UseCaseResult result = SUT.fetchUserSync(USER_ID);
        assertNull(result.getUser());
    }

    @Test
    public void fetchUserSync_notInCacheEndpointAuthError_nothingCached() throws Exception {
        authError();
        SUT.fetchUserSync(USER_ID);
        verify(mUsersCacheMock, never()).cacheUser(ArgumentMatchers.any(User.class));
    }

    @Test
    public void fetchUserSync_notInCacheEndpointServerError_failureStatus() throws Exception {
        serverError();
        FetchUserUseCaseSync.UseCaseResult result = SUT.fetchUserSync(USER_ID);
        Assert.assertEquals(FetchUserUseCaseSync.Status.FAILURE, result.getStatus());
    }

    @Test
    public void fetchUserSync_notInCacheEndpointServerError_nullUserReturned() throws Exception {
        serverError();
        FetchUserUseCaseSync.UseCaseResult result = SUT.fetchUserSync(USER_ID);
        assertNull(result.getUser());
    }

    @Test
    public void fetchUserSync_notInCacheEndpointServerError_nothingCached() throws Exception {
        serverError();
        SUT.fetchUserSync(USER_ID);
        verify(mUsersCacheMock, never()).cacheUser(ArgumentMatchers.any(User.class));    }

    @Test
    public void fetchUserSync_notInCacheEndpointGeneralError_failureStatus() throws Exception {
        generalError();
        FetchUserUseCaseSync.UseCaseResult result = SUT.fetchUserSync(USER_ID);
        Assert.assertEquals(FetchUserUseCaseSync.Status.FAILURE, result.getStatus());
    }

    @Test
    public void fetchUserSync_notInCacheEndpointGeneralError_nullUserReturned() throws Exception {
        generalError();
        FetchUserUseCaseSync.UseCaseResult result = SUT.fetchUserSync(USER_ID);
        assertNull(result.getUser());
    }

    @Test
    public void fetchUserSync_notInCacheEndpointGeneralError_nothingCached() throws Exception {
        generalError();
        SUT.fetchUserSync(USER_ID);
        verify(mUsersCacheMock, never()).cacheUser(ArgumentMatchers.any(User.class));
    }

    @Test
    public void fetchUserSync_notInCacheEndpointNetworkError_failureStatus() throws Exception {
        networkError();
        FetchUserUseCaseSync.UseCaseResult result = SUT.fetchUserSync(USER_ID);
        Assert.assertEquals(FetchUserUseCaseSync.Status.NETWORK_ERROR, result.getStatus());
    }

    @Test
    public void fetchUserSync_notInCacheEndpointNetworkError_nullUserReturned() throws Exception {
        networkError();
        FetchUserUseCaseSync.UseCaseResult result = SUT.fetchUserSync(USER_ID);
        assertNull(result.getUser());
    }

    @Test
    public void fetchUserSync_notInCacheEndpointNetworkError_nothingCached() throws Exception {
        networkError();
        SUT.fetchUserSync(USER_ID);
        verify(mUsersCacheMock, never()).cacheUser(ArgumentMatchers.any(User.class));
    }


    @Test
    public void fetchUserSync_correctUserIdPassedToCache() throws Exception {
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        SUT.fetchUserSync(USER_ID);
        verify(mUsersCacheMock).getUser(ac.capture());
        Assert.assertEquals(USER_ID, ac.getValue());
    }

    @Test
    public void fetchUserSync_inCache_successStatus() throws Exception {
        userInCache();
        FetchUserUseCaseSync.UseCaseResult result = SUT.fetchUserSync(USER_ID);
        Assert.assertEquals(FetchUserUseCaseSync.Status.SUCCESS, result.getStatus());
    }

    @Test
    public void fetchUserSync_inCache_correctUserIsReturned() throws Exception {
        userInCache();
        FetchUserUseCaseSync.UseCaseResult result = SUT.fetchUserSync(USER_ID);
        Assert.assertEquals(USER, result.getUser());
    }

    @Test
    public void fetchUserSync_inCache_endpointNotPolled() throws Exception {
        userInCache();
    SUT.fetchUserSync(USER_ID);
    verify(mFetchUserHttpEndpointSyncMock,never()).fetchUserSync(anyString());
    }

    // region helper methods
    public void userNotInCache() {
        when(mUsersCacheMock.getUser(anyString())).thenReturn(null);
    }

    public void userInCache() {
        when(mUsersCacheMock.getUser(anyString())).thenReturn(USER);
    }

    public void success() throws NetworkErrorException {
        when(mFetchUserHttpEndpointSyncMock.fetchUserSync(anyString())).thenReturn(new FetchUserHttpEndpointSync.EndpointResult(
                FetchUserHttpEndpointSync.EndpointStatus.SUCCESS, USER_ID, USER_NAME
        ));
    }

    public void generalError() throws NetworkErrorException {
        when(mFetchUserHttpEndpointSyncMock.fetchUserSync(anyString())).thenReturn(
                new FetchUserHttpEndpointSync.EndpointResult(FetchUserHttpEndpointSync.EndpointStatus.GENERAL_ERROR,"","")
        );
    }

    public void authError() throws NetworkErrorException {
        when(mFetchUserHttpEndpointSyncMock.fetchUserSync(anyString())).thenReturn(
                new FetchUserHttpEndpointSync.EndpointResult(FetchUserHttpEndpointSync.EndpointStatus.AUTH_ERROR,"","")
        );
    }

    public void serverError() throws NetworkErrorException {
        when(mFetchUserHttpEndpointSyncMock.fetchUserSync(anyString())).thenReturn(
                new FetchUserHttpEndpointSync.EndpointResult(FetchUserHttpEndpointSync.EndpointStatus.SERVER_ERROR,"","")
        );
    }

    public void networkError() throws NetworkErrorException {
        doThrow(new NetworkErrorException()).when(mFetchUserHttpEndpointSyncMock).fetchUserSync(anyString());
    }
    //endregion helper methods


    //region helper classes

    //endregion helper classes
}
