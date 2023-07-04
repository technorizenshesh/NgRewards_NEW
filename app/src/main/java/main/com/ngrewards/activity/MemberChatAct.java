package main.com.ngrewards.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.extras.Base64;
import de.hdodenhof.circleimageview.CircleImageView;
import in.gauriinfotech.commons.Commons;
import main.com.ngrewards.R;
import main.com.ngrewards.activity.app.Config;
import main.com.ngrewards.activity.app.NotificationUtils;
import main.com.ngrewards.beanclasses.ChatBeanMain;
import main.com.ngrewards.beanclasses.ConverSession;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.ExpandableHeightListView;
import main.com.ngrewards.constant.GetFilePathFromDevice;
import main.com.ngrewards.constant.MultipartUtility;
import main.com.ngrewards.constant.MySession;

public class MemberChatAct extends AppCompatActivity {
    ConversessionAdapter conversessionAdapter;
    private ListView chat_list;
    private RelativeLayout backlay;
    BroadcastReceiver mRegistrationBroadcastReceiver;
    public static boolean isInFront = false;
    ScheduledExecutorService scheduleTaskExecutor;
    int beforelength = 0;
    int mainbeforelength = 0;
    int beforelength1 = 0;
    private ArrayList<ChatBeanMain> converSessionArrayList;
    MySession mySession;
    private final String receiver_img = "";
    private final String tag = "Test";
    private String VideoPath = "";
    private String date_time_show = "";
    private String FilePath = "";
    private String ThumbnailPath = "";
    private String user_id = "";
    private String receiver_type = "";
    private String receiver_name = "";
    private String receiver_fullname = "";
    private String receiver_id = "";
    private String image_url = "";
    private String time_zone = "";
    private String type = "";
    private String messagetext = "";
    private String date_time = "";
    private TextView send_tv, username_tv, name_tv;
    private EditText message_et;
    private ProgressBar prgressbar;
    private ImageView camera_img, video_but;
    private static final int FILE_SELECT_CODE = 0;
    File file_dff;
    private String ImagePath = "";
    ChatMainAdapter chatMainAdapter;
    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;
    private SwipeRefreshLayout swipeToRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mer_chat);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null && !bundle.isEmpty()) {
            receiver_id = bundle.getString("receiver_id");
            type = bundle.getString("type");
            type = type.trim();
           // receiver_img = bundle.getString("receiver_img");
            receiver_type = bundle.getString("receiver_type");
            receiver_name = bundle.getString("receiver_name");
            receiver_fullname = bundle.getString("receiver_fullname");
            Log.e("receiver_fullname >> ", ">> " + receiver_fullname);
            Log.e("receiver_id >> ", ">> " + receiver_id);

        }

        mySession = new MySession(this);
        String user_log_data = mySession.getKeyAlldata();
        if (user_log_data == null) {

        } else {
            try {
                JSONObject jsonObject = new JSONObject(user_log_data);
                String message = jsonObject.getString("status");
                if (message.equalsIgnoreCase("1")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                    user_id = jsonObject1.getString("id");
                    if (type.equalsIgnoreCase("Member")) {
                        image_url = jsonObject1.getString("member_image");
                    } else {
                        image_url = jsonObject1.getString("merchant_image");
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        idint();
        Calendar c = Calendar.getInstance();
        TimeZone tz = c.getTimeZone();
        time_zone = tz.getID();


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    String message = intent.getStringExtra("message");
                    Log.e("Push Chat: ", "" + message);
                    JSONObject data = null;
                    try {
                        data = new JSONObject(message);
                        String keyMessage = data.getString("key").trim();
                        if (keyMessage.equalsIgnoreCase("You have a new message")) {
                            Log.e("Push Chat Come: ", "True");

                            new MyConverSession().execute();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        clickevent();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        scheduleTaskExecutor.shutdown();
        isInFront = false;
    }

    @Override
    public void onResume() {
        isInFront = true;

        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
        NotificationUtils.clearNotifications(getApplicationContext());

        scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                new MyConverSession().execute();
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    private void clickevent() {
        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        video_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messagetext = message_et.getText().toString();

                if (messagetext == null || messagetext.equalsIgnoreCase("")) {
                    selectFiles();

                   /* Intent intent = new Intent();
                    intent.setType("image*//*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);//
                    startActivityForResult(Intent.createChooser(intent, "Select File"), 1);*/
                } else {
                    Toast.makeText(MemberChatAct.this, getResources().getString(R.string.pleassend), Toast.LENGTH_LONG).show();
                }
               /* Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                intent.setType("video");
                startActivityForResult(intent, 3);*/
            }
        });

        camera_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messagetext = message_et.getText().toString();
                if (messagetext == null || messagetext.equalsIgnoreCase("")) {

                    selectImage();
                } else {
                    Toast.makeText(MemberChatAct.this, getResources().getString(R.string.pleassend), Toast.LENGTH_LONG).show();
                }
            }
        });


        send_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                messagetext = message_et.getText().toString();

                Log.e("messagetext1", messagetext);

                String ps = "techPass";
                // messagetext = toBase64(messagetext);
                Log.e("messagetext baseStr", ">> " + messagetext);

                if (messagetext == null || messagetext.equalsIgnoreCase("")) {

                } else {
                    Date today = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                    SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    date_time = format2.format(today);
                    date_time_show = format.format(today);
                    System.out.println("CURRENT " + date_time);
                    // prosts = false;
                    new SendMessage().execute();
                }

            }
        });
    }

    public static String toBase64(String message) {
        byte[] data;
        data = message.getBytes(StandardCharsets.UTF_8);
        String base64Sms = Base64.encodeToString(data, Base64.DEFAULT);
        return base64Sms;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        scheduleTaskExecutor.shutdown();
        finish();
    }

    private void idint() {
        video_but = findViewById(R.id.video_but);
        name_tv = findViewById(R.id.name_tv);
        camera_img = findViewById(R.id.camera_img);
        username_tv = findViewById(R.id.username_tv);
        username_tv.setText("@" + receiver_name);
        name_tv.setText("" + receiver_fullname);
        prgressbar = findViewById(R.id.prgressbar);
        message_et = findViewById(R.id.message_et);
        send_tv = findViewById(R.id.send_tv);
        backlay = findViewById(R.id.backlay);
        chat_list = findViewById(R.id.chat_list);

        swipeToRefresh = findViewById(R.id.swipeToRefresh);

        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new MyConverSession().execute();

            }
        });


    }

    public class ConversessionAdapter extends BaseAdapter {
        Context context;
        private LayoutInflater inflater = null;
        ArrayList<ConverSession> converSessionArrayList;

        public ConversessionAdapter(Activity chatActivity, ArrayList<ConverSession> converSessionArrayList) {
            // TODO Auto-generated constructor stub
            this.context = chatActivity;
            this.converSessionArrayList = converSessionArrayList;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            // return  4;
            return converSessionArrayList == null ? 0 : converSessionArrayList.size();
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

        public class Holder {
            TextView receivermessage, sendermessage;
            ImageView prof_mess_img;
            LinearLayout layout1, layout2;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder = new Holder();
            View rowView;
            // LinearLayout mylayout;
            RelativeLayout otheruserlay, mylayout, my_video_lay, other_video_lay, myfilelay, otherfilelay;
            ImageView otherdownload, myimage, othersendimgimage, myvideo_thumb, othervideo_thumb;
            TextView mymessagetime, filename, otherfilename, mymessage, mydate, myname, othermessagetime, othermsg, otherdate, otherusername;
            final CircleImageView otherimage, otherimage_chat, myimage_chat, myplaceholder, otherplaceholder;
            CircleImageView my_profile;
            final ProgressBar my_chat_img_progress, otehr_chat_img_progress;
            rowView = inflater.inflate(R.layout.chat_item_newlay, null);

            if (rowView != null) {
                myfilelay = rowView.findViewById(R.id.myfilelay);
                mymessagetime = rowView.findViewById(R.id.mymessagetime);
                filename = rowView.findViewById(R.id.filename);
                mylayout = rowView.findViewById(R.id.mylayout);
                mymessage = rowView.findViewById(R.id.mymessage);
                myimage = rowView.findViewById(R.id.myimage);
                myvideo_thumb = rowView.findViewById(R.id.myvideo_thumb);
                my_video_lay = rowView.findViewById(R.id.my_video_lay);

                othermessagetime = rowView.findViewById(R.id.othermessagetime);
                otherfilename = rowView.findViewById(R.id.otherfilename);
                otherdownload = rowView.findViewById(R.id.otherdownload);
                otherfilelay = rowView.findViewById(R.id.otherfilelay);
                othersendimgimage = rowView.findViewById(R.id.othersendimgimage);
                other_video_lay = rowView.findViewById(R.id.other_video_lay);
                othervideo_thumb = rowView.findViewById(R.id.othervideo_thumb);
                my_profile = rowView.findViewById(R.id.my_profile);
                otheruserlay = rowView.findViewById(R.id.otheruserlay);
                othermsg = rowView.findViewById(R.id.othermsg);
                otherimage = rowView.findViewById(R.id.otherimage);

            } else {
                rowView = inflater.inflate(R.layout.chat_item_newlay, null);
                filename = rowView.findViewById(R.id.filename);
                mymessagetime = rowView.findViewById(R.id.mymessagetime);
                myfilelay = rowView.findViewById(R.id.myfilelay);
                mylayout = rowView.findViewById(R.id.mylayout);
                mymessage = rowView.findViewById(R.id.mymessage);
                myimage = rowView.findViewById(R.id.myimage);
                myvideo_thumb = rowView.findViewById(R.id.myvideo_thumb);
                my_video_lay = rowView.findViewById(R.id.my_video_lay);


                othermessagetime = rowView.findViewById(R.id.othermessagetime);
                otherdownload = rowView.findViewById(R.id.otherdownload);
                otherfilename = rowView.findViewById(R.id.otherfilename);
                otherfilelay = rowView.findViewById(R.id.otherfilelay);
                other_video_lay = rowView.findViewById(R.id.other_video_lay);
                othervideo_thumb = rowView.findViewById(R.id.othervideo_thumb);
                otherimage = rowView.findViewById(R.id.otherimage);
                othersendimgimage = rowView.findViewById(R.id.othersendimgimage);
                my_profile = rowView.findViewById(R.id.my_profile);
                otheruserlay = rowView.findViewById(R.id.otheruserlay);
                othermsg = rowView.findViewById(R.id.othermsg);

            }

            if (user_id.equalsIgnoreCase(converSessionArrayList.get(position).getSenderid())) {


              //  Toast.makeText(context, "sssss!!!!", Toast.LENGTH_SHORT).show();

                String msgtype = converSessionArrayList.get(position).getMsg_type();
                mylayout.setVisibility(View.VISIBLE);
                otheruserlay.setVisibility(View.GONE);
                mymessagetime.setText("" + converSessionArrayList.get(position).getDatetime());
                if (converSessionArrayList.get(position).getMsg_type().equalsIgnoreCase("Video")) {
                    mymessage.setVisibility(View.GONE);
                    myimage.setVisibility(View.GONE);
                    myfilelay.setVisibility(View.GONE);
                    my_video_lay.setVisibility(View.VISIBLE);
                    Picasso.with(MemberChatAct.this).load(converSessionArrayList.get(position).getVideo_thumb_img()).into(myvideo_thumb);

                    Log.e("msg>>>>", converSessionArrayList.get(position).getMessage());
                } else if (converSessionArrayList.get(position).getMsg_type().equalsIgnoreCase("Image")) {
                    mymessage.setVisibility(View.GONE);
                    myimage.setVisibility(View.VISIBLE);
                    my_video_lay.setVisibility(View.GONE);
                    myfilelay.setVisibility(View.GONE);
                    Picasso.with(MemberChatAct.this).load(converSessionArrayList.get(position).getChat_image()).into(myimage);
                } else if (converSessionArrayList.get(position).getMsg_type().equalsIgnoreCase("zip") || converSessionArrayList.get(position).getMsg_type().equalsIgnoreCase("doc") || converSessionArrayList.get(position).getMsg_type().equalsIgnoreCase("docx") || converSessionArrayList.get(position).getMsg_type().equalsIgnoreCase("txt") || converSessionArrayList.get(position).getMsg_type().equalsIgnoreCase("pdf")) {
                    mymessage.setVisibility(View.GONE);
                    myimage.setVisibility(View.GONE);
                    my_video_lay.setVisibility(View.GONE);
                    myfilelay.setVisibility(View.VISIBLE);
                    filename.setText("" + converSessionArrayList.get(position).getMsg_type() + " file");
                } else if (msgtype.equalsIgnoreCase("jpg") || msgtype.equalsIgnoreCase("jpeg") || msgtype.equalsIgnoreCase("png") || msgtype.equalsIgnoreCase("PNG")) {

                    mymessage.setVisibility(View.GONE);
                    myimage.setVisibility(View.VISIBLE);
                    my_video_lay.setVisibility(View.GONE);
                    myfilelay.setVisibility(View.GONE);
                    Picasso.with(MemberChatAct.this).load(converSessionArrayList.get(position).getChat_image()).into(myimage);

                } else if (msgtype.equalsIgnoreCase("mp4")) {
                    mymessage.setVisibility(View.GONE);

                    myimage.setVisibility(View.GONE);
                    myfilelay.setVisibility(View.GONE);
                    my_video_lay.setVisibility(View.VISIBLE);
                    // Picasso.with(MemberChatAct.this).load(converSessionArrayList.get(position).getVideo_thumb_img()).into(myvideo_thumb);
                } else {
                    // String msg = fromBase64(converSessionArrayList.get(position).getMessage());
                    mymessage.setText("" + converSessionArrayList.get(position).getMessage());

             //       Toast.makeText(context, converSessionArrayList.get(position).getMessage(), Toast.LENGTH_SHORT).show();
                    // mymessage.setText("" + msg);

                    mymessage.setVisibility(View.VISIBLE);
                    myimage.setVisibility(View.GONE);
                    myfilelay.setVisibility(View.GONE);
                    my_video_lay.setVisibility(View.GONE);
                }


                String imagelist = image_url;
                if (imagelist.equalsIgnoreCase("") || imagelist.equalsIgnoreCase(BaseUrl.image_baseurl) || imagelist.equalsIgnoreCase(BaseUrl.image_baseurl)) {

                } else {
                    //Picasso.with(ChatingAct.this).load(imagelist).into(my_profile);

                    Glide.with(MemberChatAct.this)
                            .load(imagelist)
                            .thumbnail(0.5f)
                            .override(200, 200)
                            .centerCrop()
                            //  .placeholder(R.drawable.profile_ic)
                            .crossFade()
                            //.dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    //  myplaceholder.setVisibility(View.GONE);
                                    return false;

                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    // myplaceholder.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(my_profile);

                }


            } else {

                mylayout.setVisibility(View.GONE);
                otheruserlay.setVisibility(View.VISIBLE);
                othermessagetime.setText("" + converSessionArrayList.get(position).getDatetime());
                String msgtype = converSessionArrayList.get(position).getMsg_type();

                if (converSessionArrayList.get(position).getMsg_type().equalsIgnoreCase("Video")) {

                    othermsg.setVisibility(View.GONE);
                    othersendimgimage.setVisibility(View.GONE);
                    other_video_lay.setVisibility(View.VISIBLE);
                    otherfilelay.setVisibility(View.GONE);
                    //  otherimage_chat.setVisibility(View.GONE);
                    Picasso.with(MemberChatAct.this).load(converSessionArrayList.get(position).getVideo_thumb_img()).into(othervideo_thumb);

                } else if (converSessionArrayList.get(position).getMsg_type().equalsIgnoreCase("Image")) {

                    othermsg.setVisibility(View.GONE);
                    othersendimgimage.setVisibility(View.VISIBLE);
                    other_video_lay.setVisibility(View.GONE);
                    otherfilelay.setVisibility(View.GONE);
                    //  otherimage_chat.setVisibility(View.GONE);
                    Picasso.with(MemberChatAct.this).load(converSessionArrayList.get(position).getChat_image()).into(othersendimgimage);

                } else if (msgtype.equalsIgnoreCase("jpg") || msgtype.equalsIgnoreCase("jpeg") || msgtype.equalsIgnoreCase("png") || msgtype.equalsIgnoreCase("PNG")) {

                    othermsg.setVisibility(View.GONE);
                    othersendimgimage.setVisibility(View.VISIBLE);
                    other_video_lay.setVisibility(View.GONE);
                    otherfilelay.setVisibility(View.GONE);
                    //  otherimage_chat.setVisibility(View.GONE);
                    Picasso.with(MemberChatAct.this).load(converSessionArrayList.get(position).getChat_image()).into(othersendimgimage);

                } else if (msgtype.equalsIgnoreCase("mp4")) {

                    othermsg.setVisibility(View.GONE);
                    othersendimgimage.setVisibility(View.GONE);
                    other_video_lay.setVisibility(View.VISIBLE);
                    otherfilelay.setVisibility(View.GONE);
                    //  otherimage_chat.setVisibility(View.GONE);
                    //Picasso.with(MemberChatAct.this).load(converSessionArrayList.get(position).getVideo_thumb_img()).into(othervideo_thumb);

                } else if (converSessionArrayList.get(position).getMsg_type().equalsIgnoreCase("zip") || converSessionArrayList.get(position).getMsg_type().equalsIgnoreCase("doc") || converSessionArrayList.get(position).getMsg_type().equalsIgnoreCase("docx") || converSessionArrayList.get(position).getMsg_type().equalsIgnoreCase("txt") || converSessionArrayList.get(position).getMsg_type().equalsIgnoreCase("pdf")) {
                    othermsg.setVisibility(View.GONE);
                    othersendimgimage.setVisibility(View.GONE);
                    other_video_lay.setVisibility(View.GONE);
                    otherfilelay.setVisibility(View.VISIBLE);
                    otherfilename.setText("" + converSessionArrayList.get(position).getMsg_type() + " file");

                    if (converSessionArrayList.get(position).isFileIsAvb()) {
                        otherdownload.setVisibility(View.GONE);
                    } else {
                        otherdownload.setVisibility(View.VISIBLE);
                    }


                } else {
                //    String msg = fromBase64(converSessionArrayList.get(position).getMessage());
                    //  mymessage.setText("" + converSessionArrayList.get(position).getMessage());
                    othermsg.setText("" + converSessionArrayList.get(position).getMessage());
                    // othermsg.setText("" + converSessionArrayList.get(position).getMessage());
                    othermsg.setVisibility(View.VISIBLE);
                    othersendimgimage.setVisibility(View.GONE);
                    otherfilelay.setVisibility(View.GONE);
                    other_video_lay.setVisibility(View.GONE);
                }


                String imagelist = receiver_img;
                if (imagelist.equalsIgnoreCase("") || imagelist.equalsIgnoreCase(BaseUrl.image_baseurl) || imagelist.equalsIgnoreCase(BaseUrl.image_baseurl)) {

                } else {
                    Glide.with(MemberChatAct.this)
                            .load(imagelist)
                            .thumbnail(0.5f)
                            .override(200, 200)
                            .centerCrop()
                            // .placeholder(R.drawable.profile_ic)
                            .crossFade(1000)
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    // otherplaceholder.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    // otherplaceholder.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(otherimage);


                }

            }

            otherfilelay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (converSessionArrayList.get(position).isFileIsAvb()) {
                        if (converSessionArrayList.get(position).getFile() != null) {
                            MimeTypeMap myMime = MimeTypeMap.getSingleton();
                            Intent newIntent = new Intent(Intent.ACTION_VIEW);
                            String mimeType = myMime.getMimeTypeFromExtension(fileExt(converSessionArrayList.get(position).getFile_path()).substring(1));
                            newIntent.setDataAndType(Uri.fromFile(converSessionArrayList.get(position).getFile()), mimeType);
                            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            try {
                                context.startActivity(newIntent);
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(context, "No handler for this type of file.", Toast.LENGTH_LONG).show();
                            }
                        }

                    }

                }
            });
            otherdownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DownloadFileFromURL().execute(converSessionArrayList.get(position).getAttach_file_name(), converSessionArrayList.get(position).getMsg_type());

                }
            });
            othersendimgimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MemberChatAct.this, SingleImageViewAct.class);
                    i.putExtra("image_str", converSessionArrayList.get(position).getChat_image());
                    startActivity(i);
                }
            });
            myimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MemberChatAct.this, SingleImageViewAct.class);
                    i.putExtra("image_str", converSessionArrayList.get(position).getChat_image());
                    startActivity(i);
                }
            });

            myvideo_thumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MemberChatAct.this, VideoPlayActivity.class);
                    i.putExtra("video_url", converSessionArrayList.get(position).getChat_video());
                    startActivity(i);
                }
            });

            othervideo_thumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MemberChatAct.this, VideoPlayActivity.class);
                    i.putExtra("video_url", converSessionArrayList.get(position).getChat_video());
                    startActivity(i);
                }
            });

            return rowView;
        }

    }


    public class ChatMainAdapter extends BaseAdapter {
        Context context;
        private LayoutInflater inflater = null;
        ArrayList<ChatBeanMain> chatBeanMainArrayList;

        public ChatMainAdapter(Activity chatActivity, ArrayList<ChatBeanMain> chatBeanMainArrayList) {
            // TODO Auto-generated constructor stub
            this.context = chatActivity;
            this.chatBeanMainArrayList = chatBeanMainArrayList;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            // return  4;
            return chatBeanMainArrayList == null ? 0 : chatBeanMainArrayList.size();
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

            View rowView;
            // LinearLayout mylayout;
            TextView conversationtime;
            ExpandableHeightListView chatlist;

            rowView = inflater.inflate(R.layout.custom_chat_lay, null);
            if (rowView != null) {
                conversationtime = rowView.findViewById(R.id.conversationtime);
                chatlist = rowView.findViewById(R.id.chatlist);
                chatlist.setExpanded(true);

            } else {
                rowView = inflater.inflate(R.layout.custom_chat_lay, null);
                conversationtime = rowView.findViewById(R.id.conversationtime);
                chatlist = rowView.findViewById(R.id.chatlist);
                chatlist.setExpanded(true);


            }

            conversationtime.setText("" + chatBeanMainArrayList.get(position).getDate());

            chatBeanMainArrayList.get(position).getConverSessionArrayList();

            Log.e("arraylisttewst", "" + chatBeanMainArrayList.get(position).getConverSessionArrayList());

            if (chatBeanMainArrayList.get(position).getConverSessionArrayList() != null) {
                conversessionAdapter = new ConversessionAdapter(MemberChatAct.this, chatBeanMainArrayList.get(position).getConverSessionArrayList());
                chatlist.setAdapter(conversessionAdapter);
            }


            return rowView;
        }

    }

    private class MyConverSession extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            converSessionArrayList = new ArrayList<>();
            super.onPreExecute();
            try {
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String postReceiverUrl = "https://international.myngrewards.com/wp-content/plugins/webservice/get_chat_detail.php?sender_id=" + user_id + "&receiver_id=" + receiver_id + "&type=" + type;
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                Log.e("postReceiverUrl >>", " .." + postReceiverUrl + "sender_id=" + user_id + "&receiver_id=" + receiver_id + "&type=" + type);
                params.put("sender_id", user_id);
                params.put("receiver_id", receiver_id);

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

            Log.e("Chat MEssages >>", "" + result);

            if (result == null) {

            } else if (result.isEmpty()) {

            } else {

                try {

                    JSONObject jsonObject = new JSONObject(result);
                    int jsonlenth = 0;
                    int mainjsonlenth = 0;
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        mainjsonlenth = jsonArray.length();

                        converSessionArrayList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                            ChatBeanMain chatBeanMain = new ChatBeanMain();
                            chatBeanMain.setDate(jsonObject2.getString("date"));

                            JSONArray jsonArray1 = jsonObject2.getJSONArray("chat");

                            if (i == jsonArray.length() - 1) {
                                jsonlenth = jsonArray1.length();
                            }
                            ArrayList converSessionList = new ArrayList<>();

                            for (int j = 0; j < jsonArray1.length(); j++) {
                                JSONObject jsonObject3 = jsonArray1.getJSONObject(j);

                                ConverSession conversession = new ConverSession();
                                conversession.setId(jsonObject3.getString("id"));
                                conversession.setSenderid(jsonObject3.getString("sender_id"));
                                conversession.setMessage(jsonObject3.getString("chat_message"));
                                conversession.setChat_image(jsonObject3.getString("chat_image"));

                                conversession.setMsg_type(jsonObject3.getString("msg_type"));
                                conversession.setChat_video(jsonObject3.getString("chat_video"));
                                conversession.setVideo_thumb_img(jsonObject3.getString("video_thumb_img"));
                                conversession.setFile_name(jsonObject3.getString("file_name"));
                                conversession.setAttach_file_name(jsonObject3.getString("attach_file_name"));

                                String msg_type = jsonObject3.getString("msg_type");
                                if (msg_type != null) {
                                    if (msg_type.equalsIgnoreCase("pdf") || msg_type.equalsIgnoreCase("doc") || msg_type.equalsIgnoreCase("txt")) {
                                        String path = Environment
                                                .getExternalStorageDirectory().toString() + "/Ngreward Downloads/" + jsonObject3.getString("attach_file_name");
                                        File file = new File(path);
                                        if (file.exists()) {
                                            conversession.setFileIsAvb(true);
                                            conversession.setFile(file);
                                            conversession.setFile_path(path);
                                            Log.e("FILE IS AVB", "TRUE");
                                            // Toast File is exists
                                        } else {
                                            conversession.setFile_path("");
                                            conversession.setFile(null);
                                            conversession.setFileIsAvb(false);
                                            Log.e("FILE IS NOT AVB", "TRUE");
                                            // Toast File is not exists
                                        }
                                    } else {
                                        conversession.setFile_path("");
                                        conversession.setFile(null);
                                        conversession.setFileIsAvb(false);
                                    }

                                } else {
                                    conversession.setFile_path("");
                                    conversession.setFile(null);
                                    conversession.setFileIsAvb(false);
                                }

                                conversession.setDatetime(jsonObject3.getString("date_time"));
                                if (jsonObject3.getString("date_time") != null && !jsonObject3.getString("date_time").equalsIgnoreCase("") && !jsonObject3.getString("date_time").equalsIgnoreCase("null")) {
                                    Date date1 = null;
                                    try {
                                        date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(jsonObject3.getString("date_time"));
                                        // SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy hh:mm aa", Locale.ENGLISH);
                                        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy, hh:mm aa", Locale.ENGLISH);
                                        String strDate = formatter.format(date1);
                                        conversession.setDatetime(strDate);

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                }
                                converSessionList.add(conversession);
                            }


                            chatBeanMain.setConverSessionArrayList(converSessionList);
                            converSessionArrayList.add(chatBeanMain);

                        }

                    }


                    if (converSessionArrayList != null || !converSessionArrayList.isEmpty()) {
                        Log.e("jsonlenth >>", " .." + jsonlenth);
                        Log.e("beforelength >>", " .." + beforelength);
                        if (mainjsonlenth > mainbeforelength) {
                            chatMainAdapter = new ChatMainAdapter(MemberChatAct.this, converSessionArrayList);
                            chat_list.setAdapter(chatMainAdapter);
                            chat_list.setSelection(converSessionArrayList.size() - 1);
                            chatMainAdapter.notifyDataSetChanged();
                            mainbeforelength = mainjsonlenth;

                        } else {
                            if (jsonlenth > beforelength) {
                                // Collections.reverse(converSessionArrayList);
                                chatMainAdapter = new ChatMainAdapter(MemberChatAct.this, converSessionArrayList);
                                chat_list.setAdapter(chatMainAdapter);
                                chat_list.setSelection(converSessionArrayList.size() - 1);
                                chatMainAdapter.notifyDataSetChanged();
                                beforelength = jsonlenth;
                            }

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }


    }

    public class SendMessage extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            try {
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
            prgressbar.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... strings) {
//https://international.myngrewards.com/demo/wp-content/plugins/webservice/insert_chat.php?sender_id=1&receiver_id=2&chat_message=TestMessage&type=Merchant
            String charset = "UTF-8";
            String requestURL = BaseUrl.baseurl + "insert_chat.php?";

            type = type.replaceAll("(\\r|\\n)", "");
            receiver_type = receiver_type.replaceAll("(\\r|\\n)", "");
            Log.e("requestURL >>", " .." + requestURL + "sender_id=" + user_id + "&receiver_id=" + receiver_id + "&type=" + type + "&chat_message=" + messagetext.trim() + "&msg_type=Text&timezone=" + time_zone + "&date=" + date_time + "&date_time=" + date_time_show + "&receiver_type=" + receiver_type.trim());
            try {
                MultipartUtility multipart = new MultipartUtility(requestURL, charset);
                Log.e("SENDER ", "ID" + user_id);
                multipart.addFormField("sender_id", user_id);
                multipart.addFormField("receiver_type", receiver_type.trim());
                multipart.addFormField("date_time", date_time_show);
                multipart.addFormField("type", type.trim());
                multipart.addFormField("receiver_id", receiver_id);
                multipart.addFormField("chat_message", messagetext.trim());
                multipart.addFormField("msg_type", "Text");
                multipart.addFormField("timezone", time_zone);
                multipart.addFormField("date", date_time);

                List<String> response = multipart.finish();


                String Jsondata = "";

                for (String line : response) {


                    Jsondata = line;
                    Log.e("Send msg Response ====", Jsondata);

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
        protected void onPostExecute(String lenghtOfFile) {

            if (lenghtOfFile == null) {
                messagetext = "";
                message_et.setText("");
                // ImagePath = "";
                prgressbar.setVisibility(View.GONE);
                new MyConverSession().execute();

            } else if (lenghtOfFile.equalsIgnoreCase("")) {
                messagetext = "";
                message_et.setText("");
                //  ImagePath = "";
                prgressbar.setVisibility(View.GONE);
                new MyConverSession().execute();
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(lenghtOfFile);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        messagetext = "";
                        message_et.setText("");
                        ImagePath = "";
                        VideoPath = "";
                        ThumbnailPath = "";
                        FilePath = "";
                        prgressbar.setVisibility(View.GONE);


                        new MyConverSession().execute();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }


    }

    public class SendImageMessage extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            try {
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
            prgressbar.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... strings) {
//https://international.myngrewards.com/demo/wp-content/plugins/webservice/insert_chat.php?sender_id=1&receiver_id=2&chat_message=TestMessage&type=Merchant
            String charset = "UTF-8";
            String requestURL = BaseUrl.baseurl + "insert_chat.php?";
            //  Log.e("requestURL >>", requestURL);
            //  Log.e("requestURL >>"," .."+requestURL+"sender_id="+user_id+"&receiver_id="+receiver_id+"&type="+type);
            type = type.replaceAll("(\\r|\\n)", "");
            receiver_type = receiver_type.replaceAll("(\\r|\\n)", "");

            try {
                MultipartUtility multipart = new MultipartUtility(requestURL, charset);
                Log.e("SENDER ", "ID" + user_id);

                multipart.addFormField("sender_id", user_id);
                multipart.addFormField("type", type.trim());
                multipart.addFormField("receiver_id", receiver_id);
                multipart.addFormField("chat_message", "");
                multipart.addFormField("msg_type", "Image");
                multipart.addFormField("timezone", time_zone);
                multipart.addFormField("date", date_time);
                multipart.addFormField("date_time", date_time_show);
                multipart.addFormField("receiver_type", "" + receiver_type.trim());

                if (ImagePath == null || ImagePath.equalsIgnoreCase("")) {

                } else {
                    File ImageFile = new File(ImagePath);
                    multipart.addFilePart("chat_image", ImageFile);
                }

                List<String> response = multipart.finish();
                String Jsondata = "";

                for (String line : response) {

                    Jsondata = line;
                    Log.e("Send Img Response ====", Jsondata);

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
        protected void onPostExecute(String lenghtOfFile) {

            if (lenghtOfFile == null) {
                messagetext = "";
                message_et.setText("");
                // ImagePath = "";
                prgressbar.setVisibility(View.GONE);
                new MyConverSession().execute();
            } else if (lenghtOfFile.equalsIgnoreCase("")) {
                messagetext = "";
                message_et.setText("");
                //  ImagePath = "";
                prgressbar.setVisibility(View.GONE);
                new MyConverSession().execute();
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(lenghtOfFile);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        messagetext = "";
                        message_et.setText("");
                        ImagePath = "";
                        VideoPath = "";
                        ThumbnailPath = "";
                        FilePath = "";
                        prgressbar.setVisibility(View.GONE);
                        new MyConverSession().execute();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }


    }

    public class SendVideoMessage extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            try {
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
            prgressbar.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... strings) {
//https://international.myngrewards.com/demo/wp-content/plugins/webservice/insert_chat.php?sender_id=1&receiver_id=2&chat_message=TestMessage&type=Merchant
            String charset = "UTF-8";
            String requestURL = BaseUrl.baseurl + "insert_chat.php?";
            //  Log.e("requestURL >>", requestURL);
            //  Log.e("requestURL >>"," .."+requestURL+"sender_id="+user_id+"&receiver_id="+receiver_id+"&type="+type);
            type = type.replaceAll("(\\r|\\n)", "");
            receiver_type = receiver_type.replaceAll("(\\r|\\n)", "");

            try {
                MultipartUtility multipart = new MultipartUtility(requestURL, charset);
                Log.e("SENDER ", "ID" + user_id);

                multipart.addFormField("sender_id", user_id);
                multipart.addFormField("type", type.trim());
                multipart.addFormField("receiver_id", receiver_id);
                multipart.addFormField("chat_message", "");

                multipart.addFormField("timezone", time_zone);
                multipart.addFormField("date", date_time);
                multipart.addFormField("date_time", date_time_show);
                multipart.addFormField("msg_type", "Video");
                multipart.addFormField("receiver_type", "" + receiver_type.trim());

                if (VideoPath == null || VideoPath.equalsIgnoreCase("")) {

                } else {
                    File ImageFile = new File(VideoPath);
                    multipart.addFilePart("chat_video", ImageFile);
                }
                if (ThumbnailPath == null || ThumbnailPath.equalsIgnoreCase("")) {

                } else {
                    File video_thumb_img = new File(ThumbnailPath);
                    multipart.addFilePart("video_thumb_img", video_thumb_img);
                }

                List<String> response = multipart.finish();


                String Jsondata = "";

                for (String line : response) {


                    Jsondata = line;
                    Log.e("Send Vid Response ====", Jsondata);

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
        protected void onPostExecute(String lenghtOfFile) {
            messagetext = "";
            message_et.setText("");
            ImagePath = "";
            VideoPath = "";
            ThumbnailPath = "";
            FilePath = "";
            if (lenghtOfFile == null) {

                prgressbar.setVisibility(View.GONE);
                new MyConverSession().execute();
            } else if (lenghtOfFile.equalsIgnoreCase("")) {
                messagetext = "";
                message_et.setText("");
                //  ImagePath = "";
                prgressbar.setVisibility(View.GONE);
                new MyConverSession().execute();
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(lenghtOfFile);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        messagetext = "";
                        message_et.setText("");
                        ImagePath = "";
                        VideoPath = "";
                        prgressbar.setVisibility(View.GONE);
                        new MyConverSession().execute();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }


    }

    public class SendFilesMessage extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            try {
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
            prgressbar.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... strings) {
//https://international.myngrewards.com/demo/wp-content/plugins/webservice/insert_chat.php?sender_id=1&receiver_id=2&chat_message=TestMessage&type=Merchant
            String charset = "UTF-8";
            String requestURL = BaseUrl.baseurl + "insert_chat.php?";
            //  Log.e("requestURL >>", requestURL);
            //  Log.e("requestURL >>"," .."+requestURL+"sender_id="+user_id+"&receiver_id="+receiver_id+"&type="+type);
            type = type.replaceAll("(\\r|\\n)", "");
            receiver_type = receiver_type.replaceAll("(\\r|\\n)", "");

            try {
                MultipartUtility multipart = new MultipartUtility(requestURL, charset);
                Log.e("SENDER ", "ID" + user_id);

                multipart.addFormField("sender_id", user_id);
                multipart.addFormField("type", type.trim());
                multipart.addFormField("receiver_id", receiver_id);
                multipart.addFormField("chat_message", "");
                multipart.addFormField("date_time", date_time_show);
                multipart.addFormField("timezone", time_zone);
                multipart.addFormField("date", date_time);
                multipart.addFormField("msg_type", "");
                multipart.addFormField("receiver_type", "" + receiver_type.trim());

                if (FilePath == null || FilePath.equalsIgnoreCase("")) {

                } else {
                    File ImageFile = new File(FilePath);
                    multipart.addFilePart("file_name", ImageFile);
                }


                List<String> response = multipart.finish();


                String Jsondata = "";

                for (String line : response) {


                    Jsondata = line;
                    Log.e("Send File Response ====", Jsondata);

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
        protected void onPostExecute(String lenghtOfFile) {
            messagetext = "";
            message_et.setText("");
            ImagePath = "";
            VideoPath = "";
            ThumbnailPath = "";
            FilePath = "";
            if (lenghtOfFile == null) {

                prgressbar.setVisibility(View.GONE);
                new MyConverSession().execute();
            } else if (lenghtOfFile.equalsIgnoreCase("")) {
                messagetext = "";
                message_et.setText("");
                //  ImagePath = "";
                prgressbar.setVisibility(View.GONE);
                new MyConverSession().execute();
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(lenghtOfFile);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        messagetext = "";
                        message_et.setText("");
                        ImagePath = "";
                        VideoPath = "";
                        FilePath = "";
                        prgressbar.setVisibility(View.GONE);
                        new MyConverSession().execute();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }


    }

    private void selectImage() {
        final Dialog dialogSts = new Dialog(MemberChatAct.this, R.style.DialogSlideAnim);
        dialogSts.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSts.setCancelable(false);
        dialogSts.setContentView(R.layout.select_img_lay);
        dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button camera = (Button) dialogSts.findViewById(R.id.camera);
        Button gallary = (Button) dialogSts.findViewById(R.id.gallary);
        TextView cont_find = (TextView) dialogSts.findViewById(R.id.cont_find);
        gallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSts.dismiss();
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);

            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSts.dismiss();
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 2);

            }
        });

        cont_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSts.dismiss();
            }
        });
        dialogSts.show();
    }

    private void selectFiles() {
        final Dialog dialogSts = new Dialog(MemberChatAct.this, R.style.DialogSlideAnim);
        dialogSts.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSts.setCancelable(false);
        dialogSts.setContentView(R.layout.select_files_lay);
        dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button files = (Button) dialogSts.findViewById(R.id.files);
        Button video_but = (Button) dialogSts.findViewById(R.id.video_but);
        TextView cont_find = (TextView) dialogSts.findViewById(R.id.cont_find);
        files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSts.dismiss();
                browseDocuments();
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                intent.setType("*/*");
//                Intent i = Intent.createChooser(intent, "File");
//                startActivityForResult(i, FILE_SELECT_CODE);



               /* Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file*//**//*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                // special intent for Samsung file manager
                Intent sIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
                // if you want any file type, you can skip next line
                sIntent.putExtra("CONTENT_TYPE", "pdf*//**//*");
                sIntent.addCategory(Intent.CATEGORY_DEFAULT);

                Intent chooserIntent;
                if (getPackageManager().resolveActivity(sIntent, 0) != null){
                    // it is device with samsung file manager
                    chooserIntent = Intent.createChooser(sIntent, "Open file");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { intent});
                }
                else {
                    chooserIntent = Intent.createChooser(intent, "Open file");
                }

                try {
                    startActivityForResult(chooserIntent, FILE_SELECT_CODE);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "No suitable File Manager was found.", Toast.LENGTH_SHORT).show();
                }




               *//* Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file*//**//*");
                startActivityForResult(intent, FILE_SELECT_CODE);*/

//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//              //  intent.setType("file/*");
//                intent.setType("*/*");
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//
//                try {
//                    startActivityForResult(
//                            Intent.createChooser(intent, "Select a File to Upload"),
//                            FILE_SELECT_CODE);
//                } catch (android.content.ActivityNotFoundException ex) {
//                    // Potentially direct the user to the Market with a Dialog
//
//                }
            }
        });
        video_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSts.dismiss();
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 3);
            }
        });
        cont_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSts.dismiss();
            }
        });
        dialogSts.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Date today = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            date_time = format2.format(today);
            date_time_show = format.format(today);
            System.out.println("CURRENT " + date_time);

            switch (requestCode) {
                case 1:
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

                    break;
                case 2:
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    String cameraPath = saveToInternalStorage(photo);
                    Log.e("PATH Camera", "" + cameraPath);
                    //  String ImagePath = getPath(selectedImage);

                    decodeFile(cameraPath);

                    break;
                case 3:
                    Uri selectedVideo = data.getData();
                    //getPath(selectedImage);
                    Log.e("selectedVideo", " >> " + selectedVideo);
                    VideoPath = GetFilePathFromDevice.getPath(MemberChatAct.this, selectedVideo);
                    Log.e("Video Path On Result", " >> " + VideoPath);
                    Bitmap bMap = ThumbnailUtils.createVideoThumbnail(VideoPath, MediaStore.Video.Thumbnails.MICRO_KIND);

                    ThumbnailPath = saveToInternalStorage(bMap);
                    Log.e("ThumbnailPath >> ", " ..." + ThumbnailPath);


                    new SendVideoMessage().execute();
                    break;

                case FILE_SELECT_CODE:
                    if (resultCode == RESULT_OK) {
                        // Get the Uri of the selected file
                        Uri uri = data.getData();
                        //  FilePath = Commons.getPath(uri, MemberChatAct.this);


                        // Get the path
                        // String path = uri.uri.toString()
                        try {
                            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                                FilePath = getRealPathFromURI_API19(MemberChatAct.this, uri);

                            } else {
                                FilePath = Commons.getPath(uri, MemberChatAct.this);

                            }

                            /*Log.e("ddd",""+android.os.Build.VERSION.SDK_INT);
                            Log.e("sss",""+Build.VERSION_CODES.O);
                            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

                                File file = new File(uri.getPath());//create path from uri
                                final String[] split = file.getPath().split(":");//split the path.
                                FilePath = split[1];//assign it to a string(your choice).
                            }
                            else {
                                FilePath= PathUtil.getPath(MemberChatAct.this,uri);

                            }*/
                            //    FilePath = GetFilePathFromDevice.getPath(MemberChatAct.this, uri);
                            Log.e("FILE SEL", "File Path: " + FilePath);


                            new SendFilesMessage().execute();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        // Get the file instance
                        // File file = new File(path);
                        // Initiate the upload
                    }
                    break;

            }
        }


    }

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
        ContextWrapper cw = new ContextWrapper(MemberChatAct.this);
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


    public void decodeFile(String filePath) {
        // Decode image size
       /* BitmapFactory.Options o = new BitmapFactory.Options();
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
        o2.inSampleSize = scale;*/
        BitmapFactory.Options o2 = new BitmapFactory.Options();

        Bitmap bitmap = BitmapFactory.decodeFile(filePath, o2);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * 0.8), (int) (bitmap.getHeight() * 0.8), true);

        ImagePath = saveToInternalStorage(bitmap);
        Log.e("DECODE PATH", "ff " + ImagePath);
        // user_img.setImageBitmap(bitmap);
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        date_time = format2.format(today);
        date_time_show = format.format(today);
        System.out.println("CURRENT " + date_time);

        new SendImageMessage().execute();
    }


    public static String fromBase64(String message) {

        try {
            byte[] data = Base64.decode(message, Base64.DEFAULT);
            return new String(data, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return message;
        }


    }


    private void browseDocuments() {

        String[] mimeTypes =
                {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "text/plain",
                        "application/pdf",
                        "application/zip"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }
        startActivityForResult(Intent.createChooser(intent, "ChooseFile"), FILE_SELECT_CODE);

    }


    private String getPathTwo(final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat) {
            // MediaStore (and general)
            return getForApi19(uri);
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    @TargetApi(19)
    private String getForApi19(Uri uri) {
        Log.e(tag, "+++ API 19 URI :: " + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            Log.e(tag, "+++ Document URI");
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                Log.e(tag, "+++ External Document URI");
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    Log.e(tag, "+++ Primary External Document URI");
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                Log.e(tag, "+++ Downloads External Document URI");
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                Log.e(tag, "+++ Media Document URI");
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    Log.e(tag, "+++ Image Media Document URI");
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    Log.e(tag, "+++ Video Media Document URI");
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    Log.e(tag, "+++ Audio Media Document URI");
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            Log.e(tag, "+++ No DOCUMENT URI :: CONTENT ");

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            Log.e(tag, "+++ No DOCUMENT URI :: FILE ");
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getRealPathFromURI_API19(Context context, Uri uri) {
        String filePath = "";

        // ExternalStorageProvider
        if (isExternalStorageDocument(uri)) {
            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(":");
            final String type = split[0];

            if ("primary".equalsIgnoreCase(type)) {
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else {

                if (Build.VERSION.SDK_INT > 20) {
                    //getExternalMediaDirs() added in API 21
                    File[] extenal = context.getExternalMediaDirs();
                    if (extenal.length > 1) {
                        filePath = extenal[1].getAbsolutePath();
                        filePath = filePath.substring(0, filePath.indexOf("Android")) + split[1];
                    }
                } else {
                    filePath = "/storage/" + type + "/" + split[1];
                }
                return filePath;
            }

        } else if (isDownloadsDocument(uri)) {
            // DownloadsProvider
            final String id = DocumentsContract.getDocumentId(uri);
            //final Uri contentUri = ContentUris.withAppendedId(
            // Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

            Cursor cursor = null;
            final String column = "_data";
            final String[] projection = {column};

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int index = cursor.getColumnIndexOrThrow(column);
                    String result = cursor.getString(index);
                    cursor.close();
                    return result;
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        } else if (DocumentsContract.isDocumentUri(context, uri)) {
            // MediaProvider
            String wholeID = DocumentsContract.getDocumentId(uri);

            // Split at colon, use second item in the array
            String[] ids = wholeID.split(":");
            String id;
            String type;
            if (ids.length > 1) {
                id = ids[1];
                type = ids[0];
            } else {
                id = ids[0];
                type = ids[0];
            }

            Uri contentUri = null;
            if ("image".equals(type)) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(type)) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else if ("audio".equals(type)) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            }

            final String selection = "_id=?";
            final String[] selectionArgs = new String[]{id};
            final String column = "_data";
            final String[] projection = {column};
            Cursor cursor = context.getContentResolver().query(contentUri,
                    projection, selection, selectionArgs, null);

            if (cursor != null) {
                int columnIndex = cursor.getColumnIndex(column);

                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(columnIndex);
                }
                cursor.close();
            }
            return filePath;
        } else {
            String[] proj = {MediaStore.Audio.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                if (cursor.moveToFirst())
                    filePath = cursor.getString(column_index);
                cursor.close();
            }


            return filePath;
        }
        return null;
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgressbar.setVisibility(View.VISIBLE);
            //   showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(BaseUrl.file_baseurl + f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                Date today = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                String dateToStr = format.format(today);
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                // Output stream


                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/Ngreward Downloads");
                myDir.mkdirs();


              /*  String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());


                File file = new File(myDir, timeStamp +"."+f_url[1]);*/


                OutputStream output = new FileOutputStream(Environment
                        .getExternalStorageDirectory().toString()
                        + "/Ngreward Downloads/" + f_url[0]);

                byte[] data = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {

           //     Toast.makeText(MemberChatAct.this, getResources().getString(R.string.somethingwrong), Toast.LENGTH_LONG).show();

                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            //   pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            prgressbar.setVisibility(View.GONE);
            Toast.makeText(MemberChatAct.this, getResources().getString(R.string.downloadcomplete), Toast.LENGTH_LONG).show();
            Log.e("SUCCESS", "SUCCESS");
            // dismiss the dialog after the file was downloaded
            // dismissDialog(progress_bar_type);

        }

    }

    private String fileExt(String url) {
        if (url.indexOf("?") > -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf(".") + 1);
            if (ext.indexOf("%") > -1) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.indexOf("/") > -1) {
                ext = ext.substring(0, ext.indexOf("/"));
            }
            return ext.toLowerCase();

        }
    }

}
//xv43922
//https://stackoverflow.com/questions/13205385/how-to-check-if-file-is-available-in-internal-memory