package com.thyrocare.models.data;

import android.os.Parcel;
import android.os.Parcelable;

public class ExecutionTrack implements Parcelable {
        int TotalRegistration,SampleCollected,PaymentCollected;

        protected ExecutionTrack(Parcel in) {
            TotalRegistration = in.readInt();
            SampleCollected = in.readInt();
            PaymentCollected = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(TotalRegistration);
            dest.writeInt(SampleCollected);
            dest.writeInt(PaymentCollected);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<ExecutionTrack> CREATOR = new Creator<ExecutionTrack>() {
            @Override
            public ExecutionTrack createFromParcel(Parcel in) {
                return new ExecutionTrack(in);
            }

            @Override
            public ExecutionTrack[] newArray(int size) {
                return new ExecutionTrack[size];
            }
        };
    }