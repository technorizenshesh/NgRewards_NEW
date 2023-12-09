package main.com.ngrewards.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import main.com.ngrewards.R;
import main.com.ngrewards.activity.ManualActivity;
import main.com.ngrewards.activity.ProduCtDetailAct;
import main.com.ngrewards.beanclasses.Product;
import main.com.ngrewards.beanclasses.Productlistbean;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by technorizen on 14/6/18.
 */

public class FeaturedFrag extends Fragment {
    View v;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    ProductGridAdp productGridAdp;
    private GridView fetaured_product;
    private RecyclerView featured_product_rec;
    private ProgressBar progresbar;
    private ArrayList<Productlistbean> productlistbeanArrayList;
    private String user_id = "";
    private MySession mySession;

    public FeaturedFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fetured_frag_lay, container, false);
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
        idinit();
        return v;
    }
/*
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
        }
        else {
        }
    }
*/

    private void idinit() {
        progresbar = v.findViewById(R.id.progresbar);
        featured_product_rec = v.findViewById(R.id.featured_product_rec);
        recyclerViewLayoutManager = new GridLayoutManager(getActivity(), 2);
        featured_product_rec.setLayoutManager(recyclerViewLayoutManager);

        getFeaturedProducts();


       /* fetaured_product = v.findViewById(R.id.fetaured_product);
        customMainPro = new CustomMainPro(getActivity());
        fetaured_product.setAdapter(customMainPro);
        customMainPro.notifyDataSetChanged();*/
    }

    private void getFeaturedProducts() {
        progresbar.setVisibility(View.VISIBLE);
        productlistbeanArrayList = new ArrayList<>();
        Call<ResponseBody> call = ApiClient.getApiInterface().getFeaturedProduct(user_id, "", "", "");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progresbar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("Featured Products >", " >" + responseData);
                        if (object.getString("status").equals("1")) {

                            Product successData = new Gson().fromJson(responseData, Product.class);
                            productlistbeanArrayList.addAll(successData.getResult());

                        }
                        productGridAdp = new ProductGridAdp(productlistbeanArrayList);
                        featured_product_rec.setAdapter(productGridAdp);
                        productGridAdp.notifyDataSetChanged();

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


    class ProductGridAdp extends RecyclerView.Adapter<ProductGridAdp.MyViewHolder> {

        ArrayList<Productlistbean> productlistbeanArrayList;

        public ProductGridAdp(ArrayList<Productlistbean> productlistbeanArrayList) {
            this.productlistbeanArrayList = productlistbeanArrayList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_product, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
            holder.product_name.setText("" + productlistbeanArrayList.get(listPosition).getProductName());
            holder.product_description.setText("" + productlistbeanArrayList.get(listPosition).getProductDescription());

            String product_img = productlistbeanArrayList.get(listPosition).getThumbnailImage();
            if (product_img == null || product_img.equalsIgnoreCase("") || product_img.equalsIgnoreCase(BaseUrl.image_baseurl)) {

            } else {
                Glide.with(getActivity())
                        .load(product_img)
                        .thumbnail(0.5f)
                        .override(400, 150)
                        .centerCrop()

                        .placeholder(R.drawable.placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)

                        .into(holder.product_img);

            }

            holder.paybill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), ManualActivity.class);
                    startActivity(i);
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), ProduCtDetailAct.class);
                    i.putExtra("product_id", productlistbeanArrayList.get(listPosition).getId());
                    i.putExtra("product_name_str", productlistbeanArrayList.get(listPosition).getProductName());
                    i.putExtra("user_id", user_id);
                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            // return 4;
            return productlistbeanArrayList == null ? 0 : productlistbeanArrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView paybill, product_name, product_description;
            ImageView product_img;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.product_description = itemView.findViewById(R.id.product_description);
                this.product_name = itemView.findViewById(R.id.product_name);
                this.paybill = itemView.findViewById(R.id.paybill);
                this.product_img = itemView.findViewById(R.id.product_img);
            }
        }
    }

}