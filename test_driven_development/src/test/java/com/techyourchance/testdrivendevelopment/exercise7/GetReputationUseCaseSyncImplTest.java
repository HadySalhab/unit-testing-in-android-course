package com.techyourchance.testdrivendevelopment.exercise7;

import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetReputationUseCaseSyncImplTest {
    public static final int REPUTATION = 10;
    GetReputationHttpEndpointSync mGetReputationHttpEndpointSyncMock;
    GetReputationUseCaseSyncImpl SUT;

    @Before
    public void setUp() throws Exception {
        mGetReputationHttpEndpointSyncMock = mock(GetReputationHttpEndpointSync.class);
        SUT = new GetReputationUseCaseSyncImpl(mGetReputationHttpEndpointSyncMock);
        success();
    }

    @Test
    //unitOfWork_stateUnderTest_expectedBehavior
    public void getReputation_success_shouldReturnSuccess() {
        //ARRANGE, GIVEN
        //ACT, WHEN
        GetReputationUseCaseSync.UseCaseResult result = SUT.getReputationSync();
        //ASSERT, THEN
        Assert.assertEquals(GetReputationUseCaseSync.Status.SUCCESS, result.getStatus());
    }

    @Test
    public void getReputation_success_shouldReturnReputation() {
        GetReputationUseCaseSync.UseCaseResult result = SUT.getReputationSync();
        Assert.assertEquals(REPUTATION, result.getReputation());
    }


    @Test
    public void getReputation_generalError_shouldReturnFailure() {
        generalError();

        GetReputationUseCaseSync.UseCaseResult result = SUT.getReputationSync();

        Assert.assertEquals(GetReputationUseCaseSync.Status.FAILURE, result.getStatus());

    }

    @Test
    public void getReputation_generalError_shouldReturnZeroReputation() {
        generalError();

        GetReputationUseCaseSync.UseCaseResult result = SUT.getReputationSync();

        Assert.assertEquals(0, result.getReputation());

    }

    @Test
    public void getReputation_networkError_shouldReturnFailure() {
        networkError();

        GetReputationUseCaseSync.UseCaseResult result = SUT.getReputationSync();

        Assert.assertEquals(GetReputationUseCaseSync.Status.FAILURE, result.getStatus());

    }

    @Test
    public void getReputation_networkError_shouldReturnZeroReputation() {
        networkError();

        GetReputationUseCaseSync.UseCaseResult result = SUT.getReputationSync();


        Assert.assertEquals(0, result.getReputation());

    }




    public void networkError(){
        when(mGetReputationHttpEndpointSyncMock.getReputationSync()).thenReturn(
                new GetReputationHttpEndpointSync.EndpointResult(GetReputationHttpEndpointSync.EndpointStatus.NETWORK_ERROR, 0)
        );
    }

    public void success() {
        when(mGetReputationHttpEndpointSyncMock.getReputationSync()).thenReturn(
                new GetReputationHttpEndpointSync.EndpointResult(GetReputationHttpEndpointSync.EndpointStatus.SUCCESS, REPUTATION)
        );
    }

    public void generalError() {
        when(mGetReputationHttpEndpointSyncMock.getReputationSync()).thenReturn(
                new GetReputationHttpEndpointSync.EndpointResult(GetReputationHttpEndpointSync.EndpointStatus.GENERAL_ERROR, 0)
        );
    }
}
