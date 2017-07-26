package com.sushinski.pogodka.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import com.sushinski.pogodka.interfaces.INetworkStateChangeListener;

/**
 * Receiver for handle device network status changes
 */
public class NetworkStateReceiver extends BroadcastReceiver {
    private INetworkStateChangeListener mListener;
    private boolean mIsOnline = true;

    public NetworkStateReceiver(@NonNull INetworkStateChangeListener listener){
        mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean b_online = isGoingOnline(context);
        // handling changes only from offline mode to online
        if(!mIsOnline && b_online){
            mListener.onNetworkStateChange();
        }
        mIsOnline = b_online;
    }

    /**
     * checks if device in online mode
     * @param context Actual context
     * @return True, if online
     */
    private boolean isGoingOnline(Context context){
        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}
