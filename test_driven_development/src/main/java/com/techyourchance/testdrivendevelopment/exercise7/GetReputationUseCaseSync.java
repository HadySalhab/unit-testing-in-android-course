package com.techyourchance.testdrivendevelopment.exercise7;

public interface GetReputationUseCaseSync {
    enum Status{
        SUCCESS,
        FAILURE,
        NETWORK_ERROR
    }
    class UseCaseResult{
        private final Status mStatus;

        private final int mReputation;

        public UseCaseResult(Status status,int reputation){
            mStatus = status;
            mReputation = reputation;
        }

        public Status getStatus() {
            return mStatus;
        }

        public int getReputation() {
            return mReputation;
        }
    }
    UseCaseResult getReputationSync();
}
