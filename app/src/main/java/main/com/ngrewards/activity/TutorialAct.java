package main.com.ngrewards.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.com.ngrewards.R;
import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.Utils.Tools;
import main.com.ngrewards.beanclasses.YouTubeBean;
import main.com.ngrewards.beanclasses.YouTubeVideoList;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TutorialAct extends YouTubeBaseActivity {
    private static final int RECOVERY_REQUEST = 1;
    YouTubePlayerView youtube1;
    private RelativeLayout backlay;
    private RecyclerView playerapi;
    private YouTubeAdapter youTubeAdapter;
    private String type = "";
    private ArrayList<YouTubeVideoList> youTubeVideoListArrayList;
    private ProgressBar progressbar;

    public static String extractYTId(String ytUrl) {
        String vId = null;
        Pattern pattern = Pattern.compile(
                "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(ytUrl);
        if (matcher.matches()) {
            vId = matcher.group(1);
        }
        return vId;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.reupdateResources(this);
        setContentView(R.layout.activity_tutorial);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            type = bundle.getString("type");
        }
        idint();
        clickevent();
    }

    private void clickevent() {
        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void idint() {
        youtube1 = findViewById(R.id.youtube1);
        progressbar = findViewById(R.id.progressbar);
        backlay = findViewById(R.id.backlay);
        playerapi = findViewById(R.id.playerapi);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(TutorialAct.this, LinearLayoutManager.VERTICAL, false);
        playerapi.setLayoutManager(horizontalLayoutManagaer);
        if (type.equalsIgnoreCase("member")) {
            getVideo();
        } else {
            getMerVideo();
        }


/*
        youtube1.initialize(Config.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {

                String id =  extractYTId("https://youtu.be/B0avK6lIskg");
                Log.e("dd "," dd "+id);
                youTubePlayer.cueVideo("https://youtu.be/B0avK6lIskg"); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
                // youTubePlayer.play();
                //youTubePlayer.setFullscreen(true);
                // youTubePlayer.setShowFullscreenButton(false);
                youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {

                    @Override
                    public void onLoading() {

                    }

                    @Override
                    public void onLoaded(String s) {
                        // R_TIME=player.getDurationMillis();
                        youTubePlayer.play();


                    }

                    @Override
                    public void onAdStarted() {

                    }

                    @Override
                    public void onVideoStarted() {

                    }

                    @Override
                    public void onVideoEnded() {
                        //  OnFinish();
                    }

                    @Override
                    public void onError(YouTubePlayer.ErrorReason errorReason) {
                        Log.e("YouTubePlayer_ERROR",errorReason.toString());
                        // OnFinish();
                    }

                });


            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                if (youTubeInitializationResult.isUserRecoverableError()) {
                    youTubeInitializationResult.getErrorDialog(TutorialAct.this, RECOVERY_REQUEST).show();
                } else {
                    Toast.makeText(TutorialAct.this, "ERROR", Toast.LENGTH_LONG).show();
                }
            }
        });
*/


    }

    private void getVideo() {

        progressbar.setVisibility(View.VISIBLE);
        youTubeVideoListArrayList = new ArrayList<>();
        Call<ResponseBody> call = ApiClient.getApiInterface().getMemberTutorialVideo();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressbar.setVisibility(View.GONE);
                //    swipeToRefresh.setRefreshing(false);
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("YouTub >", " >" + responseData);
                        if (object.getString("status").equals("1")) {

                            YouTubeBean successData = new Gson().fromJson(responseData, YouTubeBean.class);
                            youTubeVideoListArrayList.addAll(successData.getResult());

                        }
                        youTubeAdapter = new YouTubeAdapter(youTubeVideoListArrayList);
                        playerapi.setAdapter(youTubeAdapter);
                        youTubeAdapter.notifyDataSetChanged();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Log error here since request failed
                t.printStackTrace();
                progressbar.setVisibility(View.GONE);
                //   swipeToRefresh.setRefreshing(false);
                Log.e("TAG", t.toString());
            }
        });
    }

    private void getMerVideo() {

        progressbar.setVisibility(View.VISIBLE);
        youTubeVideoListArrayList = new ArrayList<>();
        Call<ResponseBody> call = ApiClient.getApiInterface().getMerchantTutorialVideo();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressbar.setVisibility(View.GONE);
                //    swipeToRefresh.setRefreshing(false);
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("YouTub >", " >" + responseData);
                        if (object.getString("status").equals("1")) {

                            YouTubeBean successData = new Gson().fromJson(responseData, YouTubeBean.class);
                            youTubeVideoListArrayList.addAll(successData.getResult());

                        }
                        youTubeAdapter = new YouTubeAdapter(youTubeVideoListArrayList);
                        playerapi.setAdapter(youTubeAdapter);
                        youTubeAdapter.notifyDataSetChanged();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Log error here since request failed
                t.printStackTrace();
                progressbar.setVisibility(View.GONE);
                //   swipeToRefresh.setRefreshing(false);
                Log.e("TAG", t.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            Log.e("TRUE", "COMEHERE");
            // Retry initialization if user performed a recovery action
            //  getYouTubePlayerProvider().initialize(Config.YOUTUBE_API_KEY, this);
        }
    }

    class YouTubeAdapter extends RecyclerView.Adapter<YouTubeAdapter.MyViewHolder> {
        ArrayList<YouTubeVideoList> youTubeVideoListArrayList;


        public YouTubeAdapter(ArrayList<YouTubeVideoList> youTubeVideoListArrayList) {
            this.youTubeVideoListArrayList = youTubeVideoListArrayList;
        }

        @Override
        public YouTubeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_youtube_player, parent, false);
            YouTubeAdapter.MyViewHolder myViewHolder = new YouTubeAdapter.MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final YouTubeAdapter.MyViewHolder holder, final int listPosition) {
            //holder.offer_title.setText("" + offerBeanListArrayList.get(listPosition).getOfferName());

            //String frameVideo = "<html><body><iframe src="+youTubeVideoListArrayList.get(listPosition).getUrl()+"></iframe></body></html>";
            String id = extractYTId(youTubeVideoListArrayList.get(listPosition).getUrl());
            String frameVideo = "<html><body><iframe width=\"100%\" height=\"250\" src=\"https://www.youtube.com/embed/" + id + "\" frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe></body></html>";

            holder.webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return false;
                }
            });
            WebSettings webSettings = holder.webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            holder.webView.loadData(frameVideo, "text/html", "utf-8");
