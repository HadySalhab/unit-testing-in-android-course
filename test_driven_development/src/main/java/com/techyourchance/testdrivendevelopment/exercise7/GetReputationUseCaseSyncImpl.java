package com.techyourchance.testdrivendevelopment.exercise7;

import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync;

import javax.jws.soap.SOAPBinding;

public class GetReputationUseCaseSyncImpl implements GetReputationUseCaseSync {
    private GetReputationHttpEndpointSync mGetReputationHttpEndpointSync;

    public GetReputationUseCaseSyncImpl(GetReputationHttpEndpointSync getReputationHttpEndpointSync) {
        mGetReputationHttpEndpointSync = getReputationHttpEndpointSync;
    }

    @Override
    public UseCaseResult getReputationSync() {
        GetReputationHttpEndpointSync.EndpointResult result = mGetReputationHttpEndpointSync.getReputationSync();
        if (result.getStatus() == GetReputationHttpEndpointSync.EndpointStatus.GENERAL_ERROR || result.getStatus() == GetReputationHttpEndpointSync.EndpointStatus.NETWORK_ERROR) {
            return new UseCaseResult(Status.FAILURE,0);
        }
        return new UseCaseResult(Status.SUCCESS, result.getReputation());
    }
}
