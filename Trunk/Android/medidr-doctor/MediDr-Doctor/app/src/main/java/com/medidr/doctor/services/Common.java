package com.medidr.doctor.services;


public class Common {

    private static Common ourInstance = new Common();

    public static Common getInstance() {
        return ourInstance;
    }

    private Common() {
    }
}