/*
            holder.youtube_player.initialize(Config.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {

                       String id =  extractYTId(youTubeVideoListArrayList.get(listPosition).getUrl());
                       Log.e("dd "," dd "+id);
                       youTubePlayer.cueVideo(youTubeVideoListArrayList.get(listPosition).getUrl()); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
                       // youTubePlayer.play();
                       //youTubePlayer.setFullscreen(true);
                       // youTubePlayer.setShowFullscreenButton(false);
                        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                        youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {

                            @Override
                            public void onLoading() {

                            }

                            @Override
                            public void onLoaded(String s) {
                               // R_TIME=player.getDurationMillis();
                                youTubePlayer.play();


                            }

                            @Override
                            public void onAdStarted() {

                            }

                            @Override
                            public void onVideoStarted() {

                            }

                            @Override
                            public void onVideoEnded() {
                              //  OnFinish();
                            }

                            @Override
                            public void onError(YouTubePlayer.ErrorReason errorReason) {
                                Log.e("YouTubePlayer_ERROR",errorReason.toString());
                               // OnFinish();
                            }

                        });


                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                    if (youTubeInitializationResult.isUserRecoverableError()) {
                        youTubeInitializationResult.getErrorDialog(TutorialAct.this, RECOVERY_REQUEST).show();
                    } else {
                        Toast.makeText(TutorialAct.this, "ERROR", Toast.LENGTH_LONG).show();
                    }
                }
            });
*/

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        @Override
        public int getItemCount() {
            //return 4;
            return youTubeVideoListArrayList == null ? 0 : youTubeVideoListArrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            //  YouTubePlayerView youtube_player;
            WebView webView;

            public MyViewHolder(View itemView) {
                super(itemView);
                // this.youtube_player = itemView.findViewById(R.id.youtube_player);
                this.webView = itemView.findViewById(R.id.webView);

            }
        }
    }

}
