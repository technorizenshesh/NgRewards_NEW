package main.com.ngrewards.bottumtab;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import main.com.ngrewards.R;
import main.com.ngrewards.activity.PreferenceConnector;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySession;

public class InviteActMain extends Fragment {

    public final static int QRcodeWidth = 500;
    FrameLayout contentFrameLayout;
    MySession mySession;
    View root;
    private TextView invitefriendsbusiness, invitefriends, usernametv;
    private String username;
    private Bitmap bitmap;
    private ImageView myqrview;
    private String craete_profile;
    private String affiliate_number;
    private String how_invited_you;
    private String username_s;
    private String user_id;
    private ProgressBar progresbar;
    private String invite_str, invite_str2;
    private String id;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.activity_invite_act_main, container, false);
        mySession = new MySession(requireActivity());
        myqrview = root.findViewById(R.id.myqrview);

        progresbar = (ProgressBar) root.findViewById(R.id.progresbar);

        new GetProfile().execute();

        String user_log_data = mySession.getKeyAlldata();
        Log.e("User Data ", "> " + user_log_data);

        Log.e("User_Data", "> " + user_log_data);

        if (user_log_data == null) {

        } else {

            try {
                JSONObject jsonObject = new JSONObject(user_log_data);
                String message = jsonObject.getString("status");
                if (message.equalsIgnoreCase("1")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("result");

                    Log.e("Json_Result_Test>>>", "" + jsonObject1);

                    username = jsonObject1.getString("fullname");
                    affiliate_number = jsonObject1.getString("affiliate_number");
                    how_invited_you = jsonObject1.getString("how_invited_you");
                    username_s = jsonObject1.getString("username");
                    user_id = jsonObject1.getString("id");

                    if (username == null || username.equalsIgnoreCase("")) {
                        username = jsonObject1.getString("fullname");
                    }

                    try {
                        JSONObject jsonObject12 = new JSONObject();
                        try {
                            jsonObject12.put("Member", "" + jsonObject1.getString("affiliate_name"));
                            jsonObject12.put("affiliate_number", "" + jsonObject1.getString("affiliate_number"));
                            jsonObject12.put("fullname", "" + jsonObject1.getString("fullname"));
                            jsonObject12.put("id", "" + jsonObject1.getString("id"));
                            //   String s = "Member," + jsonObject1.getString("affiliate_name") + "," + jsonObject1.getString("fullname") + "," + jsonObject1.getString("affiliate_number") + "," + jsonObject1.getString("id");
                            String s = "Member," + jsonObject1.getString("username") + "," + jsonObject1.getString("fullname") + "," + jsonObject1.getString("affiliate_number") + "," + jsonObject1.getString("id");
                            //bitmap = TextToImageEncode(jsonObject12.toString());
                            bitmap = TextToImageEncode(s);
                            myqrview.setImageBitmap(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        idinits();
        idinitui1();
        return root;
    }

    private void idinitui1() {
        craete_profile = PreferenceConnector.readString(requireActivity(), PreferenceConnector.Create_Profile, "");
        if (!craete_profile.equals("craete_profile")) {
            //dialogSts.dismiss();
        }
    }

    private void idinits() {

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(Value, BarcodeFormat.QR_CODE, QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }

        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black) : getResources().getColor(R.color.white);
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    private class GetProfile extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progresbar.setVisibility(View.VISIBLE);

            try {
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                String postReceiverUrl = BaseUrl.baseurl + "member_profile.php?";
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("member_id", user_id);

                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }

                String urlParameters = postData.toString();
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(urlParameters);
                writer.flush();
                String response = "";
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    response += line;
                }

                writer.close();
                reader.close();
                Log.e("GetProfile test!!!", ">>>>>>>>>>>>" + response);
                return response;
            } catch (Exception e1) {

                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progresbar.setVisibility(View.GONE);
            if (result == null) {
            } else if (result.isEmpty()) {

            } else {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String message = jsonObject.getString("status");
                    if (message.equalsIgnoreCase("1")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                        username = jsonObject1.getString("username");
                        id = jsonObject1.getString("id");
                        /* affiliate_number1 = jsonObject1.getString("affiliate_number");*/


                        usernametv = root.findViewById(R.id.usernametv);
                        usernametv.setText("@" + username);
                        invite_str = "https://myngrewards.com/signup.php?affiliate_name=" + username + "&affiliate_no=" + id + "&how_invited_you=" + affiliate_number + "&country=" + mySession.getValueOf(MySession.CountryId) + "&source=app";
                        invite_str2 = "https://myngrewards.com/signup-merchant.php?affiliate_name=" + username + "&affiliate_no=" + id + "&how_invited_you=" + affiliate_number + "&country=" + mySession.getValueOf(MySession.CountryId) + "&source=app";
                        /*https://myngrewards.com/signup-merchant.php?affiliate_name=Ios&affiliate_no=287&how_invited_you=&country=101&source=app*/
                        Log.e("id>>", id);
                        Log.e("afflited_no>>", affiliate_number);

                        Log.e("invite_str>>", invite_str);

                        invitefriends = root.findViewById(R.id.invitefriends);
                        invitefriendsbusiness = root.findViewById(R.id.invitefriendsbusiness);

                        invitefriends.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent sendIntent = new Intent();
                                sendIntent.setAction(Intent.ACTION_SEND);
                                sendIntent.putExtra(Intent.EXTRA_TEXT, invite_str);
                                sendIntent.setType("text/plain");
                                startActivity(sendIntent);
                            }
                        });
                        invitefriendsbusiness.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.e("TAG", "onClick:---- " + invite_str2);
                                Intent sendIntent = new Intent();
                                sendIntent.setAction(Intent.ACTION_SEND);
                                sendIntent.putExtra(Intent.EXTRA_TEXT, invite_str2);
                                sendIntent.setType("text/plain");
                                startActivity(sendIntent);
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
    }
}
