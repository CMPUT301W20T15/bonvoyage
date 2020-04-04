package com.example.bonvoyage;

import android.os.Parcel;
import android.os.Parcelable;

public class Driver extends User implements Parcelable {

    public Driver(String firstname, String lastname, String email, String phonenumber, String password,
                  float wallet){
        super(firstname, lastname, email, phonenumber, password, wallet);
    }

    public Driver(){
        super();
    }


    private Driver(Parcel in){
        super(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Driver> CREATOR = new Parcelable.Creator<Driver>() {
        @Override
        public Driver createFromParcel(Parcel in) {
            return new Driver(in);
        }

        @Override
        public Driver[] newArray(int size) {
            return new Driver[size];
        }
    };
}
