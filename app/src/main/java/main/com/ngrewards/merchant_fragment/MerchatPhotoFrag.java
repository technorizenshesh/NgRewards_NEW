package main.com.ngrewards.merchant_fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import main.com.ngrewards.R;
import main.com.ngrewards.activity.MerchantDetailAct;
import main.com.ngrewards.beanclasses.GalleryBean;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.ExpandableHeightGridView;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.restapi.ApiClient;
import main.com.ngrewards.showzoomableimages.FullScreenActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by technorizen on 13/6/18.
 */

public class MerchatPhotoFrag extends Fragment {
    View v;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    private ExpandableHeightGridView product_photo;
    private RecyclerView product_photo_rec;
    private ProductPhotoAdp productPhotoAdp;
    private int current_offer_pos;
    private ProgressBar progresbar;
    private MySession mySession;
    private String user_id = "";
    private TextView nophototv;

    private SwipeRefreshLayout swipeToRefresh;

    public MerchatPhotoFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.pro_photofrag_lay, container, false);
        idint();
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
        return v;
    }

    private void idint() {
        nophototv = v.findViewById(R.id.nophototv);
        progresbar = v.findViewById(R.id.progresbar);
        product_photo = v.findViewById(R.id.product_photo);
        product_photo_rec = v.findViewById(R.id.product_photo_rec);
        recyclerViewLayoutManager = new GridLayoutManager(getActivity(), 3);
        product_photo_rec.setLayoutManager(recyclerViewLayoutManager);
        product_photo.setExpanded(true);
        swipeToRefresh = v.findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //  getMerchantsDetail(MerchantDetailAct.merchant_id);
                swipeToRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
       /* customPhotoAdp = new CustomPhotoAdp(getActivity());
        product_photo.setAdapter(customPhotoAdp);*/
        if (MerchantDetailAct.merchantListBeanArrayList != null && !MerchantDetailAct.merchantListBeanArrayList.isEmpty() && MerchantDetailAct.merchantListBeanArrayList.size() > 0) {
            if (MerchantDetailAct.merchantListBeanArrayList.get(0).getGalleryImages() != null && !MerchantDetailAct.merchantListBeanArrayList.get(0).getGalleryImages().isEmpty() && MerchantDetailAct.merchantListBeanArrayList.get(0).getGalleryImages().size() > 0) {


                nophototv.setVisibility(View.GONE);
                productPhotoAdp = new ProductPhotoAdp(MerchantDetailAct.merchantListBeanArrayList.get(0).getGalleryImages());
                product_photo_rec.setAdapter(productPhotoAdp);
                productPhotoAdp.notifyDataSetChanged();
            } else {
                nophototv.setVisibility(View.VISIBLE);
            }
        } else {
            nophototv.setVisibility(View.VISIBLE);
        }

    }

    public void likedislikemerchant_fun(String merchant_id, String id) {
        progresbar.setVisibility(View.VISIBLE);
        Log.e("Merc Photo >", " >" + merchant_id + " >> " + id);
        Call<ResponseBody> call = ApiClient.getApiInterface().likedislikemerchantphoto(merchant_id, id, user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progresbar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("Merc Photo >", " >" + responseData);
                        if (object.getString("status").equals("1")) {
                            if (object.getString("result").equalsIgnoreCase("Image Unlike Successfully")) {

                                GalleryBean galleryBean = new GalleryBean();
                                galleryBean.setLike_status("dislike");
                                //offerBeanListArrayList.set(current_offer_pos, offerBeanList);
                                int like = Integer.parseInt(MerchantDetailAct.merchantListBeanArrayList.get(0).getGalleryImages().get(current_offer_pos).getLike_count());
                                if (like > 0) {
                                    int likett = like - 1;
                                    MerchantDetailAct.merchantListBeanArrayList.get(0).getGalleryImages().get(current_offer_pos).setLike_count(likett + "");
                                }


                                MerchantDetailAct.merchantListBeanArrayList.get(0).getGalleryImages().get(current_offer_pos).setLike_status("dislike");
                            } else {
                                GalleryBean galleryBean = new GalleryBean();
                                galleryBean.setLike_status("like");
                                //offerBeanListArrayList.set(current_offer_pos, offerBeanList);

                                MerchantDetailAct.merchantListBeanArrayList.get(0).getGalleryImages().get(current_offer_pos).setLike_status("like");
                                int like = Integer.parseInt(MerchantDetailAct.merchantListBeanArrayList.get(0).getGalleryImages().get(current_offer_pos).getLike_count());
                                int likett = like + 1;
                                MerchantDetailAct.merchantListBeanArrayList.get(0).getGalleryImages().get(current_offer_pos).setLike_count(likett + "");

                            }
                            productPhotoAdp = new ProductPhotoAdp(MerchantDetailAct.merchantListBeanArrayList.get(0).getGalleryImages());
                            product_photo_rec.setAdapter(productPhotoAdp);
                            productPhotoAdp.notifyDataSetChanged();
                            product_photo_rec.scrollToPosition(current_offer_pos);

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

    class ProductPhotoAdp extends RecyclerView.Adapter<ProductPhotoAdp.MyViewHolder> {
        List<GalleryBean> productImages;

        public ProductPhotoAdp(List<GalleryBean> productImages) {
            this.productImages = productImages;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_photo_lay, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") final int listPosition) {
            if (productImages.get(listPosition).getLike_status().equalsIgnoreCase("like")) {
                holder.likeimg.setImageResource(R.drawable.filled_like);

                //holder.liketv.setText("" + getResources().getString(R.string.dislike));
            } else {
                holder.likeimg.setImageResource(R.drawable.ic_like);

            }
            holder.likecount.setText("" + productImages.get(listPosition).getLike_count());
            String product_img = productImages.get(listPosition).getGallery_image();
            if (product_img == null || product_img.equalsIgnoreCase("") || product_img.equalsIgnoreCase(BaseUrl.image_baseurl)) {

            } else {
                Glide.with(getActivity())
                        .load(product_img)
                        .thumbnail(0.5f)
                        .override(400, 150)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.product_photos);

            }
            holder.likelay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    current_offer_pos = listPosition;
                    likedislikemerchant_fun(productImages.get(listPosition).getMerchant_id(), productImages.get(listPosition).getId());

                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), FullScreenActivity.class);

                    i.putExtra("position", listPosition);
                    i.putExtra("status", "");
                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            // return 4;
            return productImages == null ? 0 : productImages.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView product_photos, likeimg;
            LinearLayout likelay;
            TextView likecount;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.product_photos = itemView.findViewById(R.id.product_photos);
                this.likelay = itemView.findViewById(R.id.likelay);
                this.likecount = itemView.findViewById(R.id.likecount);
                this.likeimg = itemView.findViewById(R.id.likeimg);
            }
        }
    }

}
