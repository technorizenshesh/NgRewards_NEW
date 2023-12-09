package main.com.ngrewards.draweractivity;

import static main.com.ngrewards.Utils.Tools.ToolsShowDialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import main.com.ngrewards.R;
import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.Utils.Tools;
import main.com.ngrewards.activity.PreferenceConnector;
import main.com.ngrewards.beanclasses.AddressBean;
import main.com.ngrewards.beanclasses.MemberBean;
import main.com.ngrewards.beanclasses.MemberDetail;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.ExpandableHeightListView;
import main.com.ngrewards.constant.MultipartUtility;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.constant.Myapisession;
import main.com.ngrewards.placeorderclasses.AddShipingAddress;
import main.com.ngrewards.placeorderclasses.UpdateShipingAddress;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_QR_SCAN = 3;
    MySession mySession;
    GenderAdpter genderAdpter;
    ArrayList<String> genderlist, agelist;
    CustomAddresAdp customAddresAdp;
    int count = 0;
    private EditText name_et, username, email_id, phone_number, gender;
    private String age_str = "", name_str = "", username_str = "", email_id_str = "", phone_number_str = "", gender_str = "";
    private RelativeLayout backlay, addaddressdlay;
    private TextView update_tv;
    private ProgressBar progresbar;
    private String user_id = "", ImagePath = "", how_invited_you_name = "", who_invite_str = "", time_zone = "";
    private CircleImageView user_img;
    private Spinner genderspn, agespn;
    private ArrayList<MemberDetail> memberDetailArrayList;
    private Myapisession myapisession;
    private LinearLayout whoinvitelay;
    private AutoCompleteTextView whoinvite;
    private ExpandableHeightListView addresslist;
    private ImageView qrcode;
    private ArrayList<AddressBean> addressBeanArrayList;
    private String fullname;
    private String zipcode;
    private EditText zipcode1;
    private String zipcode_string = "";

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        PreferenceConnector.writeString(ProfileActivity.this, PreferenceConnector.Create_Profile, "");
        PreferenceConnector.writeString(ProfileActivity.this, PreferenceConnector.Status_Facebook, "false");
        PreferenceConnector.writeString(ProfileActivity.this, PreferenceConnector.Profile_com, "sasasasasas");


        Calendar c = Calendar.getInstance();
        TimeZone tz = c.getTimeZone();
        time_zone = tz.getID();

        mySession = new MySession(this);
        myapisession = new Myapisession(this);
        qrcode = findViewById(R.id.qrcode);

        String user_log_data = mySession.getKeyAlldata();
        if (user_log_data == null) {

        } else {

            try {
                JSONObject jsonObject = new JSONObject(user_log_data);
                String message = jsonObject.getString("status");
                if (message.equalsIgnoreCase("1")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                    user_id = jsonObject1.getString("id");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        genderlist = new ArrayList<>();
        agelist = new ArrayList<>();
        genderlist.add(getString(R.string.select_gender));
        genderlist.add(getString(R.string.male));
        genderlist.add(getString(R.string.female));
        genderlist.add(getString(R.string.prefer_not_to_say));
        agelist.add(getString(R.string.select_age));
        agelist.add("18-24");
        agelist.add("25-34");
        agelist.add("35-44");
        agelist.add("45-54");
        agelist.add("55-64");
        agelist.add("65+");

        idinit();
        clickevent();

        new GetProfile().execute();

        getUsername();

        qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, QrCodeActivity.class);
                startActivityForResult(i, REQUEST_CODE_QR_SCAN);
            }
        });

    }

    private void clickevent() {
        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addaddressdlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, AddShipingAddress.class);
                startActivity(i);
            }
        });


        update_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name_str = name_et.getText().toString();
                username_str = username.getText().toString();
                email_id_str = email_id.getText().toString();
                phone_number_str = phone_number.getText().toString();
                how_invited_you_name = whoinvite.getText().toString();
                zipcode_string = zipcode1.getText().toString();

                if (name_str == null || name_str.equalsIgnoreCase("")) {
                    PreferenceConnector.writeString(ProfileActivity.this, PreferenceConnector.UserNAme, name_str);
                    Toast.makeText(ProfileActivity.this, getResources().getString(R.string.enternae), Toast.LENGTH_LONG).show();
                } else if (username_str == null || username_str.equalsIgnoreCase("")) {
                    PreferenceConnector.writeString(ProfileActivity.this, PreferenceConnector.UserNAme123, username_str);
                    Toast.makeText(ProfileActivity.this, getResources().getString(R.string.entervalidusername), Toast.LENGTH_LONG).show();
                } else if (!isEmailValid(email_id_str)) {
                    Toast.makeText(ProfileActivity.this, getResources().getString(R.string.entervalidemailaddress), Toast.LENGTH_LONG).show();
                } else if (email_id_str == null || email_id_str.equalsIgnoreCase("")) {
                    Toast.makeText(ProfileActivity.this, getResources().getString(R.string.enteryemail), Toast.LENGTH_LONG).show();
                } else if (phone_number_str == null || phone_number_str.equalsIgnoreCase("")) {
                    Toast.makeText(ProfileActivity.this, getResources().getString(R.string.entermobile), Toast.LENGTH_LONG).show();
                } else if (zipcode_string == null || zipcode_string.equalsIgnoreCase("")) {
                    Toast.makeText(ProfileActivity.this, getResources().getString(R.string.enter_zip), Toast.LENGTH_LONG).show();

                } else {
                    new UpdateProfile().execute();
                }
            }
        });

        user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                /*Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                cameraIntent.setType("image*//*");
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, 1000);
                }*/
            }
        });
    }

    private void idinit() {

        addaddressdlay = findViewById(R.id.addaddressdlay);
        addresslist = findViewById(R.id.addresslist);
        whoinvite = findViewById(R.id.whoinvite);
        whoinvitelay = findViewById(R.id.whoinvitelay);
        zipcode1 = findViewById(R.id.zipcode);
        progresbar = findViewById(R.id.progresbar);
        genderspn = findViewById(R.id.genderspn);
        agespn = findViewById(R.id.agespn);
        user_img = findViewById(R.id.user_img);
        update_tv = findViewById(R.id.update_tv);
        gender = findViewById(R.id.gender);
        phone_number = findViewById(R.id.phone_number);
        email_id = findViewById(R.id.email_id);
        username = findViewById(R.id.username);
        name_et = findViewById(R.id.name_et);
        backlay = findViewById(R.id.backlay);
        genderAdpter = new GenderAdpter(ProfileActivity.this, genderlist);
        genderspn.setAdapter(genderAdpter);
        genderAdpter = new GenderAdpter(ProfileActivity.this, agelist);
        agespn.setAdapter(genderAdpter);

        genderspn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (genderlist.get(position) != null) {
                    if (genderlist.get(position).equalsIgnoreCase(getString(R.string.select_gender))) {
                    } else {
                        gender_str = genderlist.get(position);
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        agespn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (agelist.get(position) != null) {


                    if (agelist.get(position).equalsIgnoreCase(getString(R.string.select_age))) {
                    } else {
                        age_str = agelist.get(position);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        whoinvite.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (count == 0) {
                    Log.e("FIRST", "KK");
                    ArrayList<MemberDetail> l1 = new ArrayList<>();
                    if (s == null) {

                    } else {
                        MemberDetail memberlist = new MemberDetail();
                        memberlist.setAffiliateName(s.toString());
                        l1.add(memberlist);

                        GeoAutoCompleteAdapter ga = new GeoAutoCompleteAdapter(ProfileActivity.this, l1, "", "");
                        whoinvite.setAdapter(ga);
                        ga.notifyDataSetChanged();

                    }

                }

                count++;

            }
        });

    }

    private void getUsername() {
        progresbar.setVisibility(View.VISIBLE);
        memberDetailArrayList = new ArrayList<>();
        Call<ResponseBody> call = ApiClient.getApiInterface().getMembersusername(user_id, mySession.getValueOf(MySession.CountryId));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progresbar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    try {

                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("UserRef_list >", " >" + responseData);
                        if (object.getString("status").equals("1")) {
                            myapisession.setKeyMemberusername(responseData);
                            MemberBean successData = new Gson().fromJson(responseData, MemberBean.class);
                            memberDetailArrayList.addAll(successData.getResult());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                progresbar.setVisibility(View.GONE);
                Log.e("TAG", t.toString());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("TAG", "onActivityResult:+requestCode )--------" + requestCode);
        Log.e("TAG", "onActivityResult:+resultCode );--------" + resultCode);
        Log.e("TAG", "onActivityResult:+data );--------" + data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    if (Build.VERSION.SDK_INT >= 33) {
                        if (data == null) return;
                        // Get photo picker response for single select.
                        final Uri selectedImage = data.getData();
                        try {
                            assert selectedImage != null;
                            try (final InputStream stream = getContentResolver().openInputStream(selectedImage)) {
                                final Bitmap bitmap = BitmapFactory.decodeStream(stream);
                                user_img.setImageBitmap(bitmap);
                                File tempfile = Tools.persistImage(bitmap, ProfileActivity.this);
                                ImagePath = tempfile.getAbsolutePath();
                                Log.e("ImagePath", "onActivityResult: " + ImagePath);
                            }
                        } catch (IOException e) {
                            ToolsShowDialog(getApplicationContext(), e.getLocalizedMessage());
                        }
                    } else {
                        Uri selectedImage = data.getData();
                        //getPath(selectedImage);
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String FinalPath = cursor.getString(columnIndex);
                        cursor.close();
                        String ImagePath = getPath(selectedImage);
                        decodeFile(ImagePath);
                    }
                    break;

                case 2:
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    String cameraPath = saveToInternalStorage(photo);
                    Log.e("PATH Camera", "" + cameraPath);
                    //  String ImagePath = getPath(selectedImage);
                    decodeFile(cameraPath);
                    break;

                case 3:
                    String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
                    try {
                        String[] arr = result.split(",");
                        whoinvite.setText(arr[1]);
                    } catch (Exception e) {
                        Toast.makeText(this, "Wrong QR Code!!!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    break;
            }
        }
    }

    @SuppressLint("Range")
    public String getPath(Uri uri) {
        String path = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();
        cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        if (cursor.moveToFirst()) {
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            //  Log.e("image_path.===..", "" + path);
        }
        cursor.close();
        return path;
    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String dateToStr = format.format(today);
        ContextWrapper cw = new ContextWrapper(ProfileActivity.this);
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory, "profile_" + dateToStr + ".JPEG");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (myapisession.getKeyAddressdata() == null ||
                myapisession.getKeyAddressdata().equalsIgnoreCase("")) {
            new GetSavedAddress().execute();
        } else {
            try {
                addressBeanArrayList = new ArrayList<>();
                String result = myapisession.getKeyAddressdata();
                JSONObject jsonObject = new JSONObject(result);
                String message = jsonObject.getString("status");
                if (message.equalsIgnoreCase("1")) {
                    myapisession.setKeyAddressdata(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        AddressBean addressBean = new AddressBean();
                        addressBean.setId(jsonObject1.getString("id"));
                        addressBean.setUser_id(jsonObject1.getString("user_id"));
                        addressBean.setFullname(jsonObject1.getString("fullname"));
                        addressBean.setPhone_number(jsonObject1.getString("phone_number"));
                        addressBean.setCountry(jsonObject1.getString("country"));
                        addressBean.setState(jsonObject1.getString("state"));
                        addressBean.setCity(jsonObject1.getString("city"));
                        addressBean.setAddress_1(jsonObject1.getString("address_1"));
                        addressBean.setAddress_2(jsonObject1.getString("address_2"));
                        addressBean.setZipcode(jsonObject1.getString("zipcode"));
                        addressBean.setCreated_date(jsonObject1.getString("created_date"));
                        addressBeanArrayList.add(addressBean);
                    }

                    customAddresAdp = new CustomAddresAdp(ProfileActivity.this, addressBeanArrayList);
                    addresslist.setAdapter(customAddresAdp);
                    customAddresAdp.notifyDataSetChanged();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void decodeFile(String filePath) {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);
        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;
        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }
        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, o2);
        ImagePath = saveToInternalStorage(bitmap);
        Log.e("DECODE PATH", "ff " + ImagePath);
        user_img.setImageBitmap(bitmap);
    }

    private void selectImage() {
        final Dialog dialogSts = new Dialog(ProfileActivity.this, R.style.DialogSlideAnim);
        dialogSts.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSts.setCancelable(false);
        dialogSts.setContentView(R.layout.select_img_lay);
        dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button camera = (Button) dialogSts.findViewById(R.id.camera);
        Button gallary = (Button) dialogSts.findViewById(R.id.gallary);
        TextView cont_find = (TextView) dialogSts.findViewById(R.id.cont_find);
        gallary.setOnClickListener(v -> {
            dialogSts.dismiss();
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, 1);
        });
        camera.setOnClickListener(v -> {
            dialogSts.dismiss();
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, 2);
        });
        cont_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSts.dismiss();
            }
        });
        dialogSts.show();
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
                Log.e("GetProfile Response", ">>>>>>>>>>>>" + response);
                return response;
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();

            } catch (IOException e1) {

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
                        if (jsonObject1.getString("gender") != null) {
                            gender_str = jsonObject1.getString("gender").trim();
                        }
                        if (jsonObject1.getString("age") != null) {
                            age_str = jsonObject1.getString("age").trim();
                        }


                        email_id.setText("" + jsonObject1.getString("email"));
                        name_et.setText("" + jsonObject1.getString("fullname"));
                        username_str = jsonObject1.getString("username");
                        who_invite_str = jsonObject1.getString("affiliate_name");
                        zipcode = jsonObject1.getString("zipcode");
                        whoinvite.setText("" + who_invite_str);

                        zipcode1.setText("" + zipcode);

                        Log.e("who_invite_str>>", who_invite_str);

                        if (who_invite_str != null) {

                            who_invite_str = who_invite_str.replaceAll("(\\r|\\n)", "");
                        }

                        String social_id = jsonObject1.getString("social_id");

                        if (who_invite_str != null && !who_invite_str.equalsIgnoreCase("") && !who_invite_str.equalsIgnoreCase("0") && !who_invite_str.equalsIgnoreCase("null")) {
                            whoinvitelay.setVisibility(View.VISIBLE);
                            whoinvite.setText("" + who_invite_str);
                            whoinvite.setEnabled(false);

                        } else {

                            whoinvitelay.setVisibility(View.VISIBLE);
                        }

                        if (social_id != null && !social_id.equalsIgnoreCase("") && !social_id.equalsIgnoreCase("0") && !social_id.equalsIgnoreCase("null")) {
                            if (who_invite_str != null && !who_invite_str.equalsIgnoreCase("") && !who_invite_str.equalsIgnoreCase("0") && !who_invite_str.equalsIgnoreCase("null")) {
                                whoinvitelay.setVisibility(View.VISIBLE);
                                whoinvite.setText("" + who_invite_str);
                                whoinvite.setEnabled(false);
                            } else {
                                whoinvitelay.setVisibility(View.VISIBLE);
                            }
                        }

                        username.setText("" + jsonObject1.getString("username"));

                        if (username_str == null ||
                                username_str.equalsIgnoreCase("")) {
                            username.setEnabled(true);
                            username.setInputType(InputType.TYPE_CLASS_TEXT
                                    | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                        }

                        phone_number.setText("" + jsonObject1.getString("phone"));
                        String image_url = jsonObject1.getString("member_image");
                        if (image_url != null && !image_url.equalsIgnoreCase("") && !image_url.equalsIgnoreCase(BaseUrl.image_baseurl)) {
                            Glide.with(ProfileActivity.this).load(image_url).placeholder(R.drawable.user_propf).into(user_img);
                        }

                        if (agelist != null && !agelist.isEmpty()) {
                            for (int i = 0; i < agelist.size(); i++) {
                                if (age_str != null && !age_str.equalsIgnoreCase("")) {
                                    if (age_str.equalsIgnoreCase(agelist.get(i))) {
                                        agespn.setSelection(i);
                                    }
                                }
                            }
                        }

                        if (genderlist != null && !genderlist.isEmpty()) {
                            for (int i = 0; i < genderlist.size(); i++) {
                                if (gender_str != null && !gender_str.equalsIgnoreCase("")) {
                                    if (gender_str.equalsIgnoreCase(genderlist.get(i))) {
                                        genderspn.setSelection(i);
                                    }
                                }
                            }

                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    public class UpdateProfile extends AsyncTask<String, String, String> {

        String Jsondata;
        private boolean checkdata = false;

        protected void onPreExecute() {
            try {
                super.onPreExecute();
                progresbar.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                e.printStackTrace();
                checkdata = true;

            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String charset = "UTF-8";
            String requestURL = BaseUrl.baseurl + "update_member_profile.php?";
            Log.e("requestURL >>", requestURL + "member_id=" + user_id + "&email=" + email_id_str + "&phone=" + phone_number_str + "&affiliate_name=" + username_str + "&gender=" + gender_str + "&fullname=" + name_str + "&age=" + age_str + "&timezone=" + time_zone + "&how_invited_you_name=" + how_invited_you_name + "&how_invited_you=" + who_invite_str.trim());
            try {
                MultipartUtility multipart = new MultipartUtility(requestURL, charset);
                multipart.addFormField("member_id", user_id);
                multipart.addFormField("email", email_id_str);
                multipart.addFormField("phone", phone_number_str);
                multipart.addFormField("affiliate_name", username_str);
                multipart.addFormField("gender", gender_str);
                multipart.addFormField("username", username_str);
                multipart.addFormField("fullname", name_str);
                multipart.addFormField("age", age_str);
                multipart.addFormField("timezone", time_zone);
                multipart.addFormField("zip_code", zipcode_string);
                multipart.addFormField("how_invited_you_name", how_invited_you_name);

                if (who_invite_str != null) {
                    who_invite_str = who_invite_str.replaceAll("(\\r|\\n)", "");
                }

                multipart.addFormField("how_invited_you", who_invite_str.trim());

                if (ImagePath == null || ImagePath.equalsIgnoreCase("")) {

                } else {
                    File ImageFile = new File(ImagePath);
                    multipart.addFilePart("member_image", ImageFile);
                }
                List<String> response = multipart.finish();

                for (String line : response) {
                    Jsondata = line;
                }

                JSONObject object = new JSONObject(Jsondata);
                return Jsondata;

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progresbar.setVisibility(View.GONE);
            Log.e("Update Member Pro", " >> " + result);

            if (result == null) {

            } else if (result.isEmpty()) {

            } else {

                JSONObject jsonObject = null;
                try {

                    jsonObject = new JSONObject(result);
                    String result_chk = jsonObject.getString("status");

                    if (result_chk.equalsIgnoreCase("1")) {

                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                        if (jsonObject1.getString("gender") != null) {
                            gender_str = jsonObject1.getString("gender").trim();
                        }

                        if (jsonObject1.getString("age") != null) {
                            age_str = jsonObject1.getString("age").trim();
                        }

                        fullname = jsonObject1.getString("fullname");
                        username_str = jsonObject1.getString("affiliate_name");

                        String image_url = jsonObject1.getString("member_image");
                        if (image_url != null && !image_url.equalsIgnoreCase("") && !image_url.equalsIgnoreCase(BaseUrl.image_baseurl)) {
                            Glide.with(ProfileActivity.this).load(image_url).placeholder(R.drawable.user_propf).into(user_img);
                        }

                        mySession.setlogindata(result);

                        PreferenceConnector.writeString(ProfileActivity.this, PreferenceConnector.Create_Profile, "create_profile");
                        PreferenceConnector.writeString(ProfileActivity.this, PreferenceConnector.UserNAme, fullname);
                        PreferenceConnector.writeString(ProfileActivity.this, PreferenceConnector.UserNAme1, username_str);

                        finish();

                        // Toast.makeText(ProfileActivity.this, getResources().getString(R.string.updateprosuccess), Toast.LENGTH_LONG).show();

                    } else if (jsonObject.getString("message").equalsIgnoreCase("User Name Already Exists")) {
                        username.setText("");
                        Toast.makeText(ProfileActivity.this, getResources().getString(R.string.usernamealredayused), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }

        }

    }

    public class GenderAdpter extends BaseAdapter {

        private final ArrayList<String> values;
        Context context;
        LayoutInflater inflter;

        public GenderAdpter(Context applicationContext, ArrayList<String> values) {
            this.context = applicationContext;
            this.values = values;

            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {

            return values == null ? 0 : values.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflter.inflate(R.layout.custom_spin, null);

            TextView names = (TextView) view.findViewById(R.id.name_tv);
            names.setText(values.get(i));
            return view;
        }
    }

    class GeoAutoCompleteAdapter extends BaseAdapter implements Filterable {

        private final Activity context;
        private final LayoutInflater layoutInflater;
        private ArrayList<MemberDetail> l2 = new ArrayList<>();

        public GeoAutoCompleteAdapter(Activity context, ArrayList<MemberDetail> l2, String lat, String lon) {
            this.context = context;
            this.l2 = l2;
            layoutInflater = LayoutInflater.from(context);
            Log.e("FIRST", "CONS");
        }

        @Override
        public int getCount() {

            return l2 == null ? 0 : l2.size();
        }

        @Override
        public Object getItem(int i) {
            return l2.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {

            view = layoutInflater.inflate(R.layout.geo_search_result, viewGroup, false);
            TextView geo_search_result_text = (TextView) view.findViewById(R.id.geo_search_result_text);
            try {
                geo_search_result_text.setText(l2.get(i).getAffiliateName());
                geo_search_result_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);

                        // checkic.setImageResource(R.drawable.check);

                        if (l2 != null && !l2.isEmpty()) {
                            ArrayList<MemberDetail> test = new ArrayList<>();
                            test.add(l2.get(i));

                            if (test != null) {
                                whoinvite.setText(test.get(0).getAffiliateName());
                                who_invite_str = test.get(0).getAffiliateNumber();
                                whoinvite.dismissDropDown();

                                Log.e("who_invite_str", who_invite_str);

                            }

                        }

                    }
                });

            } catch (Exception e) {

            }

            return view;
        }

        @Override
        public Filter getFilter() {

            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {


                        if (constraint.length() == 0) {
                        } else {

                            l2.clear();
                            for (MemberDetail wp : memberDetailArrayList) {
                                if (wp.getAffiliateName().toLowerCase().startsWith((String) constraint))//.toLowerCase(Locale.getDefault())
                                {
                                    Log.e("TRUE", " >> FILTER" + wp.getAffiliateName());
                                    l2.add(wp);
                                }
                            }
                        }
                        // Assign the data to the FilterResults
                        filterResults.values = l2;
                        filterResults.count = l2.size();
                    }

                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    if (results != null && results.count != 0) {
                        l2 = (ArrayList<MemberDetail>) results.values;
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };

            return filter;

        }

    }

    private class GetSavedAddress extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progresbar.setVisibility(View.VISIBLE);
            addressBeanArrayList = new ArrayList<>();
            try {
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                String postReceiverUrl = BaseUrl.baseurl + "get_address_lists.php?";
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("user_id", user_id);
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
                Log.e("Saved Address", ">>>>>>>>>>>>" + response);
                return response;
            } catch (UnsupportedEncodingException e1) {

                e1.printStackTrace();
            } catch (IOException e1) {

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
                        myapisession.setKeyAddressdata(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            AddressBean addressBean = new AddressBean();
                            addressBean.setId(jsonObject1.getString("id"));
                            addressBean.setUser_id(jsonObject1.getString("user_id"));
                            addressBean.setFullname(jsonObject1.getString("fullname"));
                            addressBean.setPhone_number(jsonObject1.getString("phone_number"));
                            addressBean.setCountry(jsonObject1.getString("country"));
                            addressBean.setState(jsonObject1.getString("state"));
                            addressBean.setCity(jsonObject1.getString("city"));
                            addressBean.setAddress_1(jsonObject1.getString("address_1"));
                            addressBean.setAddress_2(jsonObject1.getString("address_2"));
                            addressBean.setZipcode(jsonObject1.getString("zipcode"));
                            addressBean.setCreated_date(jsonObject1.getString("created_date"));
                            addressBeanArrayList.add(addressBean);
                        }

                        customAddresAdp = new CustomAddresAdp(ProfileActivity.this, addressBeanArrayList);
                        addresslist.setAdapter(customAddresAdp);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    public class CustomAddresAdp extends BaseAdapter {
        Context context;
        ArrayList<AddressBean> addressBeanArrayList;
        private LayoutInflater inflater = null;

        public CustomAddresAdp(Context contexts, ArrayList<AddressBean> addressBeanArrayList) {
            this.context = contexts;
            this.addressBeanArrayList = addressBeanArrayList;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return addressBeanArrayList == null ? 0 : addressBeanArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final Holder holder;
            holder = new Holder();
            View rowView;

            rowView = inflater.inflate(R.layout.custom_profile_address_list, null);
            ImageView edit_but = rowView.findViewById(R.id.edit_but);
            RadioButton addsel_rdb = rowView.findViewById(R.id.addsel_rdb);
            TextView fullname = rowView.findViewById(R.id.fullname);
            TextView address1 = rowView.findViewById(R.id.address1);
            TextView address2 = rowView.findViewById(R.id.address2);
            TextView phonetv = rowView.findViewById(R.id.phonetv);

            fullname.setText("" + addressBeanArrayList.get(position).getFullname());
            address1.setText("" + addressBeanArrayList.get(position).getAddress_1());
            address2.setText("" + addressBeanArrayList.get(position).getAddress_2());
            phonetv.setText("" + addressBeanArrayList.get(position).getPhone_number());

            edit_but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ProfileActivity.this, UpdateShipingAddress.class);
                    i.putExtra("addid", addressBeanArrayList.get(position).getId());
                    i.putExtra("fullname_str", addressBeanArrayList.get(position).getFullname());
                    i.putExtra("address1_str", addressBeanArrayList.get(position).getAddress_1());
                    i.putExtra("address2_str", addressBeanArrayList.get(position).getAddress_2());
                    i.putExtra("state_str", addressBeanArrayList.get(position).getState());
                    i.putExtra("city_str", addressBeanArrayList.get(position).getCity());
                    i.putExtra("countrytv_str", addressBeanArrayList.get(position).getCountry());
                    i.putExtra("phonetv_str", addressBeanArrayList.get(position).getPhone_number());
                    i.putExtra("zippcode_str", addressBeanArrayList.get(position).getZipcode());
                    startActivity(i);

                }
            });

            // cardnumber.setText(""+getLastfour(cardBeanArrayList.get(position).getCard_number()));
            return rowView;
        }

        public class Holder {

        }

    }


}