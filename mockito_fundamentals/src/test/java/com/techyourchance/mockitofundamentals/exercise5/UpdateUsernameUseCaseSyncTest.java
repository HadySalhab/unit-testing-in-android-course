package com.techyourchance.mockitofundamentals.exercise5;

import com.techyourchance.mockitofundamentals.example7.eventbus.LoggedInEvent;
import com.techyourchance.mockitofundamentals.exercise5.eventbus.EventBusPoster;
import com.techyourchance.mockitofundamentals.exercise5.eventbus.UserDetailsChangedEvent;
import com.techyourchance.mockitofundamentals.exercise5.networking.NetworkErrorException;
import com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync;
import com.techyourchance.mockitofundamentals.exercise5.users.User;
import com.techyourchance.mockitofundamentals.exercise5.users.UsersCache;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.omg.CORBA.Any;

import java.util.concurrent.ExecutionException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UpdateUsernameUseCaseSyncTest {
    public static final String USERNAME = "username";
    public static final String USERID = "userId";

    UpdateUsernameUseCaseSync SUT;
    @Mock UpdateUsernameHttpEndpointSync mUpdateUsernameHttpEndpointSyncMock;
    @Mock UsersCache mUsersCacheMock;
    @Mock EventBusPoster mEventBusPosterMock;

    @Before
    public void setUp() throws Exception {

        SUT = new UpdateUsernameUseCaseSync(mUpdateUsernameHttpEndpointSyncMock,mUsersCacheMock,  mEventBusPosterMock);
        success();
    }

    //when updating, USERNAME AND USERid are passed to endPoint
    @Test
    public void updateUsername_success_shouldPassToEndPoint() throws Exception {
       // ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        SUT.updateUsernameSync(USERID,USERNAME);
        verify(mUpdateUsernameHttpEndpointSyncMock).updateUsername(eq(USERID),eq(USERNAME));
        //verify(mUpdateUsernameHttpEndpointSyncMock).updateUsername(ac.capture(),ac.capture());
        //Assert.assertEquals(USERID,ac.getAllValues().get(0));
        //Assert.assertEquals(USERNAME,ac.getAllValues().get(1));
    }
    //when success, user should be cached
    @Test
    public void updateUsername_success_shouldCacheUser(){
        ArgumentCaptor<User> ac = ArgumentCaptor.forClass(User.class);
        SUT.updateUsernameSync(USERID,USERNAME);
        verify(mUsersCacheMock).cacheUser(ac.capture());
        Assert.assertEquals(USERNAME,ac.getValue().getUsername());
        Assert.assertEquals(USERID,ac.getValue().getUserId());
    }
    //when failure, user should not be cached
    @Test
    public void updateUsername_generalError_shouldNotBeCached() throws  Exception{
        generalError();
        SUT.updateUsernameSync(USERID,USERNAME);
        verify(mUsersCacheMock,never()).cacheUser(any(User.class));
    }
    @Test
    public void updateUsername_serverError_shouldNotBeCached() throws  Exception{
        serverError();
        SUT.updateUsernameSync(USERID,USERNAME);
        verify(mUsersCacheMock,never()).cacheUser(any(User.class));
    }
    @Test
    public void updateUsername_authError_shouldNotBeCached() throws  Exception{
        authError();
        SUT.updateUsernameSync(USERID,USERNAME);
        verify(mUsersCacheMock,never()).cacheUser(any(User.class));
    }
    //when success Event is posted
    @Test
    public void updateUsername_success_shouldPostEvent() throws Exception{
        ArgumentCaptor<Object> ac = ArgumentCaptor.forClass(Object.class);
        SUT.updateUsernameSync(USERID,USERNAME);
        verify(mEventBusPosterMock).postEvent(ac.capture());
       Assert.assertThat(ac.getValue(),is(instanceOf(UserDetailsChangedEvent.class)));
    }
    @Test
    public  void updateUsername_generalError_shouldNotPostEvent() throws  Exception{
        generalError();
        SUT.updateUsernameSync(USERID,USERNAME);
        verify(mEventBusPosterMock,never()).postEvent(any());
    }
    @Test
    public  void updateUsername_serverError_shouldNotPostEvent() throws  Exception{
        serverError();
        SUT.updateUsernameSync(USERID,USERNAME);
        verify(mEventBusPosterMock,never()).postEvent(any());
    }
    @Test
    public  void updateUsername_authError_shouldNotPostEvent() throws  Exception{
        authError();
        SUT.updateUsernameSync(USERID,USERNAME);
        verify(mEventBusPosterMock,never()).postEvent(any());
    }
    //when success, success should return
    @Test
    public void updateUsername_success_returnSucces(){
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USERID,USERNAME);
        Assert.assertEquals(UpdateUsernameUseCaseSync.UseCaseResult.SUCCESS,result);
    }
    //when failure, failure should return
    @Test
    public void updateUsername_generalError_returnFailure() throws Exception{
        generalError();
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USERID,USERNAME);
        Assert.assertEquals(UpdateUsernameUseCaseSync.UseCaseResult.FAILURE,result);
    }
    @Test
    public void updateUsername_serverError_returnFailure() throws Exception{
        serverError();
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USERID,USERNAME);
        Assert.assertEquals(UpdateUsernameUseCaseSync.UseCaseResult.FAILURE,result);
    }
    @Test
    public void updateUsername_authError_returnFailure() throws Exception{
        authError();
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USERID,USERNAME);
        Assert.assertEquals(UpdateUsernameUseCaseSync.UseCaseResult.FAILURE,result);
    }
    //when networkException, NETWORK failure should return
    @Test
    public void updateUsername_networkError_returnFailure() throws Exception{
        networkError();
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USERID,USERNAME);
        Assert.assertEquals(UpdateUsernameUseCaseSync.UseCaseResult.NETWORK_ERROR,result);
    }

    public void networkError() throws Exception{
        doThrow(new NetworkErrorException()).when(mUpdateUsernameHttpEndpointSyncMock).updateUsername(anyString(),anyString());
    }


    private void success() throws NetworkErrorException {
        when(mUpdateUsernameHttpEndpointSyncMock.updateUsername(anyString(), anyString()))
                .thenReturn(new UpdateUsernameHttpEndpointSync.EndpointResult(UpdateUsernameHttpEndpointSync.EndpointResultStatus.SUCCESS, USERID, USERNAME));
    }

    private void generalError() throws Exception {
        when(mUpdateUsernameHttpEndpointSyncMock.updateUsername(anyString(), anyString()))
                .thenReturn(new UpdateUsernameHttpEndpointSync.EndpointResult(UpdateUsernameHttpEndpointSync.EndpointResultStatus.GENERAL_ERROR, "", ""));
    }

    private void authError() throws Exception {
        when(mUpdateUsernameHttpEndpointSyncMock.updateUsername(anyString(), anyString()))
                .thenReturn(new UpdateUsernameHttpEndpointSync.EndpointResult(UpdateUsernameHttpEndpointSync.EndpointResultStatus.AUTH_ERROR, "", ""));
    }

    private void serverError() throws Exception {
        when(mUpdateUsernameHttpEndpointSyncMock.updateUsername(anyString(), anyString()))
                .thenReturn(new UpdateUsernameHttpEndpointSync.EndpointResult(UpdateUsernameHttpEndpointSync.EndpointResultStatus.SERVER_ERROR, "", ""));
    }
}