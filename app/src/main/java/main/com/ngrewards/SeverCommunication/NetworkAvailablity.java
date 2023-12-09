package main.com.ngrewards.SeverCommunication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkAvailablity {

    public NetworkAvailablity() {
        // TODO Auto-generated constructor stub
        super();
    }

    @SuppressLint("MissingPermission")
    public static boolean chkStatus(Context context) {
        // TODO Auto-generated method stub
        try {
            final ConnectivityManager connMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            return connMgr.getActiveNetworkInfo() != null
                    && connMgr.getActiveNetworkInfo().isAvailable()
                    && connMgr.getActiveNetworkInfo().isConnected();
        } catch (Exception e) {

            e.printStackTrace();
        }
        return false;
    }
}
