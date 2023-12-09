package main.com.ngrewards.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import main.com.ngrewards.R;
import main.com.ngrewards.androidmigx.MainTabActivity;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.marchant.merchantbottum.MerchantBottumAct;

/**
 * Created by technorizen on 7/8/18.
 */

@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {


    private final Context context;
    private final TextView textView;
    private final String logintype;
    private MySession mySession;


    // Constructor
    public FingerprintHandler(Context mContext, TextView textView, String merchant) {
        context = mContext;
        this.textView = textView;
        logintype = merchant;

    }
/*
    public FingerprintHandler(Context mContext, TextView textView, String merchant) {
        context = mContext;
        this.textView = textView;
        logintype = merchant;

    }
*/


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            cancellationSignal = new CancellationSignal();
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }


    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        this.update("Fingerprint Authentication error\n" + errString, false);
    }


    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        this.update("Fingerprint Authentication help\n" + helpString, false);
    }


    @Override
    public void onAuthenticationFailed() {
        this.update("Fingerprint Authentication failed.", false);
    }


    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        Log.e("Finger result ", ">" + result.getCryptoObject().getSignature());
        Log.e("Finger result ", ">" + result.getCryptoObject().getCipher());
        Toast.makeText(context, context.getString(R.string.identified), Toast.LENGTH_LONG).show();
        String user_type = "", status_touchid = "";
        mySession = new MySession(context);
        String user_log_data = mySession.getKeyAlldata();
        Log.e("USER DATA", ">> " + user_log_data);
        if (user_log_data == null) {
            Toast.makeText(context, context.getResources().getString(R.string.firsttimeneed), Toast.LENGTH_LONG).show();

        } else {
            try {
                JSONObject jsonObject = new JSONObject(user_log_data);
                String message = jsonObject.getString("status");
                if (message.equalsIgnoreCase("1")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                    user_type = jsonObject1.getString("user_type");
                    status_touchid = jsonObject1.getString("touch_status");
                    if (logintype.equalsIgnoreCase(user_type)) {
                        if (status_touchid.equalsIgnoreCase("Yes")) {
                            mySession.signinusers(true);
                            if (user_type.equalsIgnoreCase("Merchant")) {
                                Intent i = new Intent(context, MerchantBottumAct.class);
                                context.startActivity(i);
                            } else {
                                Intent i = new Intent(context, MainTabActivity.class);
                                context.startActivity(i);
                            }

                        } else {
                            Toast.makeText(context, context.getResources().getString(R.string.touchiddisabled), Toast.LENGTH_LONG).show();
                        }
                    } else if (logintype.equalsIgnoreCase("Payment")) {
                        if (status_touchid.equalsIgnoreCase("Yes")) {
                            Intent i = new Intent(context, ManualActivity.class);
                            context.startActivity(i);
                        }

                    } else {
                        Toast.makeText(context, context.getResources().getString(R.string.touchiddisabled), Toast.LENGTH_LONG).show();
                    }

                }
            } catch (JSONException ee) {
                ee.printStackTrace();
                Toast.makeText(context, context.getResources().getString(R.string.touchiddisabled), Toast.LENGTH_LONG).show();

            }
        }

        this.update("Fingerprint Authentication succeeded.", true);
    }


    public void update(String e, Boolean success) {

        //TextView textView = (TextView) ((Activity)context).findViewById(R.id.errorText);
        textView.setText(e);
        if (success) {
            textView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));


        }

    }
}