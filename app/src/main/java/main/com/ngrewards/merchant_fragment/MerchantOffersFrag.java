package main.com.ngrewards.merchant_fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import main.com.ngrewards.R;
import main.com.ngrewards.activity.MerchantDetailAct;
import main.com.ngrewards.beanclasses.OfferBeanList;
import main.com.ngrewards.beanclasses.Offerbean;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.ExpandableHeightListView;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by technorizen on 13/6/18.
 */

public class MerchantOffersFrag extends Fragment {

    ArrayList<Boolean> selecteded;
    View v;
    private ExpandableHeightListView product_offer;
    private OffersAdpter offersAdpter;
    private RecyclerView offers_product_rec;
    private SwipeRefreshLayout swipeToRefresh;
    private ArrayList<OfferBeanList> offerBeanListArrayList;
    private int current_offer_pos;
    private ProgressBar progresbar;
    private MySession mySession;
    private String user_id = "";
    private TextView noofferstv;

    public MerchantOffersFrag() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.pro_offerfrag_lay, container, false);
        mySession = new MySession(getActivity());
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
        selecteded = new ArrayList<>();
        selecteded.add(false);
        selecteded.add(false);
        idnit();
        getOffers();
        return v;
    }

    private void getOffers() {

        swipeToRefresh.setRefreshing(true);
        //progresbar.setVisibility(View.VISIBLE);
        offerBeanListArrayList = new ArrayList<>();
        Log.e("DATA >", ">" + MerchantDetailAct.merchant_id + " >> " + user_id);
        Call<ResponseBody> call = ApiClient.getApiInterface().getMemberMerchnatOffer(MerchantDetailAct.merchant_id, user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // progresbar.setVisibility(View.GONE);
                swipeToRefresh.setRefreshing(false);
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("Merchant Offers  >", " >" + responseData);
                        if (object.getString("status").equals("1")) {

                            Offerbean successData = new Gson().fromJson(responseData, Offerbean.class);
                            offerBeanListArrayList.addAll(successData.getResult());

                        }
                        if (offerBeanListArrayList != null && !offerBeanListArrayList.isEmpty() && offerBeanListArrayList.size() > 0) {
                            noofferstv.setVisibility(View.GONE);
                            offersAdpter = new OffersAdpter(offerBeanListArrayList);
                            offers_product_rec.setAdapter(offersAdpter);
                            offersAdpter.notifyDataSetChanged();
                        } else {
                            noofferstv.setVisibility(View.VISIBLE);
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
                // Log error here since request failed
                t.printStackTrace();
                // progresbar.setVisibility(View.GONE);
                swipeToRefresh.setRefreshing(false);
                Log.e("TAG", t.toString());
            }
        });
    }

    private void idnit() {
        //product_offer = v.findViewById(R.id.product_offer);
        noofferstv = v.findViewById(R.id.noofferstv);
        progresbar = v.findViewById(R.id.progresbar);
        offers_product_rec = v.findViewById(R.id.offers_product_rec);
        swipeToRefresh = v.findViewById(R.id.swipeToRefresh);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        offers_product_rec.setLayoutManager(horizontalLayoutManagaer);
        //product_offer.setExpanded(true);
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getOffers();
                //swipeToRefresh.setRefreshing(false);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void likedislikeoffer_fun(String id) {

        progresbar.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = ApiClient.getApiInterface().likedislikeoffer(id, user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progresbar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("My Offer Products >", " >" + responseData);
                        if (object.getString("status").equals("1")) {
                            if (object.getString("result").equalsIgnoreCase("Offer Unlike Successfully")) {
                                OfferBeanList offerBeanList = new OfferBeanList();
                                offerBeanList.setLikeStatus("dislike");
                                //offerBeanListArrayList.set(current_offer_pos, offerBeanList);
                                int like = Integer.parseInt(offerBeanListArrayList.get(current_offer_pos).getLikeCount());
                                if (like > 0) {
                                    int likett = like - 1;
                                    offerBeanListArrayList.get(current_offer_pos).setLikeCount(likett + "");
                                }


                                offerBeanListArrayList.get(current_offer_pos).setLikeStatus("dislike");

                            } else {
                                OfferBeanList offerBeanList = new OfferBeanList();
                                offerBeanList.setLikeStatus("like");
                                //offerBeanListArrayList.set(current_offer_pos, offerBeanList);

                                offerBeanListArrayList.get(current_offer_pos).setLikeStatus("like");
                                int like = Integer.parseInt(offerBeanListArrayList.get(current_offer_pos).getLikeCount());
                                int likett = like + 1;
                                offerBeanListArrayList.get(current_offer_pos).setLikeCount(likett + "");

                            }
                            offersAdpter = new OffersAdpter(offerBeanListArrayList);
                            offers_product_rec.setAdapter(offersAdpter);
                            offersAdpter.notifyDataSetChanged();
                            offers_product_rec.scrollToPosition(current_offer_pos);
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
                // Log error here since request failed
                t.printStackTrace();
                progresbar.setVisibility(View.GONE);
                Log.e("TAG", t.toString());
            }
        });
    }

    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;

        try {

            File file = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            // **Warning:** This will fail for API >= 24, use a FileProvider as shown below instead.
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;

    }

    class OffersAdpter extends RecyclerView.Adapter<OffersAdpter.MyViewHolder> {
        ArrayList<OfferBeanList> offerBeanLists;

        public OffersAdpter(ArrayList<OfferBeanList> offerBeanLists) {
            this.offerBeanLists = offerBeanLists;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_pro_offers_lay, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") int listPosition) {
            if (offerBeanLists.get(listPosition).getOffer_discount_price() == null) {
                holder.discounts.setVisibility(View.GONE);
            } else {
                if (offerBeanLists.get(listPosition).getOffer_discount_price() != null && !offerBeanLists.get(listPosition).getOffer_discount_price().equalsIgnoreCase("") && !offerBeanLists.get(listPosition).getOffer_discount_price().equalsIgnoreCase("0")) {
                    holder.discounts.setVisibility(View.VISIBLE);
                    holder.pricediscount.setText(mySession.getValueOf(MySession.CurrencySign) + offerBeanLists.get(listPosition).getOffer_discount_price().trim());
                    // holder.discounts.setText("(" + offerBeanLists.get(listPosition).getOfferDiscount() + "%)");
                    double discountss = Double.parseDouble(offerBeanLists.get(listPosition).getOfferDiscount());
                    int newdis = (int) discountss;
                    holder.discounts.setText("" + newdis + "% OFF");

                } else {
                    holder.discounts.setVisibility(View.GONE);
                }

            }

            //holder.pricediscount.setText("$" + offerBeanLists.get(listPosition).getOfferPrice() + "(" + offerBeanLists.get(listPosition).getOfferDiscount() + "%)");
            holder.offername.setText("" + offerBeanLists.get(listPosition).getOfferName());
            holder.offer_desc.setText("" + offerBeanLists.get(listPosition).getOfferDescription());
            holder.likecount.setText("" + offerBeanLists.get(listPosition).getLikeCount());


            if (offerBeanLists.get(listPosition).getOfferPrice() == null) {

            } else {

                if (offerBeanLists.get(listPosition).getOffer_discount_price() != null && !offerBeanLists.get(listPosition).getOffer_discount_price().equalsIgnoreCase("") && !offerBeanLists.get(listPosition).getOffer_discount_price().equalsIgnoreCase("0")) {
                    holder.real_price.setText(mySession.getValueOf(MySession.CurrencySign) + offerBeanLists.get(listPosition).getOfferPrice().trim());
                    holder.real_price.setTextColor(getResources().getColor(R.color.back_pop_col));
                    holder.real_price.setPaintFlags(holder.real_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                } else {
                    holder.real_price.setText(mySession.getValueOf(MySession.CurrencySign) + offerBeanLists.get(listPosition).getOfferPrice().trim());

                }
            }

            String product_img = offerBeanLists.get(listPosition).getOfferImage();
            if (product_img == null || product_img.equalsIgnoreCase("") || product_img.equalsIgnoreCase(BaseUrl.image_baseurl)) {

            } else {
                Glide.with(getActivity()).load(product_img).placeholder(R.drawable.placeholder).into(holder.offerimage);

            }

            if (offerBeanLists.get(listPosition).getLikeStatus().equalsIgnoreCase("like")) {
                holder.likeimg.setImageResource(R.drawable.filled_like);
                holder.liketv.setText("" + getResources().getString(R.string.like));
            } else {
                holder.likeimg.setImageResource(R.drawable.ic_like);
                holder.liketv.setText("" + getResources().getString(R.string.like));
            }

            holder.sharelay.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Uri bmpUri = getLocalBitmapUri(holder.offerimage);

                    try {

                        if (bmpUri != null) {
                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.putExtra(Intent.EXTRA_STREAM, "" + bmpUri);
                            shareIntent.putExtra(Intent.EXTRA_TEXT, "" + offerBeanLists.get(listPosition).getOfferName() + " \n" + " Price :" + mySession.getValueOf(MySession.CurrencySign) + offerBeanLists.get(listPosition).getOfferPrice() + "\n" + offerBeanLists.get(listPosition).getShareLink());
                            shareIntent.setType("image/*");
                            startActivity(shareIntent);
                        } else {

                        }

                    } catch (Exception e) {
                        Log.e("EXC", " > " + e.getMessage());
                        e.printStackTrace();
                    }


                }
            });

            holder.likebut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    current_offer_pos = listPosition;
                    likedislikeoffer_fun(offerBeanLists.get(listPosition).getId());

                }

            });

        }

        @Override
        public int getItemCount() {
            //return 2;
            return offerBeanLists == null ? 0 : offerBeanLists.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView real_price, liketv, discounts, offername, offer_desc, pricediscount, likecount;
            ImageView likeimg, offerimage;
            LinearLayout sharelay, likebut;


            public MyViewHolder(View itemView) {
                super(itemView);
                // this.viewLine = (View) itemView.findViewById(R.id.viewLine);
                this.offerimage = itemView.findViewById(R.id.offerimage);
                this.likecount = itemView.findViewById(R.id.likecount);
                this.discounts = itemView.findViewById(R.id.discounts);
                this.pricediscount = itemView.findViewById(R.id.pricediscount);
                this.offername = itemView.findViewById(R.id.offername);
                this.offer_desc = itemView.findViewById(R.id.offer_desc);
                this.sharelay = itemView.findViewById(R.id.sharelay);
                this.likebut = itemView.findViewById(R.id.likebut);
                this.likeimg = itemView.findViewById(R.id.likeimg);
                this.liketv = itemView.findViewById(R.id.liketv);
                this.real_price = itemView.findViewById(R.id.real_price);
            }
        }
    }

}
