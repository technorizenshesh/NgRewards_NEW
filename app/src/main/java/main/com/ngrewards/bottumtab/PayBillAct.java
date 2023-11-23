package main.com.ngrewards.bottumtab;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import main.com.ngrewards.QrCodeActivity;
import main.com.ngrewards.R;
import main.com.ngrewards.activity.ManualActivity;
import main.com.ngrewards.activity.PreferenceConnector;
import main.com.ngrewards.draweractivity.BaseActivity;

public class PayBillAct extends BaseActivity {
    FrameLayout contentFrameLayout;
    private TextView scan_qr_tv,manual_tv;
    private static final int REQUEST_CODE_QR_SCAN = 101;
    private String craete_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        contentFrameLayout = (FrameLayout) findViewById(R.id.contentFrame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_pay_bill, contentFrameLayout);
        idintss();
        clickevent();
    }

    private void clickevent() {
        scan_qr_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PayBillAct.this,QrCodeActivity.class);
                startActivityForResult( i,REQUEST_CODE_QR_SCAN);
               /* Intent i = new Intent(PayBillAct.this, ScanQrCodeAct.class);
                startActivity(i);*/
            }
        });

        manual_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PayBillAct.this, ManualActivity.class);
                i.putExtra("type","paybill");
                startActivity(i);
            }
        });
    }

    private void idintss() {
        idinitui1();

        scan_qr_tv = findViewById(R.id.scan_qr_tv);
        manual_tv = findViewById(R.id.manual_tv);
    }

    private void idinitui1() {

        craete_profile =  PreferenceConnector.readString(PayBillAct.this, PreferenceConnector.Create_Profile,"");

        if(!craete_profile.equals("craete_profile")){

            dialogSts.dismiss();
            Toast.makeText(getApplicationContext(),"success!!!",Toast.LENGTH_SHORT).show();

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode != Activity.RESULT_OK)
        {
            Log.d("BAD RESULT","COULD NOT GET A GOOD RESULT.");
            if(data==null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if( result!=null)
            {
                AlertDialog alertDialog = new AlertDialog.Builder(PayBillAct.this).create();
                alertDialog.setTitle("Scan Error");
                alertDialog.setMessage("QR Code could not be scanned");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            return;

        }
        if(requestCode == REQUEST_CODE_QR_SCAN)
        {
            if(data==null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
            Log.d("SCAR RESULT","Have scan result in your app activity :"+ result);
            AlertDialog alertDialog = new AlertDialog.Builder(PayBillAct.this).create();
            alertDialog.setTitle("Scan result");
            alertDialog.setMessage(result);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

        }
    }


}
