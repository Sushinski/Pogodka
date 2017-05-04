package com.sushinski.pogodka.DAL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

abstract class BaseManager {
    final Context mContext;

    BaseManager(Context context){
        mContext = context;
    }

    boolean isOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
