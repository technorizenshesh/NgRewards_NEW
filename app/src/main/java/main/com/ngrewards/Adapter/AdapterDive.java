package main.com.ngrewards.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import main.com.ngrewards.Models.DiveInModal;
import main.com.ngrewards.R;
import main.com.ngrewards.constant.BaseUrl;

/**
 * Created by admin4 on 11/5/2016.
 */
public class AdapterDive extends RecyclerView.Adapter<AdapterDive.MyViewHolder> {

    private final ArrayList<DiveInModal> all_category_subcategory;
    private View itemView;
    private String desage_name_string;
    private final Context activity;
    private String name_string;
    private String id_string;
    private String Image_string;


    public AdapterDive(Context a, ArrayList<DiveInModal> all_category_subcategory) {

        this.activity = a;
        this.all_category_subcategory = all_category_subcategory;
    }

    @Override
    public AdapterDive.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_die, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int i) {

        name_string = all_category_subcategory.get(i).getName();
        id_string = all_category_subcategory.get(i).getId();
        Image_string = all_category_subcategory.get(i).getImage();

        Glide.with(activity)
                .load(BaseUrl.image_baseurl+Image_string)
                .thumbnail(0.5f)
                .override(400, 150)
                .centerCrop()
                .crossFade()
                .placeholder(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;

                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                        return false;
                    }
                })
                .into(holder.menu_image);
        holder.name_menu.setText(name_string);

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView name_menu;
        private final ImageView menu_image;

        public MyViewHolder(View itemView) {

            super(itemView);

            name_menu = itemView.findViewById(R.id.name_menu);
            menu_image = itemView.findViewById(R.id.menu_image);

        }
    }


    @Override
    public int getItemCount() {

        return all_category_subcategory.size();

    }

}

