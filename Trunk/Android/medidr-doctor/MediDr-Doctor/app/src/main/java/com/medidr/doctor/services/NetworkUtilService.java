package com.medidr.doctor.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Sharvee on 5/6/2016.
 */
public class NetworkUtilService {

    private Context mcontext;

    public NetworkUtilService(Context context) {
        this.mcontext = context;
    }

    public boolean checkMobileInternetConn() {
        boolean connected = false;
        //get the connectivity manager object to identify the network state.
        ConnectivityManager connectivityManager = (ConnectivityManager)mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);
        //Check if the manager object is NULL, this check is required. to prevent crashes in few devices.
        if(connectivityManager != null)
        {

            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            connected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();

            //Check Mobile data or Wifi net is present
            if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
            {
                //we are connected to a network
                connected = true;
            }
            else
            {
                connected = false;
            }
            return connected;
        }
        else
        {
            return connected;
        }

    }
}
