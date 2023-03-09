package main.com.ngrewards.SeverCommunication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkAvailablity {

@SuppressLint("MissingPermission")
public static boolean chkStatus(Context context) {
	// TODO Auto-generated method stub
	try {
		final ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connMgr.getActiveNetworkInfo() != null
				&& connMgr.getActiveNetworkInfo().isAvailable()
				&& connMgr.getActiveNetworkInfo().isConnected()) {

			return true;
		} else {

			return false;
		}
	} catch (Exception e) {

		e.printStackTrace();
	}
	return false;
}

	public NetworkAvailablity() {
		// TODO Auto-generated constructor stub
		super();
	}
}
